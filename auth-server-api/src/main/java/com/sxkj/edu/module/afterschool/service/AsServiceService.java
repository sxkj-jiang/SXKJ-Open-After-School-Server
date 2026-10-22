package com.sxkj.edu.module.afterschool.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.base.enums.FileTypeEnum;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.exception.BaseBusinessException;
import com.sxkj.edu.common.redis.RedisCache;
import com.sxkj.edu.common.utils.DateUtils;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.config.AssembleUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.config.async.AsClassAssignAsync;
import com.sxkj.edu.domain.base.khfw.ao.AsStudentBindParentAO;
import com.sxkj.edu.domain.base.khfw.vo.AsStudentBindParentVO;
import com.sxkj.edu.module.afterschool.domain.ao.CourseScheduleDetailListQueryAO;
import com.sxkj.edu.module.afterschool.domain.ao.CourseSchedulePageQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.*;
import com.sxkj.edu.module.afterschool.domain.entity.*;
import com.sxkj.edu.domain.sys.entity.mult.TEduclClass;
import com.sxkj.edu.domain.sys.entity.BaseFileEntity;
import com.sxkj.edu.module.system.employee.domain.entity.TBaseUserEntity;
import com.sxkj.edu.service.BaseFileService;
import com.sxkj.edu.module.afterschool.domain.vo.*;
import com.sxkj.edu.module.afterschool.domain.excel.AsServiceExportExcel;
import com.sxkj.edu.module.afterschool.enums.AsServiceDisplayTabEnum;
import com.sxkj.edu.module.afterschool.mapper.AsCourseCategoryMapper;
import com.sxkj.edu.module.afterschool.mapper.AsCourseClassMapper;
import com.sxkj.edu.module.afterschool.mapper.AsCourseScheduleRuleMapper;
import com.sxkj.edu.module.afterschool.mapper.AsCourseSessionMapper;
import com.sxkj.edu.module.afterschool.mapper.AsEnrollmentLogMapper;
import com.sxkj.edu.module.afterschool.mapper.AsEnrollmentMapper;
import com.sxkj.edu.module.afterschool.mapper.AsScheduleTemplateMapper;
import com.sxkj.edu.module.afterschool.mapper.AsServiceMapper;
import com.sxkj.edu.module.afterschool.mapper.AsStudentMapper;
import com.sxkj.edu.module.afterschool.mapper.AsTeacherMapper;
import com.sxkj.edu.module.afterschool.mapper.EduclTermMapper;
import com.sxkj.edu.module.afterschool.mapper.EduclClassMapper;
import com.sxkj.edu.module.afterschool.mapper.EduclGradeMapper;
import com.sxkj.edu.module.afterschool.support.CourseClassScopeSupport;
import com.sxkj.edu.module.afterschool.support.AsAsyncOperatorContext;
import com.sxkj.edu.module.afterschool.support.CourseScheduleSessionExpander;
import com.sxkj.edu.module.afterschool.support.SessionEnrollmentSupport;
import com.sxkj.edu.module.afterschool.support.StudentTermScopeSupport;
import com.sxkj.edu.module.afterschool.support.TermSuspensionDateSupport;
import com.sxkj.edu.service.SchoolManageService;
import com.sxkj.edu.module.system.employee.TBaseUserDao;
import com.sxkj.edu.module.system.employee.domain.dto.EmployeeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cn.hutool.core.util.StrUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import com.sxkj.edu.common.utils.ExcelUtils;
import com.sxkj.edu.module.afterschool.domain.excel.AsAdminClassImportExcel;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 课后服务活动Service实现类。
 *
 * @author system
 * @date 2026-05-17
 */
@Service
@Slf4j
public class AsServiceService extends ServiceImpl<AsServiceMapper, AsServiceEntity> {

    @Resource
    private AsCourseClassMapper asCourseClassMapper;

    @Resource
    private AsCourseQuotaService asCourseQuotaService;

    @Resource
    private ExamRoomService examRoomService;

    @Resource
    private AsCourseSessionMapper asCourseSessionMapper;

    @Resource
    private AsCourseScheduleRuleMapper asCourseScheduleRuleMapper;

    @Resource
    private AsEnrollmentMapper asEnrollmentMapper;

    @Resource
    private AsEnrollmentLogMapper asEnrollmentLogMapper;

    @Resource
    private AsStudentMapper asStudentMapper;


    @Resource
    private EduclTermMapper educlTermMapper;

    @Resource
    private AsScheduleTemplateMapper asScheduleTemplateMapper;

    @Resource
    private AsCourseCategoryMapper asCourseCategoryMapper;

    @Resource
    private AsTeacherMapper asTeacherMapper;

    @Resource
    private AsCourseScheduleConflictValidator courseScheduleConflictValidator;

    @Resource
    private AsServiceCoreValidator asServiceCoreValidator;

    @Resource
    private AsServiceHelper serviceHelper;

    @Resource
    private AsEnrollmentAdminQueryService adminQueryService;

    @Resource
    private EduclClassMapper educlClassMapper;

    @Resource
    private EduclGradeMapper educlGradeMapper;

    @Resource
    private CourseClassScopeSupport courseClassScopeSupport;

    @Resource
    private SessionEnrollmentSupport sessionEnrollmentSupport;

    @Resource
    private StudentTermScopeSupport studentTermScopeSupport;

    @Resource
    private TermSuspensionDateSupport termSuspensionDateSupport;

    @Resource
    private SchoolManageService schoolManageService;

    @Resource
    private TBaseUserDao tBaseUserDao;

    @Resource
    private EduclTermService educlTermService;

    @Autowired
    private RedisCache redisCache;

    @Resource
    private BaseFileService baseFileService;

    /**
     * 分页查询课后服务列表。
     * 支持设计图顶部搜索区中的学期、状态、服务名称和开课周期筛选，并补充课程班/课次数量摘要。
     * 列表 Tab 筛选请传 {@code displayTab}，与 {@link #countServiceStatuses} 口径一致。
     */
    public PageResultDTO<AsServiceVO> pageServices(AsServicePageQueryAO query) {
        AsServicePageQueryAO safeQuery = query == null ? new AsServicePageQueryAO() : query;
        if (safeQuery.getDisplayTab() != null) {
            List<AsServiceEntity> matched = listServicesForPageQuery(safeQuery, true);
            List<AsServiceVO> rows = buildServiceRows(matched);
            return serviceHelper.toManualPageResult(safeQuery.getPageIndex(), safeQuery.getPageSize(), rows);
        }
        QueryWrapper<AsServiceEntity> wrapper = buildServicePageWrapper(safeQuery, true);
        Page<AsServiceEntity> page = new Page<>(serviceHelper.safePageIndex(safeQuery), serviceHelper.safePageSize(safeQuery));
        Page<AsServiceEntity> result = baseMapper.selectPage(page, wrapper);
        List<AsServiceEntity> records = result == null ? Collections.emptyList() : result.getRecords();
        List<AsServiceVO> list = buildServiceRows(records);
        return serviceHelper.toPageResult(result == null ? page : result, list);
    }

    /**
     * 统计排课管理各 Tab 数量，筛选条件与列表查询一致（不含 Tab 本身条件）。
     */
    public AsServiceStatusCountVO countServiceStatuses(AsServicePageQueryAO query) {
        AsServicePageQueryAO safeQuery = query == null ? new AsServicePageQueryAO() : query;
        List<AsServiceEntity> list = listServicesForPageQuery(safeQuery, false);
        AsServiceStatusCountVO vo = new AsServiceStatusCountVO();
        vo.setAllCount((long) list.size());
        vo.setUnpublishedCount(serviceHelper.countDisplayTab(list, AsServiceDisplayTabEnum.UNPUBLISHED));
        vo.setPendingEnrollCount(serviceHelper.countDisplayTab(list, AsServiceDisplayTabEnum.PENDING_ENROLL));
        vo.setEnrollingCount(serviceHelper.countDisplayTab(list, AsServiceDisplayTabEnum.ENROLLING));
        vo.setPendingCourseCount(serviceHelper.countDisplayTab(list, AsServiceDisplayTabEnum.PENDING_COURSE));
        vo.setRunningCount(serviceHelper.countDisplayTab(list, AsServiceDisplayTabEnum.RUNNING));
        vo.setFinishedCount(serviceHelper.countDisplayTab(list, AsServiceDisplayTabEnum.FINISHED));
        return vo;
    }

    /**
     * 导出课后服务列表到 Excel。
     * <p>复用与分页列表相同的筛选条件，一次性查询最多 5000 条，生成 Excel 文件并上传。</p>
     */
    public AsServiceExportResultVO exportServices(AsServicePageQueryAO query,HttpServletResponse response) {
        AsServicePageQueryAO exportQuery = query == null ? new AsServicePageQueryAO() : query;
        List<AsServiceEntity> entities = listServicesForPageQuery(exportQuery, true);
        if (entities.size() > 5000) {
            entities = entities.subList(0, 5000);
        }
        List<AsServiceVO> vos = buildServiceRows(entities);
        List<AsServiceExportExcel> rows = vos.stream().map(this::toServiceExportExcel).collect(Collectors.toList());
        String fileName = "课后服务列表_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx";
        BaseFileEntity file = uploadServiceExportFile(fileName, rows,response);
        AsServiceExportResultVO result = new AsServiceExportResultVO();
        result.setFileId(file == null ? null : file.getId());
        result.setFileName(fileName);
        result.setDownloadUrl(file == null ? null : file.getFileUrl());
        result.setTotal(rows.size());
        return result;
    }

    private AsServiceExportExcel toServiceExportExcel(AsServiceVO vo) {
        AsServiceExportExcel excel = new AsServiceExportExcel();
        excel.setServiceCode(vo.getServiceCode());
        excel.setServiceName(vo.getServiceName());
        excel.setTermName(vo.getTermName());
        excel.setCourseDateRange(serviceHelper.formatDateRange(vo.getCourseStartDate(), vo.getCourseEndDate(), "yyyy-MM-dd", " ~ "));
        excel.setEnrollTimeRange(serviceHelper.formatDateRange(vo.getEnrollStartTime(), vo.getEnrollEndTime(), "yyyy-MM-dd HH:mm:ss", " ~ "));
        excel.setCreatorName(vo.getCreatorName());
        excel.setPublishTime(serviceHelper.formatDateStr(vo.getPublishTime(), "yyyy-MM-dd HH:mm:ss"));
        excel.setServiceStatusName(vo.getServiceStatusName());
        return excel;
    }

    private BaseFileEntity uploadServiceExportFile(String fileName, List<AsServiceExportExcel> rows, HttpServletResponse response) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            ExcelUtils.exportExcel(response, rows, AsServiceExportExcel.class, "课后服务列表", "课后服务列表");
            byte[] data = outputStream.toByteArray();
            return baseFileService.uploadFile(new ByteArrayInputStream(data), fileName, (long) data.length,
                    FileTypeEnum.DOCUMENT_TYPE, "AFTERSCHOOL_SERVICE_EXPORT");
        } catch (Exception e) {
            log.error("export service failed, fileName={}", fileName, e);
            return null;
        }
    }



    private List<AsServiceEntity> listServicesForPageQuery(AsServicePageQueryAO query, boolean applyTabFilter) {
        Integer originalDisplayTab = query.getDisplayTab();
        Integer originalServiceStatus = query.getServiceStatus();
        if (!applyTabFilter) {
            query.setDisplayTab(null);
            query.setServiceStatus(null);
        } else if (originalDisplayTab != null) {
            query.setServiceStatus(null);
        }
        QueryWrapper<AsServiceEntity> wrapper = buildServicePageWrapper(query, applyTabFilter);
        List<AsServiceEntity> list = baseMapper.selectList(wrapper);
        query.setDisplayTab(originalDisplayTab);
        query.setServiceStatus(originalServiceStatus);
        if (!applyTabFilter || originalDisplayTab == null) {
            return list == null ? Collections.emptyList() : list;
        }
        AsServiceDisplayTabEnum tab = AsServiceDisplayTabEnum.getByCode(originalDisplayTab);
        if (tab == null) {
            return list == null ? Collections.emptyList() : list;
        }
        return list.stream()
                .filter(entity -> Objects.equals(serviceHelper.resolveDisplayTab(entity), tab))
                .collect(Collectors.toList());
    }

    private QueryWrapper<AsServiceEntity> buildServicePageWrapper(AsServicePageQueryAO query, boolean applyTabFilter) {
        QueryWrapper<AsServiceEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("is_valid", 1);
        Long schoolId = currentSchoolId(query.getSchoolId());
        query.setSchoolId(schoolId);
        EduclTermEntity selectedTerm = query.getTermId() == null
                ? studentTermScopeSupport.resolveReadableTerm(schoolId)
                : studentTermScopeSupport.requireTermInSchool(schoolId, query.getTermId());
        query.setTermId(selectedTerm.getId());
        if (schoolId != null && schoolId > 0) {
            wrapper.eq("school_id", schoolId);
        }
        wrapper.eq("term_id", query.getTermId());
        if (StringUtils.hasText(query.getKeyword())) {
            String keyword = query.getKeyword().trim();
            wrapper.and(w -> w.like("service_name", keyword).or().like("service_code", keyword));
        }
        if (query.getCourseStartDate() != null) {
            wrapper.ge("course_start_date", query.getCourseStartDate());
        }
        if (query.getCourseEndDate() != null) {
            wrapper.le("course_end_date", query.getCourseEndDate());
        }
        wrapper.orderByDesc("id");
        return wrapper;
    }





    /**
     * 查询课后服务聚合详情。
     * 返回第一步基本信息、第二步课程信息、排课规则、课次数量和选课统计，供创建任务回显和详情页使用。
     */
    public AsServiceDetailVO getServiceDetail(Long id) {
        AsServiceEntity service = baseMapper.selectById(id);
        if (!serviceHelper.isValid(service)) {
            return null;
        }
        return buildServiceDetail(service);
    }

    /**
     * 保存课后服务第一步基本信息。
     * 新增时创建未发布草稿，更新时按照服务状态限制关键字段，避免已报名服务被改坏。
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<AsServiceDetailVO> saveBasic(AsServiceBasicSaveAO ao) {
        if (ao != null && ao.getTermId() != null) {
            educlTermMapper.selectByIdForUpdate(ao.getTermId());
        }
        String validate = validateBasic(ao);
        if (validate != null) {
            return JsonResult.buildError(validate);
        }
        Long schoolId = currentSchoolId(ao.getSchoolId());
        AsServiceEntity entity;
        boolean create = ao.getId() == null;
        // 查询学校编码和学期类型 (上-1/下-2)
        String schoolCode = schoolManageService.getSchoolCode(schoolId);
        Integer termType = educlTermService.getTermTypeById(ao.getTermId());
        if (org.apache.commons.lang3.StringUtils.isBlank(schoolCode)) {
            return JsonResult.buildError("学校编码为空");
        }
        if (create) {
            entity = new AsServiceEntity();
            entity.setServiceCode(generateServiceCode(schoolCode, termType));
            entity.setServiceStatus(CoreConstant.SERVICE_STATUS_DRAFT);
            entity.setSelectionInitStatus(0);
            entity.setCreatorId(currentUserId());
            entity.setCreateUser(currentUserId());
            entity.setCreateTime(new Date());
            entity.setIsValid(1);
        } else {
            entity = baseMapper.selectById(ao.getId());
            if (!serviceHelper.isValid(entity)) {
                return JsonResult.buildError("课后服务不存在");
            }
            String statusValidate = validateBasicEditable(entity, ao);
            if (statusValidate != null) {
                return JsonResult.buildError(statusValidate);
            }
        }
        entity.setSchoolId(schoolId);
        entity.setTermId(ao.getTermId());
        entity.setServiceName(ao.getServiceName().trim());
        entity.setEnrollStartTime(ao.getEnrollStartTime());
        entity.setEnrollEndTime(ao.getEnrollEndTime());
        entity.setCourseStartDate(AssembleUtil.normalizeDate(ao.getCourseStartDate()));
        entity.setCourseEndDate(AssembleUtil.normalizeDate(ao.getCourseEndDate()));
        entity.setAfterSchoolTimeTemplateId(ao.getAfterSchoolTimeTemplateId());
        entity.setGradeScopeJson(null);
        entity.setClassScopeJson(null);
        entity.setUpdateUser(currentUserId());
        entity.setUpdateTime(new Date());
        if (create) {
            baseMapper.insert(entity);
        } else {
            baseMapper.updateById(entity);
        }
        return JsonResult.buildSuccess(buildServiceDetail(entity));
    }

    /**
     * 保存课后服务第二步课程信息。
     * 每个课程班会保存报名方式、容量、教师地点和排课规则；需要时可同步重建未来课次。
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<AsServiceDetailVO> saveCourses(AsServiceCourseSaveAO ao) {
        AsServiceEntity service = baseMapper.selectById(ao.getServiceId());
        if (!serviceHelper.isValid(service)) {
            return JsonResult.buildError("课后服务不存在");
        }
        if (Objects.equals(service.getServiceStatus(), CoreConstant.SERVICE_STATUS_FINISHED)) {
            return JsonResult.buildError("课后服务已结束，不能编辑课程");
        }
        if (CollectionUtil.isNotEmpty(ao.getCourses()) && ao.getCourses().size() > 10) {
            return JsonResult.buildError("课后服务排课的课程数量不能超过10个");
        }
        // 只开放公开选课
        List<Integer> typeList = ao.getCourses().stream().map(AsServiceCourseItemAO::getEnrollType).distinct().collect(Collectors.toList());
        if(typeList.size() > 1 ||  1 != typeList.get(0)){
            return JsonResult.buildError("课后服务目前只开放公开选课");
        }
        if (service.getTermId() != null) {
            educlTermMapper.selectByIdForUpdate(service.getTermId());
        }
        for (AsServiceCourseItemAO item : ao.getCourses()) {
            String validate = validateCourseItem(service, item);
            if (validate != null) {
                return JsonResult.buildError(validate);
            }
        }
        CourseScheduleConflictResult conflictResult = courseScheduleConflictValidator.validateForSave(service, ao.getCourses());
        if (conflictResult.isHasConflict()) {
            return JsonResult.buildError(conflictResult.firstMessage());
        }
        List<AsCourseClassEntity> removedCourses = resolveRemovedCourses(service.getId(), ao.getCourses());
        if (ao.getCourses().stream().anyMatch(item -> Objects.equals(item.getEnrollType(), CoreConstant.ENROLL_TYPE_PUBLIC)
                || Objects.equals(item.getEnrollType(), CoreConstant.ENROLL_TYPE_INVITE))
                || removedCourses.stream().anyMatch(course -> asCourseQuotaService.isPublic(course)
                || Objects.equals(course.getEnrollType(), CoreConstant.ENROLL_TYPE_INVITE))) {
            List<Long> mutationCourseIds = ao.getCourses().stream().map(AsServiceCourseItemAO::getId).collect(Collectors.toList());
            removedCourses.forEach(course -> mutationCourseIds.add(course.getId()));
            asCourseQuotaService.lockCourses(mutationCourseIds);
            for (AsCourseClassEntity removed : removedCourses) {
                if (asCourseQuotaService.isPublic(removed)) {
                    AsCourseClassEntity current = asCourseQuotaService.currentCourse(removed.getId());
                    if (current != null && current.getEnrolledCount() != null && current.getEnrolledCount() > 0) {
                        return JsonResult.buildError("课程班【" + removed.getCourseClassName() + "】仍有名额占用，不能删除");
                    }
                }
            }
        }
        String removeValidate = validateRemovableCourses(removedCourses);
        if (removeValidate != null) {
            return JsonResult.buildError(removeValidate);
        }
        invalidateRemovedCourses(removedCourses);
        List<AsCourseClassEntity> savedCourses = new ArrayList<>();
        for (AsServiceCourseItemAO item : ao.getCourses()) {
            AsCourseClassEntity course = saveCourseItem(service, item);
            savedCourses.add(course);
            saveScheduleRules(service, course, item.getScheduleRules());
        }
        if (Boolean.TRUE.equals(ao.getRebuildSessions())) {
            rebuildSessions(service, savedCourses, null);
        }
        if (!Objects.equals(service.getServiceStatus(), CoreConstant.SERVICE_STATUS_DRAFT)) {
            generateClassAssignEnrollments(service, savedCourses);
        }
        return JsonResult.buildSuccess(buildServiceDetail(service));
    }

    /**
     * 根据本次提交课程班ID集合，找出数据库中已存在但本次未提交的课程班。
     */
    private List<AsCourseClassEntity> resolveRemovedCourses(Long serviceId, List<AsServiceCourseItemAO> requestCourses) {
        Set<Long> requestIds = serviceHelper.emptyList(requestCourses).stream()
                .map(AsServiceCourseItemAO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        return asCourseClassMapper.listRemovedCourseCandidates(serviceId, new ArrayList<>(requestIds));
    }

    /**
     * 删除课程班前校验是否已产生报名、去向、点名或已开始课次等业务数据。
     */
    private String validateRemovableCourses(List<AsCourseClassEntity> courses) {
        if (CollectionUtils.isEmpty(courses)) {
            return null;
        }
        for (AsCourseClassEntity course : courses) {
            if (hasEnrollmentData(course.getId())) {
                return "课程班【" + course.getCourseClassName() + "】已有报名数据，不能删除";
            }
            if (hasStudentDestinationData(course.getId())) {
                return "课程班【" + course.getCourseClassName() + "】已有学生去向数据，不能删除";
            }
            if (hasAttendanceData(course.getId())) {
                return "课程班【" + course.getCourseClassName() + "】已有点名数据，不能删除";
            }
            if (hasStartedSession(course.getId())) {
                return "课程班【" + course.getCourseClassName() + "】已有开始或结束的课次，不能删除";
            }
        }
        return null;
    }

    /**
     * 判断课程班是否存在有效报名记录。
     */
    private boolean hasEnrollmentData(Long courseClassId) {
        return serviceHelper.nullToZero(asCourseClassMapper.countEnrollmentData(courseClassId)) > 0;
    }

    /**
     * 判断课程班是否存在有效学生去向记录。
     */
    private boolean hasStudentDestinationData(Long courseClassId) {
        return serviceHelper.nullToZero(asCourseClassMapper.countStudentDestinationData(courseClassId)) > 0;
    }

    /**
     * 判断课程班是否存在有效点名记录。
     */
    private boolean hasAttendanceData(Long courseClassId) {
        return serviceHelper.nullToZero(asCourseClassMapper.countAttendanceData(courseClassId)) > 0;
    }

    /**
     * 判断课程班是否存在非未开始状态的有效课次。
     */
    private boolean hasStartedSession(Long courseClassId) {
        return serviceHelper.nullToZero(asCourseClassMapper.countStartedSession(courseClassId)) > 0;
    }



    /**
     * 软删除本次保存移除的课程班及其排课规则、未开始课次。
     */
    private void invalidateRemovedCourses(List<AsCourseClassEntity> courses) {
        if (CollectionUtils.isEmpty(courses)) {
            return;
        }
        List<Long> courseIds = courses.stream()
                .map(AsCourseClassEntity::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(courseIds)) {
            return;
        }
        Long updateUser = currentUserId();
        asCourseClassMapper.invalidateCourseClasses(courseIds, updateUser);
        asCourseClassMapper.invalidateCourseScheduleRules(courseIds, updateUser);
        asCourseClassMapper.invalidateNotStartedCourseSessions(courseIds, updateUser);
    }

    /**
     * 预览课程排课结果。
     * 将周期/自定义日期规则展开为具体课次，并标注教师、地点和课程班内部时间冲突。
     */
    public JsonResult<AsServiceSessionPreviewResultVO> previewSessions(AsServiceSessionPreviewAO ao) {
        AsServiceEntity service = baseMapper.selectById(ao.getServiceId());
        if (!serviceHelper.isValid(service)) {
            return JsonResult.buildError("课后服务不存在");
        }
        List<CourseRuleBundle> bundles = resolveCourseRuleBundles(service, ao.getCourses());
        AsServiceSessionPreviewResultVO result = buildSessionPreview(service, bundles);
        return JsonResult.buildSuccess(result);
    }

    /**
     * 异步发布前置校验并将服务标记为发布中。
     *
     * <p>该方法只做校验和状态占位，不生成课次或学生数据，避免接口线程长时间等待。</p>
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<AsServicePublishResultVO> preparePublishService(
            Long serviceId, AsServicePublishAO ao, Long operatorUserId) {
        AsServiceEntity service = baseMapper.selectById(serviceId);
        String validate = validatePublishPreparation(service, ao);
        if (validate != null) {
            return JsonResult.buildError(validate);
        }

        UpdateWrapper<AsServiceEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", serviceId)
                .eq("is_valid", 1)
                .ne("service_status", CoreConstant.SERVICE_STATUS_FINISHED)
                .and(status -> status.isNull("selection_init_status")
                        .or().ne("selection_init_status", CoreConstant.SELECTION_INIT_STATUS_PROCESSING))
                .set("selection_init_status", CoreConstant.SELECTION_INIT_STATUS_PROCESSING)
                .set("update_user", operatorUserId)
                .set("update_time", new Date());
        if (baseMapper.update(null, updateWrapper) <= 0) {
            return JsonResult.buildError("存在发布中的课后服务，请勿重复操作");
        }

        AsServicePublishResultVO result = new AsServicePublishResultVO();
        result.setServiceId(serviceId);
        result.setServiceStatus(service.getServiceStatus());
        result.setServiceStatusName(AssembleUtil.getServiceStatusName(service.getServiceStatus()));
        return JsonResult.buildSuccess(result, "发布任务已提交");
    }

    /**
     * 执行异步发布任务，复用原发布事务和业务顺序。
     */
    @AsClassAssignAsync
    @Async
    @Transactional(rollbackFor = Exception.class)
    public CompletableFuture<JsonResult<AsServicePublishResultVO>> publishServiceAsync(
            Long serviceId, AsServicePublishAO ao, Long operatorUserId) {
        AsAsyncOperatorContext.setOperatorUserId(operatorUserId);
        try {
            JsonResult<AsServicePublishResultVO> result = publishService(serviceId, ao);
            if (Boolean.TRUE.equals(result.getSuccess())) {
                AsServiceEntity service = baseMapper.selectById(serviceId);
                int classAssignedCount = generateClassAssignEnrollments(
                        service, listServiceCourses(serviceId), operatorUserId);
                if (result.getData() != null) {
                    result.getData().setClassAssignedCount(classAssignedCount);
                }
            }
            return CompletableFuture.completedFuture(result);
        } finally {
            AsAsyncOperatorContext.clear();
        }
    }

    /**
     * 异步发布失败或启动失败时恢复初始化状态，服务主状态由发布事务回滚后保持原值。
     */
    @Transactional(rollbackFor = Exception.class)
    public void restorePublishPreparingStatus(Long serviceId, Long operatorUserId) {
        AsServiceEntity service = serviceId == null ? null : baseMapper.selectById(serviceId);
        if (!serviceHelper.isValid(service)
                || !Objects.equals(service.getSelectionInitStatus(), CoreConstant.SELECTION_INIT_STATUS_PROCESSING)) {
            return;
        }
        int restoredStatus = Objects.equals(service.getServiceStatus(), CoreConstant.SERVICE_STATUS_DRAFT)
                ? CoreConstant.SELECTION_INIT_STATUS_NOT_STARTED : CoreConstant.SELECTION_INIT_STATUS_DONE;
        UpdateWrapper<AsServiceEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", serviceId)
                .eq("is_valid", 1)
                .eq("selection_init_status", CoreConstant.SELECTION_INIT_STATUS_PROCESSING)
                .set("selection_init_status", restoredStatus)
                .set("update_user", operatorUserId)
                .set("update_time", new Date());
        baseMapper.update(null, updateWrapper);
    }

    /**
     * 校验异步发布请求；携带课程时校验请求内容，否则校验数据库中已经保存的课程。
     */
    private String validatePublishPreparation(AsServiceEntity service, AsServicePublishAO ao) {
        if (!serviceHelper.isValid(service)) {
            return "课后服务不存在";
        }
        if (Objects.equals(service.getSelectionInitStatus(), CoreConstant.SELECTION_INIT_STATUS_PROCESSING)) {
            return "存在发布中的课后服务，请勿重复操作";
        }
        if (Objects.equals(service.getServiceStatus(), CoreConstant.SERVICE_STATUS_FINISHED)) {
            return "课后服务已结束，不能发布";
        }
        boolean republish = !Objects.equals(service.getServiceStatus(), CoreConstant.SERVICE_STATUS_DRAFT);
        if (republish && (ao == null || !Boolean.TRUE.equals(ao.getRebuildSessions()))) {
            return "课后服务已发布，如需重新发布并重建课次请设置 rebuildSessions=true";
        }
        String basicValidate = validatePublishBasic(service);
        if (basicValidate != null) {
            return basicValidate;
        }
        if (ao != null && !CollectionUtils.isEmpty(ao.getCourses())) {
            if (ao.getCourses().size() > 10) {
                return "课后服务排课的课程数量不能超过10个";
            }
            for (AsServiceCourseItemAO item : ao.getCourses()) {
                String itemValidate = validateCourseItem(service, item);
                if (itemValidate != null) {
                    return itemValidate;
                }
            }
            CourseScheduleConflictResult conflictResult =
                    courseScheduleConflictValidator.validateForSave(service, ao.getCourses());
            if (conflictResult.isHasConflict()) {
                return conflictResult.firstMessage();
            }
            String removableValidate = validateRemovableCourses(resolveRemovedCourses(service.getId(), ao.getCourses()));
            if (removableValidate != null) {
                return removableValidate;
            }
            return validateFutureSessionPreparation(service, ao, null);
        }

        List<AsCourseClassEntity> courses = listServiceCourses(service.getId());
        if (courses.isEmpty()) {
            return "请先配置课程信息";
        }
        for (AsCourseClassEntity course : courses) {
            String courseValidate = validateSavedCourse(service, course);
            if (courseValidate != null) {
                return courseValidate;
            }
        }
        CourseScheduleConflictResult conflictResult =
                courseScheduleConflictValidator.validateSavedCourses(service, courses);
        if (conflictResult.isHasConflict()) {
            return conflictResult.firstMessage();
        }
        return validateFutureSessionPreparation(service, ao, courses);
    }

    /**
     * 同步发布前只校验是否存在未来有效课次，不执行课次写入和学生初始化。
     */
    private String validateFutureSessionPreparation(
            AsServiceEntity service, AsServicePublishAO ao, List<AsCourseClassEntity> savedCourses) {
        if (ao != null && Boolean.FALSE.equals(ao.getRebuildSessions())) {
            return null;
        }
        List<CourseRuleBundle> bundles = !CollectionUtils.isEmpty(ao == null ? null : ao.getCourses())
                ? resolveCourseRuleBundles(service, ao.getCourses())
                : resolveCourseRuleBundles(service, null, savedCourses);
        Set<LocalDate> suspensionDates = termSuspensionDateSupport.listSuspensionDates(service);
        return hasFutureSession(service, bundles, new Date(), suspensionDates)
                ? null : CoreConstant.PUBLISH_NO_SESSION_MESSAGE;
    }

    /**
     * 找到首个未来课次后立即返回，避免同步接口展开全部课次。
     */
    private boolean hasFutureSession(
            AsServiceEntity service, List<CourseRuleBundle> bundles, Date referenceTime,
            Set<LocalDate> suspensionDates) {
        if (service == null || CollectionUtils.isEmpty(bundles) || referenceTime == null) {
            return false;
        }
        for (CourseRuleBundle bundle : bundles) {
            if (bundle == null || CollectionUtils.isEmpty(bundle.rules)) {
                continue;
            }
            for (AsCourseScheduleRuleAO rule : bundle.rules) {
                if (hasFutureSession(service, rule, referenceTime, suspensionDates)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasFutureSession(
            AsServiceEntity service, AsCourseScheduleRuleAO rule, Date referenceTime,
            Set<LocalDate> suspensionDates) {
        if (serviceHelper.isDateRule(rule)) {
            Date sessionDate = AssembleUtil.normalizeDate(rule.getSessionDate());
            if (CourseScheduleSessionExpander.isSuspensionDate(sessionDate, suspensionDates)) {
                return false;
            }
            Date sessionEnd = AssembleUtil.combineDateAndTime(sessionDate, AssembleUtil.parseTime(rule.getEndTime()));
            return sessionEnd != null && sessionEnd.after(referenceTime);
        }
        if (!serviceHelper.isWeeklyRule(rule) || CollectionUtils.isEmpty(rule.getWeekDays())) {
            return false;
        }
        Date cursor = AssembleUtil.normalizeDate(service.getCourseStartDate());
        Date end = AssembleUtil.normalizeDate(service.getCourseEndDate());
        Date referenceDate = AssembleUtil.normalizeDate(referenceTime);
        if (cursor == null || end == null || cursor.after(end)) {
            return false;
        }
        if (referenceDate != null && cursor.before(referenceDate)) {
            cursor = referenceDate;
        }
        while (!cursor.after(end)) {
            if (rule.getWeekDays().contains(AssembleUtil.getWeekDay(cursor))
                    && !CourseScheduleSessionExpander.isSuspensionDate(cursor, suspensionDates)) {
                Date sessionEnd = AssembleUtil.combineDateAndTime(cursor, AssembleUtil.parseTime(rule.getEndTime()));
                if (sessionEnd != null && sessionEnd.after(referenceTime)) {
                    return true;
                }
            }
            cursor = AssembleUtil.addDays(cursor, 1);
        }
        return false;
    }

    /**
     * 发布课后服务。
     * 发布时完成课次生成、未选课初始化和服务状态更新；统一异步任务随后完成行政班安排。
     *
     * <p>发布约束：</p>
     * <ul>
     *   <li>首次发布仅允许未发布草稿；已发布服务再次发布需显式传 rebuildSessions=true</li>
     *   <li>服务基本信息、课程排课规则、课次生成结果必须完整</li>
     *   <li>行政班安排按容量和性别要求过滤，未命中学生由 initSelection 保持未分配</li>
     * </ul>
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<AsServicePublishResultVO> publishService(Long serviceId, AsServicePublishAO ao) {
        AsServiceEntity service = baseMapper.selectById(serviceId);
        if (!serviceHelper.isValid(service)) {
            return publishFailure("课后服务不存在");
        }
        if (Objects.equals(service.getServiceStatus(), CoreConstant.SERVICE_STATUS_FINISHED)) {
            return publishFailure("课后服务已结束，不能发布");
        }
        if (service.getTermId() != null) {
            educlTermMapper.selectByIdForUpdate(service.getTermId());
        }
        // 已发布服务不允许静默重复发布，避免误重建课次；需前端显式确认 rebuildSessions=true。
        boolean republish = !Objects.equals(service.getServiceStatus(), CoreConstant.SERVICE_STATUS_DRAFT);
        if (republish && (ao == null || !Boolean.TRUE.equals(ao.getRebuildSessions()))) {
            return publishFailure("课后服务已发布，如需重新发布并重建课次请设置 rebuildSessions=true");
        }
        // 发布前兜底校验第一步基本信息，避免 legacy /add 草稿直接进入发布链路。
        String publishBasicValidate = validatePublishBasic(service);
        if (publishBasicValidate != null) {
            return publishFailure(publishBasicValidate);
        }
        if (ao != null && !CollectionUtils.isEmpty(ao.getCourses())) {
            AsServiceCourseSaveAO saveAO = new AsServiceCourseSaveAO();
            saveAO.setServiceId(serviceId);
            saveAO.setCourses(ao.getCourses());
            saveAO.setRebuildSessions(false);
            JsonResult<AsServiceDetailVO> saveResult = saveCourses(saveAO);
            if (!Boolean.TRUE.equals(saveResult.getSuccess())) {
                return publishFailure(saveResult.getMsg());
            }
        }
        List<AsCourseClassEntity> courses = listServiceCourses(serviceId);
        if (courses.isEmpty()) {
            return publishFailure("请先配置课程信息");
        }
        for (AsCourseClassEntity course : courses) {
            String validate = validateSavedCourse(service, course);
            if (validate != null) {
                return publishFailure(validate);
            }
        }
        CourseScheduleConflictResult conflictResult = courseScheduleConflictValidator.validateSavedCourses(service, courses);
        if (conflictResult.isHasConflict()) {
            return publishFailure(conflictResult.firstMessage());
        }

        boolean rebuildSessions = ao == null || !Boolean.FALSE.equals(ao.getRebuildSessions());
        int generatedSessionCount = 0;
        if (rebuildSessions) {
            generatedSessionCount = rebuildSessions(service, courses, null);
            // 发布必须产出有效课次，否则家长端/调课链路缺少 t_as_course_session 数据。
            if (generatedSessionCount <= 0) {
                return publishFailure(CoreConstant.PUBLISH_NO_SESSION_MESSAGE);
            }
        }
        // 主发布事务先初始化未选学生；统一异步任务随后完成行政班范围内学生安排。
        int classAssignedCount = 0;
        AsEnrollmentInitResultVO initSelectionResult = adminQueryService.initUnassignedEnrollmentsForPublish(service);

        AsServiceEntity update = new AsServiceEntity();
        update.setId(serviceId);
        update.setServiceStatus(CoreConstant.SERVICE_STATUS_ENROLLING);
        update.setPublishTime(new Date());
        update.setSelectionInitStatus(CoreConstant.SELECTION_INIT_STATUS_DONE);
        update.setSelectionInitTime(initSelectionResult.getInitTime());
        update.setUpdateUser(currentUserId());
        update.setUpdateTime(new Date());
        int i = baseMapper.updateById(update);
        if (i <= 0) {
            return publishFailure("课后服务发布失败，请稍后重试");
        }

        this.publishServiceEnrollmentNotice(service, courses);

        AsServicePublishResultVO result = new AsServicePublishResultVO();
        result.setServiceId(serviceId);
        result.setServiceStatus(CoreConstant.SERVICE_STATUS_ENROLLING);
        result.setServiceStatusName(AssembleUtil.getServiceStatusName(CoreConstant.SERVICE_STATUS_ENROLLING));
        result.setGeneratedSessionCount(generatedSessionCount);
        result.setClassAssignedCount(classAssignedCount);
        result.setInitializedCount(initSelectionResult.getCreatedCount());
        result.setExistedEnrollmentCount(initSelectionResult.getExistsCount());
        return JsonResult.buildSuccess(result);
    }

    /**
     * 构造发布失败结果，并保证当前发布事务整体回滚。
     */
    private <T> JsonResult<T> publishFailure(String message) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return JsonResult.buildError(message);
    }


    /**
     * 根据报名方式给家长发布站内消息通知
     * 行政班：给对应班级学生的家长发送
     */
    private void publishServiceEnrollmentNotice(AsServiceEntity service, List<AsCourseClassEntity> courses) {
        List<AsCourseClassEntity> gkyqData = courses.stream().filter(e -> 1 == e.getEnrollType() || 2 == e.getEnrollType()).collect(Collectors.toList());
        List<AsCourseClassEntity> xzbData = courses.stream().filter(e -> 3 == e.getEnrollType()).collect(Collectors.toList());
        EduclTermEntity term = educlTermMapper.selectById(service.getTermId());
        String termName = term == null ? "所属学期" : term.getTermName();

        if (!CollectionUtils.isEmpty(gkyqData)) {
            // 将所有课程对应年级去重汇集
            Set<Long> gradeIds = new HashSet<>();
            gkyqData.forEach(e -> {
                if (org.apache.commons.lang3.StringUtils.isNotBlank(e.getApplicableGradeIds())) {
                    List<Long> courseGradeIds = JSON.parseArray(e.getApplicableGradeIds(), Long.class);
                    if (!CollectionUtils.isEmpty(courseGradeIds)) gradeIds.addAll(courseGradeIds);
                }
            });

            if (!CollectionUtils.isEmpty(gradeIds)) {
                AsStudentBindParentAO parentAO = new AsStudentBindParentAO();
                parentAO.setSchoolId(service.getSchoolId());
                parentAO.setTermId(service.getTermId());
                parentAO.setGradeIds(new ArrayList<>(gradeIds));
                // 通过年级查询绑定家长用户数据
                List<AsStudentBindParentVO> students = asStudentMapper.querytBindParentByGradeOrClass(parentAO);
                if (!CollectionUtils.isEmpty(students)) {
                    // 消息内容
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    List<Long> parentUserIds = students.stream()
                            .map(AsStudentBindParentVO::getParentUserId)
                            .filter(Objects::nonNull)
                            .distinct()
                            .collect(Collectors.toList());

                }
            }
        }

        if (!CollectionUtils.isEmpty(xzbData)) {
            AsStudentBindParentAO parentAO = new AsStudentBindParentAO();
            parentAO.setSchoolId(service.getSchoolId());
            parentAO.setTermId(service.getTermId());
            for (AsCourseClassEntity xzbDatum : xzbData) {
                if(org.apache.commons.lang3.StringUtils.isNotBlank(xzbDatum.getApplicableClassIds())){
                    List<Long> classIds = JSON.parseArray(xzbDatum.getApplicableClassIds(), Long.class);
                    if (!CollectionUtils.isEmpty(classIds)){
                        parentAO.setClassIds(classIds);
                    }
                }
            }
        }
    }

    /**
     * 撤回报名中服务。
     * 仅允许服务还未进入正式上课阶段时撤回，撤回后回到未发布草稿状态。
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> withdrawService(Long id) {
        AsServiceEntity service = baseMapper.selectById(id);
        if (!serviceHelper.isValid(service)) {
            return JsonResult.buildError("课后服务不存在");
        }
        if (!Objects.equals(service.getServiceStatus(), CoreConstant.SERVICE_STATUS_ENROLLING)) {
            return JsonResult.buildError("仅报名中的课后服务可撤回");
        }
        Date today = AssembleUtil.normalizeDate(new Date());
        if (service.getCourseStartDate() != null && !today.before(AssembleUtil.normalizeDate(service.getCourseStartDate()))) {
            return JsonResult.buildError("课后服务已到开课日期，不能撤回");
        }
        AsServiceEntity update = new AsServiceEntity();
        update.setId(id);
        update.setServiceStatus(CoreConstant.SERVICE_STATUS_DRAFT);
        update.setPublishTime(null);
        update.setUpdateUser(currentUserId());
        update.setUpdateTime(new Date());
        baseMapper.updateById(update);
        return JsonResult.buildSuccess("撤回成功");
    }

    /**
     * 手动结束课后服务。
     * 同步结束服务下课程班，历史选课和课次保留，便于后续统计和追溯。
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> finishService(Long id) {
        AsServiceEntity service = baseMapper.selectById(id);
        if (!serviceHelper.isValid(service)) {
            return JsonResult.buildError("课后服务不存在");
        }
        AsServiceEntity update = new AsServiceEntity();
        update.setId(id);
        update.setServiceStatus(CoreConstant.SERVICE_STATUS_FINISHED);
        update.setUpdateUser(currentUserId());
        update.setUpdateTime(new Date());
        baseMapper.updateById(update);

        UpdateWrapper<AsCourseClassEntity> courseWrapper = new UpdateWrapper<>();
        courseWrapper.eq("service_id", id).eq("is_valid", 1)
                .set("class_status", CoreConstant.CLASS_STATUS_FINISHED)
                .set("update_user", currentUserId())
                .set("update_time", new Date());
        asCourseClassMapper.update(null, courseWrapper);
        return JsonResult.buildSuccess("结束成功");
    }





    private String validateBasic(AsServiceBasicSaveAO ao) {
        Long schoolId = ao == null ? null : currentSchoolId(ao.getSchoolId());
        return asServiceCoreValidator.validateBasic(ao, schoolId);
    }

    /**
     * 发布前校验服务基本信息是否完整，避免 legacy 草稿或未保存第一步的数据进入发布链路。
     */
    private String validatePublishBasic(AsServiceEntity service) {
        if (service.getTermId() == null) {
            return "请先完善学期信息";
        }
        EduclTermEntity term = educlTermMapper.selectById(service.getTermId());
        if (!serviceHelper.isValid(term)) {
            return "学期不存在或已失效";
        }
        if (!Objects.equals(term.getSchoolId(), service.getSchoolId())) {
            return "学期不属于当前学校";
        }
        if (service.getAfterSchoolTimeTemplateId() != null) {
            AsScheduleTemplateEntity template = asScheduleTemplateMapper
                    .selectById(service.getAfterSchoolTimeTemplateId());
            if (!serviceHelper.isValid(template)) {
                return "课后时间方案不存在或已失效";
            }
            if (!Objects.equals(template.getSchoolId(), service.getSchoolId())) {
                return "课后时间方案不属于当前学校";
            }
            if (!Objects.equals(template.getEnabledStatus(), CoreConstant.ENABLED_STATUS)) {
                return "课后时间方案未启用";
            }
            if (!Objects.equals(template.getTemplateType(), CoreConstant.SCHEDULE_TEMPLATE_TYPE_AFTER_SCHOOL)) {
                return "当前时间方案不是课后时间方案";
            }
        }
        if (service.getEnrollStartTime() != null || service.getEnrollEndTime() != null) {
            if (null != service.getEnrollStartTime() &&
                    null != service.getEnrollEndTime() && !service.getEnrollStartTime().before(service.getEnrollEndTime())) {
                return "报名开始时间必须早于报名结束时间";
            }
        }
        if (service.getCourseStartDate() == null || service.getCourseEndDate() == null) {
            return "请先完善开课周期";
        }
        if (AssembleUtil.normalizeDate(service.getCourseStartDate()).after(AssembleUtil.normalizeDate(service.getCourseEndDate()))) {
            return "开课日期不能晚于结课日期";
        }
        if (service.getEnrollEndTime() != null
                && !service.getEnrollEndTime().before(AssembleUtil.normalizeDate(service.getCourseStartDate()))) {
            return "报名结束时间必须早于开课日期";
        }
        return null;
    }

    private String validateBasicEditable(AsServiceEntity existing, AsServiceBasicSaveAO ao) {
        if (Objects.equals(existing.getServiceStatus(), CoreConstant.SERVICE_STATUS_FINISHED)) {
            return "课后服务已结束，不能编辑";
        }
        if (!Objects.equals(existing.getServiceStatus(), CoreConstant.SERVICE_STATUS_DRAFT)) {
            if (!Objects.equals(existing.getTermId(), ao.getTermId())) {
                return "已发布课后服务不能修改学期";
            }
            if (!Objects.equals(existing.getServiceName(), ao.getServiceName().trim())) {
                return "已发布课后服务不能修改服务名称";
            }
            if (!Objects.equals(existing.getEnrollStartTime(), ao.getEnrollStartTime())
                    || !Objects.equals(existing.getEnrollEndTime(), ao.getEnrollEndTime())) {
                return "已发布课后服务不能修改报名时间";
            }
        }
        return null;
    }

    private String validateCourseItem(AsServiceEntity service, AsServiceCourseItemAO item) {
        return asServiceCoreValidator.validateCourseItem(service, item);
    }

    private String validateSavedCourse(AsServiceEntity service, AsCourseClassEntity course) {
        AsServiceCourseItemAO item = toCourseItem(course, listRulesByCourse(course.getId()));
        String validate = validateCourseItem(service, item);
        if (validate != null) {
            return course.getCourseClassName() + "：" + validate;
        }
        String referenceValidate = asServiceCoreValidator.validateCourseReferences(
                service, item, course.getCourseFile());
        if (referenceValidate != null) {
            return course.getCourseClassName() + "：" + referenceValidate;
        }
        return null;
    }

    private AsCourseClassEntity saveCourseItem(AsServiceEntity service, AsServiceCourseItemAO item) {
        boolean create = item.getId() == null;
        AsCourseClassEntity entity = create ? new AsCourseClassEntity()
                : (Objects.equals(item.getEnrollType(), CoreConstant.ENROLL_TYPE_PUBLIC)
                || Objects.equals(item.getEnrollType(), CoreConstant.ENROLL_TYPE_INVITE)
                ? asCourseQuotaService.currentCourse(item.getId()) : asCourseClassMapper.selectById(item.getId()));
        if (!create) {
            if (!serviceHelper.isValid(entity) || !Objects.equals(entity.getServiceId(), service.getId())) {
                throw new BaseBusinessException(ResultCode.PARA_ERROR.code(), "课程班不存在或不属于当前服务");
            }
            // 持有课程行锁后重验旧容量，避免编辑覆盖并发调课人数或先写容量再错误地认定为未修改。
            String capacityError = asCourseQuotaService.validateCourseCapacityChange(entity, item.getCapacity());
            if (capacityError != null) {
                throw new BaseBusinessException(ResultCode.PARA_ERROR.code(), capacityError);
            }
            asCourseQuotaService.saveConfiguration(entity, item);
        }
        if (create) {
            entity.setSchoolId(service.getSchoolId());
            entity.setServiceId(service.getId());
            entity.setCourseClassCode(generateCourseClassCode(item.getEnrollType()));
            entity.setEnrolledCount(0);
            entity.setClassStatus(CoreConstant.CLASS_STATUS_NOT_STARTED);
            entity.setCreateUser(currentUserId());
            entity.setCreateTime(new Date());
            entity.setIsValid(1);
        }
        entity.setSchoolId(service.getSchoolId());
        entity.setServiceId(service.getId());
        entity.setCourseClassName(item.getCourseClassName().trim());
        entity.setCategoryId(item.getCategoryId());
        entity.setModuleId(item.getModuleId());
        entity.setEnrollType(item.getEnrollType());
        entity.setApplicableClassIds(item.getApplicableClassIds());
        entity.setApplicableGradeIds(serviceHelper.normalizeApplicableGradeIds(item));
        entity.setParentConfirmRequired(item.getParentConfirmRequired() == null ? 0 : item.getParentConfirmRequired());
        entity.setGenderRequirement(item.getGenderRequirement() == null ? 0 : item.getGenderRequirement());
        entity.setCapacity(item.getCapacity());
        entity.setTeacherId(item.getTeacherId());
        entity.setPlaceId(item.getPlaceId());
        entity.setIntro(item.getIntro());
        entity.setUpdateUser(currentUserId());
        entity.setUpdateTime(new Date());
        if (create) {
            asCourseClassMapper.insert(entity);
        } else {
            asCourseClassMapper.updateById(entity);
        }
        if (create) {
            asCourseQuotaService.saveConfiguration(entity, item);
        }
        if (Objects.equals(entity.getEnrollType(), CoreConstant.ENROLL_TYPE_INVITE)) {
//            asInviteCourseTodoSupport.replaceTodo(entity, service.getEnrollStartTime());
        }

        return entity;
    }

    /**
     * 行政班安排强制作用于选中的行政班；选了班级时不再保存年级范围，避免扩展到整个年级。
     */
    private void saveScheduleRules(AsServiceEntity service, AsCourseClassEntity course, List<AsCourseScheduleRuleAO> rules) {
        if (course.getId() == null) {
            return;
        }
        UpdateWrapper<AsCourseScheduleRuleEntity> invalidWrapper = new UpdateWrapper<>();
        invalidWrapper.eq("service_id", service.getId())
                .eq("course_class_id", course.getId())
                .eq("is_valid", 1)
                .set("is_valid", 0)
                .set("update_user", currentUserId())
                .set("update_time", new Date());
        asCourseScheduleRuleMapper.update(null, invalidWrapper);
        for (AsCourseScheduleRuleAO rule : serviceHelper.emptyList(rules)) {
            AsCourseScheduleRuleEntity entity = new AsCourseScheduleRuleEntity();
            entity.setSchoolId(service.getSchoolId());
            entity.setServiceId(service.getId());
            entity.setCourseClassId(course.getId());
            entity.setRuleType(serviceHelper.isWeeklyRule(rule) ? CoreConstant.RULE_TYPE_WEEKLY : CoreConstant.RULE_TYPE_DATE);
            entity.setWeekDaysJson(serviceHelper.isWeeklyRule(rule) ? JSON.toJSONString(rule.getWeekDays()) : null);
            entity.setSessionDate(serviceHelper.isDateRule(rule) ? AssembleUtil.normalizeDate(rule.getSessionDate()) : null);
            entity.setSchedulePeriodId(rule.getSchedulePeriodId());
            entity.setStartTime(AssembleUtil.parseTime(rule.getStartTime()));
            entity.setEndTime(AssembleUtil.parseTime(rule.getEndTime()));
            entity.setPlaceName(rule.getPlaceId());
            entity.setTeacherId(rule.getTeacherId());
            entity.setCreateUser(currentUserId());
            entity.setUpdateUser(currentUserId());
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());
            entity.setIsValid(1);
            asCourseScheduleRuleMapper.insert(entity);
        }
    }

    private AsServiceDetailVO buildServiceDetail(AsServiceEntity service) {
        AsServiceDetailVO vo = new AsServiceDetailVO();
        BeanUtils.copyProperties(service, vo);
        vo.setServiceStatusName(AssembleUtil.getServiceStatusName(service.getServiceStatus()));
        EduclTermEntity term = service.getTermId() == null ? null : educlTermMapper.selectById(service.getTermId());
        vo.setTermName(term == null ? null : term.getTermName());
        AsScheduleTemplateEntity template = service.getAfterSchoolTimeTemplateId() == null ? null
                : asScheduleTemplateMapper.selectById(service.getAfterSchoolTimeTemplateId());
        vo.setAfterSchoolTimeTemplateName(template == null ? null : template.getTemplateName());
        List<AsCourseClassEntity> courses = listServiceCourses(service.getId());
        vo.setCourses(buildCourseDetails(courses));
        vo.setEnrollmentStat(buildEnrollmentStat(service.getId()));
        vo.setCanEdit(!Objects.equals(service.getServiceStatus(), CoreConstant.SERVICE_STATUS_FINISHED));
        vo.setCanPublish(!Objects.equals(service.getServiceStatus(), CoreConstant.SERVICE_STATUS_FINISHED));
        vo.setCanWithdraw(Objects.equals(service.getServiceStatus(), CoreConstant.SERVICE_STATUS_ENROLLING));
        vo.setCanFinish(Objects.equals(service.getServiceStatus(), CoreConstant.SERVICE_STATUS_ENROLLING)
                || Objects.equals(service.getServiceStatus(), CoreConstant.SERVICE_STATUS_RUNNING));
        return vo;
    }

    private List<AsServiceVO> buildServiceRows(List<AsServiceEntity> services) {
        if (CollectionUtils.isEmpty(services)) {
            return Collections.emptyList();
        }
        List<Long> serviceIds = services.stream().map(AsServiceEntity::getId).filter(Objects::nonNull).collect(Collectors.toList());
        Map<Long, EduclTermEntity> termMap = mapTerms(services.stream().map(AsServiceEntity::getTermId).collect(Collectors.toList()));
        Map<Long, AsScheduleTemplateEntity> templateMap = mapTemplates(services.stream()
                .map(AsServiceEntity::getAfterSchoolTimeTemplateId).collect(Collectors.toList()));
        Map<Long, Integer> courseCountMap = countCourses(serviceIds);
        Map<Long, Integer> sessionCountMap = countSessions(serviceIds);
        // 批量查询创建人姓名
        Set<Long> creatorIds = services.stream().map(AsServiceEntity::getCreatorId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, String> creatorNameMap = new HashMap<>();
        if (!creatorIds.isEmpty()) {
            List<EmployeeDTO> employees = tBaseUserDao.getEmployeeByIds(creatorIds);
            if (!CollectionUtils.isEmpty(employees)) {
                creatorNameMap = employees.stream().collect(Collectors.toMap(EmployeeDTO::getId, EmployeeDTO::getActualName, (a, b) -> a));
            }
        }
        List<AsServiceVO> rows = new ArrayList<>();
        for (AsServiceEntity service : services) {
            AsServiceVO vo = new AsServiceVO();
            vo.setId(service.getId());
            SmartBeanUtil.copyProperties(service, vo);
            EduclTermEntity term = termMap.get(service.getTermId());
            vo.setTermName(term == null ? null : term.getTermName());
            vo.setAfterSchoolTimeTemplateId(service.getAfterSchoolTimeTemplateId());
            AsScheduleTemplateEntity template = templateMap.get(service.getAfterSchoolTimeTemplateId());
            vo.setAfterSchoolTimeTemplateName(template == null ? null : template.getTemplateName());
            vo.setServiceStatus(service.getServiceStatus());
            vo.setServiceStatusName(AssembleUtil.getServiceStatusName(service.getServiceStatus()));
            vo.setStatus(service.getServiceStatus());
            vo.setStatusName(AssembleUtil.getServiceStatusName(service.getServiceStatus()));
            vo.setSelectionInitStatus(service.getSelectionInitStatus());
            vo.setCourseClassCount(courseCountMap.getOrDefault(service.getId(), 0));
            vo.setSessionCount(sessionCountMap.getOrDefault(service.getId(), 0));
            vo.setCreatorName(creatorNameMap.get(service.getCreatorId()));
            vo.setCanCopy(Objects.equals(service.getServiceStatus(), CoreConstant.SERVICE_STATUS_FINISHED));
            rows.add(vo);
        }
        return rows;
    }

    private List<AsServiceCourseDetailVO> buildCourseDetails(List<AsCourseClassEntity> courses) {
        if (CollectionUtils.isEmpty(courses)) {
            return Collections.emptyList();
        }
        asCourseQuotaService.load(courses);
        Map<Long, AsCourseCategoryEntity> categoryMap = mapCategories(courses.stream().map(AsCourseClassEntity::getCategoryId).collect(Collectors.toList()));
        Map<Long, AsTeacherEntity> teacherMap = mapTeachers(courses.stream().map(AsCourseClassEntity::getTeacherId).collect(Collectors.toList()));
        Map<Long, Integer> sessionCountMap = countSessionsByCourse(courses.stream().map(AsCourseClassEntity::getId).collect(Collectors.toList()));
        Map<Long, List<AsCourseScheduleRuleEntity>> ruleMap = mapRulesByCourse(courses.stream().map(AsCourseClassEntity::getId).collect(Collectors.toList()));
        Map<Long, Integer> actualEnrolledCountMap = mapActualEnrolledCounts(courses.stream().map(AsCourseClassEntity::getId).collect(Collectors.toList()));
        Map<Long, String> displayGradeIdsMap = deriveClassAssignDisplayGradeIds(courses);
        List<AsServiceCourseDetailVO> result = new ArrayList<>();
        for (AsCourseClassEntity course : courses) {
            AsServiceCourseDetailVO vo = new AsServiceCourseDetailVO();
            BeanUtils.copyProperties(course, vo);
            vo.setQuota(asCourseQuotaService.view(course, null));
            if (!StringUtils.hasText(vo.getApplicableGradeIds())) {
                vo.setApplicableGradeIds(displayGradeIdsMap.get(course.getId()));
            }
            int enrolled = actualEnrolledCountMap.getOrDefault(course.getId(), 0);
            vo.setEnrolledCount(enrolled);
            AsCourseCategoryEntity category = categoryMap.get(course.getCategoryId());
            vo.setCategoryName(category == null ? null : category.getCategoryName());
            AsTeacherEntity teacher = teacherMap.get(course.getTeacherId());
            vo.setTeacherName(teacher == null ? null : teacher.getTeacherName());
            vo.setEnrollTypeName(AssembleUtil.getEnrollTypeName(course.getEnrollType()));
            vo.setClassStatusName(AssembleUtil.getClassStatusName(course.getClassStatus()));
            vo.setPlaceName(examRoomService.resolvePlaceName(course.getPlaceId()));
            vo.setPlaceId(course.getPlaceId());
            int capacity = course.getCapacity() == null ? 0 : course.getCapacity();
            vo.setRemainingCount(capacity <= 0 ? 999999 : Math.max(capacity - enrolled, 0));
            vo.setSessionCount(sessionCountMap.getOrDefault(course.getId(), 0));
            vo.setScheduleRules(ruleMap.getOrDefault(course.getId(), Collections.emptyList()).stream()
                    .map(this::toRuleAO)
                    .collect(Collectors.toList()));
            result.add(vo);
        }
        return result;
    }

    /**
     * 为行政班安排课程批量推导详情回显年级，只补充返回值，不改变持久化及实际适用班级范围。
     */
    private Map<Long, String> deriveClassAssignDisplayGradeIds(List<AsCourseClassEntity> courses) {
        Map<Long, List<Long>> classIdsByCourseId = new LinkedHashMap<>();
        Map<Long, AsCourseClassEntity> courseMap = new LinkedHashMap<>();
        Set<Long> allClassIds = new HashSet<>();
        for (AsCourseClassEntity course : serviceHelper.emptyList(courses)) {
            if (course.getId() == null
                    || !Objects.equals(course.getEnrollType(), CoreConstant.ENROLL_TYPE_CLASS_ASSIGN)
                    || StringUtils.hasText(course.getApplicableGradeIds())) {
                continue;
            }
            List<Long> classIds = courseClassScopeSupport.parseLongList(course.getApplicableClassIds());
            if (classIds.isEmpty()) {
                continue;
            }
            classIdsByCourseId.put(course.getId(), classIds);
            courseMap.put(course.getId(), course);
            allClassIds.addAll(classIds);
        }
        if (allClassIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<TEduclClass> classes = educlClassMapper.selectBatchIds(allClassIds);
        if (CollectionUtils.isEmpty(classes)) {
            return Collections.emptyMap();
        }
        Map<Long, TEduclClass> classMap = classes.stream()
                .filter(clazz -> clazz.getId() != null)
                .collect(Collectors.toMap(
                        TEduclClass::getId,
                        clazz -> clazz,
                        (first, second) -> first,
                        LinkedHashMap::new));
        Map<Long, String> result = new LinkedHashMap<>();
        for (Map.Entry<Long, List<Long>> entry : classIdsByCourseId.entrySet()) {
            AsCourseClassEntity course = courseMap.get(entry.getKey());
            List<Long> gradeIds = entry.getValue().stream()
                    .map(classMap::get)
                    .filter(Objects::nonNull)
                    .filter(clazz -> Objects.equals(clazz.getSchoolId(), course.getSchoolId()))
                    .map(TEduclClass::getGradeId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
            if (!gradeIds.isEmpty()) {
                result.put(entry.getKey(), JSON.toJSONString(gradeIds));
            }
        }
        return result;
    }

    /**
     * 按已选课学生名单口径批量统计课程班实际人数。
     */
    private Map<Long, Integer> mapActualEnrolledCounts(Collection<Long> courseClassIds) {
        List<Long> ids = courseClassIds == null ? Collections.emptyList()
                : courseClassIds.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<AsEnrollmentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsEnrollmentEntity::getIsValid, 1)
                .in(AsEnrollmentEntity::getCourseClassId, ids)
                .isNotNull(AsEnrollmentEntity::getSessionId)
                .in(AsEnrollmentEntity::getEnrollStatus, Arrays.asList(CoreConstant.ENROLL_STATUS_ENROLLED, CoreConstant.ENROLL_STATUS_ASSIGNED));
        List<AsEnrollmentEntity> enrollments = asEnrollmentMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(enrollments)) {
            return Collections.emptyMap();
        }
        Map<Long, AsStudentEntity> approvedStudentMap = mapStudents(enrollments.stream()
                .map(AsEnrollmentEntity::getStudentId)
                .collect(Collectors.toList()));
        Map<Long, Set<Long>> studentMap = new HashMap<>();
        for (AsEnrollmentEntity enrollment : enrollments) {
            if (enrollment.getCourseClassId() == null || !serviceHelper.isApprovedStudent(approvedStudentMap.get(enrollment.getStudentId()))) {
                continue;
            }
            studentMap.computeIfAbsent(enrollment.getCourseClassId(), key -> new HashSet<>())
                    .add(enrollment.getStudentId());
        }
        Map<Long, Integer> result = new HashMap<>();
        for (Map.Entry<Long, Set<Long>> entry : studentMap.entrySet()) {
            result.put(entry.getKey(), entry.getValue().size());
        }
        return result;
    }

    /**
     * 批量查询学生档案。
     */
    private Map<Long, AsStudentEntity> mapStudents(Collection<Long> ids) {
        List<Long> targetIds = ids == null ? Collections.emptyList()
                : ids.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (targetIds.isEmpty()) {
            return Collections.emptyMap();
        }
        List<AsStudentEntity> students = asStudentMapper.selectBatchIds(targetIds);
        if (CollectionUtils.isEmpty(students)) {
            return Collections.emptyMap();
        }
        return students.stream()
                .filter(Objects::nonNull)
                .filter(student -> student.getId() != null)
                .collect(Collectors.toMap(AsStudentEntity::getId, student -> student, (a, b) -> a));
    }

    private AsServiceEnrollmentStatVO buildEnrollmentStat(Long serviceId) {
        AsServiceEnrollmentStatVO stat = new AsServiceEnrollmentStatVO();
        if (serviceId == null) {
            return stat;
        }
        QueryWrapper<AsEnrollmentEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("is_valid", 1).eq("service_id", serviceId);
        SessionEnrollmentSupport.EnrollmentDistinctStat distinctStat =
                sessionEnrollmentSupport.countDistinctEnrollmentStat(serviceId, asEnrollmentMapper.selectList(wrapper));
        stat.setSelectedCount(distinctStat.getSelectedCount());
        stat.setUnselectedCount(distinctStat.getUnselectedCount());
        stat.setPendingCount(distinctStat.getPendingCount());
        stat.setCancelledCount(distinctStat.getCancelledCount());
        return stat;
    }




    private AsServiceSessionPreviewResultVO buildSessionPreview(AsServiceEntity service, List<CourseRuleBundle> bundles) {
        AsServiceSessionPreviewResultVO result = new AsServiceSessionPreviewResultVO();
        result.setServiceId(service.getId());
        List<AsCourseSessionPreviewVO> sessions = new ArrayList<>();
        Set<Long> teacherIds = new HashSet<>();
        Set<LocalDate> suspensionDates = termSuspensionDateSupport.listSuspensionDates(service);
        for (CourseRuleBundle bundle : bundles) {
            for (AsCourseScheduleRuleAO rule : bundle.rules) {
                List<AsCourseSessionPreviewVO> expanded = expandRule(
                        service, bundle.course, rule, suspensionDates);
                for (AsCourseSessionPreviewVO session : expanded) {
                    if (session.getTeacherId() != null) {
                        teacherIds.add(session.getTeacherId());
                    }
                }
                sessions.addAll(expanded);
            }
        }
        Map<Long, AsTeacherEntity> teacherMap = mapTeachers(teacherIds);
        for (AsCourseSessionPreviewVO session : sessions) {
            AsTeacherEntity teacher = teacherMap.get(session.getTeacherId());
            session.setTeacherName(teacher == null ? null : teacher.getTeacherName());
        }
        serviceHelper.markConflicts(sessions, listExistingSessionsForConflict(service));
        sessions.sort(Comparator.comparing(AsCourseSessionPreviewVO::getSessionDate, Comparator.nullsLast(Date::compareTo))
                .thenComparing(AsCourseSessionPreviewVO::getStartTime, Comparator.nullsLast(String::compareTo))
                .thenComparing(AsCourseSessionPreviewVO::getCourseClassId, Comparator.nullsLast(Long::compareTo)));
        result.setSessions(sessions);
        result.setTotalSessionCount(sessions.size());
        int conflictCount = (int) sessions.stream().filter(item -> Boolean.TRUE.equals(item.getConflict())).count();
        result.setConflictCount(conflictCount);
        result.setHasConflict(conflictCount > 0);
        return result;
    }

    private List<AsCourseSessionPreviewVO> expandRule(AsServiceEntity service,
                                                      AsCourseClassEntity course,
                                                      AsCourseScheduleRuleAO rule,
                                                      Set<LocalDate> suspensionDates) {
        return CourseScheduleSessionExpander.expand(
                        service, course, Collections.singletonList(rule), true, suspensionDates)
                .stream()
                .map(slot -> toPreviewSession(course, rule, slot.getSessionDate()))
                .collect(Collectors.toList());
    }

    private AsCourseSessionPreviewVO toPreviewSession(AsCourseClassEntity course, AsCourseScheduleRuleAO rule, Date date) {
        AsCourseSessionPreviewVO vo = new AsCourseSessionPreviewVO();
        vo.setCourseClassId(course.getId());
        vo.setCourseClassName(course.getCourseClassName());
        vo.setSessionDate(date);
        vo.setWeekDay(AssembleUtil.getWeekDay(date));
        vo.setWeekDayName(AssembleUtil.getWeekDayName(vo.getWeekDay()));
        vo.setStartTime(rule.getStartTime());
        vo.setEndTime(rule.getEndTime());
        vo.setSessionStartDate(AssembleUtil.combineDateAndTime(date, AssembleUtil.parseTime(rule.getStartTime())));
        vo.setSessionEndDate(AssembleUtil.combineDateAndTime(date, AssembleUtil.parseTime(rule.getEndTime())));
        vo.setTeacherId(rule.getTeacherId() == null ? course.getTeacherId() : rule.getTeacherId());
        vo.setPlaceName(StringUtils.hasText(rule.getPlaceId()) ? rule.getPlaceId() : course.getPlaceId());
        vo.setSchedulePeriodId(rule.getSchedulePeriodId());
        return vo;
    }



    /**
     * 按排课规则重建未开始课次。
     * 重复发布时会先逻辑删除旧未开始课次，再插入新记录，保证课次与当前规则一致。
     */
    private int rebuildSessions(AsServiceEntity service, List<AsCourseClassEntity> courses, List<CourseRuleBundle> preferredBundles) {
        if (CollectionUtils.isEmpty(courses)) {
            return 0;
        }
        List<CourseRuleBundle> bundles = preferredBundles == null ? resolveCourseRuleBundles(service, null, courses) : preferredBundles;
        Set<LocalDate> suspensionDates = termSuspensionDateSupport.listSuspensionDates(service);
        List<Long> courseIds = courses.stream().map(AsCourseClassEntity::getId).filter(Objects::nonNull).collect(Collectors.toList());
        RebuildEnrollmentSnapshot enrollmentSnapshot = snapshotNotStartedSessionEnrollments(service, courseIds);
        if (!courseIds.isEmpty()) {
            UpdateWrapper<AsCourseSessionEntity> invalidWrapper = new UpdateWrapper<>();
            invalidWrapper.in("course_class_id", courseIds)
                    .eq("is_valid", 1)
                    .eq("session_status", CoreConstant.SESSION_STATUS_NOT_STARTED)
                    .set("is_valid", 0)
                    .set("update_user", currentUserId())
                    .set("update_time", new Date());
            asCourseSessionMapper.update(null, invalidWrapper);
        }
        int count = 0;
        Date rebuildTime = new Date();
        for (CourseRuleBundle bundle : bundles) {
            for (AsCourseScheduleRuleAO rule : bundle.rules) {
                for (AsCourseSessionPreviewVO preview : expandRule(
                        service, bundle.course, rule, suspensionDates)) {
                    if (serviceHelper.isEndedSessionPreview(preview, rebuildTime)) {
                        continue;
                    }
                    AsCourseSessionEntity session = new AsCourseSessionEntity();
                    session.setSchoolId(service.getSchoolId());
                    session.setCourseClassId(bundle.course.getId());
                    session.setSessionDate(preview.getSessionDate());
                    session.setWeekDay(preview.getWeekDay());
                    session.setSchedulePeriodId(preview.getSchedulePeriodId());
                    session.setStartTime(AssembleUtil.parseTime(preview.getStartTime()));
                    session.setEndTime(AssembleUtil.parseTime(preview.getEndTime()));
                    session.setSessionStartDate(AssembleUtil.combineDateAndTime(session.getSessionDate(), session.getStartTime()));
                    session.setSessionEndDate(AssembleUtil.combineDateAndTime(session.getSessionDate(), session.getEndTime()));
                    session.setTeacherId(preview.getTeacherId());
                    session.setOriginalTeacherId(preview.getTeacherId());
                    session.setAdjustStatus(0);
                    session.setPlaceName(preview.getPlaceName());
                    session.setSessionStatus(CoreConstant.SESSION_STATUS_NOT_STARTED);
                    session.setCreateUser(currentUserId());
                    session.setUpdateUser(currentUserId());
                    session.setCreateTime(new Date());
                    session.setUpdateTime(new Date());
                    session.setIsValid(1);
                    asCourseSessionMapper.insert(session);
                    count++;
                }
            }
        }
        migrateEnrollmentSnapshotsToNewSessions(courses, enrollmentSnapshot);
        return count;
    }

    /**
     * 重建课次前抓取未开始课次上的有效选课快照，避免新课次生成后学生课表丢失。
     * 只快照未开始课次，已开始或已完成课次保留原始报名、去向和考勤链路。
     */
    private RebuildEnrollmentSnapshot snapshotNotStartedSessionEnrollments(AsServiceEntity service, List<Long> courseIds) {
        if (service == null || CollectionUtils.isEmpty(courseIds)) {
            return RebuildEnrollmentSnapshot.empty();
        }
        QueryWrapper<AsCourseSessionEntity> sessionWrapper = new QueryWrapper<>();
        sessionWrapper.eq("is_valid", 1)
                .eq("session_status", CoreConstant.SESSION_STATUS_NOT_STARTED)
                .in("course_class_id", courseIds)
                .select("id", "course_class_id");
        List<AsCourseSessionEntity> sessions = asCourseSessionMapper.selectList(sessionWrapper);
        if (CollectionUtils.isEmpty(sessions)) {
            return RebuildEnrollmentSnapshot.empty();
        }
        List<Long> sessionIds = sessions.stream().map(AsCourseSessionEntity::getId).filter(Objects::nonNull).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(sessionIds)) {
            return RebuildEnrollmentSnapshot.empty();
        }
        Map<Long, List<Long>> sessionIdsByCourse = sessions.stream()
                .filter(session -> session.getCourseClassId() != null && session.getId() != null)
                .collect(Collectors.groupingBy(AsCourseSessionEntity::getCourseClassId, LinkedHashMap::new,
                        Collectors.mapping(AsCourseSessionEntity::getId, Collectors.toList())));
        QueryWrapper<AsEnrollmentEntity> enrollmentWrapper = new QueryWrapper<>();
        enrollmentWrapper.eq("is_valid", 1)
                .eq("school_id", service.getSchoolId())
                .eq("service_id", service.getId())
                .in("course_class_id", courseIds)
                .in("session_id", sessionIds)
                .in("enroll_status", Arrays.asList(
                        CoreConstant.ENROLL_STATUS_PENDING,
                        CoreConstant.ENROLL_STATUS_ENROLLED,
                        CoreConstant.ENROLL_STATUS_ASSIGNED));
        List<AsEnrollmentEntity> enrollments = asEnrollmentMapper.selectList(enrollmentWrapper);
        if (CollectionUtils.isEmpty(enrollments)) {
            return new RebuildEnrollmentSnapshot(Collections.emptyMap(), sessionIdsByCourse);
        }
        Map<Long, Map<Long, EnrollmentSessionSnapshot>> grouped = new LinkedHashMap<>();
        for (AsEnrollmentEntity enrollment : enrollments) {
            if (enrollment.getCourseClassId() == null || enrollment.getStudentId() == null) {
                continue;
            }
            grouped.computeIfAbsent(enrollment.getCourseClassId(), key -> new LinkedHashMap<>())
                    .putIfAbsent(enrollment.getStudentId(), new EnrollmentSessionSnapshot(enrollment));
        }
        Map<Long, List<EnrollmentSessionSnapshot>> result = new LinkedHashMap<>();
        for (Map.Entry<Long, Map<Long, EnrollmentSessionSnapshot>> entry : grouped.entrySet()) {
            result.put(entry.getKey(), new ArrayList<>(entry.getValue().values()));
        }
        return new RebuildEnrollmentSnapshot(result, sessionIdsByCourse);
    }

    private void migrateEnrollmentSnapshotsToNewSessions(List<AsCourseClassEntity> courses, RebuildEnrollmentSnapshot snapshot) {
        if (CollectionUtils.isEmpty(courses) || snapshot == null || CollectionUtils.isEmpty(snapshot.enrollmentsByCourse)) {
            return;
        }
        Long operatorUserId = currentUserId();
        for (AsCourseClassEntity course : courses) {
            List<EnrollmentSessionSnapshot> snapshots = snapshot.enrollmentsByCourse.get(course.getId());
            if (CollectionUtils.isEmpty(snapshots)) {
                continue;
            }
            List<AsCourseSessionEntity> sessions = listNotStartedSessionsByCourse(course.getId());
            if (CollectionUtils.isEmpty(sessions)) {
                continue;
            }
            for (EnrollmentSessionSnapshot enrollmentSnapshot : snapshots) {
                sessionEnrollmentSupport.replaceStudentCourseSessions(enrollmentSnapshot.toTemplate(),
                        snapshot.oldSessionIdsByCourse.get(course.getId()), sessions, operatorUserId);
            }
        }
    }

    /**
     * 查询课程班重建后仍处于未开始状态的有效课次，避免迁移逻辑触碰已开始或已结束的历史课次。
     */
    private List<AsCourseSessionEntity> listNotStartedSessionsByCourse(Long courseClassId) {
        if (courseClassId == null) {
            return Collections.emptyList();
        }
        QueryWrapper<AsCourseSessionEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("is_valid", 1)
                .eq("course_class_id", courseClassId)
                .eq("session_status", CoreConstant.SESSION_STATUS_NOT_STARTED)
                .orderByAsc("session_date", "start_time", "id");
        return asCourseSessionMapper.selectList(wrapper);
    }

    /**
     * 为行政班安排课程增量补齐分班记录。
     * 已存在的学生保持原记录不变，仅为当前范围内尚未分班的待注册或注册已通过学生追加课次。
     */
    private int generateClassAssignEnrollments(AsServiceEntity service, List<AsCourseClassEntity> courses) {
        return generateClassAssignEnrollments(service, courses, currentUserId());
    }

    /**
     * 发布成功后异步执行行政班学生安排。
     */
    @AsClassAssignAsync
    @Async
    @Transactional(rollbackFor = Exception.class)
    public CompletableFuture<Void> generateClassAssignEnrollmentsAsync(Long serviceId) {
        AsServiceEntity service = serviceId == null ? null : baseMapper.selectById(serviceId);
        if (!serviceHelper.isValid(service)) {
            log.warn("异步行政班安排失败，课后服务不存在，serviceId={}", serviceId);
            return CompletableFuture.completedFuture(null);
        }
        List<AsCourseClassEntity> courses = listServiceCourses(serviceId);
        int assignedCount = generateClassAssignEnrollments(service, courses, service.getUpdateUser());
        log.info("异步行政班安排完成，serviceId={}，安排学生{}人", serviceId, assignedCount);
        return CompletableFuture.completedFuture(null);
    }

    private int generateClassAssignEnrollments(AsServiceEntity service, List<AsCourseClassEntity> courses,
                                               Long operatorUserId) {
        int totalAssigned = 0;
        for (AsCourseClassEntity course : courses) {
            if (!Objects.equals(course.getEnrollType(), CoreConstant.ENROLL_TYPE_CLASS_ASSIGN)) {
                continue;
            }
            List<AsCourseSessionEntity> sessions = sessionEnrollmentSupport.listValidSessionsByCourseClass(course.getId());
            if (!Objects.equals(service.getServiceStatus(), CoreConstant.SERVICE_STATUS_DRAFT)) {
                Date now = new Date();
                sessions = sessions.stream()
                        .filter(session -> Objects.equals(session.getSessionStatus(), CoreConstant.SESSION_STATUS_NOT_STARTED))
                        .filter(session -> session.getSessionEndDate() == null || session.getSessionEndDate().after(now))
                        .collect(Collectors.toList());
            }
            if (sessions.isEmpty()) {
                continue;
            }
            List<Long> classIds = courseClassScopeSupport.resolveClassIds(service.getSchoolId(), course);
            if (classIds.isEmpty()) {
                continue;
            }
            QueryWrapper<AsStudentEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("is_valid", 1)
                    .eq("school_id", service.getSchoolId())
                    .eq("term_id", service.getTermId())
                    .in("register_status", CoreConstant.CLASS_ASSIGN_STUDENT_REGISTER_STATUSES)
                    .in("class_id", classIds);
            List<AsStudentEntity> students = asStudentMapper.selectList(wrapper);
            Set<Long> assignedStudentIds = listAssignedStudentIds(service.getId(), course.getId());
            int assigned = assignedStudentIds.size();
            for (AsStudentEntity student : serviceHelper.emptyList(students)) {
                if (!CoreConstant.CLASS_ASSIGN_STUDENT_REGISTER_STATUSES.contains(student.getRegisterStatus())) {
                    continue;
                }
                if (assignedStudentIds.contains(student.getId())) {
                    continue;
                }
                if (!serviceHelper.matchesCourseGenderRequirement(student, course)) {
                    continue;
                }
                if (!serviceHelper.hasCourseCapacity(course, assigned)) {
                    continue;
                }
                AsEnrollmentEntity template = new AsEnrollmentEntity();
                template.setSchoolId(service.getSchoolId());
                template.setServiceId(service.getId());
                template.setStudentId(student.getId());
                template.setStudentNo(student.getStudentNo());
                template.setCourseClassId(course.getId());
                template.setEnrollSource(CoreConstant.ENROLL_SOURCE_CLASS_ASSIGN);
                template.setEnrollStatus(CoreConstant.ENROLL_STATUS_ASSIGNED);
                template.setParentConfirmRequired(course.getParentConfirmRequired());
                template.setOperatorType(CoreConstant.OPERATOR_SYSTEM);
                template.setOperatorUserId(operatorUserId);
                template.setOperatorName("系统");
                template.setEnrollTime(new Date());
                AsEnrollmentEntity first = sessionEnrollmentSupport.appendStudentToCourseSessions(template, sessions, operatorUserId);
                if (first == null) {
                    continue;
                }
                saveEnrollmentLog(first, null, course.getId(), CoreConstant.ENROLL_STATUS_UNASSIGNED, CoreConstant.ENROLL_STATUS_ASSIGNED,
                        CoreConstant.ACTION_CLASS_ASSIGN, "行政班安排", operatorUserId);
                assignedStudentIds.add(student.getId());
                assigned++;
                totalAssigned++;
            }
            AsCourseClassEntity update = new AsCourseClassEntity();
            update.setId(course.getId());
            update.setEnrolledCount(assigned);
            update.setUpdateUser(operatorUserId);
            update.setUpdateTime(new Date());
            asCourseClassMapper.updateById(update);
        }
        return totalAssigned;
    }

    /**
     * 查询课程班已有的有效学生，防止保存或重复发布时覆盖原分班记录。
     */
    private Set<Long> listAssignedStudentIds(Long serviceId, Long courseClassId) {
        if (serviceId == null || courseClassId == null) {
            return new HashSet<>();
        }
        LambdaQueryWrapper<AsEnrollmentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsEnrollmentEntity::getIsValid, 1)
                .eq(AsEnrollmentEntity::getServiceId, serviceId)
                .eq(AsEnrollmentEntity::getCourseClassId, courseClassId)
                .isNotNull(AsEnrollmentEntity::getSessionId)
                .in(AsEnrollmentEntity::getEnrollStatus,
                        Arrays.asList(CoreConstant.ENROLL_STATUS_ENROLLED, CoreConstant.ENROLL_STATUS_ASSIGNED));
        List<AsEnrollmentEntity> enrollments = asEnrollmentMapper.selectList(wrapper);
        return serviceHelper.emptyList(enrollments).stream()
                .map(AsEnrollmentEntity::getStudentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * 判断学生性别是否满足课程班要求。
     * genderRequirement：0不限，1男，2女；student.gender：0男，1女。
     */
    private void saveEnrollmentLog(AsEnrollmentEntity enrollment, Long beforeClassId, Long afterClassId,
                                   Integer beforeStatus, Integer afterStatus, Integer actionType, String remark,
                                   Long operatorUserId) {
        AsEnrollmentLogEntity logEntity = new AsEnrollmentLogEntity();
        logEntity.setSchoolId(enrollment.getSchoolId());
        logEntity.setEnrollmentId(enrollment.getId());
        logEntity.setServiceId(enrollment.getServiceId());
        logEntity.setStudentId(enrollment.getStudentId());
        logEntity.setStudentNo(enrollment.getStudentNo());
        logEntity.setBeforeCourseClassId(beforeClassId);
        logEntity.setAfterCourseClassId(afterClassId);
        logEntity.setBeforeEnrollStatus(beforeStatus);
        logEntity.setAfterEnrollStatus(afterStatus);
        logEntity.setActionType(actionType);
        logEntity.setOperatorType(CoreConstant.OPERATOR_SYSTEM);
        logEntity.setOperatorUserId(operatorUserId);
        logEntity.setOperatorName("系统");
        logEntity.setRemark(remark);
        logEntity.setCreateUser(operatorUserId);
        logEntity.setCreateTime(new Date());
        logEntity.setIsValid(1);
        asEnrollmentLogMapper.insert(logEntity);
    }

    private List<CourseRuleBundle> resolveCourseRuleBundles(AsServiceEntity service, List<AsServiceCourseItemAO> requestCourses) {
        if (!CollectionUtils.isEmpty(requestCourses)) {
            List<CourseRuleBundle> result = new ArrayList<>();
            for (AsServiceCourseItemAO item : requestCourses) {
                AsCourseClassEntity course = item.getId() == null ? toTransientCourse(service, item) : asCourseClassMapper.selectById(item.getId());
                if (course == null) {
                    course = toTransientCourse(service, item);
                }
                result.add(new CourseRuleBundle(course, serviceHelper.emptyList(item.getScheduleRules())));
            }
            return result;
        }
        return resolveCourseRuleBundles(service, null, listServiceCourses(service.getId()));
    }

    private List<CourseRuleBundle> resolveCourseRuleBundles(AsServiceEntity service, List<AsServiceCourseItemAO> requestCourses, List<AsCourseClassEntity> savedCourses) {
        if (!CollectionUtils.isEmpty(requestCourses)) {
            return resolveCourseRuleBundles(service, requestCourses);
        }
        Map<Long, List<AsCourseScheduleRuleEntity>> ruleMap = mapRulesByCourse(savedCourses.stream().map(AsCourseClassEntity::getId).collect(Collectors.toList()));
        List<CourseRuleBundle> result = new ArrayList<>();
        for (AsCourseClassEntity course : savedCourses) {
            List<AsCourseScheduleRuleAO> rules = ruleMap.getOrDefault(course.getId(), Collections.emptyList()).stream()
                    .map(this::toRuleAO)
                    .collect(Collectors.toList());
            result.add(new CourseRuleBundle(course, rules));
        }
        return result;
    }

    private AsCourseClassEntity toTransientCourse(AsServiceEntity service, AsServiceCourseItemAO item) {
        AsCourseClassEntity course = new AsCourseClassEntity();
        course.setId(item.getId());
        course.setSchoolId(service.getSchoolId());
        course.setServiceId(service.getId());
        course.setCourseClassName(item.getCourseClassName());
        course.setCategoryId(item.getCategoryId());
        course.setEnrollType(item.getEnrollType());
        course.setApplicableClassIds(item.getApplicableClassIds());
        course.setApplicableGradeIds(item.getApplicableGradeIds());
        course.setParentConfirmRequired(item.getParentConfirmRequired());
        course.setGenderRequirement(item.getGenderRequirement());
        course.setCapacity(item.getCapacity());
        course.setEnrolledCount(0);
        course.setTeacherId(item.getTeacherId());
        course.setPlaceId(item.getPlaceId());
        course.setIntro(item.getIntro());
        course.setClassStatus(CoreConstant.CLASS_STATUS_NOT_STARTED);
        course.setIsValid(1);
        return course;
    }

    private AsServiceCourseItemAO toCourseItem(AsCourseClassEntity course, List<AsCourseScheduleRuleEntity> rules) {
        AsServiceCourseItemAO item = new AsServiceCourseItemAO();
        item.setId(course.getId());
        item.setCourseClassName(course.getCourseClassName());
        item.setCategoryId(course.getCategoryId());
        item.setModuleId(course.getModuleId());
        item.setEnrollType(course.getEnrollType());
        item.setApplicableClassIds(course.getApplicableClassIds());
        item.setApplicableGradeIds(course.getApplicableGradeIds());
        item.setParentConfirmRequired(course.getParentConfirmRequired());
        item.setGenderRequirement(course.getGenderRequirement());
        item.setCapacity(course.getCapacity());
        item.setGradeQuotas(asCourseQuotaService.configuration(course));
        item.setTeacherId(course.getTeacherId());
        item.setPlaceId(course.getPlaceId());
        item.setIntro(course.getIntro());
        item.setScheduleRules(rules.stream().map(this::toRuleAO).collect(Collectors.toList()));
        return item;
    }

    private List<AsCourseSessionEntity> listExistingSessionsForConflict(AsServiceEntity service) {
        QueryWrapper<AsCourseSessionEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("is_valid", 1)
                .eq("school_id", service.getSchoolId())
                .ne("session_status", CoreConstant.SESSION_STATUS_CANCELLED);
        return asCourseSessionMapper.selectList(wrapper);
    }

    private List<AsCourseClassEntity> listServiceCourses(Long serviceId) {
        QueryWrapper<AsCourseClassEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("is_valid", 1)
                .eq("service_id", serviceId)
                .orderByAsc("id");
        return asCourseClassMapper.selectList(wrapper);
    }

    private List<AsCourseScheduleRuleEntity> listRulesByCourse(Long courseClassId) {
        if (courseClassId == null) {
            return Collections.emptyList();
        }
        QueryWrapper<AsCourseScheduleRuleEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("is_valid", 1)
                .eq("course_class_id", courseClassId)
                .orderByAsc("id");
        return asCourseScheduleRuleMapper.selectList(wrapper);
    }

    private Map<Long, List<AsCourseScheduleRuleEntity>> mapRulesByCourse(Collection<Long> courseIds) {
        List<Long> ids = serviceHelper.cleanIds(courseIds);
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        QueryWrapper<AsCourseScheduleRuleEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("is_valid", 1)
                .in("course_class_id", ids)
                .orderByAsc("id");
        return asCourseScheduleRuleMapper.selectList(wrapper).stream()
                .collect(Collectors.groupingBy(AsCourseScheduleRuleEntity::getCourseClassId, LinkedHashMap::new, Collectors.toList()));
    }

    private AsCourseScheduleRuleAO toRuleAO(AsCourseScheduleRuleEntity entity) {
        AsCourseScheduleRuleAO rule = new AsCourseScheduleRuleAO();
        rule.setRuleType(Objects.equals(entity.getRuleType(), CoreConstant.RULE_TYPE_WEEKLY) ? "weekly" : "date");
        rule.setWeekDays(serviceHelper.parseIntegerList(entity.getWeekDaysJson()));
        rule.setSessionDate(entity.getSessionDate());
        rule.setSchedulePeriodId(entity.getSchedulePeriodId());
        rule.setStartTime(AssembleUtil.formatTime(entity.getStartTime()));
        rule.setEndTime(AssembleUtil.formatTime(entity.getEndTime()));
        rule.setPlaceId(entity.getPlaceName());
        rule.setTeacherId(entity.getTeacherId());
        return rule;
    }

    private Map<Long, EduclTermEntity> mapTerms(Collection<Long> ids) {
        List<Long> cleanIds = serviceHelper.cleanIds(ids);
        if (cleanIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return educlTermMapper.selectBatchIds(cleanIds).stream()
                .collect(Collectors.toMap(EduclTermEntity::getId, item -> item, (a, b) -> a));
    }

    private Map<Long, AsScheduleTemplateEntity> mapTemplates(Collection<Long> ids) {
        List<Long> cleanIds = serviceHelper.cleanIds(ids);
        if (cleanIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return asScheduleTemplateMapper.selectBatchIds(cleanIds).stream()
                .collect(Collectors.toMap(AsScheduleTemplateEntity::getId, item -> item, (a, b) -> a));
    }

    private Map<Long, AsCourseCategoryEntity> mapCategories(Collection<Long> ids) {
        List<Long> cleanIds = serviceHelper.cleanIds(ids);
        if (cleanIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return asCourseCategoryMapper.selectBatchIds(cleanIds).stream()
                .collect(Collectors.toMap(AsCourseCategoryEntity::getId, item -> item, (a, b) -> a));
    }

    private Map<Long, AsTeacherEntity> mapTeachers(Collection<Long> ids) {
        List<Long> cleanIds = serviceHelper.cleanIds(ids);
        if (cleanIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return asTeacherMapper.selectBatchIds(cleanIds).stream()
                .collect(Collectors.toMap(AsTeacherEntity::getId, item -> item, (a, b) -> a));
    }

    private Map<Long, Integer> countCourses(Collection<Long> serviceIds) {
        List<Long> ids = serviceHelper.cleanIds(serviceIds);
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        QueryWrapper<AsCourseClassEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("is_valid", 1).in("service_id", ids);
        List<AsCourseClassEntity> courses = asCourseClassMapper.selectList(wrapper);
        Map<Long, Integer> result = new HashMap<>();
        for (AsCourseClassEntity course : serviceHelper.emptyList(courses)) {
            result.put(course.getServiceId(), result.getOrDefault(course.getServiceId(), 0) + 1);
        }
        return result;
    }

    private Map<Long, Integer> countSessions(Collection<Long> serviceIds) {
        List<Long> ids = serviceHelper.cleanIds(serviceIds);
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        QueryWrapper<AsCourseClassEntity> courseWrapper = new QueryWrapper<>();
        courseWrapper.eq("is_valid", 1).in("service_id", ids);
        List<AsCourseClassEntity> courses = asCourseClassMapper.selectList(courseWrapper);
        Map<Long, Long> courseServiceMap = courses.stream()
                .filter(course -> course.getId() != null)
                .collect(Collectors.toMap(AsCourseClassEntity::getId, AsCourseClassEntity::getServiceId, (a, b) -> a));
        if (courseServiceMap.isEmpty()) {
            return Collections.emptyMap();
        }
        QueryWrapper<AsCourseSessionEntity> sessionWrapper = new QueryWrapper<>();
        sessionWrapper.eq("is_valid", 1).in("course_class_id", courseServiceMap.keySet());
        List<AsCourseSessionEntity> sessions = asCourseSessionMapper.selectList(sessionWrapper);
        Map<Long, Integer> result = new HashMap<>();
        for (AsCourseSessionEntity session : serviceHelper.emptyList(sessions)) {
            Long serviceId = courseServiceMap.get(session.getCourseClassId());
            if (serviceId != null) {
                result.put(serviceId, result.getOrDefault(serviceId, 0) + 1);
            }
        }
        return result;
    }

    private Map<Long, Integer> countSessionsByCourse(Collection<Long> courseIds) {
        List<Long> ids = serviceHelper.cleanIds(courseIds);
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        QueryWrapper<AsCourseSessionEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("is_valid", 1).in("course_class_id", ids);
        List<AsCourseSessionEntity> sessions = asCourseSessionMapper.selectList(wrapper);
        Map<Long, Integer> result = new HashMap<>();
        for (AsCourseSessionEntity session : serviceHelper.emptyList(sessions)) {
            result.put(session.getCourseClassId(), result.getOrDefault(session.getCourseClassId(), 0) + 1);
        }
        return result;
    }

    private Map<Long, EduclGradeEntity> mapGrades(Collection<Long> ids) {
        List<Long> cleanIds = serviceHelper.cleanIds(ids);
        if (cleanIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return educlGradeMapper.selectBatchIds(cleanIds).stream()
                .collect(Collectors.toMap(EduclGradeEntity::getId, g -> g, (a, b) -> a));
    }


    /**
     * 生成排课服务编号，termType:1-上学期，2-下学期
     * S+2位学年+学校编码后4位+1位学期（第一学期1，第二学期2）+6位自增序号；例如S2637951000001 共14位
     *
     * @param schoolId
     * @param termType
     * @return
     */
    private String generateServiceCode(String schoolId, Integer termType) {
        String year = DateUtils.getYearOfDateLast2(new Date());
        // 获取学校编码后4位
        String code = schoolId.substring(schoolId.length() - 4);
        String serviceCode = "S" + year + code + termType + redisCache.incrementAndGetPadded(CoreConstant.edu_bus_pk_code, 6, 0);
        return serviceCode;
    }

    /**
     * 生成课程班编号:C+年份+选课类型（公开选课：01）+5位序号自增 例如：C20260100001共12位
     *
     * @return
     */
    private String generateCourseClassCode(Integer enrollType) {
        String year = DateUtils.getYearOfDate(new Date()) + "";
        String serviceCode = "C" + year + "0" + enrollType + redisCache.incrementAndGetPadded(CoreConstant.edu_bus_pk_code_class, 5, 0);
        return serviceCode;
    }

    private Long currentSchoolId(Long fallback) {
        if (fallback != null) {
            return fallback;
        }
        Long schoolId = SmartRequestTokenUtil.getRequestUserStuSchoolId();
        return schoolId == null || schoolId == 0 ? fallback : schoolId;
    }

    private Long currentUserId() {
        Long requestUserId = SmartRequestTokenUtil.getRequestUserId();
        return requestUserId == null ? AsAsyncOperatorContext.getOperatorUserId() : requestUserId;
    }

    public PageResultDTO<CourseSchedulePageVO> queryCourseSchedulePage(CourseSchedulePageQueryAO qo) {
        Long schoolId = currentSchoolId(qo.getSchoolId());
        qo.setSchoolId(schoolId);
        EduclTermEntity term = qo.getTermId() == null
                ? studentTermScopeSupport.resolveReadableTerm(schoolId)
                : studentTermScopeSupport.requireTermInSchool(schoolId, qo.getTermId());
        qo.setTermId(term.getId());
        Page<CourseSchedulePageVO> page = baseMapper.queryCourseSchedulePage(
                new Page<>(qo.getPageIndex(), qo.getPageSize()), qo);
        PageResultDTO<CourseSchedulePageVO> result = new PageResultDTO<>();
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setPageIndex(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setList(page.getRecords());
        return result;
    }

    public JsonResult<ServiceCourseScheduleVO> getCourseScheduleDetailList(CourseScheduleDetailListQueryAO ao) {
        if (ao == null || ao.getId() == null) {
            return JsonResult.buildError("服务id不能为空");
        }

        Long schoolId = currentSchoolId(ao.getSchoolId());
        AsServiceEntity service = baseMapper.selectById(ao.getId());
        if (service == null || !Integer.valueOf(1).equals(service.getIsValid())) {
            return JsonResult.buildError("服务不存在或已删除");
        }
        if (!Objects.equals(service.getSchoolId(), schoolId)) {
            return JsonResult.buildError("课后服务不属于当前学校");
        }
        EduclTermEntity term = studentTermScopeSupport.requireTermInSchool(
                service.getSchoolId(), service.getTermId());

        // ========== 1. 构建班级列表（从服务scope字段解析） ==========
        List<TEduclClass> classList = resolveServiceScopeClasses(service, schoolId);
        if (CollectionUtils.isEmpty(classList)) {
            ServiceCourseScheduleVO emptyVo = new ServiceCourseScheduleVO();
            emptyVo.setTermId(term.getId());
            emptyVo.setTermName(term.getTermName());
            emptyVo.setWeekHeaders(Collections.emptyList());
            emptyVo.setRows(Collections.emptyList());
            return JsonResult.buildSuccess(emptyVo);
        }

        // 按年级、班级排序
        classList.sort(Comparator.comparing(TEduclClass::getGradeId, Comparator.nullsFirst(Long::compareTo))
                .thenComparing(TEduclClass::getClassName, Comparator.nullsFirst(String::compareTo)));

        // 筛选
        if (ObjectUtil.isNotNull(ao.getGradeId())) {
            classList = classList.stream()
                    .filter(c -> ao.getGradeId().equals(c.getGradeId()))
                    .collect(Collectors.toList());
        }
        if (ObjectUtil.isNotNull(ao.getClassId())) {
            classList = classList.stream()
                    .filter(c -> ao.getClassId().equals(c.getId()))
                    .collect(Collectors.toList());
        }

        // ========== 2. 获取年级Map ==========
        Set<Long> gradeIds = classList.stream()
                .map(TEduclClass::getGradeId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, EduclGradeEntity> gradeMap = mapGrades(gradeIds);

        // ========== 3. 查询服务下所有课次 ==========
        List<CourseScheduleListDTO> sessions = baseMapper.queryCourseSessionList(service.getId());

        // ========== 4. 从课次数据中动态生成表头（weekDay × sectionNo 去重排序） ==========
        List<WeekHeaderVO> weekHeaders = buildWeekHeadersFromSessions(sessions);

        // ========== 5. 组装行数据 ==========
        List<ScheduleRowVO> rows = new ArrayList<>();
        for (TEduclClass clazz : classList) {
            ScheduleRowVO row = new ScheduleRowVO();
            row.setClassId(clazz.getId());
            row.setClassName(clazz.getClassName());
            row.setGradeId(clazz.getGradeId());
            EduclGradeEntity grade = gradeMap.get(clazz.getGradeId());
            row.setGradeName(grade != null ? grade.getGradeName() : null);

            Map<String, List<ScheduleCellVO>> cellMap = new HashMap<>();
            for (CourseScheduleListDTO session : sessions) {
                if(org.apache.commons.lang3.StringUtils.isNotBlank(session.getApplicableClassIds())){
                    List<Long> applicableClassIds = JSONArray.parseArray(session.getApplicableClassIds(), Long.class);
                    if(!CollectionUtils.isEmpty(applicableClassIds) && applicableClassIds.contains(clazz.getId())) {
                        int weekDay = session.getWeekDay() == null ? 0 : session.getWeekDay();
                        int sectionNo = session.getSectionNo() == null ? 0 : session.getSectionNo();
                        String key = weekDay + "_" + sectionNo;
                        cellMap.computeIfAbsent(key, k -> new ArrayList<>())
                                .add(new ScheduleCellVO(session.getCourseClassName(), session.getTeacherName()));
                    }
                }else {
                    List<Long> applicableGardeIds = JSONArray.parseArray(session.getApplicableGradeIds(), Long.class);
                    if(!CollectionUtils.isEmpty(applicableGardeIds) && applicableGardeIds.contains(clazz.getGradeId())) {
                        int weekDay = session.getWeekDay() == null ? 0 : session.getWeekDay();
                        int sectionNo = session.getSectionNo() == null ? 0 : session.getSectionNo();
                        String key = weekDay + "_" + sectionNo;
                        cellMap.computeIfAbsent(key, k -> new ArrayList<>())
                                .add(new ScheduleCellVO(session.getCourseClassName(), session.getTeacherName()));
                    }
                }
            }
            row.setCellMap(cellMap);
            rows.add(row);
        }

        ServiceCourseScheduleVO vo = new ServiceCourseScheduleVO();
        vo.setTermId(term.getId());
        vo.setTermName(term.getTermName());
        vo.setWeekHeaders(weekHeaders);
        vo.setRows(rows);
        return JsonResult.buildSuccess(vo);
    }

    /**
     * 从服务下课程班适用范围解析应展示的行政班列表。
     */
    private List<TEduclClass> resolveServiceScopeClasses(AsServiceEntity service, Long schoolId) {
        List<Long> classIds = courseClassScopeSupport.listServiceScopeClassIds(schoolId, service.getId(), asCourseClassMapper);
        if (classIds.isEmpty()) {
            return Collections.emptyList();
        }
        QueryWrapper<TEduclClass> wrapper = new QueryWrapper<>();
        wrapper.in("id", classIds).eq("is_valid", 1);
        if (schoolId != null && schoolId > 0) {
            wrapper.eq("school_id", schoolId);
        }
        return educlClassMapper.selectList(wrapper);
    }

    /**
     * 从课次数据中动态生成表头（不依赖schedule_period表）
     * 固定周一到周五，有课次的天从数据中提取节次，没有课次的天 periods 为空列表
     */
    private List<WeekHeaderVO> buildWeekHeadersFromSessions(List<CourseScheduleListDTO> sessions) {
        // key: weekDay, value: LinkedHashMap<sectionNo, PeriodHeaderVO>（以sectionNo为key去重）
        Map<Integer, LinkedHashMap<Integer, PeriodHeaderVO>> weekPeriodMap = new LinkedHashMap<>();
        if (!CollectionUtils.isEmpty(sessions)) {
            for (CourseScheduleListDTO session : sessions) {
                int weekDay = session.getWeekDay() == null ? 0 : session.getWeekDay();
                // 只处理周一到周五（1-5）
                if (weekDay < 1 || weekDay > 5) {
                    continue;
                }
                int sectionNo = session.getSectionNo() == null ? 0 : session.getSectionNo();
                weekPeriodMap.computeIfAbsent(weekDay, k -> new LinkedHashMap<>())
                        .computeIfAbsent(sectionNo, k -> {
                            PeriodHeaderVO ph = new PeriodHeaderVO();
                            ph.setSectionNo(sectionNo);
                            ph.setSectionName(session.getSectionName());
                            ph.setTimeRange(AssembleUtil.formatTime(session.getStartTime()) + "-" + AssembleUtil.formatTime(session.getEndTime()));
                            return ph;
                        });
            }
        }
        // 固定周一(1)到周五(5)，没有课次的天 periods 为空列表
        List<WeekHeaderVO> result = new ArrayList<>();
        for (int weekDay = 1; weekDay <= 5; weekDay++) {
            WeekHeaderVO weekHeader = new WeekHeaderVO();
            weekHeader.setWeekDay(weekDay);
            weekHeader.setWeekDayName(AssembleUtil.getWeekDayName(weekDay));
            LinkedHashMap<Integer, PeriodHeaderVO> periodMap = weekPeriodMap.get(weekDay);
            List<PeriodHeaderVO> periods;
            if (periodMap == null || periodMap.isEmpty()) {
                periods = Collections.emptyList();
            } else {
                periods = periodMap.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey(Comparator.nullsFirst(Integer::compareTo)))
                        .map(Map.Entry::getValue)
                        .collect(Collectors.toList());
            }
            weekHeader.setPeriods(periods);
            weekHeader.setPeriodCount(periods.size());
            result.add(weekHeader);
        }
        return result;
    }

    // 定时更新服务状态
    public void taskUpdateServiceMd() {
        Date now = new Date();

        // 规则1：报名中 → 进行中（当前时间已过报名开始时间且已过报名结束时间，说明报名阶段结束，进入进行中）
        try {
            LambdaQueryWrapper<AsServiceEntity> enrollWrapper = new LambdaQueryWrapper<>();
            enrollWrapper.eq(AsServiceEntity::getServiceStatus, CoreConstant.SERVICE_STATUS_ENROLLING)
                    .eq(AsServiceEntity::getIsValid, 1)
                    .and(w -> w.le(AsServiceEntity::getEnrollEndTime, now)
                            .or().le(AsServiceEntity::getCourseStartDate, now));
            List<AsServiceEntity> enrollingList = baseMapper.selectList(enrollWrapper);
            if (enrollingList != null && !enrollingList.isEmpty()) {
                List<Long> ids = enrollingList.stream()
                        .map(AsServiceEntity::getId)
                        .collect(Collectors.toList());
                LambdaUpdateWrapper<AsServiceEntity> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.in(AsServiceEntity::getId, ids)
                        .set(AsServiceEntity::getServiceStatus, CoreConstant.SERVICE_STATUS_RUNNING)
                        .set(AsServiceEntity::getUpdateTime, now);
                int updated = baseMapper.update(null, updateWrapper);
                log.info("【课后服务状态定时任务】报名中→进行中，更新{}条，IDs: {}", updated, ids);
            }
        } catch (Exception e) {
            log.error("【课后服务状态定时任务】报名中→进行中 处理异常", e);
        }

        // 规则2：进行中 → 已结束（当前时间超过结课日期）
        try {
            LambdaQueryWrapper<AsServiceEntity> runningWrapper = new LambdaQueryWrapper<>();
            runningWrapper.eq(AsServiceEntity::getServiceStatus, CoreConstant.SERVICE_STATUS_RUNNING)
                    .eq(AsServiceEntity::getIsValid, 1)
                    .lt(AsServiceEntity::getCourseEndDate, now);
            List<AsServiceEntity> runningList = baseMapper.selectList(runningWrapper);
            if (runningList != null && !runningList.isEmpty()) {
                List<Long> ids = runningList.stream()
                        .map(AsServiceEntity::getId)
                        .collect(Collectors.toList());
                LambdaUpdateWrapper<AsServiceEntity> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.in(AsServiceEntity::getId, ids)
                        .set(AsServiceEntity::getServiceStatus, CoreConstant.SERVICE_STATUS_FINISHED)
                        .set(AsServiceEntity::getUpdateTime, now);
                int updated = baseMapper.update(null, updateWrapper);
                log.info("【课后服务状态定时任务】进行中→已结束，更新{}条，IDs: {}", updated, ids);
            }
        } catch (Exception e) {
            log.error("【课后服务状态定时任务】进行中→已结束 处理异常", e);
        }

        log.info("【课后服务状态定时任务】执行完毕");
    }




}
