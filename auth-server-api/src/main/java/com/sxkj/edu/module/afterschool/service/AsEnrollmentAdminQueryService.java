package com.sxkj.edu.module.afterschool.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.enums.FileTypeEnum;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.utils.CheckParamUtil;
import com.sxkj.edu.common.utils.ExcelUtils;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.config.AssembleUtil;
import com.sxkj.edu.config.handle.business.*;
import com.sxkj.edu.domain.sys.entity.mult.TEduclClass;
import com.sxkj.edu.module.afterschool.domain.dto.*;
import com.sxkj.edu.module.afterschool.domain.entity.*;
import com.sxkj.edu.module.afterschool.domain.excel.AsEnrollmentExportExcel;
import com.sxkj.edu.module.afterschool.domain.vo.*;
import com.sxkj.edu.module.afterschool.enums.CourseCapacityPolicy;
import com.sxkj.edu.module.afterschool.mapper.*;
import com.sxkj.edu.module.afterschool.support.*;
import com.sxkj.edu.service.BaseFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AsEnrollmentAdminQueryService {

    @Resource
    private AsEnrollmentBuildHelper buildHelper;
    @Resource
    private AsEnrollmentTimeSlotService timeSlotService;
    @Resource
    private AsServiceMapper asServiceMapper;
    @Resource
    private AsCourseClassMapper asCourseClassMapper;
    @Resource
    private AsStudentMapper asStudentMapper;
    @Resource
    private SessionEnrollmentSupport sessionEnrollmentSupport;
    @Resource
    private CourseClassScopeSupport courseClassScopeSupport;
    @Resource
    private StudentTermScopeSupport studentTermScopeSupport;
    @Resource
    private AsEnrollmentPageQueryService enrollmentPageQueryService;
    @Resource
    private AsStudentDestinationMapper asStudentDestinationMapper;
    @Resource
    private AsEnrollmentLogMapper asEnrollmentLogMapper;
    @Resource
    private ExamRoomService examRoomService;
    @Resource
    private EduclGradeMapper educlGradeMapper;
    @Resource
    private EduclClassMapper educlClassMapper;
    @Resource
    private BaseFileService baseFileService;
    @Resource
    private AsEnrollmentBaseBuildHelper baseBuildHelper;
    @Resource
    private AsEnrollmentMapper asEnrollmentMapper;



    /**
     * 查询管理端“已选课组班”列表。
     * <p>指定课程班时走精确名单查询，确保课程班过滤生效并兼容行政班动态名单；
     * 未指定课程班时通过 MyBatis XML 多表 JOIN 下推过滤条件并完成数据库分页。</p>
     *
     * <p>只返回已绑定课程班且状态为已报名或已分配的当前有效选课记录。</p>
     */
    public PageResultDTO<AsEnrollmentSelectionRowVO> pageSelected(AsEnrollmentSelectionPageQueryAO query) {
        AsEnrollmentSelectionPageQueryAO safeQuery = query == null ? new AsEnrollmentSelectionPageQueryAO() : query;
        buildHelper.normalizeSelectionQueryTerm(safeQuery);

        if (safeQuery.getCourseClassId() != null) {
            LambdaQueryWrapper<AsEnrollmentEntity> wrapper = buildHelper.selectionStatusWrapper(safeQuery, true);
            wrapper.orderByDesc(AsEnrollmentEntity::getId);
            return buildHelper.pageSelection(safeQuery, wrapper, true);
        }

        // 1. 解析学期对应的课后服务ID集合
        Long schoolId = buildHelper.currentSchoolId(safeQuery.getSchoolId());
        List<Long> serviceIds = listServiceIdsForTerm(safeQuery.getTermId(), schoolId, safeQuery.getServiceId());
        if (CollectionUtils.isEmpty(serviceIds)) {
            return buildHelper.emptyPageResult(safeQuery.getPageIndex(), safeQuery.getPageSize());
        }

        // 2. 在同一数据库连接内查询当前页和 total，避免重复执行一遍同口径聚合。
        int safePageIndex = safeQuery.getPageIndex() <= 0 ? 1 : safeQuery.getPageIndex();
        int safePageSize = safeQuery.getPageSize() <= 0 ? 20 : safeQuery.getPageSize();
        PageResultDTO<AsEnrollmentSelectedPageData> dataPage = enrollmentPageQueryService.pageSelectedData(
                schoolId, serviceIds, safeQuery.getGradeId(), safeQuery.getClassId(),
                safeQuery.getKeyword(), safeQuery.getEnrollType(), safePageIndex, safePageSize);
        long total = dataPage.getTotal() == null ? 0L : dataPage.getTotal();
        if (total == 0) {
            return buildHelper.emptyPageResult(safePageIndex, safePageSize);
        }

        // 3. 当前页扁平化数据已经随分页查询返回，Java 中最多保留 pageSize 条。
        List<AsEnrollmentSelectedPageData> dataList = dataPage.getList();
        if (CollectionUtils.isEmpty(dataList)) {
            return buildHelper.emptyPageResult(safePageIndex, safePageSize);
        }

        // 4. 批量查询家长绑定关系（仅家长操作记录需要），转换为 VO
        List<AsEnrollmentEntity> guardianLookupEntities = buildGuardianLookupEntities(dataList);
        Map<String, AsGuardianStudentEntity> guardianRelationMap = buildHelper.mapGuardianRelations(guardianLookupEntities);
        List<AsEnrollmentSelectionRowVO> rows = new ArrayList<>(dataList.size());
        for (AsEnrollmentSelectedPageData data : dataList) {
            rows.add(buildSelectedPageRow(data, guardianRelationMap));
        }

        // 5. 行政班动态名单：不限容量的行政班课程需合并班级内全部学生
        rows = buildHelper.mergeApprovedClassAssignRosterRows(safeQuery, rows, true);

        // 6. 行政班合并后可能超出单页条数，重新手动分页
        rows.sort(Comparator.comparing(AsEnrollmentSelectionRowVO::getStudentNo, Comparator.nullsLast(Comparator.naturalOrder())));
        PageResultDTO<AsEnrollmentSelectionRowVO> result = new PageResultDTO<>();
        result.setTotal(total);
        result.setPages((long) Math.ceil(total * 1.0 / safePageSize));
        result.setPageIndex((long) safePageIndex);
        result.setPageSize((long) safePageSize);
        result.setList(rows);
        return result;
    }

    /**
     * 查询管理端“未选课组班”列表。
     * 数据来源为初始化后的未分配记录，便于后续管理端或教师端分配课程班。
     */
    public PageResultDTO<AsEnrollmentSelectionRowVO> pageUnselected(AsEnrollmentSelectionPageQueryAO query) {
        AsEnrollmentSelectionPageQueryAO safeQuery = query == null ? new AsEnrollmentSelectionPageQueryAO() : query;
        buildHelper.normalizeSelectionQueryTerm(safeQuery);
        LambdaQueryWrapper<AsEnrollmentEntity> wrapper = buildHelper.selectionStatusWrapper(safeQuery, false);
        wrapper.orderByDesc(AsEnrollmentEntity::getId);
        List<AsEnrollmentSelectionRowVO> rows = buildHelper.listSelectionRows(safeQuery, wrapper, false, false);
        rows.sort(Comparator.comparing(AsEnrollmentSelectionRowVO::getStudentNo, Comparator.nullsLast(Comparator.naturalOrder())));
        return buildHelper.toManualPageResult(safeQuery.getPageIndex(), safeQuery.getPageSize(), rows);
    }

    /**
     * 查询管理端“未选课组班”列表，仅展示已经进入分配窗口的课后服务。
     */
    public PageResultDTO<AsEnrollmentSelectionRowVO> pageAdminUnselected(AsEnrollmentSelectionPageQueryAO query) {
        AsEnrollmentSelectionPageQueryAO safeQuery = query == null ? new AsEnrollmentSelectionPageQueryAO() : query;
        buildHelper.normalizeSelectionQueryTerm(safeQuery);
        ensureAdminUnselectedPlaceholders(safeQuery);
        // 课程班和报名方式需要按课程适用范围过滤，暂时保留原查询链路，避免改变已有筛选语义。
        if (safeQuery.getCourseClassId() != null || safeQuery.getEnrollType() != null) {
            return pageAdminUnselectedLegacy(safeQuery);
        }
        try {
            return pageAdminUnselectedOptimized(safeQuery);
        } catch (RuntimeException ex) {
            // 优化查询异常时保留原查询作为兼容兜底，不阻断已有管理端业务。
            log.error("管理端未选课优化查询失败，回退原查询，schoolId={}, termId={}",
                    safeQuery.getSchoolId(), safeQuery.getTermId(), ex);
            return pageAdminUnselectedLegacy(safeQuery);
        }
    }

    /**
     * 管理端选课 Tab 数量统计（SQL 层分离已选/未选计数，减少数据库请求与数据传输量）。
     * <p>已选计数：复用 {@link AsEnrollmentMapper#countSelectedPageData}，
     * 在 SQL 层完成去重 + 学期匹配 + 可见性校验 + 条件过滤，零 Java 后处理。</p>
     * <p>未选计数：常用筛选在数据库中完成候选学生和剩余时段判断；
     * 课程班、报名方式等复杂筛选继续复用原有 Java 后处理口径。</p>
     */
    public AsEnrollmentSelectionStatusCountVO countAdminSelectionStatusesV2(AsEnrollmentSelectionCountQueryAO query) {

        AsEnrollmentSelectionPageQueryAO safeQuery = new AsEnrollmentSelectionPageQueryAO();
        SmartBeanUtil.copyProperties(query, safeQuery);
        buildHelper.normalizeSelectionQueryTerm(safeQuery);
        ensureAdminUnselectedPlaceholders(safeQuery);

        // 1. 解析学期对应的课后服务ID集合
        Long schoolId = buildHelper.currentSchoolId(safeQuery.getSchoolId());
        List<Long> serviceIds = listServiceIdsForTerm(safeQuery.getTermId(), schoolId, safeQuery.getServiceId());
        if (CollectionUtils.isEmpty(serviceIds)) {
            return new AsEnrollmentSelectionStatusCountVO();
        }

        // 2. 已选计数：复用分页计数 SQL，在数据库层完成去重 + 学期匹配 + 可见性校验 + 条件过滤
        long selectedCount = asEnrollmentMapper.countSelectedPageData(
                schoolId, serviceIds, safeQuery.getGradeId(),
                safeQuery.getClassId(), safeQuery.getKeyword(), safeQuery.getEnrollType());

        // 3. 未选计数：常用筛选直接复用数据库候选学生统计，复杂课程筛选继续沿用原逻辑。
        long unselectedCount;
        if (safeQuery.getCourseClassId() == null && safeQuery.getEnrollType() == null) {
            try {
                AdminUnselectedQueryContext context = buildAdminUnselectedQueryContext(safeQuery);
                String keyword = StringUtils.hasText(safeQuery.getKeyword()) ? safeQuery.getKeyword().trim() : null;
                if (context.serviceIds.isEmpty()) {
                    unselectedCount = 0L;
                } else {
                    List<Long> candidateStudentIds = asEnrollmentMapper.listAdminUnselectedStudentCandidateIds(
                            context.schoolId, safeQuery.getTermId(), context.serviceIds,
                            safeQuery.getGradeId(), safeQuery.getClassId(), keyword, context.assignableSlots);
                    unselectedCount = candidateStudentIds == null ? 0L : candidateStudentIds.size();
                }
            } catch (RuntimeException ex) {
                log.error("管理端未选课优化计数失败，回退原计数，schoolId={}, termId={}",
                        schoolId, safeQuery.getTermId(), ex);
                unselectedCount = countAdminUnselectedLegacy(schoolId, serviceIds, safeQuery);
            }
        } else {
            unselectedCount = countAdminUnselectedLegacy(schoolId, serviceIds, safeQuery);
        }

        AsEnrollmentSelectionStatusCountVO vo = new AsEnrollmentSelectionStatusCountVO();
        vo.setSelectedCount(selectedCount);
        vo.setUnselectedCount(unselectedCount);
        vo.setAllCount(selectedCount + unselectedCount);
        return vo;
    }

    /**
     * 查询选课详情。
     * 已选和未选记录共用该详情，课程班为空时只返回学生和服务信息。
     */
    public AsEnrollmentSelectionDetailVO getSelectionDetail(Long enrollmentId) {
        AsEnrollmentEntity enrollment = asEnrollmentMapper.selectById(enrollmentId);
        if (!buildHelper.isValid(enrollment)) {
            return null;
        }
        Map<Long, AsStudentEntity> studentMap = buildHelper.mapStudents(Collections.singletonList(enrollment.getStudentId()));
        Map<Long, AsServiceEntity> serviceMap = buildHelper.mapServices(Collections.singletonList(enrollment.getServiceId()));
        Map<Long, EduclTermEntity> termMap = buildHelper.mapTerms(serviceMap.values().stream()
                .map(AsServiceEntity::getTermId).collect(Collectors.toList()));
        Map<Long, AsCourseClassEntity> courseMap = enrollment.getCourseClassId() == null
                ? Collections.emptyMap() : buildHelper.mapCourseClasses(Collections.singletonList(enrollment.getCourseClassId()));
        Map<Long, AsCourseCategoryEntity> categoryMap = courseMap.isEmpty()
                ? Collections.emptyMap() : buildHelper.mapCategories(courseMap.values().stream().map(AsCourseClassEntity::getCategoryId).collect(Collectors.toList()));
        Map<Long, EduclGradeEntity> gradeMap = buildHelper.mapGrades(studentMap.values().stream().map(AsStudentEntity::getGradeId).collect(Collectors.toList()));
        Map<Long, TEduclClass> classMap = buildHelper.mapClasses(studentMap.values().stream().map(AsStudentEntity::getClassId).collect(Collectors.toList()));
        Map<Long, AsTeacherEntity> teacherMap = courseMap.isEmpty()
                ? Collections.emptyMap() : buildHelper.mapTeachers(courseMap.values().stream().map(AsCourseClassEntity::getTeacherId).collect(Collectors.toList()));

        AsStudentEntity student = studentMap.get(enrollment.getStudentId());
        AsServiceEntity service = serviceMap.get(enrollment.getServiceId());
        AsCourseClassEntity course = courseMap.get(enrollment.getCourseClassId());

        AsEnrollmentSelectionDetailVO vo = new AsEnrollmentSelectionDetailVO();
        vo.setEnrollmentId(enrollment.getId());

        AsEnrollmentSelectionDetailVO.StudentInfo studentInfo = new AsEnrollmentSelectionDetailVO.StudentInfo();
        if (student != null) {
            studentInfo.setStudentId(student.getId());
            studentInfo.setStudentNo(student.getStudentNo());
            studentInfo.setStudentName(student.getStudentName());
            studentInfo.setGradeId(student.getGradeId());
            studentInfo.setClassId(student.getClassId());
            EduclGradeEntity grade = gradeMap.get(student.getGradeId());
            TEduclClass clazz = classMap.get(student.getClassId());
            studentInfo.setGradeName(grade == null ? null : grade.getGradeName());
            studentInfo.setClassName(clazz == null ? null : clazz.getClassName());
        }
        vo.setStudent(studentInfo);

        AsEnrollmentSelectionDetailVO.CourseInfo courseInfo = new AsEnrollmentSelectionDetailVO.CourseInfo();
        if (service != null) {
            courseInfo.setServiceId(service.getId());
            courseInfo.setServiceName(service.getServiceName());
            courseInfo.setTermId(service.getTermId());
            EduclTermEntity term = termMap.get(service.getTermId());
            courseInfo.setTermName(term == null ? null : term.getTermName());
        }
        if (course != null) {
            courseInfo.setCourseClassId(course.getId());
            courseInfo.setCourseClassName(course.getCourseClassName());
            courseInfo.setCategoryId(course.getCategoryId());
            AsCourseCategoryEntity category = categoryMap.get(course.getCategoryId());
            courseInfo.setCategoryName(category == null ? null : category.getCategoryName());
            courseInfo.setEnrollType(course.getEnrollType());
            courseInfo.setEnrollTypeName(AssembleUtil.getEnrollTypeName(course.getEnrollType()));
            courseInfo.setTeacherId(course.getTeacherId());
            AsTeacherEntity teacher = teacherMap.get(course.getTeacherId());
            courseInfo.setTeacherName(teacher == null ? null : teacher.getTeacherName());
            courseInfo.setPlaceName(examRoomService.resolvePlaceName(course.getPlaceId()));
            courseInfo.setCapacity(course.getCapacity());
            courseInfo.setEnrolledCount(buildHelper.countActualEnrolledStudents(course.getId()));
        }
        AsStudentDestinationEntity destination = getLatestNonCourseDestination(enrollment);
        if (course != null) {
            courseInfo.setDestinationType(CoreConstant.DESTINATION_TYPE_COURSE_CLASS);
            courseInfo.setDestinationName(destinationTypeName(CoreConstant.DESTINATION_TYPE_COURSE_CLASS));
        } else if (destination != null) {
            courseInfo.setDestinationType(destination.getDestinationType());
            courseInfo.setDestinationName(destination.getDestinationName());
        }
        vo.setCourse(courseInfo);

        AsEnrollmentSelectionDetailVO.OperationInfo operationInfo = new AsEnrollmentSelectionDetailVO.OperationInfo();
        operationInfo.setOperatorType(enrollment.getOperatorType());
        operationInfo.setOperatorUserId(enrollment.getOperatorUserId());
        operationInfo.setOperatorName(buildHelper.displayOperatorName(enrollment, student,
                buildHelper.mapGuardianRelations(Collections.singletonList(enrollment))));
        operationInfo.setOperateTime(enrollment.getEnrollTime() == null ? enrollment.getUpdateTime() : enrollment.getEnrollTime());
        operationInfo.setRemark(enrollment.getRemark());
        vo.setOperation(operationInfo);
        vo.setLogs(listEnrollmentLogs(enrollment));
        return vo;
    }

    /**
     * 查询管理端可分配课程候选。
     * 返回服务下有效课程班，并逐条计算容量、性别、班级适用范围等不可选原因。
     */
    public JsonResult<AsEnrollmentAssignOptionsVO> listAssignOptions(AsEnrollmentAssignOptionsQueryAO query) {
        if (query == null || query.getStudentId() == null) {
            return JsonResult.buildError("学生ID不能为空");
        }
        AsStudentEntity student = asStudentMapper.selectById(query.getStudentId());
        if (!buildHelper.isValid(student)) {
            return JsonResult.buildError("学生不存在");
        }
        if (query.getServiceId() == null) {
            return buildHelper.listCurrentTermAssignOptions(query, student, buildHelper.currentSchoolId(student.getSchoolId()));
        }
        AsServiceEntity service = asServiceMapper.selectById(query.getServiceId());
        if (!buildHelper.isValid(service)) {
            return JsonResult.buildError("服务不存在");
        }
        if (!buildHelper.isServiceAssignableWindow(service)) {
            return JsonResult.buildError("课后服务不在分配时间内");
        }
        String scopeError = buildHelper.validateStudentServiceScope(service, student);
        if (scopeError != null) {
            return JsonResult.buildError(scopeError);
        }
        LambdaQueryWrapper<AsCourseClassEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsCourseClassEntity::getIsValid, 1)
                .eq(AsCourseClassEntity::getServiceId, query.getServiceId())
                .in(AsCourseClassEntity::getClassStatus, Arrays.asList(1, 2));
        if (query.getCategoryId() != null) {
            wrapper.eq(AsCourseClassEntity::getCategoryId, query.getCategoryId());
        }
        List<AsCourseClassEntity> courses = asCourseClassMapper.selectList(wrapper);
        buildHelper.applyActualEnrolledCounts(courses);
        Map<Long, AsCourseCategoryEntity> categoryMap = buildHelper.mapCategories(courses.stream().map(AsCourseClassEntity::getCategoryId).collect(Collectors.toList()));
        Map<Long, AsTeacherEntity> teacherMap = buildHelper.mapTeachers(courses.stream().map(AsCourseClassEntity::getTeacherId).collect(Collectors.toList()));
        Map<Long, List<AsGuardianScheduleItemVO>> scheduleMap = buildHelper.mapGuardianSchedules(courses.stream().map(AsCourseClassEntity::getId).collect(Collectors.toList()));
        Map<Long, Boolean> customDateMap = buildHelper.isCustomDateCourses(courses.stream().map(AsCourseClassEntity::getId).collect(Collectors.toList()));
        Map<Long, String> scheduleTextMap = timeSlotService.mapCourseScheduleTimeDesc(courses.stream().map(AsCourseClassEntity::getId).collect(Collectors.toList()));

        AsEnrollmentAssignOptionsVO vo = new AsEnrollmentAssignOptionsVO();
        buildHelper.fillAssignOptionsTerm(vo, service);
        Set<Long> categoryIds = new LinkedHashSet<>();
        for (AsCourseClassEntity course : courses) {
            if (course.getCategoryId() != null && categoryIds.add(course.getCategoryId())) {
                AsEnrollmentAssignOptionsVO.CategoryOption option = new AsEnrollmentAssignOptionsVO.CategoryOption();
                option.setCategoryId(course.getCategoryId());
                option.setDestinationType(CoreConstant.DESTINATION_TYPE_COURSE_CLASS);
                AsCourseCategoryEntity category = categoryMap.get(course.getCategoryId());
                option.setCategoryName(category == null ? null : category.getCategoryName());
                vo.getCategories().add(option);
            }
            AsAssignableCourseClassVO item = buildHelper.toAssignableCourse(course, student, categoryMap, teacherMap,
                    scheduleMap.get(course.getId()), customDateMap, scheduleTextMap);
            item.setServiceName(service.getServiceName());
            buildHelper.fillAssignableCourseTerm(item, service);
            vo.getCourseClasses().add(item);
        }
        buildHelper.populateNonCourseDestinationOptions(vo);
        return JsonResult.buildSuccess(vo);
    }


    /**
     * 查询学生选课页面筛选项。
     * 聚合课后服务、课程班、报名方式、年级和班级，减少前端初始化请求数量。
     */
    public AsEnrollmentFilterOptionsVO filterOptions(Long schoolId, Long serviceId,Long termId) {
        schoolId = buildHelper.currentSchoolId(schoolId);
        if(!CheckParamUtil.checkLongIdParam(termId)){
            EduclTermEntity termEntity = studentTermScopeSupport.resolveReadableTerm(schoolId);
            if(null == termEntity) return null;
            termId = termEntity.getId();
        }
        AsEnrollmentFilterOptionsVO vo = new AsEnrollmentFilterOptionsVO();
        LambdaQueryWrapper<AsServiceEntity> serviceWrapper = new LambdaQueryWrapper<>();
        serviceWrapper.eq(AsServiceEntity::getTermId, termId);
        serviceWrapper.eq(AsServiceEntity::getIsValid, 1);
        if (schoolId != null && schoolId > 0) {
            serviceWrapper.eq(AsServiceEntity::getSchoolId, schoolId);
        }
        serviceWrapper.orderByDesc(AsServiceEntity::getCreateTime);
        List<AsServiceEntity> services = asServiceMapper.selectList(serviceWrapper);
        for (AsServiceEntity service : services) {
            AsEnrollmentFilterOptionsVO.Option option = buildHelper.option(service.getId(), null, service.getServiceName());
            vo.getServices().add(option);
        }
        LambdaQueryWrapper<AsCourseClassEntity> courseWrapper = new LambdaQueryWrapper<>();
        courseWrapper.eq(AsCourseClassEntity::getIsValid, 1);
        courseWrapper.eq(AsCourseClassEntity::getSchoolId, schoolId);
        if (serviceId != null) {
            courseWrapper.eq(AsCourseClassEntity::getServiceId, serviceId);
        }
        List<AsCourseClassEntity> courses = asCourseClassMapper.selectList(courseWrapper);
        for (AsCourseClassEntity course : courses) {
            vo.getCourseClasses().add(buildHelper.option(course.getId(), null, course.getCourseClassName()));
        }
        vo.getEnrollTypes().add(buildHelper.option(null, CoreConstant.ENROLL_TYPE_PUBLIC, "公开选课"));
        LambdaQueryWrapper<EduclGradeEntity> gradeWrapper = new LambdaQueryWrapper<>();
        gradeWrapper.eq(EduclGradeEntity::getIsValid, 1);
        gradeWrapper.eq(EduclGradeEntity::getSchoolId, schoolId);
        List<EduclGradeEntity> grades = educlGradeMapper.selectList(gradeWrapper);
        for (EduclGradeEntity grade : grades) {
            vo.getGrades().add(buildHelper.option(grade.getId(), null, grade.getGradeName()));
        }
        LambdaQueryWrapper<TEduclClass> classWrapper = new LambdaQueryWrapper<>();
        classWrapper.eq(TEduclClass::getIsValid, 1);
        classWrapper.eq(TEduclClass::getSchoolId, schoolId);
        classWrapper.eq(TEduclClass::getTermId, termId);
        List<TEduclClass> classes = educlClassMapper.selectList(classWrapper);
        for (TEduclClass clazz : classes) {
            vo.getClasses().add(buildHelper.option(clazz.getId(), null, clazz.getClassName()));
        }
        return vo;
    }


    /**
     * 初始化某个课后服务下的学生选课状态。
     * 根据服务下课程班适用年级/班级范围生成未分配记录，方法幂等，已存在的学生不会重复生成。
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<AsEnrollmentInitResultVO> initSelection(Long serviceId) {
        AsServiceEntity service = asServiceMapper.selectById(serviceId);
        if (!buildHelper.isValid(service)) {
            return JsonResult.buildError("课后服务不存在");
        }
        if (!buildHelper.isServiceAssignable(service)) {
            return JsonResult.buildError("课后服务未开始报名或已结束，不能初始化选课数据");
        }
        AsEnrollmentInitResultVO result = initUnassignedEnrollments(service);
        AsServiceEntity update = new AsServiceEntity();
        update.setId(service.getId());
        update.setSelectionInitStatus(2);
        update.setSelectionInitTime(result.getInitTime());
        asServiceMapper.updateById(update);
        return JsonResult.buildSuccess(result);
    }

    /**
     * 发布课后服务时初始化未分配选课记录，供 AsServiceService 调用。
     */
    public AsEnrollmentInitResultVO initUnassignedEnrollmentsForPublish(AsServiceEntity service) {
        return initUnassignedEnrollments(service);
    }


    @Transactional(rollbackFor = Exception.class)
    public JsonResult<AsEnrollmentOperateResultVO> changeCourse(AsEnrollmentChangeAO ao) {
        AsEnrollmentEntity enrollment = buildHelper.resolveRepresentativeEnrollment(ao.getEnrollmentId());
        return baseBuildHelper.changeCourseInternal(ao, enrollment, CoreConstant.OPERATOR_ADMIN);
    }


    /**
     * 管理端取消选课。
     * 将当前记录回退为未分配，释放原课程班容量，并保留取消原因。
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> cancelEnrollment(Long enrollmentId, String cancelReason) {
        AsEnrollmentEntity enrollment = buildHelper.resolveRepresentativeEnrollment(enrollmentId);
        if (!buildHelper.isValid(enrollment)) {
            return JsonResult.buildError("选课记录不存在");
        }
        if (enrollment.getCourseClassId() == null) {
            return JsonResult.buildError("仅允许驳回已报名课程");
        }
        AsServiceEntity service = asServiceMapper.selectById(enrollment.getServiceId());
        if (!buildHelper.isServiceInEnrollmentPeriod(service)) {
            return JsonResult.buildError("仅允许在报名期间驳回报名");
        }
        Long beforeClassId = enrollment.getCourseClassId();
        Integer beforeStatus = enrollment.getEnrollStatus();
        if (beforeClassId != null) {
            buildHelper.decreaseCourseClassCount(beforeClassId, enrollment.getStudentId());
        }
        String rejectRemark = buildHelper.buildAdminRejectRemark(cancelReason);
        AsEnrollmentEntity template = buildHelper.buildEnrollmentTemplate(enrollment, null, enrollment.getEnrollSource(),
                CoreConstant.ENROLL_STATUS_UNASSIGNED, CoreConstant.OPERATOR_ADMIN, buildHelper.currentUserId(), buildHelper.currentUserName("管理员"), rejectRemark);
        template.setCancelReason(rejectRemark);
        template.setCancelTime(new Date());
        sessionEnrollmentSupport.invalidateCourseSessionEnrollments(enrollment.getSchoolId(), enrollment.getServiceId(),
                enrollment.getStudentId(), beforeClassId, buildHelper.currentUserId());
        AsEnrollmentEntity placeholder = null;
        if (buildHelper.getCurrentEnrollment(enrollment.getSchoolId(), enrollment.getServiceId(), enrollment.getStudentId()) == null) {
            placeholder = sessionEnrollmentSupport.createUnassignedPlaceholder(template, buildHelper.currentUserId());
        }
        baseBuildHelper.saveLog(placeholder == null ? enrollment : placeholder, beforeClassId, null, beforeStatus, CoreConstant.ENROLL_STATUS_UNASSIGNED, CoreConstant.ENROLL_ACTION_CANCEL,
                CoreConstant.OPERATOR_ADMIN, buildHelper.currentUserId(), buildHelper.currentUserName("管理员"), rejectRemark);
        return JsonResult.buildSuccess("驳回成功");
    }

    /**
     * 导出学生已选课列表/学生未选列表。
     * 复用列表查询条件生成 Excel，并通过文件服务上传后返回文件信息。
     */
    public void exportEnrollments(AsEnrollmentExportAO query, HttpServletResponse response) {
        AsEnrollmentSelectionPageQueryAO pageQuery = new AsEnrollmentSelectionPageQueryAO();
        BeanUtils.copyProperties(query, pageQuery);
        PageResultDTO<AsEnrollmentSelectionRowVO> page = "unselected".equals(query.getTabType())
                ? pageAdminUnselectedForExport(pageQuery) : pageSelected(pageQuery);
        List<AsEnrollmentExportExcel> exports = page.getList().stream()
                .map(row -> buildHelper.toExportExcel(row, "unselected".equals(query.getTabType())))
                .collect(Collectors.toList());
        log.info("call exportEnrollments, total: {}", exports.size());

        String fileName = "unselected".equals(query.getTabType()) ? "学生选课-未选课组班.xlsx" : "学生选课-已选课组班.xlsx";
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ExcelUtils.exportExcel(response, exports, AsEnrollmentExportExcel.class, "学生选课记录", "学生选课记录");

            byte[] data = outputStream.toByteArray();
            baseFileService.uploadFile(new ByteArrayInputStream(data), fileName, (long) data.length,
                    FileTypeEnum.DOCUMENT_TYPE, "AFTERSCHOOL_ENROLLMENT_EXPORT");
        } catch (Exception e) {
            log.error("export student enrollment excel failed", e);
        }
    }

    /**
     * 未选课导出继续沿用原全量口径，避免分页性能改造改变导出范围。
     */
    private PageResultDTO<AsEnrollmentSelectionRowVO> pageAdminUnselectedForExport(
            AsEnrollmentSelectionPageQueryAO query) {
        buildHelper.normalizeSelectionQueryTerm(query);
        ensureAdminUnselectedPlaceholders(query);
        return pageAdminUnselectedLegacy(query);
    }




    private String destinationTypeName(Integer destinationType) {
        if (Objects.equals(destinationType, CoreConstant.DESTINATION_TYPE_GO_HOME)) return "回家";
        if (Objects.equals(destinationType, CoreConstant.DESTINATION_TYPE_TRAINING_INSTITUTION)) return "校外机构";
        if (Objects.equals(destinationType, CoreConstant.DESTINATION_TYPE_OTHER)) return "其他";
        return "课程班";
    }

    private List<Long> listServiceIdsForTerm(Long termId, Long schoolId, Long serviceId) {
        if (termId == null || termId <= 0) return Collections.emptyList();
        LambdaQueryWrapper<AsServiceEntity> serviceWrapper = new LambdaQueryWrapper<>();
        serviceWrapper.eq(AsServiceEntity::getTermId, termId)
                .eq(com.sxkj.edu.common.utils.Args.isNotNull(serviceId), AsServiceEntity::getId, serviceId)
                .eq(schoolId != null && schoolId > 0, AsServiceEntity::getSchoolId, schoolId);
        return asServiceMapper.selectList(serviceWrapper).stream()
                .map(AsServiceEntity::getId).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private List<AsEnrollmentEntity> buildGuardianLookupEntities(List<AsEnrollmentSelectedPageData> dataList) {
        if (CollectionUtils.isEmpty(dataList)) return Collections.emptyList();
        List<AsEnrollmentEntity> entities = new ArrayList<>();
        for (AsEnrollmentSelectedPageData data : dataList) {
            if (Objects.equals(data.getOperatorType(), CoreConstant.OPERATOR_GUARDIAN)) {
                AsEnrollmentEntity entity = new AsEnrollmentEntity();
                entity.setStudentId(data.getStudentId());
                entity.setOperatorType(data.getOperatorType());
                entity.setOperatorUserId(data.getOperatorUserId());
                entities.add(entity);
            }
        }
        return entities;
    }

    private AsEnrollmentSelectionRowVO buildSelectedPageRow(AsEnrollmentSelectedPageData data,
                                                            Map<String, AsGuardianStudentEntity> guardianRelationMap) {
        AsEnrollmentSelectionRowVO row = new AsEnrollmentSelectionRowVO();
        row.setEnrollmentId(data.getEnrollmentId());
        row.setSchoolId(data.getSchoolId());
        row.setServiceId(data.getServiceId());
        row.setServiceName(data.getServiceName());
        row.setTermId(data.getServiceTermId());
        row.setTermName(data.getTermName());
        row.setCourseClassId(data.getCourseClassId());
        row.setCourseClassName(data.getCourseClassName());
        row.setCategoryId(data.getCategoryId());
        row.setCategoryName(data.getCategoryName());
        row.setEnrollType(data.getEnrollType());
        row.setEnrollTypeName(AssembleUtil.getEnrollTypeName(data.getEnrollType()));
        row.setEnrollSource(data.getEnrollSource());
        row.setEnrollSourceName(AssembleUtil.getEnrollSourceName(data.getEnrollSource()));
        row.setEnrollStatus(data.getEnrollStatus());
        row.setEnrollStatusName(AssembleUtil.getEnrollStatusName(data.getEnrollStatus()));
        row.setStudentId(data.getStudentId());
        row.setStudentNo(data.getStudentNo());
        row.setStudentName(data.getStudentName());
        row.setGender(data.getGender());
        row.setGradeId(data.getGradeId());
        row.setGradeName(data.getGradeName());
        row.setClassId(data.getClassId());
        row.setClassName(data.getClassName());
        row.setEnrollTime(data.getEnrollTime());
        row.setLearnedSessions(data.getLearnedSessions() == null ? 0 : data.getLearnedSessions());
        row.setCanDetail(true);
        row.setCanAssign(false);
        row.setCanEdit(true);
        if (Objects.equals(data.getOperatorType(), CoreConstant.OPERATOR_GUARDIAN) && guardianRelationMap != null) {
            String key = data.getStudentId() + ":" + data.getOperatorUserId();
            AsGuardianStudentEntity relation = guardianRelationMap.get(key);
            if (data.getStudentName() != null && relation != null && StringUtils.hasText(relation.getRelationName())) {
                row.setOperatorName(data.getStudentName() + relation.getRelationName());
            } else {
                row.setOperatorName(data.getOperatorName());
            }
        } else {
            row.setOperatorName(data.getOperatorName());
        }
        return row;
    }

    private AsStudentDestinationEntity getLatestNonCourseDestination(AsEnrollmentEntity enrollment) {
        if (enrollment == null || enrollment.getSchoolId() == null || enrollment.getStudentId() == null) return null;
        LambdaQueryWrapper<AsStudentDestinationEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsStudentDestinationEntity::getIsValid, 1)
                .eq(AsStudentDestinationEntity::getSchoolId, enrollment.getSchoolId())
                .eq(AsStudentDestinationEntity::getStudentId, enrollment.getStudentId())
                .ne(AsStudentDestinationEntity::getDestinationType, CoreConstant.DESTINATION_TYPE_COURSE_CLASS)
                .orderByDesc(AsStudentDestinationEntity::getId)
                .last("limit 1");
        return asStudentDestinationMapper.selectOne(wrapper);
    }

    private List<AsEnrollmentSelectionDetailVO.LogInfo> listEnrollmentLogs(AsEnrollmentEntity enrollment) {
        if (enrollment == null || enrollment.getId() == null) return Collections.emptyList();
        LambdaQueryWrapper<AsEnrollmentLogEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsEnrollmentLogEntity::getEnrollmentId, enrollment.getId())
                .eq(AsEnrollmentLogEntity::getIsValid, 1)
                .orderByDesc(AsEnrollmentLogEntity::getId);
        List<AsEnrollmentLogEntity> logs = asEnrollmentLogMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(logs)) return Collections.emptyList();
        return logs.stream().map(this::toSelectionLogInfo).collect(Collectors.toList());
    }

    private AsEnrollmentSelectionDetailVO.LogInfo toSelectionLogInfo(AsEnrollmentLogEntity entity) {
        AsEnrollmentSelectionDetailVO.LogInfo info = new AsEnrollmentSelectionDetailVO.LogInfo();
        info.setActionType(entity.getActionType());
        info.setOperatorType(entity.getOperatorType());
        info.setOperatorName(entity.getOperatorName());
        info.setRemark(entity.getRemark());
        info.setOperateTime(entity.getCreateTime());
        return info;
    }


    private AsEnrollmentInitResultVO initUnassignedEnrollments(AsServiceEntity service) {
        Long schoolId = service.getSchoolId();
        List<AsStudentEntity> students = buildHelper.listStudentsInServiceScope(service);
        int created = 0;
        int exists = 0;
        Date initTime = new Date();
        Long operatorUserId = buildHelper.currentUserId();
        for (AsStudentEntity student : students) {
            AsEnrollmentEntity current = sessionEnrollmentSupport.findRepresentativeEnrollment(schoolId, service.getId(), student.getId());
            if (current != null) { exists++; continue; }
            AsEnrollmentEntity template = new AsEnrollmentEntity();
            template.setSchoolId(schoolId);
            template.setServiceId(service.getId());
            template.setStudentId(student.getId());
            template.setStudentNo(student.getStudentNo());
            template.setOperatorType(CoreConstant.OPERATOR_SYSTEM);
            template.setOperatorUserId(operatorUserId);
            template.setOperatorName("系统");
            AsEnrollmentEntity entity = sessionEnrollmentSupport.createUnassignedPlaceholder(template, operatorUserId);
            if (entity != null) {
                baseBuildHelper.saveLog(entity, null, null, null, CoreConstant.ENROLL_STATUS_UNASSIGNED, CoreConstant.ENROLL_ACTION_INIT,
                        CoreConstant.OPERATOR_SYSTEM, operatorUserId, "系统", "选课初始化");
                created++;
            }
        }
        AsEnrollmentInitResultVO result = new AsEnrollmentInitResultVO();
        result.setServiceId(service.getId());
        result.setCreatedCount(created);
        result.setExistsCount(exists);
        result.setInitTime(initTime);
        return result;
    }

    /** 管理端未选课优化查询所需的服务和时段上下文。 */
    private static final class AdminUnselectedQueryContext {
        private final Long schoolId;
        private final List<Long> serviceIds;
        private final List<AsAdminUnselectedSlotQueryData> assignableSlots;
        private AdminUnselectedQueryContext(Long schoolId, Map<Long, AsServiceEntity> serviceMap,
                                            List<AsAdminUnselectedSlotQueryData> assignableSlots) {
            this.schoolId = schoolId;
            this.serviceIds = serviceMap == null ? Collections.emptyList() : serviceMap.keySet().stream()
                    .filter(Objects::nonNull).sorted().collect(Collectors.toList());
            this.assignableSlots = assignableSlots == null ? Collections.emptyList() : assignableSlots;
        }
    }

    private PageResultDTO<AsEnrollmentSelectionRowVO> pageAdminUnselectedLegacy(AsEnrollmentSelectionPageQueryAO query) {
        LambdaQueryWrapper<AsEnrollmentEntity> wrapper = buildHelper.selectionStatusWrapper(query, false);
        wrapper.orderByDesc(AsEnrollmentEntity::getId);
        return buildHelper.pageSelection(query, wrapper, false, true);
    }

    /**
     * 管理端未选课组班专用分页：数据库先确定轻量候选学生，再只为当前页复用原有行组装逻辑。
     */
    private PageResultDTO<AsEnrollmentSelectionRowVO> pageAdminUnselectedOptimized(
            AsEnrollmentSelectionPageQueryAO query) {
        AdminUnselectedQueryContext context = buildAdminUnselectedQueryContext(query);
        int pageIndex = query.getPageIndex() <= 0 ? 1 : query.getPageIndex();
        int pageSize = query.getPageSize() <= 0 ? 20 : query.getPageSize();
        if (context.serviceIds.isEmpty()) {
            return buildHelper.emptyPageResult(pageIndex, pageSize);
        }

        String keyword = StringUtils.hasText(query.getKeyword()) ? query.getKeyword().trim() : null;
        PageResultDTO<Long> candidatePage = enrollmentPageQueryService.pageAdminUnselectedStudentIds(
                context.schoolId, query.getTermId(), context.serviceIds,
                query.getGradeId(), query.getClassId(), keyword, context.assignableSlots,
                pageIndex, pageSize);
        long total = candidatePage.getTotal() == null ? 0L : candidatePage.getTotal();
        if (total <= 0) {
            return buildHelper.emptyPageResult(pageIndex, pageSize);
        }

        List<Long> pageStudentIds = candidatePage.getList();
        if (CollectionUtils.isEmpty(pageStudentIds)) {
            return buildHelper.buildManualPageResult(pageIndex, pageSize, total, Collections.emptyList());
        }

        LambdaQueryWrapper<AsEnrollmentEntity> wrapper = buildHelper.selectionStatusWrapper(query, false);
        wrapper.in(AsEnrollmentEntity::getStudentId, pageStudentIds)
                .orderByDesc(AsEnrollmentEntity::getId);
        List<AsEnrollmentSelectionRowVO> rows = buildHelper.listSelectionRows(query, wrapper, false, true);
        Map<Long, AsEnrollmentSelectionRowVO> rowMap = rows.stream()
                .filter(Objects::nonNull)
                .filter(row -> row.getStudentId() != null)
                .collect(Collectors.toMap(AsEnrollmentSelectionRowVO::getStudentId, Function.identity(), (a, b) -> a));
        List<AsEnrollmentSelectionRowVO> orderedRows = pageStudentIds.stream()
                .map(rowMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (orderedRows.size() != pageStudentIds.size()) {
            log.warn("管理端未选课优化分页结果不一致，回退原查询，schoolId={}, termId={}, expected={}, actual={}",
                    context.schoolId, query.getTermId(), pageStudentIds.size(), orderedRows.size());
            return pageAdminUnselectedLegacy(query);
        }
        orderedRows.sort(Comparator.comparing(AsEnrollmentSelectionRowVO::getStudentNo, Comparator.nullsLast(Comparator.naturalOrder())));
        return buildHelper.buildManualPageResult(pageIndex, pageSize, total, orderedRows);
    }

    /**
     * 构建管理端未选课数据库分页上下文，只包含当前学校、学期内已经进入分配窗口的服务。
     */
    private AdminUnselectedQueryContext buildAdminUnselectedQueryContext(
            AsEnrollmentSelectionPageQueryAO query) {
        Long schoolId = buildHelper.currentSchoolId(query == null ? null : query.getSchoolId());
        if (schoolId == null || schoolId <= 0 || query == null || query.getTermId() == null) {
            return new AdminUnselectedQueryContext(schoolId, Collections.emptyMap(), Collections.emptyList());
        }
        LambdaQueryWrapper<AsServiceEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsServiceEntity::getIsValid, 1)
                .eq(AsServiceEntity::getSchoolId, schoolId)
                .eq(AsServiceEntity::getTermId, query.getTermId())
                .eq(query.getServiceId() != null, AsServiceEntity::getId, query.getServiceId());
        List<AsServiceEntity> services = asServiceMapper.selectList(wrapper).stream()
                .filter(service -> buildHelper.isServiceAssignableWindow(service))
                .collect(Collectors.toList());
        if (services.isEmpty()) {
            return new AdminUnselectedQueryContext(schoolId, Collections.emptyMap(), Collections.emptyList());
        }
        Map<Long, AsServiceEntity> serviceMap = services.stream()
                .filter(service -> service.getId() != null)
                .collect(Collectors.toMap(AsServiceEntity::getId, Function.identity(), (a, b) -> a));
        Map<Long, List<EnrollmentTimeSlot>> slotMap = timeSlotService.mapAssignableConcreteServiceTimeSlots(
                serviceMap.keySet(), serviceMap);
        List<AsAdminUnselectedSlotQueryData> assignableSlots = toAdminUnselectedSlotQueryData(slotMap);
        return new AdminUnselectedQueryContext(schoolId, serviceMap, assignableSlots);
    }

    /**
     * 将可分配课次压缩为服务+具体时段，供数据库判断学生是否仍有空闲时间。
     */
    private List<AsAdminUnselectedSlotQueryData> toAdminUnselectedSlotQueryData(
            Map<Long, List<EnrollmentTimeSlot>> slotMap) {
        if (CollectionUtils.isEmpty(slotMap)) {
            return Collections.emptyList();
        }
        LinkedHashMap<String, AsAdminUnselectedSlotQueryData> result = new LinkedHashMap<>();
        for (Map.Entry<Long, List<EnrollmentTimeSlot>> entry : slotMap.entrySet()) {
            Long serviceId = entry.getKey();
            if (serviceId == null || CollectionUtils.isEmpty(entry.getValue())) {
                continue;
            }
            for (EnrollmentTimeSlot slot : entry.getValue()) {
                if (slot == null || slot.getStartMinute() == null || slot.getEndMinute() == null) {
                    continue;
                }
                String gradeScope = toAdminUnselectedGradeScope(slot.getGradeIds());
                String slotKey = serviceId + ":" + timeSlotService.buildConcreteSessionTimeKey(slot) + ":" + gradeScope;
                if (result.containsKey(slotKey)) {
                    continue;
                }
                AsAdminUnselectedSlotQueryData data = new AsAdminUnselectedSlotQueryData();
                data.setServiceId(serviceId);
                data.setSessionDate(slot.getSessionDate());
                data.setWeekDay(slot.getWeekDay());
                data.setStartMinute(slot.getStartMinute());
                data.setEndMinute(slot.getEndMinute());
                data.setGradeScope(gradeScope);
                result.put(slotKey, data);
            }
        }
        return new ArrayList<>(result.values());
    }

    /**
     * 使用逗号边界保存适用年级，确保数据库查询不会把“1”和“11”误判为同一年级。
     */
    private String toAdminUnselectedGradeScope(Collection<Long> gradeIds) {
        if (CollectionUtils.isEmpty(gradeIds)) {
            return null;
        }
        String values = gradeIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        return StringUtils.hasText(values) ? "," + values + "," : null;
    }

    private long countAdminUnselectedLegacy(Long schoolId, List<Long> serviceIds, AsEnrollmentSelectionPageQueryAO query) {
        List<AsEnrollmentSelectionCountData> unselectedData = asEnrollmentMapper.listUnselectedCountData(
                schoolId, serviceIds, query.getGradeId(), query.getClassId(), query.getKeyword());
        return countUnselectedFromData(unselectedData, query);
    }

    private long countUnselectedFromData(List<AsEnrollmentSelectionCountData> unselectedData,
                                         AsEnrollmentSelectionPageQueryAO query) {
        if (CollectionUtils.isEmpty(unselectedData)) return 0L;
        List<AsEnrollmentEntity> unselectedEnrollments = convertUnselectedCountDataToEnrollments(unselectedData);
        List<AsEnrollmentEntity> deduped = sessionEnrollmentSupport.listDistinctRepresentativeEnrollments(unselectedEnrollments);
        Map<Long, AsServiceEntity> serviceMap = buildHelper.mapServices(deduped.stream()
                .map(AsEnrollmentEntity::getServiceId).collect(Collectors.toList()));
        List<AsEnrollmentSelectionRowVO> rows = buildHelper.buildUnselectedRows(deduped, true, Collections.emptyMap());
        rows = buildHelper.mergeApprovedClassAssignRosterRows(query, rows, false);
        rows = rows.stream().filter(row -> matchSelectionQuery(row, query, false)).collect(Collectors.toList());
        return rows.size();
    }

    private List<AsEnrollmentEntity> convertUnselectedCountDataToEnrollments(List<AsEnrollmentSelectionCountData> dataList) {
        if (CollectionUtils.isEmpty(dataList)) return Collections.emptyList();
        List<AsEnrollmentEntity> entities = new ArrayList<>(dataList.size());
        for (AsEnrollmentSelectionCountData data : dataList) {
            AsEnrollmentEntity entity = new AsEnrollmentEntity();
            entity.setId(data.getEnrollmentId());
            entity.setSchoolId(data.getSchoolId());
            entity.setServiceId(data.getServiceId());
            entity.setStudentId(data.getStudentId());
            entity.setStudentNo(data.getStudentNo());
            entity.setEnrollStatus(data.getEnrollStatus());
            entity.setInviteId(data.getInviteId());
            entity.setEnrollTime(data.getEnrollTime());
            entities.add(entity);
        }
        return entities;
    }

    private boolean matchSelectionQuery(AsEnrollmentSelectionRowVO row, AsEnrollmentSelectionPageQueryAO query, boolean selected) {
        if (selected && query.getEnrollType() != null && !Objects.equals(row.getEnrollType(), query.getEnrollType())) {
            return false;
        }
        if (query.getGradeId() != null && !Objects.equals(row.getGradeId(), query.getGradeId())) {
            return false;
        }
        if (query.getClassId() != null && !Objects.equals(row.getClassId(), query.getClassId())) {
            return false;
        }
        if (StringUtils.hasText(query.getKeyword())) {
            String keyword = query.getKeyword().trim();
            boolean noMatchStudentNo = row.getStudentNo() == null || !row.getStudentNo().contains(keyword);
            boolean noMatchStudentName = row.getStudentName() == null || !row.getStudentName().contains(keyword);
            return !(noMatchStudentNo && noMatchStudentName);
        }
        return true;
    }

    private void ensureAdminUnselectedPlaceholders(AsEnrollmentSelectionPageQueryAO query) {
        Long schoolId = buildHelper.currentSchoolId(query == null ? null : query.getSchoolId());
        if (schoolId == null || schoolId <= 0) {
            return;
        }
        List<AsServiceEntity> services = listAdminUnselectedPlaceholderServices(query, schoolId);
        if (CollectionUtils.isEmpty(services)) {
            return;
        }
        Map<Long, List<Long>> serviceClassIds = mapAdminPlaceholderServiceClassIds(schoolId, services);
        Long operatorUserId = buildHelper.currentUserId();
        for (AsServiceEntity service : services) {
            List<Long> classIds = serviceClassIds.get(service.getId());
            if (CollectionUtils.isEmpty(classIds)) {
                continue;
            }
            while (true) {
                String keyword = query != null && StringUtils.hasText(query.getKeyword())
                        ? query.getKeyword().trim() : null;
                List<AsStudentEntity> students = asEnrollmentMapper.listMissingPlaceholderStudents(
                        service.getSchoolId(), service.getTermId(), service.getId(), classIds,
                        query == null ? null : query.getGradeId(),
                        query == null ? null : query.getClassId(),
                        keyword, CoreConstant.PLACEHOLDER_COMPENSATION_BATCH_SIZE);
                if (CollectionUtils.isEmpty(students)) {
                    break;
                }
                for (AsStudentEntity student : students) {
                    if (student == null || student.getId() == null) {
                        continue;
                    }
                    AsEnrollmentEntity template = new AsEnrollmentEntity();
                    template.setSchoolId(service.getSchoolId());
                    template.setServiceId(service.getId());
                    template.setStudentId(student.getId());
                    template.setStudentNo(student.getStudentNo());
                    template.setOperatorType(CoreConstant.OPERATOR_SYSTEM);
                    template.setOperatorUserId(operatorUserId);
                    template.setOperatorName("系统");
                    sessionEnrollmentSupport.createUnassignedPlaceholder(template, operatorUserId);
                }
                if (students.size() < CoreConstant.PLACEHOLDER_COMPENSATION_BATCH_SIZE) {
                    break;
                }
            }
        }
    }

    private List<AsServiceEntity> listAdminUnselectedPlaceholderServices(AsEnrollmentSelectionPageQueryAO query, Long schoolId) {
        Long serviceId = query == null ? null : query.getServiceId();
        if (serviceId == null && query != null && query.getCourseClassId() != null) {
            AsCourseClassEntity course = asCourseClassMapper.selectById(query.getCourseClassId());
            serviceId = course == null ? null : course.getServiceId();
            if (serviceId == null) {
                return Collections.emptyList();
            }
        }
        LambdaQueryWrapper<AsServiceEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsServiceEntity::getIsValid, 1)
                .eq(AsServiceEntity::getSchoolId, schoolId)
                .eq(AsServiceEntity::getSelectionInitStatus, CoreConstant.SELECTION_INIT_STATUS_DONE)
                .eq(serviceId != null, AsServiceEntity::getId, serviceId)
                .eq(query != null && query.getTermId() != null && query.getTermId() > 0,
                        AsServiceEntity::getTermId, query == null ? null : query.getTermId());
        List<AsServiceEntity> services = asServiceMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(services)) {
            return Collections.emptyList();
        }
        return services.stream()
                .filter(s -> buildHelper.isServiceAssignableWindow(s))
                .collect(Collectors.toList());
    }

    private Map<Long, List<Long>> mapAdminPlaceholderServiceClassIds(
            Long schoolId, List<AsServiceEntity> services) {
        List<Long> serviceIds = services.stream()
                .map(AsServiceEntity::getId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (serviceIds.isEmpty()) {
            return Collections.emptyMap();
        }
        QueryWrapper<AsCourseClassEntity> courseWrapper = new QueryWrapper<>();
        courseWrapper.select("service_id", "enroll_type", "applicable_class_ids", "applicable_grade_ids")
                .eq("is_valid", 1)
                .in("service_id", serviceIds);
        List<AsCourseClassEntity> courses = asCourseClassMapper.selectList(courseWrapper);
        if (CollectionUtils.isEmpty(courses)) {
            return Collections.emptyMap();
        }
        QueryWrapper<TEduclClass> classWrapper = new QueryWrapper<>();
        classWrapper.select("id", "grade_id")
                .eq("is_valid", 1)
                .eq("school_id", schoolId);
        List<TEduclClass> schoolClasses = educlClassMapper.selectList(classWrapper);
        Map<Long, List<Long>> gradeClassIds = CollectionUtils.isEmpty(schoolClasses)
                ? Collections.emptyMap()
                : schoolClasses.stream()
                .filter(clazz -> clazz.getId() != null && clazz.getGradeId() != null)
                .collect(Collectors.groupingBy(
                        TEduclClass::getGradeId,
                        Collectors.mapping(TEduclClass::getId, Collectors.toList())));
        Map<Long, LinkedHashSet<Long>> scopeMap = new HashMap<>();
        for (AsCourseClassEntity course : courses) {
            if (course == null || course.getServiceId() == null) {
                continue;
            }
            LinkedHashSet<Long> classIds = scopeMap.computeIfAbsent(
                    course.getServiceId(), key -> new LinkedHashSet<>());
            List<Long> explicitClassIds = courseClassScopeSupport.parseLongList(course.getApplicableClassIds());
            classIds.addAll(explicitClassIds);
            if (Objects.equals(course.getEnrollType(), CoreConstant.ENROLL_TYPE_CLASS_ASSIGN)
                    && !explicitClassIds.isEmpty()) {
                continue;
            }
            for (Long gradeId : courseClassScopeSupport.parseLongList(course.getApplicableGradeIds())) {
                classIds.addAll(gradeClassIds.getOrDefault(gradeId, Collections.emptyList()));
            }
        }
        Map<Long, List<Long>> result = new HashMap<>();
        scopeMap.forEach((serviceId, classIds) -> result.put(serviceId, new ArrayList<>(classIds)));
        return result;
    }
}