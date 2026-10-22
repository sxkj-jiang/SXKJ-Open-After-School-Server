package com.sxkj.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.sxkj.edu.common.utils.ExcelUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.PageVO;
import com.sxkj.edu.common.utils.CheckParamUtil;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.mult.ao.*;
import com.sxkj.edu.domain.mult.excel.TPortalArticleManageExportExcel;
import com.sxkj.edu.domain.mult.vo.PortalArticleCategoryVO;
import com.sxkj.edu.domain.mult.vo.TPortalArticleManageDetailVO;
import com.sxkj.edu.domain.mult.vo.TPortalArticleManageVO;
import com.sxkj.edu.domain.mult.vo.TPortalArticleTypeVO;
import com.sxkj.edu.domain.sys.entity.mult.TPortalArticleManage;
import com.sxkj.edu.domain.sys.entity.mult.TPortalArticleType;
import com.sxkj.edu.module.mapper.base.TPortalArticleManageMapper;
import com.sxkj.edu.service.TPortalArticleManageService;
import com.sxkj.edu.service.TPortalArticleTypeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TPortalArticleManageServiceImpl extends ServiceImpl<TPortalArticleManageMapper, TPortalArticleManage>implements TPortalArticleManageService {


    @Value("${default.news.logo.url}")
    private String newsLogoUlr;

    @Resource
    private TPortalArticleTypeService articleTypeService;

    @Override
    public JsonResult<String> articleManageUpdateOrIns(TPortalArticleManageAO update) {
        JsonResult<String> res = JsonResult.buildError(ResultCode.PTGL_WZGLFL_ERROR.code(), "平台管理-文章管理操作异常");

        LambdaQueryWrapper<TPortalArticleManage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TPortalArticleManage::getIsValid, 1);
        queryWrapper.eq(TPortalArticleManage::getNewsName, update.getNewsName().trim());
        queryWrapper.eq(TPortalArticleManage::getSchoolId, SmartRequestTokenUtil.getRequestUserStuSchoolId());
        if (ObjectUtil.isNotNull(update.getId())) {
            queryWrapper.ne(TPortalArticleManage::getId, update.getId());
        }
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0) return JsonResult.buildError("文章名称不能重复!");

        boolean flag = false;
        TPortalArticleManage origin = new TPortalArticleManage();
        try {
            BeanUtil.copyProperties(update, origin);
            if(StringUtils.isBlank(update.getPhotoUrl())){
                origin.setPhotoUrl(newsLogoUlr);
            }
            if (null == update.getId()) {
                if(ObjectUtil.isNull(SmartRequestTokenUtil.getRequestUserStuSchoolId())){
                    origin.setSchoolId(6L);
                }else {
                    origin.setSchoolId(SmartRequestTokenUtil.getRequestUserStuSchoolId());
                }
                origin.setCreateTime(new Date());
                origin.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
                flag = this.save(origin);
            } else {
                origin.setUpdateTime(new Date());
                origin.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
                flag = this.updateById(origin);
            }
        } catch (Exception e) {
            log.warn("文章管理操作异常");
        }
        if(flag)res = JsonResult.buildSuccess("操作成功！");
        return res;
    }

    @Override
    public TPortalArticleManageDetailVO detailArticleManage(Long id) {
        TPortalArticleManageDetailVO detailVO = new TPortalArticleManageDetailVO();
        TPortalArticleManage articleManage = this.getById(id);
        if (ObjectUtil.isNotNull(articleManage)) {
            BeanUtil.copyProperties(articleManage, detailVO);
            if(StringUtils.isNotBlank(articleManage.getPhotoUrl())){
                detailVO.setPhotoUrl(articleManage.getPhotoUrl());
            }
        }
        return detailVO;
    }

    @Override
    public JsonResult<String> deleteArticleManage(Long id) {
        JsonResult<String> res = JsonResult.buildError("删除文章失败！");

        boolean flag = false;
        TPortalArticleManage tPortalArticleManage = this.getById(id);
        if (null == tPortalArticleManage) {
            res = JsonResult.buildError(ResultCode.QUERY_ERROR.code(), "文章查无数据！");
        } else {
            //逻辑删除
            tPortalArticleManage.setIsValid(0);
            tPortalArticleManage.setUpdateTime(new Date());
            tPortalArticleManage.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
            flag = this.updateById(tPortalArticleManage);
        }
        if(flag){
            res = JsonResult.buildSuccess("删除文章成功！");
        }
        return res;
    }

    @Override
    public PageVO<TPortalArticleManageVO> queryArticleManagePage(TPortalArticleManagePageAO query) {
        Page<TPortalArticleManageVO> page = new Page<>(query.getPageIndex(), query.getPageSize());
        if(ObjectUtil.isNull(query.getSchoolId()) && ObjectUtil.isNotNull(SmartRequestTokenUtil.getRequestUserStuSchoolId())){
            query.setSchoolId(SmartRequestTokenUtil.getRequestUserStuSchoolId());
        }
        baseMapper.queryArticleManagePage(page, query);
        return new PageVO<>(page.getTotal(), page.getPages(), page.getRecords());
    }

    @Override
    public void exportArticleManage(TPortalArticleManagePageAO query, HttpServletResponse response) {
        PageVO<TPortalArticleManageVO> pageVO = this.queryArticleManagePage(query);
        List<TPortalArticleManageVO> records = pageVO.getList();
        if (CollectionUtil.isEmpty(records)) {
            return;
        }
        List<TPortalArticleManageExportExcel> excelList = new ArrayList<>();
        int index = 1;
        for (TPortalArticleManageVO vo : records) {
            TPortalArticleManageExportExcel excel = new TPortalArticleManageExportExcel();
            BeanUtils.copyProperties(vo, excel);
            excel.setSequence(String.valueOf(index++));
            excel.setId(String.valueOf(vo.getId()));
            // 是否展示转换
            if (vo.getIsShow() != null) {
                excel.setIsShowName(vo.getIsShow() == 1 ? "是" : "否");
            }
            // 是否外部站点转换
            if (vo.getIsOutside() != null) {
                excel.setIsOutsideName(vo.getIsOutside() == 1 ? "是" : "否");
            }
            excelList.add(excel);
        }
        
        try {
            ExcelUtils.exportExcel(response, excelList, TPortalArticleManageExportExcel.class, "文章管理导出", "文章管理");
        } catch (Exception e) {
            log.error("导出文章管理异常", e);
        }
    }

    @Override
    public JsonResult<String> articleManageeIsShow(TPortalArticleManageShowAo ao) {
        JsonResult<String> res = JsonResult.buildError("文章是否展示操作失败！");

        boolean flag = false;
        TPortalArticleManage articleManage = this.getById(ao.getId());
        if (null == articleManage) {
            res = JsonResult.buildError(ResultCode.QUERY_ERROR.code(), "文章查无数据！");
        } else {
            //逻辑删除
            articleManage.setIsShow(ao.getIsShow());
            articleManage.setUpdateTime(new Date());
            articleManage.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
            flag = this.updateById(articleManage);
        }
        if(flag){
            res = JsonResult.buildSuccess("操作成功！");
        }
        return res;
    }

    @Override
    public JsonResult<List<PortalArticleCategoryVO>> articleManageeMobile(PortalArticleManageListAo query) {
        List<PortalArticleCategoryVO> res = new ArrayList<>();
        List<TPortalArticleTypeVO> articleTypeVOS = articleTypeService.queryArticleTypeList();
        LambdaQueryWrapper<TPortalArticleManage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TPortalArticleManage::getIsValid,1)
                .eq(TPortalArticleManage::getIsShow,1);
        Long schoolId = 6L;
        if(ObjectUtil.isNotNull(query.getSchoolId()) ){
            schoolId = query.getSchoolId();
        }
        queryWrapper.eq(TPortalArticleManage::getSchoolId, schoolId);
        queryWrapper.orderByDesc(TPortalArticleManage::getSort);
        List<TPortalArticleManage> articleManages = this.list(queryWrapper);

        if(CollectionUtil.isNotEmpty(articleTypeVOS)){
            //小程序暂时固定显示栏目 新闻资讯 校园文化 通知公告
//            List<TPortalArticleTypeVO> collect = articleTypeVOS.stream().filter(e -> 1 == e.getId() || 2 == e.getId() || 3 == e.getId()).collect(Collectors.toList());
            for (TPortalArticleTypeVO articleTypeVO : articleTypeVOS) {
                PortalArticleCategoryVO categoryVO = new PortalArticleCategoryVO();
                categoryVO.setId(articleTypeVO.getId());
                categoryVO.setArticleTypeName(articleTypeVO.getArticleTypeName());

                if(CollectionUtil.isNotEmpty(articleManages)){
                    List<TPortalArticleManage> manages = articleManages.stream().filter(e ->
                            e.getArticleTypeId().equals(articleTypeVO.getId())).limit(4).collect(Collectors.toList());
                    if(!manages.isEmpty()) {
                        List<TPortalArticleManageVO> tPortalArticleManageVOS = SmartBeanUtil.copyList(manages, TPortalArticleManageVO.class);
                        tPortalArticleManageVOS.forEach(e -> {
                            if(StringUtils.isNotBlank(e.getPhotoUrl())){
                                e.setPhotoUrl(e.getPhotoUrl());
                            }
                        });
                        categoryVO.setArticleManageVOS(tPortalArticleManageVOS);
                    }
                }
                res.add(categoryVO);
            }
        }
        return JsonResult.buildSuccess(res);
    }

    @Override
    public PageVO<TPortalArticleManageVO> articleManageeMobileMore(PortalArticleManageAo manageAo) {
        Page<TPortalArticleManageVO> page = new Page<>(manageAo.getPageIndex(), manageAo.getPageSize());
        Long schId = SmartRequestTokenUtil.getRequestUserStuSchoolId();
        if(CheckParamUtil.checkLongIdIsNull(manageAo.getSchoolId())){
            if(CheckParamUtil.checkLongIdParam(schId)){
                manageAo.setSchoolId(schId);
            }else{
                // 设置默认学校
                manageAo.setSchoolId(6L);
            }
        }
        baseMapper.articleManageeMobileMore(page, manageAo);
        return new PageVO<>(page.getTotal(), page.getPages(), page.getRecords());
    }

    @Override
    public JsonResult<String> articleManageCarouselUpdateOrIns(ArticleManageCarouselAO update) {
        JsonResult<String> res = JsonResult.buildError(ResultCode.PTGL_WZGLFL_ERROR.code(), "平台管理-文章管理操作异常");

        LambdaQueryWrapper<TPortalArticleType> typeQueryWrapper = new LambdaQueryWrapper<>();
        typeQueryWrapper.eq(TPortalArticleType::getArticleTypeName,"轮播图公告");
        typeQueryWrapper.eq(TPortalArticleType::getIsValid,1);
        typeQueryWrapper.eq(TPortalArticleType::getIsEnable,1);
        List<TPortalArticleType> types = articleTypeService.list(typeQueryWrapper);
        if(CollectionUtil.isEmpty(types)) return JsonResult.buildError(ResultCode.QUERY_ERROR.code(), "文章类型不存在名为轮播图公告的类型！");

        LambdaQueryWrapper<TPortalArticleManage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TPortalArticleManage::getIsValid, 1);
        queryWrapper.eq(TPortalArticleManage::getNewsName, update.getNewsName().trim());
        if (ObjectUtil.isNotNull(update.getId())) {
            queryWrapper.ne(TPortalArticleManage::getId, update.getId());
        }
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0) return JsonResult.buildError("通知公告标题重复或已存在同名的文章标题!");

        boolean flag = false;

        TPortalArticleManage origin = new TPortalArticleManage();
        BeanUtil.copyProperties(update, origin);
        origin.setArticleTypeId(types.get(0).getId());
        Long userId = SmartRequestTokenUtil.getRequestUserId();
        if (null == update.getId()) {
            Long schoolId = SmartRequestTokenUtil.getRequestUserStuSchoolId();
            if(ObjectUtil.isNotNull(schoolId))  origin.setSchoolId(schoolId);
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
}
