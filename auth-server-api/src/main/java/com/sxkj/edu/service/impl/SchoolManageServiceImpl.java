package com.sxkj.edu.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.common.base.enums.LoginTypeEnum;
import com.sxkj.edu.common.config.GrobProperties;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.PageVO;
import com.sxkj.edu.common.redis.RedisCache;
import com.sxkj.edu.common.utils.CheckParamUtil;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.config.AssembleUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.ao.SchoolManageSwithAO;
import com.sxkj.edu.domain.dto.SchoolManageDTO;
import com.sxkj.edu.domain.vo.FileVO;
import com.sxkj.edu.module.afterschool.mapper.AsStudentMapper;
import com.sxkj.edu.module.business.entity.SchoolManagePo;
import com.sxkj.edu.module.mapper.SchoolManageMapper;
import com.sxkj.edu.module.system.employee.domain.bo.EmployeeBO;
import com.sxkj.edu.module.system.employee.domain.bo.RequestTokenBO;
import com.sxkj.edu.module.system.login.domain.LoginDetailVO;
import com.sxkj.edu.module.system.login.domain.LoginStuSchoolDetailVO;
import com.sxkj.edu.service.BaseFileService;
import com.sxkj.edu.service.CacheServiceImpl;
import com.sxkj.edu.service.SchoolManageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SchoolManageServiceImpl extends ServiceImpl<SchoolManageMapper, SchoolManagePo> implements SchoolManageService {

    @Resource
    private SchoolManageMapper schoolManageRepository;
    @Resource
    private CacheServiceImpl cacheService;
    @Resource
    private BaseFileService baseFileService;
    @Resource
    private RedisCache redisCache;
    @Resource
    private AsStudentMapper asStudentMapper;


    @Override
    public PageVO<SchoolManageDTO> pageQuery(SchoolManageDTO schoolManageDTO, int pageNum, int pageSize) {
        LambdaQueryWrapper<SchoolManagePo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SchoolManagePo::getIsValid, 1);
        // 动态添加查询条件
        wrapper.like(StrUtil.isNotEmpty(schoolManageDTO.getSchoolName()), SchoolManagePo::getSchoolName, schoolManageDTO.getSchoolName());
        if (CheckParamUtil.checkLongIdParam(schoolManageDTO.getManagerId())) {
            wrapper.eq(SchoolManagePo::getManagerId, schoolManageDTO.getManagerId());
        }
        if (StrUtil.isNotEmpty(schoolManageDTO.getManagerName())) {
            wrapper.apply("EXISTS (SELECT 1 FROM t_base_user u WHERE u.id = t_school_manage.create_user AND u.actual_name LIKE '%" + schoolManageDTO.getManagerName() + "%')");
        }
        //按照更新时间倒序排序
        wrapper.orderByDesc(SchoolManagePo::getUpdateTime);
        // 分页查询
        try {
            Page<SchoolManagePo> page = this.page(new Page<>(pageNum, pageSize), wrapper);
            PageVO<SchoolManageDTO> recruitManageDTOPage = new PageVO<>(page.getTotal(),
                    page.getPages(),
                    page.getRecords().stream().map(projectRecruitManage -> {
                        SchoolManageDTO schoolManageDTO1 = new SchoolManageDTO();
                        BeanUtils.copyProperties(projectRecruitManage, schoolManageDTO1);
                        return schoolManageDTO1;
                    }).collect(Collectors.toList())
            );
            // 批量查询校友数量
            List<Long> schoolIds = page.getRecords().stream()
                    .map(SchoolManagePo::getId)
                    .collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(schoolIds)) {
                List<Map<String, Object>> countList = asStudentMapper.countSchoolMateBySchoolIds(schoolIds);
                Map<Long, Integer> countMap = countList.stream()
                        .collect(Collectors.toMap(
                                m -> ((Number) m.get("schoolId")).longValue(),
                                m -> ((Number) m.get("cnt")).intValue()
                        ));
                recruitManageDTOPage.getList().forEach(dto -> 
                        dto.setSchoolMateCount(countMap.getOrDefault(dto.getId(), 0))
                );
            }
            return recruitManageDTOPage;
        } catch (Exception e) {
//            log.error(e.getMessage());
            return new PageVO<>(pageNum, pageSize, new ArrayList<>());
        }
    }


    @Override
    public List<SchoolManageDTO> listSchoolManage(SchoolManageDTO schoolManageDTO) {
        LambdaQueryWrapper<SchoolManagePo> wrapper = new LambdaQueryWrapper<>();
        // 动态添加查询条件
        wrapper.like(StrUtil.isNotEmpty(schoolManageDTO.getSchoolName()), SchoolManagePo::getSchoolName, schoolManageDTO.getSchoolName());
        if (schoolManageDTO.getManagerId() != null) {
            wrapper.eq(SchoolManagePo::getCreateUser, schoolManageDTO.getManagerId());
        }
        if (StrUtil.isNotEmpty(schoolManageDTO.getManagerName())) {
            wrapper.apply("EXISTS (SELECT 1 FROM t_base_user u WHERE u.id = t_school_manage.create_user AND u.actual_name LIKE '%" + schoolManageDTO.getManagerName() + "%')");
        }
        wrapper.orderByDesc(SchoolManagePo::getSchoolCode);
        wrapper.last("limit 20");
        List<SchoolManagePo> pos = this.list(wrapper);
        return SchoolManageDTO.convert(pos);
    }

    @Override
    public LoginStuSchoolDetailVO querySchoolNameById(Long id) {
        LambdaQueryWrapper<SchoolManagePo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SchoolManagePo::getId, id);
        queryWrapper.eq(SchoolManagePo::getIsValid, 1);
        List<SchoolManagePo> list = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            SchoolManagePo po = list.get(0);
            LoginStuSchoolDetailVO detailVO = new LoginStuSchoolDetailVO();
            SmartBeanUtil.copyProperties(po, detailVO);
            detailVO.setSchoolId(po.getId());
            if(StringUtils.isNotBlank(po.getSchoolLogo())){
                detailVO.setSchoolFileVo(baseFileService.getFileById(new Long(po.getSchoolLogo())));
            }

            return detailVO;
        }
        return null;
    }

    @Override
    public JsonResult swithSchoolData(SchoolManageSwithAO ao, HttpServletRequest request) {
        String loginType = AssembleUtil.getUserAgentLoginType();
        if(StringUtils.isEmpty(loginType)) loginType = LoginTypeEnum.MANAGER_TYPE.getCode();

        EmployeeBO userBo = SmartRequestTokenUtil.getRequestUser().getEmployeeBO();
        String loginName = userBo.getLoginName();
        // 从redis里获取用户缓存的学校信息
        LoginDetailVO redisDtoResult = cacheService.getLoginUserCache(loginType, loginName);
        if(null != redisDtoResult){
            LoginStuSchoolDetailVO schoolDetailVO = redisDtoResult.getSchoolInfo();
            if(null == schoolDetailVO){
                schoolDetailVO = new LoginStuSchoolDetailVO();
            }
            schoolDetailVO.setSchoolId(ao.getSchoolId());
            schoolDetailVO.setSchoolName(ao.getSchoolName());
            redisDtoResult.setSchoolInfo(schoolDetailVO);

            // 更新缓存并且延长失效时间
            cacheService.setLoginUserSchoolCache(loginType, loginName,
                    JSON.parseObject(JSONObject.toJSONString(redisDtoResult)));

            RequestTokenBO requestTokenBO = new RequestTokenBO(userBo,schoolDetailVO.getSchoolId());
            SmartRequestTokenUtil.setUser(request, requestTokenBO);

            // 同步设置缓存用户-学校map
            redisCache.hset("set_user_school_info", loginName, String.valueOf(ao.getSchoolId()));

            log.info("call swithSchoolData in exec success,request:{}",ao.toString());
        }
        return JsonResult.buildSuccess();
    }

    @Override
    public Map<Long, String> getSchoolNameById(List<Long> schoolIds) {
        List<SchoolManagePo> pos = this.listByIds(schoolIds);
        return pos == null ? Collections.emptyMap() : pos.stream().collect(Collectors.toMap(SchoolManagePo::getId, SchoolManagePo::getSchoolName));
    }

    @Override
    public Map<Long, FileVO> getSchoolLogo(List<Long> schoolIds) {
        if (CollectionUtils.isEmpty(schoolIds)) return Collections.emptyMap();
        List<SchoolManagePo> pos = listByIds(schoolIds);
        if (CollectionUtils.isEmpty(pos)) return Collections.emptyMap();
        Map<Long, FileVO> fileMap = baseFileService.getFileById(pos.stream().map(x -> Long.valueOf(x.getSchoolLogo())).collect(Collectors.toList()));
        Map<Long, FileVO> result = new HashMap<>();
        for (SchoolManagePo po : pos) {
            result.put(po.getId(), fileMap.get(Long.valueOf(po.getSchoolLogo())));
        }
        return result;
    }

    @Override
    public String getSchoolName(Long id) {
        LambdaQueryWrapper<SchoolManagePo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SchoolManagePo::getId, id);
        queryWrapper.eq(SchoolManagePo::getIsValid, 1);
        List<SchoolManagePo> list = this.list(queryWrapper);
        if(CollectionUtil.isNotEmpty(list)) return list.get(0).getSchoolName();
        return null;
    }

    @Override
    public String getSchoolCode(Long id) {
        LambdaQueryWrapper<SchoolManagePo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SchoolManagePo::getId, id);
        queryWrapper.eq(SchoolManagePo::getIsValid, 1);
        List<SchoolManagePo> list = this.list(queryWrapper);
        if(CollectionUtil.isNotEmpty(list)) return list.get(0).getSchoolCode();
        return null;
    }

    @Override
    public Long registSchoolManageByName(String schoolName) {
        if(StringUtils.isEmpty(schoolName)) return null;
        LambdaQueryWrapper<SchoolManagePo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SchoolManagePo::getSchoolName, schoolName);
        queryWrapper.eq(SchoolManagePo::getIsValid, 1);
        List<SchoolManagePo> list = this.list(queryWrapper);
        if(CollectionUtil.isEmpty(list)) {
            SchoolManagePo po = new SchoolManagePo();
            po.setSchoolName(schoolName);
            po.setSchoolCode("100008");
            po.setManagerId(1L);
            po.setSchoolLogo(GrobProperties.getInstance().SCHOOL_COVER.toString());
            po.setCreateUser(1L);
            po.setCreateTime(new Date());
            Boolean saveRow = this.save(po);
            log.info("registSchoolManageByName schoolName:{}, saveRow:{}", schoolName, saveRow);
            return po.getId();
        }else{
            return list.get(0).getId();
        }
    }


    @Override
    public JsonResult<String> saveSchoolManage(SchoolManageDTO schoolManageDTO) {
        JsonResult<String> res = JsonResult.buildError("操作失败！");
        //检查是否同名学校存在
        LambdaQueryWrapper<SchoolManagePo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SchoolManagePo::getSchoolName, schoolManageDTO.getSchoolName());
        queryWrapper.or().eq(SchoolManagePo::getSchoolCode,schoolManageDTO.getSchoolCode());
        List<SchoolManagePo> list = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            return JsonResult.buildError("学校名称或代码已存在");
        }
        schoolManageDTO.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
        schoolManageDTO.setCreateTime(new Date());
        boolean save = this.save(schoolManageDTO.convert(schoolManageDTO));
        if(save){
            res = JsonResult.buildSuccess("操作成功！");

        }
        return res;
    }

    @Override
    public JsonResult<String> updateSchoolManage(SchoolManageDTO schoolManageDTO) {
        JsonResult<String> res = JsonResult.buildError("操作失败！");
        //检查是否存在记录
        SchoolManagePo po = schoolManageRepository.selectById(schoolManageDTO.getId());
        if (po == null) {
            return JsonResult.buildError("该学校不存在");
        }
        int i = schoolManageRepository.updateById(schoolManageDTO.convert(schoolManageDTO));
        if(i > 0) res = JsonResult.buildSuccess("操作成功！");
        return res;
    }

    @Override
    public JsonResult<String> deleteSchoolManage(Long id) {
        JsonResult<String> res = JsonResult.buildError("操作失败！");
        //检查是否存在记录
        SchoolManagePo po = schoolManageRepository.selectById(id);
        if (po == null) {
            return JsonResult.buildError("该学校不存在");
        }
        po.setIsValid(0);
        int i = schoolManageRepository.updateById(po);
        if(i > 0) res = JsonResult.buildSuccess("操作成功！");
        return res;
    }

}
