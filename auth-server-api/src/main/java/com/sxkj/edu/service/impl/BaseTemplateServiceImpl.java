package com.sxkj.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.base.enums.EnableEnum;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.PageVO;
import com.sxkj.edu.common.redis.RedisCache;
import com.sxkj.edu.common.utils.*;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.dto.export.BaseTemplateExcelExportDTO;
import com.sxkj.edu.domain.mult.ao.BaseTemplateChildQueryAO;
import com.sxkj.edu.domain.mult.ao.BaseTemplatePageCodeQueryAO;
import com.sxkj.edu.domain.mult.ao.BaseTemplateQueryAO;
import com.sxkj.edu.domain.mult.ao.BaseTemplateUpdateAO;
import com.sxkj.edu.domain.mult.vo.BaseTemplatePageCodeVO;
import com.sxkj.edu.domain.mult.vo.BaseTemplateVO;
import com.sxkj.edu.domain.mult.vo.LabelVO;
import com.sxkj.edu.domain.sys.entity.TBaseDictItemEntity;
import com.sxkj.edu.domain.vo.FileVO;
import com.sxkj.edu.module.business.entity.BaseTemplatePo;
import com.sxkj.edu.module.mapper.base.BaseTemplateMapper;
import com.sxkj.edu.module.system.employee.TbaseUserService;
import com.sxkj.edu.module.system.employee.domain.entity.TBaseUserEntity;
import com.sxkj.edu.module.system.privilege.domain.entity.UserMenuEntity;
import com.sxkj.edu.module.system.privilege.service.UserMenuService;
import com.sxkj.edu.service.BaseDictItemService;
import com.sxkj.edu.service.BaseFileService;
import com.sxkj.edu.service.BaseTemplateService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BaseTemplateServiceImpl extends ServiceImpl<BaseTemplateMapper, BaseTemplatePo> implements BaseTemplateService {

    @Autowired
    private BaseFileService fileService;
    @Autowired
    private TbaseUserService userService;
    @Autowired
    private BaseDictItemService dictItemService;
    @Resource
    private UserMenuService userMenuService;
    @Autowired
    private RedisCache redisCache;

    @Override
    public PageVO<BaseTemplateVO> pageQuery(BaseTemplateQueryAO ao) {
        Page<BaseTemplatePo> page = this.page(new Page<>(ao.getPageIndex(), ao.getPageSize()), Wrappers.lambdaQuery(BaseTemplatePo.class)
                .like(StringUtils.isNotBlank(ao.getTemplateName()), BaseTemplatePo::getTemplateName, ao.getTemplateName())
                .eq(Args.isNotEmpty(ao.getMenu()), BaseTemplatePo::getMenu, ao.getMenu())
                .ge(Args.isNotNull(ao.getCreateTimeStart()), BaseTemplatePo::getCreateTime, ao.getCreateTimeStart())
                .le(Args.isNotNull(ao.getCreateTimeEnd()), BaseTemplatePo::getCreateTime, ao.getCreateTimeEnd()));
        if (page == null || CollectionUtils.isEmpty(page.getRecords())) return new PageVO<>(0, 0, null);
        Map<Long, FileVO> fileIdUrlMap = fileService.getFileById(page.getRecords().stream().map(BaseTemplatePo::getAttachment).collect(Collectors.toList()));
        Map<Long, String> userIdNameMap = Optional.ofNullable(userService.listByIds(page.getRecords().stream().map(BaseTemplatePo::getCreateUser).collect(Collectors.toList())))
                .map(x -> x.stream().collect(Collectors.toMap(TBaseUserEntity::getId, TBaseUserEntity::getActualName)))
                .orElse(Collections.emptyMap());
        List<UserMenuEntity> UserMenuEntityList = userMenuService.getPrivilegeListByRole();
        // 获取菜单
        Map<String,UserMenuEntity> UserMenuEntityMap = UserMenuEntityList.stream().collect(Collectors.toMap(UserMenuEntity::getKey, v -> v, (p1, p2) -> p1));
        List<BaseTemplateVO> voList = buildTemplateVoList(page.getRecords(), fileIdUrlMap, userIdNameMap, UserMenuEntityMap);
        return new PageVO<>(page.getTotal(), page.getTotal() / ao.getPageSize(), voList);
    }

    /**
     * 根据页面code查询启用模板列表,供页面下载导入模板使用
     */
    @Override
    public List<BaseTemplatePageCodeVO> queryByPageCode(BaseTemplatePageCodeQueryAO ao) {
        // 仅查有效且启用的模板,按页面code(菜单key或父级前缀)右模糊匹配,可选按子页面key精确过滤
        // 传父级code(如courseScheduling)可聚合查出所有子页面模板(courseScheduling_open/_Administrative/_invite)
        List<BaseTemplatePo> pos = this.list(Wrappers.lambdaQuery(BaseTemplatePo.class)
                .eq(BaseTemplatePo::getIsValid, 1)
                .eq(BaseTemplatePo::getEnable, 1)
                .likeRight(BaseTemplatePo::getMenu, ao.getPageCode())
                .eq(Args.isNotEmpty(ao.getMenuChildCode()), BaseTemplatePo::getMenuChildCode, ao.getMenuChildCode())
                .last("LIMIT 999"));
        if (CollectionUtils.isEmpty(pos)) {
            return Collections.emptyList();
        }
        // 下载场景仅需附件,无需查创建人/菜单名
        Map<Long, FileVO> fileIdUrlMap = fileService.getFileById(pos.stream().map(BaseTemplatePo::getAttachment).collect(Collectors.toList()));
        List<BaseTemplatePageCodeVO> voList = new ArrayList<>();
        for (BaseTemplatePo x : pos) {
            BaseTemplatePageCodeVO vo = new BaseTemplatePageCodeVO();
            vo.setId(x.getId());
            vo.setTemplateName(x.getTemplateName());
            vo.setMenuChildCode(x.getMenuChildCode());
            vo.setAttachment(fileIdUrlMap.get(x.getAttachment()));
            voList.add(vo);
        }
        return voList;
    }

    /**
     * 装配模板VO列表:补齐菜单名称、创建人名称、附件信息
     */
    private List<BaseTemplateVO> buildTemplateVoList(List<BaseTemplatePo> pos,
                                                     Map<Long, FileVO> fileIdUrlMap,
                                                     Map<Long, String> userIdNameMap,
                                                     Map<String, UserMenuEntity> userMenuEntityMap) {
        List<BaseTemplateVO> voList = new ArrayList<>();
        for (BaseTemplatePo x : pos) {
            BaseTemplateVO vo = new BaseTemplateVO();
            vo.setId(x.getId());
            vo.setTemplateCode(x.getTemplateCode());
            vo.setTemplateName(x.getTemplateName());
            vo.setMenu(x.getMenu());
            vo.setMenuChildCode(x.getMenuChildCode());
            if (CollectionUtil.isNotEmpty(userMenuEntityMap) && userMenuEntityMap.containsKey(x.getMenu())) {
                vo.setMenuName(userMenuEntityMap.get(x.getMenu()).getName());
            }
            vo.setCreateUser(userIdNameMap.get(x.getCreateUser()));
            vo.setCreateTime(x.getCreateTime());
            vo.setEnable(x.getEnable());
            vo.setAttachment(fileIdUrlMap.get(x.getAttachment()));
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public void exportExcel(HttpServletResponse response, List<Long> ids) {
        List<BaseTemplatePo> pos = CollectionUtils.isEmpty(ids) ? this.list() : this.listByIds(ids);
        List<BaseTemplateExcelExportDTO> dtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(pos)) {
            List<Long> userIds = pos.stream().map(BaseTemplatePo::getCreateUser).distinct().collect(Collectors.toList());
            Map<Long, String> userIdActualNameMap = userService.getActualNameMap(userIds);
            List<UserMenuEntity> UserMenuEntityList = userMenuService.getPrivilegeListByRole();
            // 获取菜单
            Map<String,UserMenuEntity> UserMenuEntityMap = UserMenuEntityList.stream().collect(Collectors.toMap(UserMenuEntity::getKey, v -> v, (p1, p2) -> p1));
            for(BaseTemplatePo po: pos){
                BaseTemplateExcelExportDTO dto = new BaseTemplateExcelExportDTO();
                dto.setTemplateCode(po.getTemplateCode());
                dto.setTemplateName(po.getTemplateName());
                if(CollectionUtil.isNotEmpty(UserMenuEntityMap) && UserMenuEntityMap.containsKey(po.getMenu())){
                    dto.setMenuName(UserMenuEntityMap.get(po.getMenu()).getName());
                }
                dto.setCreateUser(userIdActualNameMap.get(po.getCreateUser()));
                dto.setCreateTime(po.getCreateTime());
                dto.setEnable(EnableEnum.getDescriptionByCode(po.getEnable()));
                dtoList.add(dto);
            }
        }
        ExcelUtils.export(response, dtoList, BaseTemplateExcelExportDTO.class, "模板管理");
    }

    @Override
    public List<LabelVO> menuList() {
        List<TBaseDictItemEntity> entities = dictItemService.list(Wrappers.lambdaQuery(TBaseDictItemEntity.class).eq(TBaseDictItemEntity::getTypeCode, "menu"));
        return Optional.ofNullable(entities).map(x -> x.stream().map(y -> new LabelVO(y.getTypeName(), y.getId())).collect(Collectors.toList())).orElse(Collections.emptyList());
    }

    @Override
    public JsonResult<BaseTemplateVO> queryByMenu(BaseTemplateChildQueryAO menu) {
        JsonResult<BaseTemplateVO> jsonResult = new JsonResult<>();
        BaseTemplatePo po = queryBaseTemplateByCode(menu.getMenu(),menu.getMenuChild());
        if (po == null) return JsonResult.buildError(ResultCode.NOT_EXISTS.code(),"模板查无数据");

        Map<Long, FileVO> fileIdUrlMap = fileService.getFileById(Collections.singletonList(po.getAttachment()));
        Map<Long, String> userIdNameMap = Optional.ofNullable(userService.listByIds(Collections.singletonList(po.getCreateUser())))
                .map(x -> x.stream().collect(Collectors.toMap(TBaseUserEntity::getId, TBaseUserEntity::getActualName)))
                .orElse(Collections.emptyMap());
        List<UserMenuEntity> UserMenuEntityList = userMenuService.getPrivilegeListByRole();
        // 获取菜单
        Map<String,UserMenuEntity> UserMenuEntityMap = UserMenuEntityList.stream().collect(Collectors.toMap(UserMenuEntity::getKey, v -> v, (p1, p2) -> p1));
        BaseTemplateVO vo = new BaseTemplateVO();
        SmartBeanUtil.copyProperties(po,vo);
        vo.setId(po.getId());
        if(CollectionUtil.isNotEmpty(UserMenuEntityMap) && UserMenuEntityMap.containsKey(po.getMenu())){
            vo.setMenuName(UserMenuEntityMap.get(po.getMenu()).getName());
        }else{
            return JsonResult.buildError(ResultCode.NOT_EXISTS.code(),"模板查无数据");
        }
        vo.setCreateUser(userIdNameMap.get(po.getCreateUser()));
        vo.setAttachment(fileIdUrlMap.get(po.getAttachment()));
        jsonResult = JsonResult.buildSuccess(vo);
        return jsonResult;
    }

    /**
     *@Description : 新增、更新通用实现
     *@Author : JIANGJJ
     *@Date : 2025-10-31 11:39
     *@Company : SXKJ
     *@Version : 1.0
     **/
    @Override
    public JsonResult<String> saveOrUpdateBaseTemplate(BaseTemplateUpdateAO ao) {
        JsonResult<String> res = JsonResult.buildError(ResultCode.MBGL_ERROR.code(), "模板管理操作异常");
        Long userId = SmartRequestTokenUtil.getRequestUserId();
        BaseTemplatePo templatePO = new BaseTemplatePo();
        Boolean flag = false;
        String menuAll = "";
        if(StringUtils.isNotBlank(ao.getMenuChildCode())){
            menuAll = ao.getMenu()+"_"+ao.getMenuChildCode();
        }else{
            menuAll = ao.getMenu();
        }
        ao.setMenu(menuAll);
        if(CheckParamUtil.checkLongIdParam(ao.getId())){
            BaseTemplatePo origin = this.getById(ao.getId());
            if(null == origin || checkPrikeyIdExist(ao.getId(),ao.getMenu(),ao.getTemplateName())){
                return JsonResult.buildError(ResultCode.MBGL_ERROR.code(), "模板管理查无数据或模板数据已重复");
            }
            BeanUtil.copyProperties(ao, templatePO);
            templatePO.setId(origin.getId());
            templatePO.setUpdateTime(new Date());
            templatePO.setUpdateUser(userId);
            flag = this.updateById(templatePO);
        }else{
            BaseTemplatePo origin = checkExistEntenry(ao.getMenu(),ao.getTemplateName());
            if(null != origin){
                return JsonResult.buildError(ResultCode.MBGL_ERROR.code(), "模板管理数据已重复");
            }

            origin = new BaseTemplatePo();
            BeanUtil.copyProperties(ao, origin);
            origin.setId(UUIDUtils.randomNumber12());
            String yearMonthDay = DateUtils.format(new Date(), "yyMMdd");
            origin.setTemplateCode("tl" + yearMonthDay + String.format("%04d", redisCache.increment(String.format("%s:%s:%s", CoreConstant.edu_bus_base, "TEMPLATE_CODE", yearMonthDay), 1L)));

            origin.setCreateTime(new Date());
            origin.setCreateUser(userId);
            flag = this.save(origin);
        }

        if(flag) res = JsonResult.buildSuccess("模板操作成功");
        return res;
    }

    private BaseTemplatePo checkExistEntenry(String menuCode, String menuName) {
        BaseTemplatePo origin = null;
        LambdaQueryWrapper<BaseTemplatePo> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.ne(BaseTemplatePo::getId, id);
        queryWrapper.eq(BaseTemplatePo::getIsValid, 1);
        queryWrapper.and(wrapper -> wrapper.eq(BaseTemplatePo::getMenu, menuCode)
                .or()
                .eq(BaseTemplatePo::getTemplateName, menuName));
        List<BaseTemplatePo> list = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            origin = list.get(0);
        }
        return origin;
    }

    // 更新时校验当前id下核心数据是否重复
    private Boolean checkPrikeyIdExist(Long id,String menuCode, String menuName) {
        Boolean flag = false;
        LambdaQueryWrapper<BaseTemplatePo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(BaseTemplatePo::getId, id);
        queryWrapper.eq(BaseTemplatePo::getIsValid, 1);
        queryWrapper.and(wrapper -> wrapper.eq(BaseTemplatePo::getMenu, menuCode)
                .or()
                .eq(BaseTemplatePo::getTemplateName, menuName));
        List<BaseTemplatePo> list = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(list) && list.size() > 1) {
            flag = true;
        }
        return flag;
    }
    private BaseTemplatePo queryBaseTemplateByCode(String menuCode, String childCode) {
        BaseTemplatePo templatePO = null;
        LambdaQueryWrapper<BaseTemplatePo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseTemplatePo::getIsValid, 1);
        queryWrapper.eq(BaseTemplatePo::getMenu, menuCode);
        queryWrapper.eq(Args.isNotEmpty(childCode),BaseTemplatePo::getMenuChildCode, childCode);
        List<BaseTemplatePo> list = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            templatePO = list.get(0);
        }
        return templatePO;
    }

}
