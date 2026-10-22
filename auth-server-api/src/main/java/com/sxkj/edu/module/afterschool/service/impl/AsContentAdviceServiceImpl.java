package com.sxkj.edu.module.afterschool.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.utils.CheckParamUtil;
import com.sxkj.edu.common.utils.ExcelUtils;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.afterschool.domain.dto.AsContentAdviceAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsContentAdviceAuditAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsContentAdviceQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsContentAdviceEntity;
import com.sxkj.edu.module.afterschool.domain.excel.AsContentAdviceExcel;
import com.sxkj.edu.module.afterschool.domain.excel.AsContentAdviceExportExcel;
import com.sxkj.edu.module.afterschool.domain.vo.AsContentAdviceVO;
import com.sxkj.edu.module.afterschool.mapper.AsContentAdviceMapper;
import com.sxkj.edu.module.afterschool.service.AsContentAdviceService;
import com.sxkj.edu.module.system.employee.TbaseUserService;
import com.sxkj.edu.service.BaseFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 意见反馈 Service 实现类
 *
 * @author system
 * @date 2026-06-20
 */
@Service
@Slf4j
public class AsContentAdviceServiceImpl extends ServiceImpl<AsContentAdviceMapper, AsContentAdviceEntity>
        implements AsContentAdviceService {

    @Resource
    private BaseFileService baseFileService;

    @Resource
    private TbaseUserService tbaseUserService;

    // ======================== 通用私有方法 ========================

    /**
     * 校验同一学校下意见内容是否已存在
     */
    private boolean existsByAdviceName(Long schoolId, String adviceName, Long excludeId) {
        LambdaQueryWrapper<AsContentAdviceEntity> w = new LambdaQueryWrapper<>();
        w.eq(AsContentAdviceEntity::getIsValid, 1)
         .eq(AsContentAdviceEntity::getSchoolId, schoolId)
         .eq(AsContentAdviceEntity::getAdviceName, adviceName);
        if (excludeId != null) {
            w.ne(AsContentAdviceEntity::getId, excludeId);
        }
        return this.count(w) > 0;
    }

    /**
     * Entity -> VO
     */
    private AsContentAdviceVO toVO(AsContentAdviceEntity entity) {
        if (entity == null) return null;
        AsContentAdviceVO vo = new AsContentAdviceVO();
        BeanUtils.copyProperties(entity, vo);
        // 来源描述
        if (entity.getSourceType() != null) {
            switch (entity.getSourceType()) {
                case 1: vo.setSourceTypeDesc("教师端"); break;
                case 2: vo.setSourceTypeDesc("家长端"); break;
                case 3: vo.setSourceTypeDesc("班主任"); break;
                default: vo.setSourceTypeDesc("未知"); break;
            }
        }
        // 状态描述
        if (entity.getStatus() != null) {
            vo.setStatusDesc(entity.getStatus() == 1 ? "已处理" : "未处理");
        }
        return vo;
    }

    // ======================== 对外方法 ========================

    @Override
    public PageResultDTO<AsContentAdviceVO> queryAsContentAdvicePage(AsContentAdviceQueryAO qo) {
        Page<AsContentAdviceVO> page = new Page<>(qo.getPageIndex(), qo.getPageSize());
        baseMapper.pageContentAdviceList(page, qo);
        if(CollectionUtil.isNotEmpty(page.getRecords())){
            page.getRecords().forEach(e -> {
                if(StringUtils.isNotBlank(e.getFileUrl())){
                    e.setFileUrlList(baseFileService.fileListForUrlByPrefix(e.getFileUrl(),","));
                }
                if(CheckParamUtil.checkLongIdParam(e.getAuthorUpdate())){
                    e.setHandler(tbaseUserService.getById(e.getAuthorUpdate()).getActualName());
                }
            });
        }
        PageResultDTO<AsContentAdviceVO> result = new PageResultDTO<>();
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setPageIndex(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setList(page.getRecords());
        return result;
    }

    @Override
    public JsonResult<String> addAsContentAdvice(AsContentAdviceAddAO ao) {
        if (existsByAdviceName(ao.getSchoolId(), ao.getAdviceName(), null)) {
            return JsonResult.buildError("意见内容在该学校下已存在");
        }
        AsContentAdviceEntity entity = new AsContentAdviceEntity();
        BeanUtils.copyProperties(ao, entity);
        entity.setIsValid(1);
        entity.setUserId(SmartRequestTokenUtil.getRequestUserId());
        entity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
        entity.setCreateTime(new Date());
        entity.setStatus(0);
        boolean save = this.save(entity);
        if (!save) return JsonResult.buildError("操作失败");
        return JsonResult.buildSuccess("操作成功");
    }

    @Override
    public JsonResult<String> updateAsContentAdvice(AsContentAdviceAddAO ao) {
        if (ao.getId() == null) return JsonResult.buildError("意见ID不能为空");
        AsContentAdviceEntity exist = this.getById(ao.getId());
        if (exist == null || exist.getIsValid() != 1) return JsonResult.buildError("意见反馈记录不存在");
        if (existsByAdviceName(ao.getSchoolId(), ao.getAdviceName(), ao.getId())) {
            return JsonResult.buildError("意见内容在该学校下已存在");
        }
        AsContentAdviceEntity entity = new AsContentAdviceEntity();
        BeanUtils.copyProperties(ao, entity);
        entity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        entity.setUpdateTime(new Date());
        boolean flag = this.updateById(entity);
        if (!flag) return JsonResult.buildError("操作失败");
        return JsonResult.buildSuccess("操作成功");
    }

    @Override
    public JsonResult<String> auditAsContentAdvice(AsContentAdviceAuditAO ao) {
        if (ao.getId() == null) return JsonResult.buildError("意见ID不能为空");
        AsContentAdviceEntity exist = this.getById(ao.getId());
        if (exist == null || exist.getIsValid() != 1){
            return JsonResult.buildError("意见反馈记录不存在");
        }else if(exist.getStatus() == 1){
            return JsonResult.buildError("意见反馈记录已处理，不能重复处理");
        }

        AsContentAdviceEntity entity = new AsContentAdviceEntity();
        BeanUtils.copyProperties(ao, entity);
        entity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        entity.setUpdateTime(new Date());
        boolean flag = this.updateById(entity);
        if (!flag) return JsonResult.buildError("操作失败");
        return JsonResult.buildSuccess("操作成功");
    }

    @Override
    public JsonResult<String> deleteAsContentAdvice(Long id) {
        AsContentAdviceEntity entity = this.getById(id);
        if (entity == null || entity.getIsValid() != 1) return JsonResult.buildError("意见反馈记录不存在");
        entity.setIsValid(0);
        entity.setUpdateTime(new Date());
        entity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        this.updateById(entity);
        return JsonResult.buildSuccess("操作成功");
    }


    @Override
    public void exportAsContentAdvice(AsContentAdviceQueryAO qo, HttpServletResponse response) {
        try {
            // 查询所有数据（不分页）
            AsContentAdviceQueryAO exportQuery = new AsContentAdviceQueryAO();
            exportQuery.setSchoolId(qo.getSchoolId());
            exportQuery.setSourceType(qo.getSourceType());
            exportQuery.setAdviceName(qo.getAdviceName());
            exportQuery.setCategoryId(qo.getCategoryId());
            exportQuery.setAuthor(qo.getAuthor());
            exportQuery.setMobile(qo.getMobile());
            exportQuery.setStatus(qo.getStatus());

            Page<AsContentAdviceVO> page = new Page<>(1, 5000);
            baseMapper.pageContentAdviceList(page, exportQuery);

            if (CollectionUtil.isEmpty(page.getRecords())) {
                return;
            }

            // 转换为导出 Excel 模型
            List<AsContentAdviceExportExcel> excelList = new ArrayList<>();
            int index = 1;
            for (AsContentAdviceVO vo : page.getRecords()) {
                AsContentAdviceExportExcel excel = new AsContentAdviceExportExcel();
                excel.setSequence(String.valueOf(index++));
                excel.setSourceTypeDesc(vo.getSourceTypeDesc());
                excel.setAdviceName(vo.getAdviceName());
                excel.setSchoolName(vo.getSchoolName());
                excel.setCategoryName(vo.getCategoryName());
                excel.setAuthor(vo.getAuthor());
                excel.setMobile(vo.getMobile());
                excel.setStatusDesc(vo.getStatusDesc());
                excel.setCheckContent(vo.getCheckContent());
                excelList.add(excel);
            }


            ExcelUtils.exportExcel(response, excelList, AsContentAdviceExportExcel.class, "意见反馈结果记录", "意见反馈结果记录");
        } catch (Exception e) {
            log.error("导出意见反馈异常", e);
        }
    }

    @Override
    public JsonResult<AsContentAdviceVO> getAsContentAdviceDetail(Long id) {
        if (!CheckParamUtil.checkLongIdParam(id)) {
            return JsonResult.buildError("意见ID不能为空");
        }
        AsContentAdviceVO vo = baseMapper.selectContentAdviceDetail(id);
        if (vo == null) {
            return JsonResult.buildError("意见反馈记录不存在");
        }
        if(StringUtils.isNotBlank(vo.getFileUrl())){
            vo.setFileUrlList(baseFileService.fileListForUrlByPrefix(vo.getFileUrl(),","));
        }
        if(CheckParamUtil.checkLongIdParam(vo.getAuthorUpdate())){
            vo.setHandler(tbaseUserService.getById(vo.getAuthorUpdate()).getActualName());
        }
        return JsonResult.buildSuccess(vo);
    }
}
