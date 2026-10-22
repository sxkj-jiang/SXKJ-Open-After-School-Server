package com.sxkj.edu.module.afterschool.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.config.AssembleUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.config.handle.business.EnrollmentTimeSlot;
import com.sxkj.edu.config.handle.business.HeadteacherEnrollmentBuildContext;
import com.sxkj.edu.config.handle.business.HeadteacherEnrollmentScopeContext;
import com.sxkj.edu.domain.sys.entity.mult.TEduclClass;
import com.sxkj.edu.module.afterschool.domain.dto.*;
import com.sxkj.edu.module.afterschool.domain.entity.*;
import com.sxkj.edu.module.afterschool.domain.vo.*;
import com.sxkj.edu.module.afterschool.mapper.*;
import com.sxkj.edu.module.afterschool.support.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sxkj.edu.module.afterschool.domain.vo.AsAssignableCourseClassVO;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * 班主任端选课服务：班级学生报名列表、状态统计、分配候选、分配课程、调课。
 *
 * @author system
 */
@Service
@Slf4j
public class AsEnrollmentHeadteacherService {

    @Resource
    private AsCourseClassMapper asCourseClassMapper;

    @Resource
    private AsEnrollmentMapper asEnrollmentMapper;
    @Resource
    private AsServiceMapper asServiceMapper;
    @Resource
    private AsCourseQuotaService asCourseQuotaService;
    @Resource
    private AsStudentMapper asStudentMapper;

    @Resource
    private AsStudentDestinationMapper asStudentDestinationMapper;
    @Resource
    private AsTeacherClassMapper asTeacherClassMapper;
    @Resource
    private AsTeacherMapper asTeacherMapper;

    @Resource
    private SessionEnrollmentSupport sessionEnrollmentSupport;
    @Resource
    private AsCourseSessionMapper asCourseSessionMapper;
    @Resource
    private AsEnrollmentBuildHelper buildHelper;
    @Resource
    private AsEnrollmentBaseBuildHelper baseBuildHelper;
    @Resource
    private AsEnrollmentTimeSlotService timeSlotService;
    @Autowired
    private AsTeacherService asTeacherService;

    public JsonResult<AsEnrollmentAssignOptionsVO> listHeadteacherAssignOptions(AsEnrollmentAssignOptionsQueryAO query) {
        if (query == null || query.getStudentId() == null) {
            return JsonResult.buildError("学生ID不能为空");
        }
        AsStudentEntity student = asStudentMapper.selectById(query.getStudentId());
        AsTeacherEntity teacher = currentHeadteacherProfile();
        Long schoolId = buildHelper.currentSchoolId(buildHelper.isValid(teacher) ? teacher.getSchoolId() : null);
        if (!buildHelper.isValid(student) || !buildHelper.isValid(teacher)
                || (schoolId != null && !Objects.equals(student.getSchoolId(), schoolId))
                || !listHeadteacherClassIds(teacher.getId(), schoolId).contains(student.getClassId())) {
            return JsonResult.buildError("学生不在当前班主任管理班级内");
        }
        if (query.getServiceId() == null) {
            return buildHelper.listCurrentTermAssignOptions(query, student, schoolId, true);
        }
        return listHeadteacherAssignOptionsForService(query);
    }

    /** 班主任指定服务时查询可分配课程候选，只返回公开课程（enroll_type=1）。 */
    private JsonResult<AsEnrollmentAssignOptionsVO> listHeadteacherAssignOptionsForService(
            AsEnrollmentAssignOptionsQueryAO query) {
        AsServiceEntity service = asServiceMapper.selectById(query.getServiceId());
        if (!buildHelper.isValid(service)) {
            return JsonResult.buildError("服务不存在");
        }
        if (!buildHelper.isServiceAssignableWindow(service)) {
            return JsonResult.buildError("课后服务不在分配时间内");
        }
        AsStudentEntity student = asStudentMapper.selectById(query.getStudentId());
        if (!buildHelper.isValid(student)) {
            return JsonResult.buildError("学生不存在");
        }
        String scopeError = buildHelper.validateStudentServiceScope(service, student);
        if (scopeError != null) {
            return JsonResult.buildError(scopeError);
        }
        LambdaQueryWrapper<AsCourseClassEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsCourseClassEntity::getIsValid, 1)
                .eq(AsCourseClassEntity::getServiceId, query.getServiceId())
                .eq(AsCourseClassEntity::getEnrollType, CoreConstant.ENROLL_TYPE_PUBLIC)
                .in(AsCourseClassEntity::getClassStatus, Arrays.asList(1, 2));
        if (query.getCategoryId() != null) {
            wrapper.eq(AsCourseClassEntity::getCategoryId, query.getCategoryId());
        }
        List<AsCourseClassEntity> courses = asCourseClassMapper.selectList(wrapper);
        buildHelper.applyActualEnrolledCounts(courses);
        Map<Long, AsCourseCategoryEntity> categoryMap = buildHelper.mapCategories(courses.stream()
                .map(AsCourseClassEntity::getCategoryId).collect(Collectors.toList()));
        Map<Long, AsTeacherEntity> teacherMap = buildHelper.mapTeachers(courses.stream()
                .map(AsCourseClassEntity::getTeacherId).collect(Collectors.toList()));
        Map<Long, List<AsGuardianScheduleItemVO>> scheduleMap = buildHelper.mapGuardianSchedules(courses.stream()
                .map(AsCourseClassEntity::getId).collect(Collectors.toList()));
        Map<Long, Boolean> customDateMap = buildHelper.isCustomDateCourses(courses.stream()
                .map(AsCourseClassEntity::getId).collect(Collectors.toList()));
        Map<Long, String> scheduleTextMap = timeSlotService.mapCourseScheduleTimeDesc(courses.stream()
                .map(AsCourseClassEntity::getId).collect(Collectors.toList()),
                timeSlotService.mapCourseSessions(courses.stream()
                        .map(AsCourseClassEntity::getId).collect(Collectors.toList())));

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


    private List<AsHeadteacherEnrollmentStudentRowVO> buildHeadteacherEnrollmentRecordRows(List<AsEnrollmentEntity> enrollments,
                                                                                           List<AsStudentEntity> students,
                                                                                           Map<Long, AsServiceEntity> serviceMap,
                                                                                           String tabType) {
        return buildHeadteacherEnrollmentRecordRows(enrollments, students, serviceMap, tabType, null);
    }

    private List<AsHeadteacherEnrollmentStudentRowVO> buildHeadteacherEnrollmentRecordRows(List<AsEnrollmentEntity> enrollments,
                                                                                           List<AsStudentEntity> students,
                                                                                           Map<Long, AsServiceEntity> serviceMap,
                                                                                           String tabType,
                                                                                           Map<String, List<EnrollmentTimeSlot>> remainingSlotMap) {
        return buildHeadteacherEnrollmentRecordRows(enrollments, students, serviceMap, tabType, remainingSlotMap,
                buildHeadteacherEnrollmentContext(enrollments, students, serviceMap));
    }

    private List<AsHeadteacherEnrollmentStudentRowVO> buildHeadteacherEnrollmentRecordRows(List<AsEnrollmentEntity> enrollments,
                                                                                           List<AsStudentEntity> students,
                                                                                           Map<Long, AsServiceEntity> serviceMap,
                                                                                           String tabType,
                                                                                           Map<String, List<EnrollmentTimeSlot>> remainingSlotMap,
                                                                                           HeadteacherEnrollmentBuildContext buildContext) {
        if (CollectionUtil.isEmpty(enrollments)) {
            return Collections.emptyList();
        }
        List<AsHeadteacherEnrollmentStudentRowVO> rows = new ArrayList<>();
        for (AsEnrollmentEntity enrollment : enrollments) {
            AsServiceEntity service = serviceMap.get(enrollment.getServiceId());
            AsStudentEntity student = buildContext.studentMap.get(enrollment.getStudentId());
            if (service == null || student == null) {
                continue;
            }
            boolean expiredPendingInvite = isExpiredPendingInviteEnrollment(enrollment, service);
            List<EnrollmentTimeSlot> precomputedRemainingSlots = resolvePrecomputedUnregisteredTimeSlots(enrollment, remainingSlotMap);
            boolean displayAsUnassigned = expiredPendingInvite
                    || (CoreConstant.TAB_UNASSIGNED.equals(tabType) && (precomputedRemainingSlots == null
                    ? timeSlotService.hasUnregisteredTime(enrollment, service)
                    : !CollectionUtil.isEmpty(precomputedRemainingSlots)));
            AsEnrollmentEntity displayEnrollment = displayAsUnassigned
                    ? toHeadteacherUnassignedDisplayEnrollment(enrollment)
                    : enrollment;
            AsCourseClassEntity course = buildContext.courseMap.get(displayEnrollment.getCourseClassId());
            AsCourseCategoryEntity category = course == null ? null : buildContext.categoryMap.get(course.getCategoryId());
            AsHeadteacherEnrollmentStudentRowVO row = buildHeadteacherEnrollmentRow(
                    displayEnrollment, student, service, buildContext.termMap.get(service.getTermId()),
                    buildContext.gradeMap, buildContext.classMap);
            row.setCourseInfo(buildHeadteacherEnrollmentCourse(displayEnrollment, service, course, category,
                    buildContext.courseStartedMap, buildContext.scheduleTimeDescMap,
                    !CoreConstant.TAB_REGISTERED.equals(tabType), precomputedRemainingSlots,
                    buildContext.nonCourseDestinationMap, buildContext.nonCourseDestinationSlotMap));
            refreshHeadteacherEnrollmentRowStatus(row, service);
            if (CoreConstant.TAB_REGISTERED.equals(tabType)) {
                applyHeadteacherRegisteredRowStatus(row);
            } else if (CoreConstant.TAB_ASSIGNED.equals(tabType)) {
                applyHeadteacherAssignedRowStatus(row);
            }
            rows.add(row);
        }
        return CoreConstant.TAB_UNASSIGNED.equals(tabType) ? mergeHeadteacherUnassignedRowsByStudentAndTerm(rows) : rows;
    }

    /** 未传入预计算时段时返回 null，保持旧调用方原有查询行为。 */
    private List<EnrollmentTimeSlot> resolvePrecomputedUnregisteredTimeSlots(AsEnrollmentEntity enrollment,
                                                                             Map<String, List<EnrollmentTimeSlot>> remainingSlotMap) {
        if (enrollment == null || remainingSlotMap == null) {
            return null;
        }
        return remainingSlotMap.getOrDefault(buildHelper.serviceStudentKey(enrollment), Collections.emptyList());
    }

    /** 一次性构建班主任列表全部批量映射，避免逐行查询数据库。 */
    private HeadteacherEnrollmentBuildContext buildHeadteacherEnrollmentContext(List<AsEnrollmentEntity> enrollments,
                                                                                List<AsStudentEntity> students,
                                                                                Map<Long, AsServiceEntity> serviceMap) {
        HeadteacherEnrollmentBuildContext context = new HeadteacherEnrollmentBuildContext();
        context.studentMap = students == null ? Collections.emptyMap() : students.stream()
                .filter(student -> student != null && student.getId() != null)
                .collect(Collectors.toMap(AsStudentEntity::getId, Function.identity(), (a, b) -> a));
        List<Long> courseClassIds = enrollments == null ? Collections.emptyList() : enrollments.stream()
                .map(AsEnrollmentEntity::getCourseClassId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        context.courseMap = buildHelper.mapCourseClasses(courseClassIds);
        Map<Long, List<AsCourseSessionEntity>> courseSessionMap = timeSlotService.mapCourseSessions(courseClassIds);
        context.courseStartedMap = timeSlotService.mapCourseStarted(courseClassIds, courseSessionMap);
        context.scheduleTimeDescMap = timeSlotService.mapCourseScheduleTimeDesc(courseClassIds, courseSessionMap);
        context.categoryMap = buildHelper.mapCategories(context.courseMap.values().stream()
                .map(AsCourseClassEntity::getCategoryId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        context.gradeMap = buildHelper.mapGrades(context.studentMap.values().stream()
                .map(AsStudentEntity::getGradeId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        context.classMap = buildHelper.mapClasses(context.studentMap.values().stream()
                .map(AsStudentEntity::getClassId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        context.termMap = buildHelper.mapTerms(serviceMap == null ? Collections.emptyList() : serviceMap.values().stream()
                .map(AsServiceEntity::getTermId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        context.nonCourseDestinationMap = buildHelper.mapLatestNonCourseDestinations(enrollments);
        context.nonCourseDestinationSlotMap = timeSlotService.mapNonCourseDestinationTimeSlots(
                enrollments, context.nonCourseDestinationMap);
        return context;
    }

    /** 已报名 Tab 是展示分类；行政班安排课程在 courseInfo 中保留原始状态。 */
    private void applyHeadteacherRegisteredRowStatus(AsHeadteacherEnrollmentStudentRowVO row) {
        if (row == null) {
            return;
        }
        row.setTabType(CoreConstant.TAB_REGISTERED);
        row.setStatusName(AssembleUtil.headteacherStatusName(CoreConstant.TAB_REGISTERED));
        row.setCourseCount(1);
        row.setUnassignedCount(0);
    }

    /** 已分配 Tab 只表达当前已分配去向，剩余未分配时间由未分配 Tab 独立展示。 */
    private void applyHeadteacherAssignedRowStatus(AsHeadteacherEnrollmentStudentRowVO row) {
        if (row == null) {
            return;
        }
        row.setTabType(CoreConstant.TAB_ASSIGNED);
        row.setStatusName(AssembleUtil.headteacherStatusName(CoreConstant.TAB_ASSIGNED));
        row.setCourseCount(1);
        row.setUnassignedCount(0);
        AsHeadteacherEnrollmentStudentRowVO.CourseInfo courseInfo = row.getCourseInfo();
        if (courseInfo != null && courseInfo.getCourseClassId() != null) {
            courseInfo.setUnregisteredScheduleItems(Collections.emptyList());
            courseInfo.setUnregisteredScheduleText(null);
        }
    }

    private List<AsHeadteacherEnrollmentStudentRowVO> mergeHeadteacherUnassignedRowsByStudentAndTerm(
            List<AsHeadteacherEnrollmentStudentRowVO> rows) {
        if (CollectionUtil.isEmpty(rows)) {
            return Collections.emptyList();
        }
        LinkedHashMap<String, AsHeadteacherEnrollmentStudentRowVO> rowMap = new LinkedHashMap<>();
        for (AsHeadteacherEnrollmentStudentRowVO row : rows) {
            if (row == null || row.getStudentId() == null) {
                continue;
            }
            String key = row.getStudentId() + ":" + (row.getTermId() == null ? "0" : String.valueOf(row.getTermId()));
            AsHeadteacherEnrollmentStudentRowVO existing = rowMap.get(key);
            if (existing == null) {
                rowMap.put(key, row);
                continue;
            }
            mergeHeadteacherUnassignedRow(existing, row);
        }
        return new ArrayList<>(rowMap.values());
    }

    /** 合并同一学生未分配卡片的剩余时间、去向和可分配状态。 */
    private void mergeHeadteacherUnassignedRow(AsHeadteacherEnrollmentStudentRowVO target,
                                               AsHeadteacherEnrollmentStudentRowVO source) {
        if (target == null || source == null) {
            return;
        }
        AsHeadteacherEnrollmentStudentRowVO.CourseInfo targetCourse = target.getCourseInfo();
        AsHeadteacherEnrollmentStudentRowVO.CourseInfo sourceCourse = source.getCourseInfo();
        if (targetCourse == null || sourceCourse == null) {
            return;
        }
        if (CollectionUtil.isEmpty(targetCourse.getUnregisteredScheduleItems())
                && !CollectionUtil.isEmpty(sourceCourse.getUnregisteredScheduleItems())) {
            mergeHeadteacherDestination(sourceCourse, targetCourse);
            target.setTermId(source.getTermId());
            target.setTermName(source.getTermName());
            target.setCourseInfo(sourceCourse);
            targetCourse = sourceCourse;
        } else {
            targetCourse.setCanAssign(Boolean.TRUE.equals(targetCourse.getCanAssign())
                    || Boolean.TRUE.equals(sourceCourse.getCanAssign()));
            targetCourse.setUnregisteredScheduleItems(buildHelper.mergeTextList(targetCourse.getUnregisteredScheduleItems(),
                    sourceCourse.getUnregisteredScheduleItems()));
            targetCourse.setUnregisteredScheduleText(String.join("、", targetCourse.getUnregisteredScheduleItems()));
            mergeHeadteacherDestination(targetCourse, sourceCourse);
        }
        target.setCourseCount(0);
        target.setUnassignedCount(1);
        target.setTabType(CoreConstant.TAB_UNASSIGNED);
        target.setStatusName("未分配");
    }

    /** 合并班主任端未分配卡片的非课程去向文案。 */
    private void mergeHeadteacherDestination(AsHeadteacherEnrollmentStudentRowVO.CourseInfo target,
                                             AsHeadteacherEnrollmentStudentRowVO.CourseInfo source) {
        if (target == null || source == null || !StringUtils.hasText(source.getDestinationName())) {
            return;
        }
        if (!StringUtils.hasText(target.getDestinationName())) {
            target.setDestinationType(source.getDestinationType());
            target.setDestinationName(source.getDestinationName());
            return;
        }
        if (Objects.equals(target.getDestinationName(), source.getDestinationName())) {
            return;
        }
        target.setDestinationType(null);
        target.setDestinationName(String.join("、", buildHelper.mergeTextList(
                Collections.singletonList(target.getDestinationName()),
                Collections.singletonList(source.getDestinationName()))));
    }

    private AsEnrollmentEntity toHeadteacherUnassignedDisplayEnrollment(AsEnrollmentEntity enrollment) {
        AsEnrollmentEntity displayEnrollment = new AsEnrollmentEntity();
        SmartBeanUtil.copyProperties(enrollment, displayEnrollment);
        displayEnrollment.setCourseClassId(null);
        displayEnrollment.setSessionId(null);
        displayEnrollment.setEnrollStatus(CoreConstant.ENROLL_STATUS_UNASSIGNED);
        return displayEnrollment;
    }

    private AsHeadteacherEnrollmentStudentRowVO buildHeadteacherEnrollmentRow(AsEnrollmentEntity enrollment, AsStudentEntity student,
                                                                             AsServiceEntity service, EduclTermEntity term,
                                                                             Map<Long, EduclGradeEntity> gradeMap,
                                                                             Map<Long, TEduclClass> classMap) {
        AsHeadteacherEnrollmentStudentRowVO row = new AsHeadteacherEnrollmentStudentRowVO();
        row.setSchoolId(enrollment.getSchoolId());
        row.setStudentId(student.getId());
        row.setStudentNo(student.getStudentNo());
        row.setStudentName(student.getStudentName());
        row.setGradeId(student.getGradeId());
        row.setClassId(student.getClassId());
        EduclGradeEntity grade = gradeMap.get(student.getGradeId());
        TEduclClass clazz = classMap.get(student.getClassId());
        row.setGradeName(grade == null ? null : grade.getGradeName());
        row.setClassName(clazz == null ? null : clazz.getClassName());
        row.setTermId(service.getTermId());
        row.setTermName(term == null ? null : term.getTermName());
        return row;
    }

    private AsHeadteacherEnrollmentStudentRowVO.CourseInfo buildHeadteacherEnrollmentCourse(AsEnrollmentEntity enrollment,
                                                                                           AsServiceEntity service,
                                                                                           AsCourseClassEntity course,
                                                                                           AsCourseCategoryEntity category,
                                                                                           Map<Long, Boolean> courseStartedMap,
                                                                                           Map<Long, String> scheduleTimeDescMap,
                                                                                           boolean includeUnregisteredTime,
                                                                                           List<EnrollmentTimeSlot> precomputedRemainingSlots,
                                                                                           Map<String, AsStudentDestinationEntity> nonCourseDestinationMap,
                                                                                           Map<String, List<EnrollmentTimeSlot>> nonCourseDestinationSlotMap) {
        AsHeadteacherEnrollmentStudentRowVO.CourseInfo item = new AsHeadteacherEnrollmentStudentRowVO.CourseInfo();
        item.setEnrollmentId(enrollment.getId());
        item.setServiceId(enrollment.getServiceId());
        item.setServiceName(service == null ? null : service.getServiceName());
        item.setCourseClassId(enrollment.getCourseClassId());
        item.setCourseClassName(course == null ? null : course.getCourseClassName());
        item.setScheduleTimeDesc(scheduleTimeDescMap == null ? null : scheduleTimeDescMap.get(enrollment.getCourseClassId()));
        item.setCategoryId(course == null ? null : course.getCategoryId());
        item.setCategoryName(category == null ? null : category.getCategoryName());
        item.setEnrollType(course == null ? null : course.getEnrollType());
        item.setEnrollTypeName(course == null ? null : AssembleUtil.getEnrollTypeName(course.getEnrollType()));
        item.setEnrollSource(enrollment.getEnrollSource());
        item.setEnrollSourceName(AssembleUtil.getEnrollSourceName(enrollment.getEnrollSource()));
        item.setEnrollStatus(enrollment.getEnrollStatus());
        item.setEnrollStatusName(AssembleUtil.getEnrollStatusName(enrollment.getEnrollStatus()));
        item.setCourseStarted(Boolean.TRUE.equals(courseStartedMap.get(enrollment.getCourseClassId())));
        item.setEnrollTime(enrollment.getEnrollTime());
        boolean expiredPendingInvite = isExpiredPendingInviteEnrollment(enrollment, service);
        boolean nonCourseAssigned = enrollment.getCourseClassId() == null && isHeadteacherAssignedEnrollment(enrollment);
        List<EnrollmentTimeSlot> remainingSlots = precomputedRemainingSlots != null
                ? precomputedRemainingSlots
                : includeUnregisteredTime
                ? timeSlotService.findUnregisteredTimeSlots(enrollment, service)
                : Collections.emptyList();
        List<String> slotTexts = timeSlotService.toSlotTexts(remainingSlots);
        if (enrollment.getCourseClassId() != null) {
            item.setDestinationType(CoreConstant.DESTINATION_TYPE_COURSE_CLASS);
            item.setDestinationName(AssembleUtil.getDestinationTypeName(CoreConstant.DESTINATION_TYPE_COURSE_CLASS));
        } else {
            AsStudentDestinationEntity destination = buildHelper.resolveLatestNonCourseDestination(enrollment, nonCourseDestinationMap);
            if (destination != null) {
                item.setDestinationType(destination.getDestinationType());
                item.setDestinationName(destination.getDestinationName());
                if (nonCourseAssigned) {
                    slotTexts = timeSlotService.toSlotTexts(timeSlotService.resolveNonCourseDestinationTimeSlots(
                            enrollment, destination, nonCourseDestinationSlotMap));
                    String destinationScheduleText = String.join("、", slotTexts);
                    item.setScheduleTimeDesc(destinationScheduleText);
                }
            }
        }
        item.setUnregisteredScheduleItems(slotTexts);
        item.setUnregisteredScheduleText(String.join("、", slotTexts));
        item.setCanAssign(!nonCourseAssigned && (enrollment.getCourseClassId() == null || expiredPendingInvite || !remainingSlots.isEmpty()));
        item.setCanAdjust(enrollment.getCourseClassId() != null && !expiredPendingInvite);
        return item;
    }

    /**
     * 班主任端列表口径：邀请待确认在报名结束后不再属于已报名，归入待分配。
     */
    private boolean isExpiredPendingInviteEnrollment(AsEnrollmentEntity enrollment, AsServiceEntity service) {
        if (enrollment == null || service == null || service.getEnrollEndTime() == null) {
            return false;
        }
        return Objects.equals(enrollment.getEnrollStatus(), CoreConstant.ENROLL_STATUS_PENDING)
                && Objects.equals(enrollment.getEnrollSource(), CoreConstant.ENROLL_SOURCE_INVITE)
                && !new Date().before(service.getEnrollEndTime());
    }

    /**
     * 班主任端已分配 Tab 仅包含手动分配课程和非课程去向。
     */
    private boolean isHeadteacherAssignedEnrollment(AsEnrollmentEntity enrollment) {
        return enrollment != null
                && Objects.equals(enrollment.getEnrollStatus(), CoreConstant.ENROLL_STATUS_ASSIGNED)
                && Objects.equals(enrollment.getEnrollSource(), CoreConstant.ENROLL_SOURCE_MANUAL_ASSIGN);
    }


    /** 全部 Tab 基于同一批报名记录拆分状态，避免重复查询学生、服务和报名数据。 */
    private List<AsHeadteacherEnrollmentStudentRowVO> buildHeadteacherAllStatusRows(List<AsEnrollmentEntity> enrollments,
                                                                                    List<AsStudentEntity> students,
                                                                                    Map<Long, AsServiceEntity> serviceMap,
                                                                                    String tabType,
                                                                                    HeadteacherEnrollmentBuildContext buildContext) {
        if (CollectionUtil.isEmpty(enrollments)) {
            return Collections.emptyList();
        }
        // 未分配时间按学生+服务维度判断，先压缩课次级记录，避免同一服务下重复计算剩余时间。
        List<AsEnrollmentEntity> source = CoreConstant.TAB_UNASSIGNED.equals(tabType)
                ? sessionEnrollmentSupport.listDistinctRepresentativeEnrollments(enrollments)
                : enrollments;
        List<AsEnrollmentEntity> matched = source.stream()
                .filter(enrollment -> matchesHeadteacherAllStatusTab(
                        enrollment, tabType, serviceMap.get(enrollment.getServiceId()), buildContext.remainingSlotMap))
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(matched)) {
            return Collections.emptyList();
        }
        List<AsEnrollmentEntity> representatives = CoreConstant.TAB_REGISTERED.equals(tabType) || CoreConstant.TAB_ASSIGNED.equals(tabType)
                ? buildHelper.listHeadteacherEnrollmentRepresentatives(matched, serviceMap)
                : sessionEnrollmentSupport.listDistinctRepresentativeEnrollments(matched);
        return toHeadteacherAllStatusRows(
                buildHeadteacherEnrollmentRecordRows(
                        representatives, students, serviceMap, tabType, buildContext.remainingSlotMap, buildContext),
                tabType);
    }

    /** 全部 Tab 状态拆分专用匹配逻辑，保持各状态语义不变并避免无关状态计算未分配时间。 */
    private boolean matchesHeadteacherAllStatusTab(AsEnrollmentEntity enrollment, String tabType, AsServiceEntity service,
                                                   Map<String, List<EnrollmentTimeSlot>> remainingSlotMap) {
        if (CoreConstant.TAB_REGISTERED.equals(tabType)) {
            return buildHelper.isHeadteacherRegisteredEnrollment(enrollment);
        }
        if (CoreConstant.TAB_ASSIGNED.equals(tabType)) {
            return buildHelper.isHeadteacherAssignedEnrollment(enrollment);
        }
        return buildHelper.hasPrecomputedUnregisteredTime(enrollment, remainingSlotMap) && buildHelper.isServiceAssignableWindow(service);
    }

    /** 全部 Tab 中每个状态独立展示，单条记录不再显示为混合状态。 */
    private List<AsHeadteacherEnrollmentStudentRowVO> toHeadteacherAllStatusRows(List<AsHeadteacherEnrollmentStudentRowVO> rows,
                                                                                 String tabType) {
        if (CollectionUtil.isEmpty(rows)) {
            return Collections.emptyList();
        }
        for (AsHeadteacherEnrollmentStudentRowVO row : rows) {
            row.setTabType(tabType);
            row.setStatusName(AssembleUtil.headteacherStatusName(tabType));
            row.setCourseCount(CoreConstant.TAB_UNASSIGNED.equals(tabType) ? 0 : 1);
            row.setUnassignedCount(CoreConstant.TAB_UNASSIGNED.equals(tabType) ? 1 : 0);
            AsHeadteacherEnrollmentStudentRowVO.CourseInfo courseInfo = row.getCourseInfo();
            if (courseInfo != null) {
                if (CoreConstant.TAB_UNASSIGNED.equals(tabType)
                        && !StringUtils.hasText(courseInfo.getScheduleTimeDesc())
                        && StringUtils.hasText(courseInfo.getUnregisteredScheduleText())) {
                    // 全部列表复用课程卡片展示字段，未分配卡片需要同步展示剩余课后时间。
                    courseInfo.setScheduleTimeDesc(courseInfo.getUnregisteredScheduleText());
                } else if (courseInfo.getCourseClassId() != null) {
                    courseInfo.setUnregisteredScheduleItems(Collections.emptyList());
                    courseInfo.setUnregisteredScheduleText(null);
                }
            }
        }
        return rows;
    }

    private List<AsHeadteacherEnrollmentStudentRowVO> mergeHeadteacherAllRowsByStudentTermAndStatus(
            List<AsHeadteacherEnrollmentStudentRowVO> rows) {
        if (CollectionUtil.isEmpty(rows)) {
            return Collections.emptyList();
        }
        // 第一步：按学号确定每个学生应归属的最高优先级 Tab 类型
        Map<String, Integer> studentBestTabOrder = new HashMap<>();
        for (AsHeadteacherEnrollmentStudentRowVO row : rows) {
            if (row == null || !StringUtils.hasText(row.getStudentNo()) || !StringUtils.hasText(row.getTabType())) {
                continue;
            }
            int order = headteacherTabOrder(row.getTabType());
            Integer existing = studentBestTabOrder.get(row.getStudentNo());
            if (existing == null || order < existing) {
                studentBestTabOrder.put(row.getStudentNo(), order);
            }
        }
        // 第二步：仅保留属于最高优先级 Tab 的行，同时按业务键去重
        LinkedHashMap<String, AsHeadteacherEnrollmentStudentRowVO> rowMap = new LinkedHashMap<>();
        for (AsHeadteacherEnrollmentStudentRowVO row : rows) {
            if (row == null || !StringUtils.hasText(row.getStudentNo()) || !StringUtils.hasText(row.getTabType())) {
                continue;
            }
            Integer bestOrder = studentBestTabOrder.get(row.getStudentNo());
            if (bestOrder == null || headteacherTabOrder(row.getTabType()) != bestOrder) {
                continue;
            }
            String key = headteacherAllRowKey(row);
            rowMap.putIfAbsent(key, row);
        }
        // 第三步：排序输出
        List<AsHeadteacherEnrollmentStudentRowVO> mergedRows = new ArrayList<>(rowMap.values());
        mergedRows.sort(this::compareHeadteacherAllRows);
        return mergedRows;
    }

    /** 全部 Tab 的去重键保留课程维度，避免同一学生同一状态下多门课程被压成一条。 */
    private String headteacherAllRowKey(AsHeadteacherEnrollmentStudentRowVO row) {
        return row.getStudentId() + ":"
                + (row.getTermId() == null ? "0" : row.getTermId()) + ":"
                + row.getTabType() + ":"
                + headteacherAllCourseKey(row.getCourseInfo(), row.getTabType());
    }

    private String headteacherAllCourseKey(AsHeadteacherEnrollmentStudentRowVO.CourseInfo courseInfo, String tabType) {
        if (CoreConstant.TAB_UNASSIGNED.equals(tabType)) {
            return "U";
        }
        if (courseInfo == null) {
            return "N";
        }
        if (courseInfo.getCourseClassId() != null) {
            return "C:" + courseInfo.getCourseClassId();
        }
        if (courseInfo.getServiceId() != null) {
            return "S:" + courseInfo.getServiceId() + ":D:" + courseInfo.getDestinationType();
        }
        return courseInfo.getEnrollmentId() == null ? "N" : "E:" + courseInfo.getEnrollmentId();
    }

    /** 全部 Tab 先按学生归拢，再按状态展示，保证同一学生的多条课程记录连续出现。 */
    private int compareHeadteacherAllRows(AsHeadteacherEnrollmentStudentRowVO left,
                                          AsHeadteacherEnrollmentStudentRowVO right) {
        int result = AssembleUtil.compareNullableLong(left.getTermId(), right.getTermId());
        if (result != 0) {
            return result;
        }
        result = AssembleUtil.compareNullableLong(left.getClassId(), right.getClassId());
        if (result != 0) {
            return result;
        }
        result = AssembleUtil.compareNullableString(left.getStudentNo(), right.getStudentNo());
        if (result != 0) {
            return result;
        }
        result = AssembleUtil.compareNullableLong(left.getStudentId(), right.getStudentId());
        if (result != 0) {
            return result;
        }
        return Integer.compare(headteacherTabOrder(left.getTabType()), headteacherTabOrder(right.getTabType()));
    }

    private int headteacherTabOrder(String tabType) {
        if (CoreConstant.TAB_REGISTERED.equals(tabType)) {
            return 1;
        }
        if (CoreConstant.TAB_ASSIGNED.equals(tabType)) {
            return 2;
        }
        if (CoreConstant.TAB_UNASSIGNED.equals(tabType)) {
            return 3;
        }
        return 4;
    }


    /** 根据课程卡内容刷新行的 Tab 归属和计数。 */
    private void refreshHeadteacherEnrollmentRowStatus(AsHeadteacherEnrollmentStudentRowVO row, AsServiceEntity service) {
        AsHeadteacherEnrollmentStudentRowVO.CourseInfo item = row.getCourseInfo();
        boolean hasCourse = item != null && item.getCourseClassId() != null;
        boolean nonCourseAssigned = item != null
                && item.getCourseClassId() == null
                && Objects.equals(item.getEnrollStatus(), CoreConstant.ENROLL_STATUS_ASSIGNED)
                && Objects.equals(item.getEnrollSource(), CoreConstant.ENROLL_SOURCE_MANUAL_ASSIGN)
                && item.getDestinationType() != null;
        boolean unassigned = item != null && item.getCourseClassId() == null && !nonCourseAssigned;
        boolean expiredPendingInvite = item != null
                && Objects.equals(item.getEnrollStatus(), CoreConstant.ENROLL_STATUS_PENDING)
                && Objects.equals(item.getEnrollSource(), CoreConstant.ENROLL_SOURCE_INVITE)
                && isExpiredPendingInviteService(service);
        boolean hasRegistered = hasCourse && Objects.equals(item.getEnrollStatus(), CoreConstant.ENROLL_STATUS_ENROLLED);
        boolean hasAssigned = (hasCourse || nonCourseAssigned) && Objects.equals(item.getEnrollStatus(), CoreConstant.ENROLL_STATUS_ASSIGNED);
        boolean hasRemainingTime = item != null && !nonCourseAssigned
                && !CollectionUtil.isEmpty(item.getUnregisteredScheduleItems());
        long unassignedCount = unassigned || expiredPendingInvite || hasRemainingTime ? 1 : 0;
        row.setCourseCount((hasCourse || nonCourseAssigned) && !expiredPendingInvite ? 1 : 0);
        row.setUnassignedCount((int) unassignedCount);
        if (hasAssigned) {
            row.setTabType(CoreConstant.TAB_ASSIGNED);
            row.setStatusName("已分配");
        } else if (hasRegistered) {
            row.setTabType(CoreConstant.TAB_REGISTERED);
            row.setStatusName("已报名");
        } else {
            row.setTabType(CoreConstant.TAB_UNASSIGNED);
            row.setStatusName("未分配");
        }
    }

    private boolean isExpiredPendingInviteService(AsServiceEntity service) {
        return service != null && service.getEnrollEndTime() != null && !new Date().before(service.getEnrollEndTime());
    }

    private AsTeacherEntity currentHeadteacherProfile() {
        Long userId = buildHelper.currentUserId();
        if (userId == null) {
            return null;
        }
        QueryWrapper<AsTeacherEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("is_valid", 1).eq("user_id", userId).last("limit 1");
        AsTeacherEntity teacher = asTeacherMapper.selectOne(wrapper);
        if (teacher != null) {
            return teacher;
        }

        // 教师多个角色通过号码匹配教师信息
        String phone = Objects.requireNonNull(SmartRequestTokenUtil.getRequestUser()).getEmployeeBO().getPhone();
        if(phone == null) return null;
        QueryWrapper<AsTeacherEntity> pWrapper = new QueryWrapper<>();
        pWrapper.eq("is_valid", 1).eq("phone", phone).last("limit 1");
        AsTeacherEntity pTeacher = asTeacherMapper.selectOne(pWrapper);
        if (pTeacher != null) {
            return pTeacher;
        }
        return null;
    }

    /** 查询当前班主任有效绑定的行政班ID列表（bind_type=1 且绑定期覆盖当前日期）。 */
    private List<Long> listHeadteacherClassIds(Long teacherId, Long schoolId) {
        if (teacherId == null) {
            return Collections.emptyList();
        }
        Date now = new Date();
        QueryWrapper<AsTeacherClassEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("is_valid", 1)
                .eq("teacher_id", teacherId)
                .eq("bind_type", 1)
                .eq(schoolId != null, "school_id", schoolId)
                .and(w -> w.isNull("start_date").or().le("start_date", now))
                .and(w -> w.isNull("end_date").or().ge("end_date", now));
        List<AsTeacherClassEntity> bindings = asTeacherClassMapper.selectList(wrapper);
        if (CollectionUtil.isEmpty(bindings)) {
            return Collections.emptyList();
        }
        return bindings.stream()
                .map(AsTeacherClassEntity::getClassId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    /** 校验学生是否在当前班主任管理的班级内。 */
    private boolean isCurrentHeadteacherStudent(Long studentId) {
        AsStudentEntity student = asStudentMapper.selectById(studentId);
        if (!buildHelper.isValid(student)) {
            return false;
        }
        AsTeacherEntity teacher = currentHeadteacherProfile();
        if (!buildHelper.isValid(teacher)) {
            return false;
        }
        Long schoolId = buildHelper.currentSchoolId(teacher.getSchoolId());
        if (schoolId != null && !Objects.equals(student.getSchoolId(), schoolId)) {
            return false;
        }
        return listHeadteacherClassIds(teacher.getId(), schoolId).contains(student.getClassId());
    }

    private Long resolveHeadteacherAssignServiceId(AsEnrollmentAssignAO ao) {
        if (ao.getServiceId() != null) {
            return ao.getServiceId();
        }
        if (ao.getEnrollmentId() != null) {
            AsEnrollmentEntity enrollment = asEnrollmentMapper.selectById(ao.getEnrollmentId());
            if (buildHelper.isValid(enrollment)) {
                return enrollment.getServiceId();
            }
        }
        AsTeacherEntity teacher = currentHeadteacherProfile();
        if (!buildHelper.isValid(teacher)) {
            return null;
        }
        LambdaQueryWrapper<AsEnrollmentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsEnrollmentEntity::getIsValid, 1)
                .eq(AsEnrollmentEntity::getSchoolId, teacher.getSchoolId())
                .eq(AsEnrollmentEntity::getStudentId, ao.getStudentId())
                .orderByDesc(AsEnrollmentEntity::getId)
                .last("limit 1");
        AsEnrollmentEntity enrollment = asEnrollmentMapper.selectOne(wrapper);
        return enrollment == null ? null : enrollment.getServiceId();
    }

    // ======================== 班主任端分页/统计公共入口 ========================

    /** 班主任端分页查询学生报名记录，全部 Tab 按三状态独立取数后去重。 */
    public PageResultDTO<AsHeadteacherEnrollmentTermGroupVO> pageHeadteacherEnrollments(AsHeadteacherEnrollmentPageQueryAO query) {
        AsHeadteacherEnrollmentPageQueryAO safeQuery = safeHeadteacherEnrollmentQuery(query);
        String tabType = normalizeHeadteacherEnrollmentTab(safeQuery.getTabType());
        long startAt = System.currentTimeMillis();
        List<AsHeadteacherEnrollmentStudentRowVO> rows = CoreConstant.TAB_ALL.equals(tabType)
                ? listHeadteacherAllStatusRows(safeQuery)
                : listHeadteacherEnrollmentSelectionRows(safeQuery, tabType);
        PageResultDTO<AsHeadteacherEnrollmentTermGroupVO> result =
                toHeadteacherTermGroupPageResult(safeQuery.getPageIndex(), safeQuery.getPageSize(), rows);
        if (CoreConstant.TAB_ALL.equals(tabType)) {
            log.info("headteacher enrollment page done, tabType={}, pageIndex={}, pageSize={}, rowCount={}, total={}, costMs={}",
                    tabType, safeQuery.getPageIndex(), safeQuery.getPageSize(),
                    rows == null ? 0 : rows.size(), result == null ? null : result.getTotal(),
                    System.currentTimeMillis() - startAt);
        }
        return result;
    }

    /**
     * 统计班主任课后报名各 Tab 的数量。
     * <p>与分页列表接口使用相同的过滤和分组规则，以学生+学期维度聚合统计。</p>
     */
    public AsHeadteacherEnrollmentStatusCountVO countHeadteacherEnrollmentStatuses(AsHeadteacherEnrollmentPageQueryAO query) {
        AsHeadteacherEnrollmentPageQueryAO safeQuery = safeHeadteacherEnrollmentQuery(query);
        AsHeadteacherEnrollmentStatusCountVO vo = new AsHeadteacherEnrollmentStatusCountVO();
        long registeredCount = listHeadteacherEnrollmentSelectionRows(safeQuery, CoreConstant.TAB_REGISTERED).size();
        long assignedCount = listHeadteacherEnrollmentSelectionRows(safeQuery, CoreConstant.TAB_ASSIGNED).size();
        long unassignedCount = listHeadteacherEnrollmentSelectionRows(safeQuery, CoreConstant.TAB_UNASSIGNED).size();
        vo.setRegisteredCount(registeredCount);
        vo.setAssignedCount(assignedCount);
        vo.setUnassignedCount(unassignedCount);
        // 同一学生可能同时存在已分配课程和剩余未分配时间，全部数量需与三个业务 Tab 行数保持一致。
        vo.setAllCount(registeredCount + assignedCount + unassignedCount);
        return vo;
    }

    /**
     * 基于 MyBatis XML 关联查询的优化版本：公共数据只取一次，已报名/已分配用 SQL 条件聚合，
     * 未分配复用已有报名记录在 Java 侧计算，大幅减少数据库请求。
     */
    public AsHeadteacherEnrollmentStatusCountVO countHeadteacherEnrollmentStatusesV2(AsHeadteacherEnrollmentPageQueryAO query) {
        AsHeadteacherEnrollmentPageQueryAO safeQuery = safeHeadteacherEnrollmentQuery(query);
        AsHeadteacherEnrollmentStatusCountVO vo = new AsHeadteacherEnrollmentStatusCountVO();

        HeadteacherEnrollmentScopeContext scope = prepareHeadteacherEnrollmentScope(safeQuery);
        if (scope == null) {
            return vo;
        }

        Map<String, Long> counts = asEnrollmentMapper.countHeadteacherEnrollmentStatuses(
                scope.schoolId, scope.studentIds, scope.serviceMap.keySet(), safeQuery.getTermId());
        long registeredCount = counts != null && counts.get("registeredCount") != null ? counts.get("registeredCount") : 0L;
        long assignedCount = counts != null && counts.get("assignedCount") != null ? counts.get("assignedCount") : 0L;
        long unassignedCount = countHeadteacherUnassignedFromScope(scope);

        vo.setRegisteredCount(registeredCount);
        vo.setAssignedCount(assignedCount);
        vo.setUnassignedCount(unassignedCount);
        vo.setAllCount(registeredCount + assignedCount + unassignedCount);
        return vo;
    }

    /**
     * 提取班主任报名查询的公共上下文（教师/班级/学生/服务/报名记录），只查一次数据库。
     */
    private HeadteacherEnrollmentScopeContext prepareHeadteacherEnrollmentScope(AsHeadteacherEnrollmentPageQueryAO query) {
        AsTeacherEntity teacher = currentHeadteacherProfile();
        if (!buildHelper.isValid(teacher)) {
            return null;
        }
        Long schoolId = buildHelper.currentSchoolId(query.getSchoolId() == null ? teacher.getSchoolId() : query.getSchoolId());
        List<Long> classIds = listHeadteacherClassIds(teacher.getId(), schoolId);
        if (CollectionUtil.isEmpty(classIds)) {
            return null;
        }
        if (query.getClassId() != null) {
            if (!classIds.contains(query.getClassId())) {
                return null;
            }
            classIds = Collections.singletonList(query.getClassId());
        }
        List<AsStudentEntity> students = listHeadteacherEnrollmentStudents(query, schoolId, classIds);
        if (CollectionUtil.isEmpty(students)) {
            return null;
        }
        List<AsServiceEntity> services = listHeadteacherEnrollmentServices(query, schoolId);
        if (CollectionUtil.isEmpty(services)) {
            return null;
        }
        Set<Long> studentIds = students.stream().map(AsStudentEntity::getId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, AsServiceEntity> serviceMap = services.stream()
                .filter(service -> service.getId() != null)
                .collect(Collectors.toMap(AsServiceEntity::getId, Function.identity(), (a, b) -> a, LinkedHashMap::new));

        LambdaQueryWrapper<AsEnrollmentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsEnrollmentEntity::getIsValid, 1)
                .eq(schoolId != null, AsEnrollmentEntity::getSchoolId, schoolId)
                .in(AsEnrollmentEntity::getStudentId, studentIds)
                .in(AsEnrollmentEntity::getServiceId, serviceMap.keySet())
                .orderByDesc(AsEnrollmentEntity::getId);
        List<AsEnrollmentEntity> allEnrollments = asEnrollmentMapper.selectList(wrapper);

        HeadteacherEnrollmentScopeContext scope = new HeadteacherEnrollmentScopeContext();
        scope.schoolId = schoolId;
        scope.classIds = classIds;
        scope.students = students;
        scope.studentIds = studentIds;
        scope.serviceMap = serviceMap;
        scope.allEnrollments = allEnrollments;
        return scope;
    }

    private long countHeadteacherUnassignedFromScope(HeadteacherEnrollmentScopeContext scope) {
        if (CollectionUtil.isEmpty(scope.allEnrollments) || CollectionUtil.isEmpty(scope.serviceMap)) {
            return 0L;
        }
        Map<Long, AsServiceEntity> assignableServices = scope.serviceMap.entrySet().stream()
                .filter(entry -> buildHelper.isServiceAssignableWindow(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (assignableServices.isEmpty()) {
            return 0L;
        }
        Map<String, List<EnrollmentTimeSlot>> remainingSlotMap =
                mapHeadteacherUnregisteredTimeSlots(scope.allEnrollments, scope.students, assignableServices);
        if (remainingSlotMap.isEmpty()) {
            return 0L;
        }
        Set<String> studentTermPairs = new HashSet<>();
        for (Map.Entry<String, List<EnrollmentTimeSlot>> entry : remainingSlotMap.entrySet()) {
            if (CollectionUtil.isEmpty(entry.getValue())) {
                continue;
            }
            String[] parts = entry.getKey().split("_");
            if (parts.length < 3) {
                continue;
            }
            Long serviceId;
            Long studentId;
            try {
                serviceId = Long.valueOf(parts[1]);
                studentId = Long.valueOf(parts[2]);
            } catch (NumberFormatException e) {
                continue;
            }
            AsServiceEntity service = assignableServices.get(serviceId);
            if (service == null) {
                continue;
            }
            studentTermPairs.add(studentId + ":" + (service.getTermId() == null ? "0" : service.getTermId()));
        }
        return studentTermPairs.size();
    }



    private AsHeadteacherEnrollmentPageQueryAO safeHeadteacherEnrollmentQuery(AsHeadteacherEnrollmentPageQueryAO query) {
        return query == null ? new AsHeadteacherEnrollmentPageQueryAO() : query;
    }

    private String normalizeHeadteacherEnrollmentTab(String tabType) {
        if (!StringUtils.hasText(tabType)) {
            return CoreConstant.TAB_ALL;
        }
        String normalized = tabType.trim().toLowerCase(Locale.ROOT);
        if (CoreConstant.TAB_REGISTERED.equals(normalized) || CoreConstant.TAB_ASSIGNED.equals(normalized)
                || CoreConstant.TAB_UNASSIGNED.equals(normalized)) {
            return normalized;
        }
        return CoreConstant.TAB_ALL;
    }

    /**
     * 按管理端选课列表口径返回班主任可见的报名记录行。
     * <p>仅额外限制当前班主任绑定班级下的学生，返回结构仍保持班主任端学生卡片 VO。</p>
     */
    private List<AsHeadteacherEnrollmentStudentRowVO> listHeadteacherEnrollmentSelectionRows(AsHeadteacherEnrollmentPageQueryAO query,
                                                                                            String tabType) {
        Long currentTeacherId = asTeacherService.queryTeacherIdByPhone(null, null);
        if (null == currentTeacherId) {
            return Collections.emptyList();
        }
        Long schoolId = buildHelper.currentSchoolId(query.getSchoolId() == null ? SmartRequestTokenUtil.getRequestUserStuSchoolId() : query.getSchoolId());
        List<Long> classIds = listHeadteacherClassIds(currentTeacherId, schoolId);
        if (CollectionUtil.isEmpty(classIds)) {
            return Collections.emptyList();
        }
        if (query.getClassId() != null) {
            if (!classIds.contains(query.getClassId())) {
                return Collections.emptyList();
            }
            classIds = Collections.singletonList(query.getClassId());
        }
        List<AsStudentEntity> students = listHeadteacherEnrollmentStudents(query, schoolId, classIds);
        if (CollectionUtil.isEmpty(students)) {
            return Collections.emptyList();
        }
        List<AsServiceEntity> services = listHeadteacherEnrollmentServices(query, schoolId);
        if (CollectionUtil.isEmpty(services)) {
            return Collections.emptyList();
        }

        Set<Long> studentIds = students.stream().map(AsStudentEntity::getId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, AsServiceEntity> serviceMap = services.stream()
                .filter(service -> service.getId() != null)
                .collect(Collectors.toMap(AsServiceEntity::getId, Function.identity(), (a, b) -> a, LinkedHashMap::new));
        if (CoreConstant.TAB_REGISTERED.equals(tabType)) {
            return listHeadteacherRegisteredSelectionRows(schoolId, studentIds, serviceMap, students);
        }
        if (CoreConstant.TAB_ASSIGNED.equals(tabType)) {
            return listHeadteacherAssignedSelectionRows(schoolId, studentIds, serviceMap, students);
        }
        if (CoreConstant.TAB_UNASSIGNED.equals(tabType)) {
            return listHeadteacherUnassignedSelectionRows(schoolId, studentIds, serviceMap, students);
        }
        LambdaQueryWrapper<AsEnrollmentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsEnrollmentEntity::getIsValid, 1)
                .eq(schoolId != null, AsEnrollmentEntity::getSchoolId, schoolId)
                .in(AsEnrollmentEntity::getStudentId, studentIds)
                .in(AsEnrollmentEntity::getServiceId, serviceMap.keySet())
                .orderByDesc(AsEnrollmentEntity::getId);
        List<AsEnrollmentEntity> enrollments = asEnrollmentMapper.selectList(wrapper).stream()
                .filter(enrollment -> matchesHeadteacherSelectionTab(enrollment, tabType, serviceMap.get(enrollment.getServiceId())))
                .collect(Collectors.toList());
        List<AsEnrollmentEntity> representatives = CoreConstant.TAB_REGISTERED.equals(tabType)
                ? buildHelper.listHeadteacherEnrollmentRepresentatives(enrollments, serviceMap)
                : sessionEnrollmentSupport.listDistinctRepresentativeEnrollments(enrollments);
        return buildHeadteacherEnrollmentRecordRows(representatives, students, serviceMap, tabType);
    }

    /**
     * 构建班主任端已报名行，并将已报名业务条件下推到 SQL。
     */
    private List<AsHeadteacherEnrollmentStudentRowVO> listHeadteacherRegisteredSelectionRows(Long schoolId,
                                                                                              Set<Long> studentIds,
                                                                                              Map<Long, AsServiceEntity> serviceMap,
                                                                                              List<AsStudentEntity> students) {
        if (CollectionUtil.isEmpty(studentIds) || CollectionUtil.isEmpty(serviceMap)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<AsEnrollmentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsEnrollmentEntity::getIsValid, 1)
                .eq(schoolId != null, AsEnrollmentEntity::getSchoolId, schoolId)
                .in(AsEnrollmentEntity::getStudentId, studentIds)
                .in(AsEnrollmentEntity::getServiceId, serviceMap.keySet())
                .isNotNull(AsEnrollmentEntity::getCourseClassId)
                .isNotNull(AsEnrollmentEntity::getSessionId)
                .and(w -> w.eq(AsEnrollmentEntity::getEnrollStatus, CoreConstant.ENROLL_STATUS_ENROLLED)
                        .in(AsEnrollmentEntity::getEnrollSource, Arrays.asList(
                                CoreConstant.ENROLL_SOURCE_PUBLIC, CoreConstant.ENROLL_SOURCE_INVITE))
                        .or()
                        .eq(AsEnrollmentEntity::getEnrollStatus, CoreConstant.ENROLL_STATUS_ASSIGNED)
                        .eq(AsEnrollmentEntity::getEnrollSource, CoreConstant.ENROLL_SOURCE_CLASS_ASSIGN))
                .orderByDesc(AsEnrollmentEntity::getId);
        List<AsEnrollmentEntity> enrollments = asEnrollmentMapper.selectList(wrapper);
        List<AsEnrollmentEntity> representatives = buildHelper.listHeadteacherEnrollmentRepresentatives(enrollments, serviceMap);
        return buildHeadteacherEnrollmentRecordRows(representatives, students, serviceMap, CoreConstant.TAB_REGISTERED);
    }

    /**
     * 构建班主任端未分配行，并批量计算剩余时段。
     */
    private List<AsHeadteacherEnrollmentStudentRowVO> listHeadteacherUnassignedSelectionRows(Long schoolId,
                                                                                              Set<Long> studentIds,
                                                                                              Map<Long, AsServiceEntity> serviceMap,
                                                                                              List<AsStudentEntity> students) {
        LambdaQueryWrapper<AsEnrollmentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsEnrollmentEntity::getIsValid, 1)
                .eq(schoolId != null, AsEnrollmentEntity::getSchoolId, schoolId)
                .in(AsEnrollmentEntity::getStudentId, studentIds)
                .in(AsEnrollmentEntity::getServiceId, serviceMap.keySet())
                .orderByDesc(AsEnrollmentEntity::getId);
        List<AsEnrollmentEntity> enrollments = asEnrollmentMapper.selectList(wrapper);
        if (CollectionUtil.isEmpty(enrollments)) {
            return Collections.emptyList();
        }
        Map<String, List<EnrollmentTimeSlot>> remainingSlotMap = mapHeadteacherUnregisteredTimeSlots(
                enrollments, students, serviceMap);
        List<AsEnrollmentEntity> matchedEnrollments = enrollments.stream()
                .filter(enrollment -> matchesHeadteacherUnassignedSelectionTab(enrollment,
                        serviceMap.get(enrollment.getServiceId()), remainingSlotMap))
                .collect(Collectors.toList());
        List<AsEnrollmentEntity> representatives = sessionEnrollmentSupport.listDistinctRepresentativeEnrollments(matchedEnrollments);
        return buildHeadteacherEnrollmentRecordRows(representatives, students, serviceMap, CoreConstant.TAB_UNASSIGNED, remainingSlotMap);
    }

    /**
     * 已分配行：仅手动分配课程/非课程去向，且非课程去向必须仍存在有效记录。
     */
    private List<AsHeadteacherEnrollmentStudentRowVO> listHeadteacherAssignedSelectionRows(Long schoolId,
                                                                                           Set<Long> studentIds,
                                                                                           Map<Long, AsServiceEntity> serviceMap,
                                                                                           List<AsStudentEntity> students) {
        if (CollectionUtil.isEmpty(studentIds) || CollectionUtil.isEmpty(serviceMap)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<AsEnrollmentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsEnrollmentEntity::getIsValid, 1)
                .eq(schoolId != null, AsEnrollmentEntity::getSchoolId, schoolId)
                .in(AsEnrollmentEntity::getStudentId, studentIds)
                .in(AsEnrollmentEntity::getServiceId, serviceMap.keySet())
                .eq(AsEnrollmentEntity::getEnrollStatus, CoreConstant.ENROLL_STATUS_ASSIGNED)
                .eq(AsEnrollmentEntity::getEnrollSource, CoreConstant.ENROLL_SOURCE_MANUAL_ASSIGN)
                .orderByDesc(AsEnrollmentEntity::getId);
        List<AsEnrollmentEntity> enrollments = asEnrollmentMapper.selectList(wrapper).stream()
                .filter(this::hasCourseOrNonCourseDestination)
                .collect(Collectors.toList());
        List<AsEnrollmentEntity> representatives = buildHelper.listHeadteacherEnrollmentRepresentatives(enrollments, serviceMap);
        return buildHeadteacherEnrollmentRecordRows(representatives, students, serviceMap, CoreConstant.TAB_ASSIGNED);
    }

    /**
     * 全部 Tab 按已报名、已分配、未分配三个状态独立取数，避免同一学生被压成混合状态。
     */
    private List<AsHeadteacherEnrollmentStudentRowVO> listHeadteacherAllStatusRows(AsHeadteacherEnrollmentPageQueryAO query) {
        Long currentTeacherId = asTeacherService.queryTeacherIdByPhone(null, null);
        if (null == currentTeacherId) {
            return Collections.emptyList();
        }
        Long schoolId = buildHelper.currentSchoolId(query.getSchoolId() == null ?  SmartRequestTokenUtil.getRequestUserStuSchoolId(): query.getSchoolId());
        List<Long> classIds = listHeadteacherClassIds(currentTeacherId, schoolId);
        if (CollectionUtil.isEmpty(classIds)) {
            return Collections.emptyList();
        }
        if (query.getClassId() != null) {
            if (!classIds.contains(query.getClassId())) {
                return Collections.emptyList();
            }
            classIds = Collections.singletonList(query.getClassId());
        }
        List<AsStudentEntity> students = listHeadteacherEnrollmentStudents(query, schoolId, classIds);
        if (CollectionUtil.isEmpty(students)) {
            return Collections.emptyList();
        }
        List<AsServiceEntity> services = listHeadteacherEnrollmentServices(query, schoolId);
        if (CollectionUtil.isEmpty(services)) {
            return Collections.emptyList();
        }

        Set<Long> studentIds = students.stream().map(AsStudentEntity::getId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, AsServiceEntity> serviceMap = services.stream()
                .filter(service -> service.getId() != null)
                .collect(Collectors.toMap(AsServiceEntity::getId, Function.identity(), (a, b) -> a, LinkedHashMap::new));
        LambdaQueryWrapper<AsEnrollmentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsEnrollmentEntity::getIsValid, 1)
                .eq(schoolId != null, AsEnrollmentEntity::getSchoolId, schoolId)
                .in(AsEnrollmentEntity::getStudentId, studentIds)
                .in(AsEnrollmentEntity::getServiceId, serviceMap.keySet())
                .orderByDesc(AsEnrollmentEntity::getId);
        List<AsEnrollmentEntity> enrollments = asEnrollmentMapper.selectList(wrapper);

        HeadteacherEnrollmentBuildContext buildContext =
                buildHeadteacherEnrollmentContext(enrollments, students, serviceMap);
        buildContext.remainingSlotMap = mapHeadteacherUnregisteredTimeSlots(enrollments, students, serviceMap);
        List<AsHeadteacherEnrollmentStudentRowVO> rows = new ArrayList<>();
        rows.addAll(buildHeadteacherAllStatusRows(enrollments, students, serviceMap, CoreConstant.TAB_REGISTERED, buildContext));
        rows.addAll(buildHeadteacherAllStatusRows(enrollments, students, serviceMap, CoreConstant.TAB_ASSIGNED, buildContext));
        rows.addAll(buildHeadteacherAllStatusRows(enrollments, students, serviceMap, CoreConstant.TAB_UNASSIGNED, buildContext));
        return mergeHeadteacherAllRowsByStudentTermAndStatus(rows);
    }

    /** 班主任管辖班级内已注册的学生，支持年级与关键字过滤。 */
    private List<AsStudentEntity> listHeadteacherEnrollmentStudents(AsHeadteacherEnrollmentPageQueryAO query, Long schoolId,
                                                                    List<Long> classIds) {
        LambdaQueryWrapper<AsStudentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsStudentEntity::getIsValid, 1)
                .eq(AsStudentEntity::getRegisterStatus, CoreConstant.STUDENT_REGISTER_STATUS_APPROVED)
                .eq(schoolId != null, AsStudentEntity::getSchoolId, schoolId)
                .in(AsStudentEntity::getClassId, classIds);
        if (query.getGradeId() != null) {
            wrapper.eq(AsStudentEntity::getGradeId, query.getGradeId());
        }
        if (StringUtils.hasText(query.getKeyword())) {
            String keyword = query.getKeyword().trim();
            wrapper.and(w -> w.like(AsStudentEntity::getStudentNo, keyword).or().like(AsStudentEntity::getStudentName, keyword));
        }
        return asStudentMapper.selectList(wrapper);
    }

    /** 班主任可见的课后服务，支持学期与单服务过滤。 */
    private List<AsServiceEntity> listHeadteacherEnrollmentServices(AsHeadteacherEnrollmentPageQueryAO query, Long schoolId) {
        LambdaQueryWrapper<AsServiceEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsServiceEntity::getIsValid, 1)
                .eq(schoolId != null, AsServiceEntity::getSchoolId, schoolId);
        if (query.getTermId() != null) {
            wrapper.eq(AsServiceEntity::getTermId, query.getTermId());
        }
        if (query.getServiceId() != null) {
            wrapper.eq(AsServiceEntity::getId, query.getServiceId());
        }
        return asServiceMapper.selectList(wrapper);
    }

    private boolean matchesHeadteacherSelectionTab(AsEnrollmentEntity enrollment, String tabType, AsServiceEntity service) {
        boolean registered = buildHelper.isHeadteacherRegisteredEnrollment(enrollment);
        boolean assigned = buildHelper.isHeadteacherAssignedEnrollment(enrollment);
        boolean hasUnregisteredTime = (CoreConstant.TAB_UNASSIGNED.equals(tabType) || CoreConstant.TAB_ALL.equals(tabType))
                && timeSlotService.hasUnregisteredTime(enrollment, service);
        boolean assignableUnassigned = hasUnregisteredTime && buildHelper.isServiceAssignableWindow(service);
        if (CoreConstant.TAB_REGISTERED.equals(tabType)) {
            return registered;
        }
        if (CoreConstant.TAB_ASSIGNED.equals(tabType)) {
            return assigned;
        }
        if (CoreConstant.TAB_UNASSIGNED.equals(tabType)) {
            return assignableUnassigned;
        }
        return registered || assigned || assignableUnassigned;
    }

    /**
     * 使用一致的业务条件匹配未分配行，并复用预计算剩余时段。
     */
    private boolean matchesHeadteacherUnassignedSelectionTab(AsEnrollmentEntity enrollment,
                                                             AsServiceEntity service,
                                                             Map<String, List<EnrollmentTimeSlot>> remainingSlotMap) {
        boolean hasUnregisteredTime = buildHelper.hasPrecomputedUnregisteredTime(enrollment, remainingSlotMap);
        return hasUnregisteredTime && buildHelper.isServiceAssignableWindow(service);
    }

    /**
     * 已分配列表中，非课程去向必须仍存在有效去向记录，避免历史失效记录展示为已分配。
     */
    private boolean hasCourseOrNonCourseDestination(AsEnrollmentEntity enrollment) {
        if (enrollment == null) {
            return false;
        }
        if (enrollment.getCourseClassId() != null && enrollment.getSessionId() != null) {
            return true;
        }
        return buildHelper.findLatestNonCourseDestination(enrollment) != null;
    }

    /**
     * 班主任未分配列表按真实有效课次计算剩余时间，与管理端未选课列表保持一致。
     */
    private Map<String, List<EnrollmentTimeSlot>> mapHeadteacherUnregisteredTimeSlots(
            List<AsEnrollmentEntity> enrollments, List<AsStudentEntity> students,
            Map<Long, AsServiceEntity> serviceMap) {
        if (CollectionUtil.isEmpty(enrollments) || CollectionUtil.isEmpty(serviceMap)) {
            return Collections.emptyMap();
        }
        Map<Long, AsStudentEntity> studentMap = students == null ? Collections.emptyMap() : students.stream()
                .filter(student -> student != null && student.getId() != null)
                .collect(Collectors.toMap(AsStudentEntity::getId, Function.identity(), (a, b) -> a));
        Map<Long, List<EnrollmentTimeSlot>> serviceTimeSlotMap =
                timeSlotService.mapAssignableConcreteServiceTimeSlots(serviceMap.keySet(), serviceMap);
        Map<String, Set<String>> occupiedSlotKeyMap =
                timeSlotService.mapStudentTermOccupiedSlotKeys(enrollments, serviceMap, true);
        return mapUnregisteredTimeSlots(enrollments, studentMap, serviceTimeSlotMap, occupiedSlotKeyMap, true);
    }

    /**
     * 按学生+服务维度批量计算剩余可分配时段，同一组合只计算一次。
     */
    private Map<String, List<EnrollmentTimeSlot>> mapUnregisteredTimeSlots(List<AsEnrollmentEntity> enrollments,
                                                                           Map<Long, AsStudentEntity> studentMap,
                                                                           Map<Long, List<EnrollmentTimeSlot>> serviceTimeSlotMap,
                                                                           Map<String, Set<String>> occupiedSlotKeyMap,
                                                                           boolean compareConcreteSessionDate) {
        if (CollectionUtil.isEmpty(enrollments)) {
            return Collections.emptyMap();
        }
        Map<String, List<EnrollmentTimeSlot>> result = new HashMap<>();
        for (AsEnrollmentEntity enrollment : enrollments) {
            String key = buildHelper.serviceStudentKey(enrollment);
            if (!StringUtils.hasText(key) || result.containsKey(key)) {
                continue;
            }
            result.put(key, timeSlotService.findUnregisteredTimeSlots(enrollment.getSchoolId(), enrollment.getServiceId(),
                    enrollment.getStudentId(), studentMap == null ? null : studentMap.get(enrollment.getStudentId()),
                    serviceTimeSlotMap, occupiedSlotKeyMap, compareConcreteSessionDate));
        }
        return result;
    }

    /** 分页后按学期分组输出班主任端学生卡片。 */
    private PageResultDTO<AsHeadteacherEnrollmentTermGroupVO> toHeadteacherTermGroupPageResult(
            int pageIndex, int pageSize, List<AsHeadteacherEnrollmentStudentRowVO> rows) {
        PageResultDTO<AsHeadteacherEnrollmentStudentRowVO> rowPage = buildHelper.toManualPageResult(pageIndex, pageSize, rows);
        LinkedHashMap<Long, AsHeadteacherEnrollmentTermGroupVO> groupMap = new LinkedHashMap<>();
        for (AsHeadteacherEnrollmentStudentRowVO row : rowPage.getList()) {
            Long termId = row.getTermId();
            AsHeadteacherEnrollmentTermGroupVO group = groupMap.computeIfAbsent(termId, ignored -> {
                AsHeadteacherEnrollmentTermGroupVO item = new AsHeadteacherEnrollmentTermGroupVO();
                item.setTermId(termId);
                item.setTermName(row.getTermName());
                return item;
            });
            group.getStudentList().add(row);
        }
        PageResultDTO<AsHeadteacherEnrollmentTermGroupVO> result = new PageResultDTO<>();
        result.setTotal(rowPage.getTotal());
        result.setPages(rowPage.getPages());
        result.setPageIndex(rowPage.getPageIndex());
        result.setPageSize(rowPage.getPageSize());
        result.setList(new ArrayList<>(groupMap.values()));
        return result;
    }


}




