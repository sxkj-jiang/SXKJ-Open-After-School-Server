package com.sxkj.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.PageVO;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.mult.ao.TPortalArticleTypeAO;
import com.sxkj.edu.domain.mult.ao.TPortalArticleTypePageAO;
import com.sxkj.edu.domain.mult.ao.TPortalArticleTypeUptDisableAo;
import com.sxkj.edu.domain.mult.vo.TPortalArticleTypeVO;
import com.sxkj.edu.domain.sys.entity.mult.TPortalArticleType;
import com.sxkj.edu.module.mapper.base.TPortalArticleTypeMapper;
import com.sxkj.edu.service.TPortalArticleTypeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TPortalArticleTypeServiceImpl extends ServiceImpl<TPortalArticleTypeMapper, TPortalArticleType> implements TPortalArticleTypeService {


    @Override
    public JsonResult<String> articleTypeUpdateOrIns(TPortalArticleTypeAO update) {

        JsonResult<String> res = JsonResult.buildError(ResultCode.PTGL_WZGLFL_ERROR.code(), "平台管理-文章管理操作异常");

        LambdaQueryWrapper<TPortalArticleType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TPortalArticleType::getIsValid, 1);
        queryWrapper.eq(TPortalArticleType::getArticleTypeName, update.getArticleTypeName().trim());
        if (ObjectUtil.isNotNull(update.getId())) {
            queryWrapper.ne(TPortalArticleType::getId, update.getId());
        }
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0) return JsonResult.buildError("文章类型名称不能重复!!!!");

        boolean flag = false;

        TPortalArticleType origin = new TPortalArticleType();
        BeanUtil.copyProperties(update, origin);
        Long userId = SmartRequestTokenUtil.getRequestUserId();
        if (null == update.getId()) {
            origin.setCreateTime(new Date());
            origin.setCreateUser(userId);
            flag = this.save(origin);
        } else {
            origin.setUpdateTime(new Date());
            origin.setUpdateUser(userId);
            flag = this.updateById(origin);
        }
        if(flag)res = JsonResult.buildSuccess("操作成功！");
        return res;
    }

    @Override
    public JsonResult<String> deleteArticleTypeInfo(Long id) {

        JsonResult<String> res = JsonResult.buildError("删除文章类型失败！");

        boolean flag = false;
        TPortalArticleType tPortalArticleType = this.getById(id);
        if (null == tPortalArticleType) {
            res = JsonResult.buildError(ResultCode.QUERY_ERROR.code(), "文章类型查无数据！");
        } else {
            //逻辑删除
            tPortalArticleType.setIsValid(0);
            tPortalArticleType.setUpdateTime(new Date());
            tPortalArticleType.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
            flag = this.updateById(tPortalArticleType);
        }
        if(flag){
            res = JsonResult.buildSuccess("删除文章类型成功！");
        }
        return res;
    }

    @Override
    public PageVO<TPortalArticleTypeVO> queryArticleTypePage(TPortalArticleTypePageAO query) {
        Page<TPortalArticleTypeVO> page = new Page<>(query.getPageIndex(), query.getPageSize());
        baseMapper.queryArticleTypePage(page, query);
        return new PageVO<>(page.getTotal(), page.getPages(), page.getRecords());
    }

    @Override
    public JsonResult<String> articleTypeEnableOrDis(TPortalArticleTypeUptDisableAo ao) {
        JsonResult<String> res = JsonResult.buildError("文章类型启用/禁用失败！");

        boolean flag = false;
        TPortalArticleType tPortalArticleType = this.getById(ao.getId());
        if (null == tPortalArticleType) {
            res = JsonResult.buildError(ResultCode.QUERY_ERROR.code(), "文章类型查无数据！");
        } else {
            //逻辑删除
            tPortalArticleType.setIsEnable(ao.getIsEnable());
            tPortalArticleType.setUpdateTime(new Date());
            tPortalArticleType.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
            flag = this.updateById(tPortalArticleType);
        }
        if(flag){
            res = JsonResult.buildSuccess("操作成功！");
        }
        return res;
    }

    @Override
    public List<TPortalArticleTypeVO> queryArticleTypeList() {
        List<TPortalArticleTypeVO> typeVOS = new ArrayList<>();

        LambdaQueryWrapper<TPortalArticleType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TPortalArticleType::getIsValid, 1);
        queryWrapper.eq(TPortalArticleType::getIsEnable, 1);
        queryWrapper.orderByAsc(TPortalArticleType::getSort);
        List<TPortalArticleType> list = this.list(queryWrapper);
        if(CollectionUtil.isNotEmpty(list)){
            typeVOS = SmartBeanUtil.copyList(list, TPortalArticleTypeVO.class);
        }
        return typeVOS;
    }

    @Override
    public List<TPortalArticleTypeVO> articleTypeAppListQuery() {
        List<TPortalArticleTypeVO> typeVOS = new ArrayList<>();

        LambdaQueryWrapper<TPortalArticleType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TPortalArticleType::getIsValid, 1);
        queryWrapper.eq(TPortalArticleType::getIsEnable, 1);
        queryWrapper.orderByDesc(TPortalArticleType::getSort);
        List<TPortalArticleType> list = this.list(queryWrapper);
        if(CollectionUtil.isNotEmpty(list)){
            List<TPortalArticleType> collect = list.stream().filter(e -> e.getId().equals(1L) ||  e.getId().equals(2L) || e.getId().equals(3L)).collect(Collectors.toList());
            typeVOS = SmartBeanUtil.copyList(collect, TPortalArticleTypeVO.class);
        }
        return typeVOS;
    }
}
