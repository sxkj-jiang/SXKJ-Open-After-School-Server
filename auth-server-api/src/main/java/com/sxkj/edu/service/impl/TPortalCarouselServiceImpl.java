package com.sxkj.edu.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.PageVO;
import com.sxkj.edu.common.utils.CheckParamUtil;
import com.sxkj.edu.common.utils.UUIDUtils;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.base.mult.TPortalCarouselAo;
import com.sxkj.edu.domain.base.mult.TPortalCarouselQueryDetailAo;
import com.sxkj.edu.domain.base.mult.TPortalCarouselUptAo;
import com.sxkj.edu.domain.base.mult.TPortalCarouselVO;
import com.sxkj.edu.domain.sys.entity.BaseFileEntity;
import com.sxkj.edu.domain.sys.entity.TPortalCarouselEntity;
import com.sxkj.edu.module.mapper.website.TPortalCarouselMapper;
import com.sxkj.edu.service.BaseFileService;
import com.sxkj.edu.service.TPortalCarouselService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Description : 平台管理-轮播图
 * @Author : JIANGJJ
 * @Date : 2022-12-26 20:47
 * @Company : SXKJ
 * @Version : 1.0
 **/
@Slf4j
@Service
public class TPortalCarouselServiceImpl extends ServiceImpl<TPortalCarouselMapper, TPortalCarouselEntity> implements TPortalCarouselService {

    @Resource
    TPortalCarouselMapper carouselMapper;
    @Resource
    BaseFileService baseFileService;


    @Override
    public PageVO<TPortalCarouselVO> queryPortalCarouselList(TPortalCarouselAo query) {
        Page<TPortalCarouselVO> page = new Page<>(query.getPageIndex(), query.getPageSize());
        if(ObjectUtil.isNull(query.getSchoolId())){
            query.setSchoolId(6L);
        }
        carouselMapper.selectPortalCarouselList(page, query);
        page.getRecords().forEach(e -> {
            if(StringUtils.isNotBlank(e.getCulUrl())) {
                e.setCulUrl(e.getCulUrl());
            }
        });
        return new PageVO<>(page.getTotal(), page.getPages(), page.getRecords());
    }

    @Override
    public JsonResult<String> updateCarouselInfo(TPortalCarouselUptAo cwUptAo) {
        JsonResult<String> res = null;
        try {
            QueryWrapper<TPortalCarouselEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("is_valid", 1);
            queryWrapper.eq("cul_name", cwUptAo.getCulName().trim());
            queryWrapper.eq("school_id", cwUptAo.getSchoolId());
            if (ObjectUtil.isNotNull(cwUptAo.getId())) {
                queryWrapper.ne("id", cwUptAo.getId());
            }
            Integer count = carouselMapper.selectCount(queryWrapper);
            if (count > 0 && !CheckParamUtil.checkLongIdParam(cwUptAo.getId())) return JsonResult.buildError("轮播图名称名称不能重复!");
            TPortalCarouselEntity origin = new TPortalCarouselEntity();
            BeanUtils.copyProperties(cwUptAo, origin);

            Long fileId = null;
            BaseFileEntity baseFile = baseFileService.queryBaseFileByUrl(cwUptAo.getCulUrl());
            if (null == baseFile)
                return JsonResult.buildError(ResultCode.PTGL_BGT_ERROR.code(), "平台管理-轮播图地址异常");
            fileId = baseFile.getId();

            origin.setCulUrlId(fileId);
            Long userId = SmartRequestTokenUtil.getRequestUserId();
            origin.setPublishDate(new Date());
            if(CheckParamUtil.checkLongIdParam(cwUptAo.getId())) {
                origin.setId(cwUptAo.getId());
                origin.setUpdateTime(new Date());
                origin.setUpdateUser(userId);
                carouselMapper.updateById(origin);
            } else {
                log.info("call updateCarouselInfo in insert");
                origin.setIsValid(1);
                origin.setCulNum(UUIDUtils.random());
                origin.setCreateTime(new Date());
                origin.setCreateUser(userId);
                carouselMapper.insert(origin);
            }
            res = JsonResult.buildSuccess();
            log.info("call updateCarouselInfo in exec success");
        } catch (Exception e) {
           return JsonResult.buildError(ResultCode.PTGL_BGT_ERROR.code(), "平台管理-轮播图操作异常");
        }
        return res;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public JsonResult<String> deleteCarouselInfo(Long id) {
        JsonResult<String> res = null;
        try {
            TPortalCarouselEntity origin = carouselMapper.selectById(id);
            if (null == origin) {
                res = JsonResult.buildError(ResultCode.QUERY_ERROR.code(), "查无数据");
            } else {
                // 逻辑删除
                origin.setIsValid(0);
                origin.setStatus(0);
                origin.setUpdateTime(new Date());
                origin.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
                carouselMapper.updateById(origin);
                res = JsonResult.buildSuccess();
            }
        } catch (Exception e) {
            return JsonResult.buildError(ResultCode.PTGL_BGT_ERROR.code(), "平台管理-轮播图删除异常");
        }
        return res;
    }

    @Override
    public JsonResult<TPortalCarouselVO> queryPortalCarouselDetail(TPortalCarouselQueryDetailAo query) {
        JsonResult<TPortalCarouselVO> jsonResult = JsonResult.buildError(ResultCode.NOT_EXISTS.code(),"查无数据");
        TPortalCarouselVO tPortalCarouselVO = carouselMapper.selectPortalCarouselDetail(query);
        if(null != tPortalCarouselVO){
            if(StringUtils.isNotBlank(tPortalCarouselVO.getCulUrl())){
                tPortalCarouselVO.setCulUrl(tPortalCarouselVO.getCulUrl());
            }
            jsonResult = JsonResult.buildSuccess(tPortalCarouselVO);
        }
        return jsonResult;
    }
}
