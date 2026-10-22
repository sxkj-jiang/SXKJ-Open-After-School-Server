package com.sxkj.edu.module.afterschool.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.common.base.domain.BaseEntity;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.utils.DateUtils;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.afterschool.domain.dto.AsSchedulePeriodAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsScheduleTemplateListQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsScheduleTemplateQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.KhfwAsScheduleTemplateAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.ZxglAsScheduleTemplateAddAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsSchedulePeriodEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsScheduleTemplateEntity;
import com.sxkj.edu.module.afterschool.domain.entity.EduclGradeEntity;
import com.sxkj.edu.module.afterschool.domain.excel.AsScheduleTemplateImportExcel;
import com.sxkj.edu.module.afterschool.domain.vo.AsSchedulePeriodVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsScheduleTemplateListVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsScheduleTemplateVO;
import com.sxkj.edu.module.afterschool.domain.vo.KhfwAsScheduleTemplateDetailVO;
import com.sxkj.edu.module.afterschool.domain.vo.ZxglAsScheduleTemplateDetailVO;
import com.sxkj.edu.module.afterschool.mapper.AsSchedulePeriodMapper;
import com.sxkj.edu.module.afterschool.mapper.AsScheduleTemplateMapper;
import com.sxkj.edu.module.afterschool.mapper.EduclGradeMapper;
import com.sxkj.edu.module.business.entity.SchoolManagePo;
import com.sxkj.edu.module.mapper.SchoolManageMapper;
import com.sxkj.edu.common.utils.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

/**
 * 作息管理方案 Service
 *
 * @author YH
 * @date 2026-05-22
 */
@Service
@Slf4j
public class AsScheduleTemplateServiceImpl extends ServiceImpl<AsScheduleTemplateMapper, AsScheduleTemplateEntity> implements AsScheduleTemplateService {

    @Autowired
    private AsSchedulePeriodService asSchedulePeriodService;
    @Autowired
    private AsSchedulePeriodMapper asSchedulePeriodMapper;
    @Autowired
    private EduclGradeMapper educlGradeMapper;
    @Autowired
    private SchoolManageMapper schoolManageMapper;

    // =============================== 分页查询 ===============================

    /**
     * 作息管理方案-分页查询
     */
    @Override
    public PageResultDTO<AsScheduleTemplateVO> queryScheduleTemplatePage(AsScheduleTemplateQueryAO qo) {
        Page<AsScheduleTemplateVO> page = baseMapper.pageList(
                new Page<>(qo.getPageIndex(), qo.getPageSize()), qo);
        PageResultDTO<AsScheduleTemplateVO> result = new PageResultDTO<>();
        if(!CollectionUtils.isEmpty(page.getRecords())){
            LambdaQueryWrapper<EduclGradeEntity> gradeQueryWrapper = new LambdaQueryWrapper<>();
            gradeQueryWrapper.eq(BaseEntity::getIsValid,1).eq(EduclGradeEntity::getSchoolId,qo.getSchoolId());
            List<EduclGradeEntity> gradeList = educlGradeMapper.selectList(gradeQueryWrapper);
            page.getRecords().forEach(record -> {
                if(StringUtils.isNotBlank(record.getGradeScopeJson())){
                    List<Long> gradeIds = JSON.parseArray(record.getGradeScopeJson(), Long.class);
                    record.setGradeIdList(gradeIds);
                    if(!CollectionUtils.isEmpty(gradeList)){
                        List<String> gradeNames = gradeList.stream().filter(grade -> gradeIds.contains(grade.getId())).map(EduclGradeEntity::getGradeName).collect(Collectors.toList());
                        record.setGradeNameList(gradeNames);
                    }
                }
                String sjap = "";
                if(StringUtils.isNotBlank(record.getDayScopeJson())){
                    List<String> stringList = JSON.parseArray(record.getDayScopeJson(), String.class);
                    if(!CollectionUtils.isEmpty(stringList)){
                        sjap = "每"+stringList.get(0);
                        if(stringList.size()>1) sjap = sjap+"至"+stringList.get(stringList.size()-1)+" ";
                    }
                }
                // 查询课后时间记录列表
                LambdaQueryWrapper<AsSchedulePeriodEntity> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(AsSchedulePeriodEntity::getTemplateId, record.getId())
                        .eq(AsSchedulePeriodEntity::getPeriodType, 2)
                        .eq(AsSchedulePeriodEntity::getIsValid, 1)
                        .orderByAsc(AsSchedulePeriodEntity::getSectionNo);
                List<AsSchedulePeriodEntity> periodList = asSchedulePeriodService.list(wrapper);
                if(!CollectionUtils.isEmpty(periodList)){
                   sjap = sjap + periodList.stream().map(AsSchedulePeriodEntity::getSectionName).collect(Collectors.joining(" "));
                }
                record.setTimeArrange(sjap);
            });
        }
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setPageIndex(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setList(page.getRecords());
        return result;
    }

    // =============================== 新增 ===============================

    /**
     * 新增作息管理方案（含节次列表）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> addZxglScheduleTemplate(ZxglAsScheduleTemplateAddAO ao) {
        // 校验同一学校下是否已有相同名称的启用方案
        if (existsByName(ao.getSchoolId(), ao.getTemplateName(), ao.getTemplateType(), null)) {
            return JsonResult.buildError("该学校下已存在同名称的作息方案");
        }
        // 校验节次时间段是否重叠
        if (hasPeriodTimeOverlap(ao.getPeriodList())) {
            return JsonResult.buildError("时间段存在重叠，请调整时间");
        }
        // 保存主表
        AsScheduleTemplateEntity entity = new AsScheduleTemplateEntity();
        BeanUtils.copyProperties(ao, entity);
        entity.setEnabledStatus(0);
        entity.setIsValid(1);
        entity.setCreateTime(new Date());
        entity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
        boolean save = this.save(entity);
        // 保存节次列表
        save = savePeriodList(entity.getId(), ao.getSchoolId(), ao.getPeriodList());
        return JsonResult.buildSuccess("操作成功");
    }

    // =============================== 编辑 ===============================

    /**
     * 编辑作息管理方案（含节次列表）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> updateZxglScheduleTemplate(ZxglAsScheduleTemplateAddAO ao) {
        if (ao.getId() == null) {
            return JsonResult.buildError("方案ID不能为空");
        }
        AsScheduleTemplateEntity exist = this.getById(ao.getId());
        if (exist == null || !Integer.valueOf(1).equals(exist.getIsValid())) {
            return JsonResult.buildError("方案记录不存在");
        }
        // 启用时校验同一学校下是否已有相同名称的启用方案（排除自身）
        if (existsByName(ao.getSchoolId(), ao.getTemplateName(),ao.getTemplateType(), ao.getId())) {
            return JsonResult.buildError("该学校下已存在同名称的作息管理方案");
        }

        // 校验节次时间段是否重叠
        if (hasPeriodTimeOverlap(ao.getPeriodList())) {
            return JsonResult.buildError("时间段存在重叠，请调整时间");
        }

        // 更新主表
        AsScheduleTemplateEntity entity = new AsScheduleTemplateEntity();
        BeanUtils.copyProperties(ao, entity);
        entity.setUpdateTime(new Date());
        entity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        boolean flag = this.updateById(entity);
        // 删除旧节次（逻辑删除）后重新保存
        flag = logicDeletePeriods(ao.getId());
        flag = savePeriodList(ao.getId(), ao.getSchoolId(), ao.getPeriodList());
        if(!flag) return JsonResult.buildError("操作失败");
        return JsonResult.buildSuccess("操作成功");
    }

    // =============================== 删除 ===============================

    /**
     * 逻辑删除作息管理方案
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> deleteScheduleTemplate(Long id) {
        Boolean flag = false;
        AsScheduleTemplateEntity entity = this.getById(id);
        if (entity == null || !Integer.valueOf(1).equals(entity.getIsValid())) {
            return JsonResult.buildError("方案记录不存在");
        }
        if(1 == entity.getEnabledStatus()) return JsonResult.buildError("启用的方案不能删除");
        entity.setIsValid(0);
        entity.setUpdateTime(new Date());
        entity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        flag = this.updateById(entity);
        // 同时逻辑删除节次
        flag = logicDeletePeriods(id);
        if(!flag) return JsonResult.buildError("操作失败");
        return JsonResult.buildSuccess("操作成功");
    }

    /**
     * 作息管理-复制方案
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> copyZxglScheduleTemplate(Long id) {
        AsScheduleTemplateEntity source = this.getById(id);
        if (source == null || !Integer.valueOf(1).equals(source.getIsValid())) {
            return JsonResult.buildError("方案记录不存在");
        }

        // 计算复制名称：原名称_copy_复制次数
        String baseName = source.getTemplateName();
        // 去除已有的_copy_N后缀，获取原始名称
        String originalName = baseName.replaceAll("_copy_\\d+$", "");
        // 统计同学校下以originalName_copy_开头的记录数量，确定复制次数
        LambdaQueryWrapper<AsScheduleTemplateEntity> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(AsScheduleTemplateEntity::getIsValid, 1)
                .eq(AsScheduleTemplateEntity::getSchoolId, source.getSchoolId())
                .likeRight(AsScheduleTemplateEntity::getTemplateName, originalName + "_copy_");
        long copyCount = this.count(countWrapper);
        String newTemplateName = originalName + "_copy_" + (copyCount + 1);

        // 复制主表
        AsScheduleTemplateEntity newEntity = new AsScheduleTemplateEntity();
        BeanUtils.copyProperties(source, newEntity);
        newEntity.setId(null);
        newEntity.setTemplateName(newTemplateName);
        newEntity.setEnabledStatus(0);
        newEntity.setCreateTime(new Date());
        newEntity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
        newEntity.setUpdateTime(null);
        newEntity.setUpdateUser(null);
        boolean save = this.save(newEntity);

        // 复制子表节次
        LambdaQueryWrapper<AsSchedulePeriodEntity> periodWrapper = new LambdaQueryWrapper<>();
        periodWrapper.eq(AsSchedulePeriodEntity::getTemplateId, id)
                .eq(AsSchedulePeriodEntity::getIsValid, 1);
        List<AsSchedulePeriodEntity> sourcePeriods = asSchedulePeriodService.list(periodWrapper);
        if (!CollectionUtils.isEmpty(sourcePeriods)) {
            List<AsSchedulePeriodEntity> newPeriods = sourcePeriods.stream().map(p -> {
                AsSchedulePeriodEntity newPeriod = new AsSchedulePeriodEntity();
                BeanUtils.copyProperties(p, newPeriod);
                newPeriod.setId(null);
                newPeriod.setTemplateId(newEntity.getId());
                newPeriod.setCreateTime(new Date());
                newPeriod.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
                newPeriod.setUpdateTime(null);
                newPeriod.setUpdateUser(null);
                return newPeriod;
            }).collect(Collectors.toList());
           save = asSchedulePeriodService.saveBatch(newPeriods);
        }
        if(!save) return JsonResult.buildError("复制失败");
        return JsonResult.buildSuccess("复制成功");
    }

    // =============================== 详情 ===============================

    /**
     * 查询方案详情（含节次列表）
     */
    @Override
    public JsonResult<ZxglAsScheduleTemplateDetailVO> getZxglScheduleTemplateDetail(Long id) {
        AsScheduleTemplateEntity entity = this.getById(id);
        if (entity == null || !Integer.valueOf(1).equals(entity.getIsValid())) {
            return JsonResult.buildError("方案记录不存在");
        }
        LambdaQueryWrapper<AsSchedulePeriodEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsSchedulePeriodEntity::getTemplateId, id)
                .eq(AsSchedulePeriodEntity::getIsValid, 1);
        List<AsSchedulePeriodEntity> periodList = asSchedulePeriodService.list(wrapper);
        ZxglAsScheduleTemplateDetailVO vo = new ZxglAsScheduleTemplateDetailVO();
        BeanUtils.copyProperties(entity, vo);
        if(!CollectionUtils.isEmpty(periodList)){
            vo.setPeriodList(periodList.stream().map(p -> {
                AsSchedulePeriodVO periodVO = new AsSchedulePeriodVO();
                BeanUtils.copyProperties(p, periodVO);
                return periodVO;
            }).collect(Collectors.toList()));
        }
        return JsonResult.buildSuccess(vo);
    }

    @Override
    public JsonResult<ZxglAsScheduleTemplateDetailVO> getZxglScheduleTemplateEnableDetail(Long schoolId) {
        ZxglAsScheduleTemplateDetailVO vo = new ZxglAsScheduleTemplateDetailVO();
        LambdaQueryWrapper<AsScheduleTemplateEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsScheduleTemplateEntity::getSchoolId, schoolId)
                .eq(AsScheduleTemplateEntity::getTemplateType, 1)
                .eq(AsScheduleTemplateEntity::getEnabledStatus, 1)
                .eq(AsScheduleTemplateEntity::getIsValid, 1);
        List<AsScheduleTemplateEntity> list = this.list(wrapper);
        if (!CollectionUtils.isEmpty(list)) {
            AsScheduleTemplateEntity entity = list.get(0);
            BeanUtils.copyProperties(entity, vo);
            LambdaQueryWrapper<AsSchedulePeriodEntity> pw = new LambdaQueryWrapper<>();
            pw.eq(AsSchedulePeriodEntity::getTemplateId, entity.getId())
                    .eq(AsSchedulePeriodEntity::getIsValid, 1);
            List<AsSchedulePeriodEntity> periodList = asSchedulePeriodService.list(pw);
            if(!CollectionUtils.isEmpty(periodList)){
                vo.setPeriodList(periodList.stream().map(p -> {
                    AsSchedulePeriodVO periodVO = new AsSchedulePeriodVO();
                    BeanUtils.copyProperties(p, periodVO);
                    return periodVO;
                }).collect(Collectors.toList()));
            }
        }
        return JsonResult.buildSuccess(vo);
    }

    // =============================== 启用/禁用 ===============================

    /**
     * 作息管理方案-启用/禁用
     */
    @Override
    public JsonResult<String> updateEnabledStatus(Long id, Integer enabledStatus) {
        AsScheduleTemplateEntity entity = this.getById(id);
        if (entity == null || !Integer.valueOf(1).equals(entity.getIsValid())) {
            return JsonResult.buildError("方案记录不存在");
        }
        // 启用时校验同一学校下是否已有相同名称的启用方案
        if (Integer.valueOf(1).equals(enabledStatus)) {
            if (existsEnabledByName(entity.getSchoolId(), entity.getTemplateName(), entity.getTemplateType(), id)) {
                return JsonResult.buildError("该学校下已存在同名称的启用方案");
            }
        }
        //每个学校只能启用一个作息管理方案
        if(1 == entity.getTemplateType() && 1 == enabledStatus){
            LambdaQueryWrapper<AsScheduleTemplateEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(AsScheduleTemplateEntity::getSchoolId, entity.getSchoolId())
                    .eq(AsScheduleTemplateEntity::getTemplateType, entity.getTemplateType())
                    .ne(AsScheduleTemplateEntity::getId, id)
                    .eq(AsScheduleTemplateEntity::getEnabledStatus, 1);
            List<AsScheduleTemplateEntity> list = this.list(wrapper);
            if (!CollectionUtils.isEmpty(list)) {
                return JsonResult.buildError("作息管理已有启用方案");
            }
        }
        // 每个学校每个年级只能启用一个课后时间方案
        if (2 == entity.getTemplateType() && 1 == enabledStatus) {
            // 获取当前方案的课后时间段记录
            List<AsSchedulePeriodEntity> currentPeriodList = asSchedulePeriodService.list(
                    new LambdaQueryWrapper<AsSchedulePeriodEntity>()
                            .eq(AsSchedulePeriodEntity::getTemplateId, id)
                            .eq(AsSchedulePeriodEntity::getPeriodType, 2)
                            .eq(AsSchedulePeriodEntity::getIsValid, 1)
            );
            if (CollectionUtil.isNotEmpty(currentPeriodList)) {
                AsSchedulePeriodEntity periodEntity = currentPeriodList.get(0);
                if(StringUtils.isNotBlank(periodEntity.getGradeScopeJson())){
                    List<Long> gradeScope = JSON.parseArray(periodEntity.getGradeScopeJson(), Long.class);
                    // 通过SQL检查年级是否有交集
                    int overlapCount = asSchedulePeriodMapper.countOverlappingEnabledPeriods(
                            entity.getSchoolId(), id, gradeScope);
                    if (overlapCount > 0) {
                        return JsonResult.buildError("每个年级只能启用一个课后时间方案");
                    }
                }
            }
        }
        entity.setEnabledStatus(enabledStatus);
        entity.setUpdateTime(new Date());
        entity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        boolean flag = this.updateById(entity);
        if(!flag) return JsonResult.buildError("操作失败");
        return JsonResult.buildSuccess("操作成功");
    }

    // =============================== 下拉列表查询 ===============================

    /**
     * 方案下拉列表查询（支持多条件筛选）
     */
    @Override
    public List<AsScheduleTemplateListVO> queryScheduleTemplateList(AsScheduleTemplateListQueryAO qo) {
        LambdaQueryWrapper<AsScheduleTemplateEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsScheduleTemplateEntity::getIsValid, 1)
                .eq(AsScheduleTemplateEntity::getEnabledStatus, 1)
                .eq(qo.getSchoolId() != null, AsScheduleTemplateEntity::getSchoolId, qo.getSchoolId())
                .like(StringUtils.isNotBlank(qo.getTemplateName()), AsScheduleTemplateEntity::getTemplateName, qo.getTemplateName())
                .eq(qo.getTemplateType() != null, AsScheduleTemplateEntity::getTemplateType, qo.getTemplateType())
                .orderByDesc(AsScheduleTemplateEntity::getCreateTime);
        List<AsScheduleTemplateEntity> entityList = this.list(wrapper);
        return entityList.stream().map(entity -> {
            AsScheduleTemplateListVO vo = new AsScheduleTemplateListVO();
            vo.setId(entity.getId());
            vo.setTemplateName(entity.getTemplateName());
            vo.setTemplateType(entity.getTemplateType());
            vo.setEnabledStatus(entity.getEnabledStatus());
            return vo;
        }).collect(Collectors.toList());
    }

    // =============================== 私有方法 ===============================

    /**
     * 批量保存节次列表
     */
    private Boolean savePeriodList(Long templateId, Long schoolId, List<AsSchedulePeriodAO> periodList) {
        List<AsSchedulePeriodEntity> entities = periodList.stream().map(p -> {
            AsSchedulePeriodEntity pe = new AsSchedulePeriodEntity();
            pe.setTemplateId(templateId);
            pe.setSchoolId(schoolId);
            pe.setTimeBlock(p.getTimeBlock());
            pe.setSectionNo(p.getSectionNo());
            pe.setSectionName(p.getSectionName());
            pe.setStartTime(p.getStartTime());
            pe.setEndTime(p.getEndTime());
            pe.setPeriodType(1); // 普通节次
            pe.setIsValid(1);
            pe.setCreateTime(new Date());
            pe.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
            return pe;
        }).collect(Collectors.toList());
        boolean flag = asSchedulePeriodService.saveBatch(entities);
        return flag;
    }

    /**
     * 逻辑删除方案下所有节次
     */

    private boolean logicDeletePeriods(Long templateId) {
        boolean flag = false;
        LambdaQueryWrapper<AsSchedulePeriodEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsSchedulePeriodEntity::getTemplateId, templateId)
                .eq(AsSchedulePeriodEntity::getIsValid, 1);
        List<AsSchedulePeriodEntity> periods = asSchedulePeriodService.list(wrapper);
        if (!CollectionUtils.isEmpty(periods)) {
            periods.forEach(p -> {
                p.setIsValid(0);
                p.setUpdateTime(new Date());
                p.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
            });
           flag = asSchedulePeriodService.updateBatchById(periods);
        }
        return flag;
    }

    /**
     * 校验同一学校下是否已有启用的相同名称方案（排除自身）
     *
     * @param schoolId     学校ID
     * @param templateName 方案名称
     * @param excludeId    排除的方案ID（新增时为null）
     * @return true 表示已存在
     */
    private boolean existsEnabledByName(Long schoolId, String templateName,Integer type, Long excludeId) {
        LambdaQueryWrapper<AsScheduleTemplateEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsScheduleTemplateEntity::getIsValid, 1)
                .eq(AsScheduleTemplateEntity::getSchoolId, schoolId)
                .eq(AsScheduleTemplateEntity::getTemplateType, type)
                .eq(AsScheduleTemplateEntity::getEnabledStatus, 1)
                .eq(StrUtil.isNotBlank(templateName), AsScheduleTemplateEntity::getTemplateName, templateName);
        if (excludeId != null) {
            wrapper.ne(AsScheduleTemplateEntity::getId, excludeId);
        }
        return this.count(wrapper) > 0;
    }

    /**
     * 校验同一学校下是否已有同名称方案（排除自身）
     */
    private boolean existsByName(Long schoolId, String templateName,Integer type, Long excludeId) {
        LambdaQueryWrapper<AsScheduleTemplateEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsScheduleTemplateEntity::getIsValid, 1)
                .eq(AsScheduleTemplateEntity::getSchoolId, schoolId)
                .eq(AsScheduleTemplateEntity::getTemplateType, type)
                .eq(StrUtil.isNotBlank(templateName), AsScheduleTemplateEntity::getTemplateName, templateName);
        if (excludeId != null) {
            wrapper.ne(AsScheduleTemplateEntity::getId, excludeId);
        }
        return this.count(wrapper) > 0;
    }

    /**
     * 校验节次列表中是否存在时间段重叠
     * 判断规则：两个时间段 [s1,e1] 和 [s2,e2] 重叠当且仅当 s1 < e2 且 s2 < e1
     * 增加校验：每节课必须大于30分钟
     */
    private boolean hasPeriodTimeOverlap(List<AsSchedulePeriodAO> periodList) {
        if (CollectionUtils.isEmpty(periodList) || periodList.size() < 2) {
            return false;
        }
        for (int i = 0; i < periodList.size(); i++) {
            AsSchedulePeriodAO a = periodList.get(i);
            if (a.getStartTime() == null || a.getEndTime() == null) continue;
            for (int j = i + 1; j < periodList.size(); j++) {
                AsSchedulePeriodAO b = periodList.get(j);
                if (b.getStartTime() == null || b.getEndTime() == null) continue;
                // a.start < b.end && b.start < a.end => 重叠
                if (a.getStartTime().before(b.getEndTime()) && b.getStartTime().before(a.getEndTime())) {
                    return true;
                }else if(DateUtils.isDurationGreaterThan45Minutes(a.getStartTime(), a.getEndTime())){
                    log.warn("call DateUtils.isDurationGreater le 45Minutes, a.getStartTime():{}, a.getEndTime(){}", a.getStartTime(), a.getEndTime());
                    return true;
                }
            }
        }
        return false;
    }

    // =============================== 课后时间管理 ===============================

    /**
     * 课后时间管理-新增
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> addKhfwScheduleTemplate(KhfwAsScheduleTemplateAddAO ao) {
        if(existsByName(ao.getSchoolId(), ao.getTemplateName(), ao.getTemplateType(), null)){
            return JsonResult.buildError("该学校下已存在同名称的课后时间方案");
        }
        // 保存主表
        AsScheduleTemplateEntity entity = new AsScheduleTemplateEntity();
        BeanUtils.copyProperties(ao, entity);
        entity.setEnabledStatus(0);
        entity.setTemplateType(2);
        entity.setIsValid(1);
        entity.setCreateTime(new Date());
        entity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
        boolean save = this.save(entity);
        // 批量保存课后时间段记录到 t_as_schedule_period
        List<AsSchedulePeriodEntity> periodList = new ArrayList<>();
        for (int i = 0; i < ao.getPeriodList().size(); i++) {
            AsSchedulePeriodEntity period = new AsSchedulePeriodEntity();
            period.setTemplateId(entity.getId());
            period.setSchoolId(ao.getSchoolId());
            period.setPeriodType(2); // 课后时间段
            period.setTimeBlock(ao.getPeriodList().get(i).getTimeBlock());
            period.setDayScopeJson(JSON.toJSONString(ao.getDayScopeList()));
            period.setGradeScopeJson(JSON.toJSONString(ao.getGradeScopeList()));
            period.setSectionNo(ao.getPeriodList().get(i).getSectionNo());
            period.setSectionName(i < ao.getPeriodList().size() ? ao.getPeriodList().get(i).getSectionName() : null);
            period.setStartTime(ao.getPeriodList().get(i).getStartTime());
            period.setEndTime(ao.getPeriodList().get(i).getEndTime());
            period.setIsValid(1);
            period.setCreateTime(new Date());
            period.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
            periodList.add(period);
        }
        save = asSchedulePeriodService.saveBatch(periodList);
        if(!save) return JsonResult.buildError("操作失败");
        return JsonResult.buildSuccess("操作成功");
    }

    /**
     * 课后时间管理-编辑
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> updateKhfwScheduleTemplate(KhfwAsScheduleTemplateAddAO ao) {
        if (ao.getId() == null) {
            return JsonResult.buildError("方案ID不能为空");
        }
        AsScheduleTemplateEntity exist = this.getById(ao.getId());
        if (exist == null || !Integer.valueOf(1).equals(exist.getIsValid())) {
            return JsonResult.buildError("方案记录不存在");
        }
        // 校验同一学校下是否已有相同名称的课后时间方案（排除自身）
        if (existsByName(ao.getSchoolId(), ao.getTemplateName(), ao.getTemplateType(), ao.getId())) {
            return JsonResult.buildError("该学校下已存在同名称的课后时间方案");
        }
        // 更新主表
        AsScheduleTemplateEntity entity = new AsScheduleTemplateEntity();
        BeanUtils.copyProperties(ao, entity);
        entity.setTemplateType(2);
        entity.setUpdateTime(new Date());
        entity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        this.updateById(entity);
        // 先逻辑删除原有课后时间段记录，再全量新增
        LambdaQueryWrapper<AsSchedulePeriodEntity> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(AsSchedulePeriodEntity::getTemplateId, ao.getId())
                .eq(AsSchedulePeriodEntity::getPeriodType, 2)
                .eq(AsSchedulePeriodEntity::getIsValid, 1);
        AsSchedulePeriodEntity deleteEntity = new AsSchedulePeriodEntity();
        deleteEntity.setIsValid(0);
        deleteEntity.setUpdateTime(new Date());
        deleteEntity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        asSchedulePeriodService.update(deleteEntity, deleteWrapper);
        // 根据sectionNoList批量新增课后时间段记录
        List<AsSchedulePeriodEntity> periodList = new ArrayList<>();
        for (int i = 0; i < ao.getPeriodList().size(); i++) {
            AsSchedulePeriodEntity newPeriod = new AsSchedulePeriodEntity();
            newPeriod.setTemplateId(ao.getId());
            newPeriod.setSchoolId(ao.getSchoolId());
            newPeriod.setPeriodType(2);
            newPeriod.setDayScopeJson(JSON.toJSONString(ao.getDayScopeList()));
            newPeriod.setGradeScopeJson(JSON.toJSONString(ao.getGradeScopeList()));
            newPeriod.setTimeBlock(ao.getPeriodList().get(i).getTimeBlock());
            newPeriod.setSectionNo(ao.getPeriodList().get(i).getSectionNo());
            newPeriod.setSectionName(i < ao.getPeriodList().size() ? ao.getPeriodList().get(i).getSectionName() : null);
            newPeriod.setStartTime(ao.getPeriodList().get(i).getStartTime());
            newPeriod.setEndTime(ao.getPeriodList().get(i).getEndTime());
            newPeriod.setIsValid(1);
            newPeriod.setCreateTime(new Date());
            newPeriod.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
            periodList.add(newPeriod);
        }
        asSchedulePeriodService.saveBatch(periodList);
        return JsonResult.buildSuccess("操作成功");
    }

    /**
     * 课后时间管理-查询详情（一对一：主表+period表单条记录）
     */
    @Override
    public JsonResult<KhfwAsScheduleTemplateDetailVO> getKhfwScheduleTemplateDetail(Long id) {
        AsScheduleTemplateEntity entity = this.getById(id);
        if (entity == null || !Integer.valueOf(1).equals(entity.getIsValid())) {
            return JsonResult.buildError("方案记录不存在");
        }
        KhfwAsScheduleTemplateDetailVO vo = new KhfwAsScheduleTemplateDetailVO();
        BeanUtils.copyProperties(entity, vo);
        // 查询课后时间记录列表
        LambdaQueryWrapper<AsSchedulePeriodEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsSchedulePeriodEntity::getTemplateId, id)
                .eq(AsSchedulePeriodEntity::getPeriodType, 2)
                .eq(AsSchedulePeriodEntity::getIsValid, 1)
                .orderByAsc(AsSchedulePeriodEntity::getSectionNo);
        List<AsSchedulePeriodEntity> periodList = asSchedulePeriodService.list(wrapper);
        if (!periodList.isEmpty()) {
            AsSchedulePeriodEntity first = periodList.get(0);
            if(StringUtils.isNotBlank(first.getDayScopeJson())){
                vo.setDayScopeList(JSON.parseArray(first.getDayScopeJson(), String.class).stream().collect(Collectors.toList()));
            }
            if(StringUtils.isNotBlank(first.getGradeScopeJson())){
                vo.setGradeScopeList(JSON.parseArray(first.getGradeScopeJson(), Long.class).stream().collect(Collectors.toList()));
            }
            List<AsSchedulePeriodAO> periodVOList = new ArrayList<>();
            periodList.forEach(period -> {
                AsSchedulePeriodAO periodVO = new AsSchedulePeriodAO();
                SmartBeanUtil.copyProperties(period, periodVO);
                periodVOList.add(periodVO);
            });
            vo.setPeriodList(periodVOList);
        }
        return JsonResult.buildSuccess(vo);
    }

    // =============================== 课后时间管理-批量导入 ===============================

    /**
     * 课后时间管理-批量导入
     * 开始时间和结束时间根据节次查询学校启用的作息方案(template_type=1, enabled_status=1)关联的节次表获取
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> importKhfwScheduleTemplate(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            List<AsScheduleTemplateImportExcel> list = ExcelUtils.customerReadExcel(is, AsScheduleTemplateImportExcel.class, 2);
            if (list == null || list.isEmpty()) {
                return JsonResult.buildError("导入数据为空");
            }

            // 学校名称->ID映射
            Map<String, Long> schoolNameIdMap = new HashMap<>();
            schoolManageMapper.selectList(new LambdaQueryWrapper<SchoolManagePo>().eq(SchoolManagePo::getIsValid, 1))
                    .forEach(s -> schoolNameIdMap.put(s.getSchoolName(), s.getId()));

            // 预加载年级名称->ID映射（按学校分组）
            List<EduclGradeEntity> allGrades = educlGradeMapper.selectList(
                    new LambdaQueryWrapper<EduclGradeEntity>().eq(EduclGradeEntity::getIsValid, 1));
            Map<String, Long> gradeNameIdMap = new HashMap<>();
            allGrades.forEach(g -> gradeNameIdMap.put(g.getSchoolId() + "_" + g.getGradeName(), g.getId()));

            List<String> errors = new ArrayList<>();
            int rowNum = 2;
            Long userId = SmartRequestTokenUtil.getRequestUserId();

            for (AsScheduleTemplateImportExcel row : list) {
                rowNum++;
                String schoolName = row.getSchoolName();
                String templateName = row.getTemplateName();

                // 校验学校
                Long schoolId = schoolNameIdMap.get(schoolName);
                if (schoolId == null) {
                    errors.add("第" + rowNum + "行: 学校名称[" + schoolName + "]不存在");
                    continue;
                }
                if (StringUtils.isBlank(templateName)) {
                    errors.add("第" + rowNum + "行: 课后时间名称不能为空");
                    continue;
                }
                if (StringUtils.isBlank(row.getGradeNames())) {
                    errors.add("第" + rowNum + "行: 适用年级不能为空");
                    continue;
                }
                if (StringUtils.isBlank(row.getDayScope())) {
                    errors.add("第" + rowNum + "行: 适用星期不能为空");
                    continue;
                }
                if (StringUtils.isBlank(row.getSectionNo())) {
                    errors.add("第" + rowNum + "行: 适用节次不能为空");
                    continue;
                }

                // 适用年级（分号隔开）
                List<Long> gradeIds = new ArrayList<>();
                if (StringUtils.isNotBlank(row.getGradeNames())) {
                    String[] gradeNameArr = row.getGradeNames().split(";");
                    for (String gn : gradeNameArr) {
                        gn = gn.trim();
                        if (gn.isEmpty()) continue;
                        Long gradeId = gradeNameIdMap.get(schoolId + "_" + gn);
                        if (gradeId == null) {
                            errors.add("第" + rowNum + "行: 年级名称[" + gn + "]在学校[" + schoolName + "]下不存在");
                            continue;
                        }
                        gradeIds.add(gradeId);
                    }
                }

                // 适用星期（顿号隔开）
                List<String> dayScopeList = new ArrayList<>();
                if (StringUtils.isNotBlank(row.getDayScope())) {
                    String[] dayArr = row.getDayScope().split("、");
                    for (String d : dayArr) {
                        String dayNum = "周"+d;
                        dayScopeList.add(dayNum);
                    }
                }

                // 适用节次（顿号隔开）
                List<Integer> sectionNos = new ArrayList<>();
                if (StringUtils.isNotBlank(row.getSectionNo())) {
                    String[] secArr = row.getSectionNo().split("、");
                    for (String s : secArr) {
                        if (StringUtils.isNotBlank(s.trim())) {
                            try {
                                sectionNos.add(Integer.parseInt(s.trim()));
                            } catch (NumberFormatException e) {
                                errors.add("第" + rowNum + "行: 节次[" + s + "]格式不正确，应为数字");
                            }
                        }
                    }
                }

                // 查询该学校启用的作息方案的节次信息
                Map<Integer, AsSchedulePeriodEntity> schedulePeriodMap = new HashMap<>();
                LambdaQueryWrapper<AsScheduleTemplateEntity> zxTemplateWrapper = new LambdaQueryWrapper<>();
                zxTemplateWrapper.eq(AsScheduleTemplateEntity::getIsValid, 1)
                        .eq(AsScheduleTemplateEntity::getSchoolId, schoolId)
                        .eq(AsScheduleTemplateEntity::getTemplateType, 1)
                        .eq(AsScheduleTemplateEntity::getEnabledStatus, 1);
                AsScheduleTemplateEntity zxTemplate = this.getOne(zxTemplateWrapper, false);
                if (zxTemplate != null) {
                    LambdaQueryWrapper<AsSchedulePeriodEntity> periodWrapper = new LambdaQueryWrapper<>();
                    periodWrapper.eq(AsSchedulePeriodEntity::getTemplateId, zxTemplate.getId())
                            .in(AsSchedulePeriodEntity::getSectionNo, sectionNos)
                            .eq(AsSchedulePeriodEntity::getIsValid, 1);
                    List<AsSchedulePeriodEntity> zxPeriods = asSchedulePeriodService.list(periodWrapper);
                    for (AsSchedulePeriodEntity p : zxPeriods) {
                        if (p.getSectionNo() != null) {
                            schedulePeriodMap.put(p.getSectionNo(), p);
                        }
                    }
                } else {
                    errors.add("第" + rowNum + "行: 学校[" + schoolName + "]未找到启用的作息方案，无法获取节次时间");
                    continue;
                }

                // 查找是否已存在同名课后时间方案
                LambdaQueryWrapper<AsScheduleTemplateEntity> tplWrapper = new LambdaQueryWrapper<>();
                tplWrapper.eq(AsScheduleTemplateEntity::getIsValid, 1)
                        .eq(AsScheduleTemplateEntity::getSchoolId, schoolId)
                        .eq(AsScheduleTemplateEntity::getTemplateType, 2)
                        .eq(AsScheduleTemplateEntity::getTemplateName, templateName);
                AsScheduleTemplateEntity existTemplate = this.getOne(tplWrapper, false);

                Long templateId;
                if (existTemplate != null) {
                    // 已存在：逻辑删除旧节次
                    templateId = existTemplate.getId();
                    LambdaQueryWrapper<AsSchedulePeriodEntity> delWrapper = new LambdaQueryWrapper<>();
                    delWrapper.eq(AsSchedulePeriodEntity::getTemplateId, templateId)
                            .eq(AsSchedulePeriodEntity::getPeriodType, 2)
                            .eq(AsSchedulePeriodEntity::getIsValid, 1);
                    AsSchedulePeriodEntity delEntity = new AsSchedulePeriodEntity();
                    delEntity.setIsValid(0);
                    delEntity.setUpdateTime(new Date());
                    delEntity.setUpdateUser(userId);
                    asSchedulePeriodService.update(delEntity, delWrapper);
                } else {
                    // 不存在：新增主表
                    AsScheduleTemplateEntity entity = new AsScheduleTemplateEntity();
                    entity.setSchoolName(schoolName);
                    entity.setSchoolId(schoolId);
                    entity.setTemplateName(templateName);
                    entity.setTemplateType(2);
                    entity.setEnabledStatus(0);
                    entity.setIsValid(1);
                    entity.setCreateTime(new Date());
                    entity.setCreateUser(userId);
                    this.save(entity);
                    templateId = entity.getId();
                }

                // 逐行新增节次记录，开始/结束时间从作息方案节次中获取
                String dayScopeJson = !dayScopeList.isEmpty() ? JSON.toJSONString(dayScopeList) : null;
                String gradeScopeJson = !gradeIds.isEmpty() ? JSON.toJSONString(gradeIds) : null;

                List<AsSchedulePeriodEntity> periodList = new ArrayList<>();
                for (int i = 0; i < sectionNos.size(); i++) {
                    Integer secNo = sectionNos.get(i);
                    AsSchedulePeriodEntity schedulePeriod = schedulePeriodMap.get(secNo);
                    if (schedulePeriod == null) {
                        errors.add("第" + rowNum + "行: 节次[" + secNo + "]在作息方案中未找到对应的时间配置");
                        continue;
                    }

                    AsSchedulePeriodEntity period = new AsSchedulePeriodEntity();
                    period.setTemplateId(templateId);
                    period.setSchoolId(schoolId);
                    period.setPeriodType(2);
                    period.setDayScopeJson(dayScopeJson);
                    period.setGradeScopeJson(gradeScopeJson);
                    period.setTimeBlock(2); // 课后时间段
                    period.setSectionNo(secNo);
                    // 转中文节次名
                    String[] cnNums = { "一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};
                    period.setSectionName("第" + cnNums[secNo-1] + "节");
                    // 从作息方案节次中获取开始和结束时间
                    period.setStartTime(schedulePeriod.getStartTime());
                    period.setEndTime(schedulePeriod.getEndTime());
                    period.setIsValid(1);
                    period.setCreateTime(new Date());
                    period.setCreateUser(userId);
                    periodList.add(period);
                }
                if (!periodList.isEmpty()) {
                    asSchedulePeriodService.saveBatch(periodList);
                }
            }

            if (!errors.isEmpty()) {
                return JsonResult.buildError("部分数据导入失败：" + String.join("；", errors));
            }
            return JsonResult.buildSuccess("导入成功");
        } catch (Exception e) {
            log.error("课后时间导入异常", e);
            return JsonResult.buildError("导入异常：" + e.getMessage());
        }
    }
}
