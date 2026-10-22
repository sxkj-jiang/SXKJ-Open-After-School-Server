package com.sxkj.edu.module.afterschool.service;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.config.AssembleUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.config.handle.business.EnrollmentTimeSlot;
import com.sxkj.edu.config.handle.business.ScheduleConflict;
import com.sxkj.edu.domain.sys.entity.mult.TEduclClass;
import com.sxkj.edu.module.afterschool.domain.dto.AsEnrollmentChangeAO;
import com.sxkj.edu.module.afterschool.domain.entity.*;
import com.sxkj.edu.module.afterschool.domain.vo.*;
import com.sxkj.edu.module.afterschool.enums.CourseCapacityPolicy;
import com.sxkj.edu.module.afterschool.mapper.*;
import com.sxkj.edu.module.afterschool.support.*;
import com.sxkj.edu.module.system.employee.TBaseUserDao;
import com.sxkj.edu.module.system.employee.domain.entity.TBaseUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 选课报名共享基础设施：行构建、验证、映射、课程容量管理、身份辅助等。
 * <p>作为 {@code @Component} 被所有拆分后的 Service 注入使用，不继承 ServiceImpl。</p>
 *
 * @author system
 */
@Component
@Slf4j
public class AsEnrollmentBaseBuildHelper {

    @Resource
    private SessionEnrollmentSupport sessionEnrollmentSupport;

    /** 读路径共享方法统一委托 AsEnrollmentBuildHelper，保持单一事实来源。 */
    @Resource
    private AsEnrollmentBuildHelper buildHelper;
    @Resource
    private AsEnrollmentMapper asEnrollmentMapper;
    @Resource
    private AsServiceMapper asServiceMapper;
    @Resource
    private AsCourseClassMapper asCourseClassMapper;
    @Resource
    private AsCourseQuotaService asCourseQuotaService;
    @Resource
    private AsStudentMapper asStudentMapper;
    @Resource
    private AsStudentDestinationMapper asStudentDestinationMapper;

    @Resource
    private ExamRoomService examRoomService;
    @Resource
    private AsEnrollmentTimeSlotService timeSlotService;
    @Resource
    private AsEnrollmentLogMapper asEnrollmentLogMapper;
    @Resource
    private TBaseUserDao tBaseUserDao;



    // ======================== 身份辅助 ========================

    /** 获取当前登录用户ID，异步场景回退到上下文操作人。 */
    public Long currentUserId() {
        Long requestUserId = SmartRequestTokenUtil.getRequestUserId();
        return requestUserId == null ? AsAsyncOperatorContext.getOperatorUserId() : requestUserId;
    }

    /** 获取当前登录用户姓名。 */
    public String currentUserName(String fallback) {
        try {
            if (SmartRequestTokenUtil.getRequestUser() != null
                    && SmartRequestTokenUtil.getRequestUser().getEmployeeBO() != null
                    && StringUtils.hasText(SmartRequestTokenUtil.getRequestUser().getEmployeeBO().getActualName())) {
                return SmartRequestTokenUtil.getRequestUser().getEmployeeBO().getActualName();
            }
        } catch (Exception ignore) {
            log.debug("get current user name failed", ignore);
        }
        return fallback;
    }

    /** 获取当前学校ID，优先使用传入值。 */
    public Long currentSchoolId(Long fallback) {
        if (fallback != null) {
            return fallback;
        }
        Long schoolId = SmartRequestTokenUtil.getRequestUserStuSchoolId();
        return schoolId == null || schoolId == 0 ? fallback : schoolId;
    }

    // ======================== 写路径共享内核（家长端/管理端/教师端共用） ========================

    /**
     * 写入选课操作日志，供报名、分配、调课、取消等所有写入口统一留痕。
     */
    public void saveLog(AsEnrollmentEntity enrollment, Long beforeClassId, Long afterClassId, Integer beforeStatus,
                        Integer afterStatus, Integer actionType, Integer operatorType, Long operatorUserId,
                        String operatorName, String remark) {
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
        logEntity.setOperatorType(operatorType);
        logEntity.setOperatorUserId(operatorUserId);
        logEntity.setOperatorName(operatorName);
        logEntity.setRemark(remark);
        logEntity.setCreateTime(new Date());
        logEntity.setIsValid(1);
        asEnrollmentLogMapper.insert(logEntity);
    }



    /**
     * 学生报名成功给家长发送成功站内通知。
     */
    private void sendChooseSuccessToParentNotice(Long studentId, String courseClassName, Long schoolId) {
        List<TeacherNoticeStudentVO> studentVOS = asStudentMapper.queryStudentByIds(schoolId, Lists.newArrayList(studentId));
        if (CollectionUtils.isEmpty(studentVOS)) {
            return;
        }
        List<Long> parentUserIds = studentVOS.stream().map(TeacherNoticeStudentVO::getUserId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(parentUserIds)) {
            return;
        }
        String tempContent = String.format("您已成功为孩子报名%s。", courseClassName);
        String title = "【报名成功】您已成功报名课程";
        LambdaQueryWrapper<TBaseUserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TBaseUserEntity::getIsValid, 1).in(TBaseUserEntity::getId, parentUserIds);
        List<TBaseUserEntity> tBaseUserEntityList = tBaseUserDao.selectList(queryWrapper);
    }

    // ======================== 选课详情共享链（管理端/家长端共用） ========================

    /**
     * 查询选课详情。
     * 已选和未选记录共用该详情，课程班为空时只返回学生和服务信息。
     */
    public AsEnrollmentSelectionDetailVO getSelectionDetail(Long enrollmentId) {
        AsEnrollmentEntity enrollment = asEnrollmentMapper.selectById(enrollmentId);
        if (!isValid(enrollment)) {
            return null;
        }
        Map<Long, AsStudentEntity> studentMap = mapStudents(Collections.singletonList(enrollment.getStudentId()));
        Map<Long, AsServiceEntity> serviceMap = mapServices(Collections.singletonList(enrollment.getServiceId()));
        Map<Long, EduclTermEntity> termMap = mapTerms(serviceMap.values().stream()
                .map(AsServiceEntity::getTermId).collect(Collectors.toList()));
        Map<Long, AsCourseClassEntity> courseMap = enrollment.getCourseClassId() == null
                ? Collections.emptyMap() : mapCourseClasses(Collections.singletonList(enrollment.getCourseClassId()));
        Map<Long, AsCourseCategoryEntity> categoryMap = courseMap.isEmpty()
                ? Collections.emptyMap() : mapCategories(courseMap.values().stream().map(AsCourseClassEntity::getCategoryId).collect(Collectors.toList()));
        Map<Long, EduclGradeEntity> gradeMap = mapGrades(studentMap.values().stream().map(AsStudentEntity::getGradeId).collect(Collectors.toList()));
        Map<Long, TEduclClass> classMap = mapClasses(studentMap.values().stream().map(AsStudentEntity::getClassId).collect(Collectors.toList()));
        Map<Long, AsTeacherEntity> teacherMap = courseMap.isEmpty()
                ? Collections.emptyMap() : mapTeachers(courseMap.values().stream().map(AsCourseClassEntity::getTeacherId).collect(Collectors.toList()));

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
            courseInfo.setEnrolledCount(countActualEnrolledStudents(course.getId()));
        }
        AsStudentDestinationEntity destination = getLatestNonCourseDestination(enrollment);
        if (course != null) {
            courseInfo.setDestinationType(CoreConstant.DESTINATION_TYPE_COURSE_CLASS);
            courseInfo.setDestinationName(AssembleUtil.getDestinationTypeName(CoreConstant.DESTINATION_TYPE_COURSE_CLASS));
        } else if (destination != null) {
            courseInfo.setDestinationType(destination.getDestinationType());
            courseInfo.setDestinationName(destination.getDestinationName());
        }
        vo.setCourse(courseInfo);

        AsEnrollmentSelectionDetailVO.OperationInfo operationInfo = new AsEnrollmentSelectionDetailVO.OperationInfo();
        operationInfo.setOperatorType(enrollment.getOperatorType());
        operationInfo.setOperatorUserId(enrollment.getOperatorUserId());
        operationInfo.setOperatorName(displayOperatorName(enrollment, student,
                mapGuardianRelations(Collections.singletonList(enrollment))));
        operationInfo.setOperateTime(enrollment.getEnrollTime() == null ? enrollment.getUpdateTime() : enrollment.getEnrollTime());
        operationInfo.setRemark(enrollment.getRemark());
        vo.setOperation(operationInfo);
        vo.setLogs(listEnrollmentLogs(enrollment));
        return vo;
    }

    private AsStudentDestinationEntity getLatestNonCourseDestination(AsEnrollmentEntity enrollment) {
        if (enrollment == null || enrollment.getSchoolId() == null
                || enrollment.getServiceId() == null || enrollment.getStudentId() == null) {
            return null;
        }
        LambdaQueryWrapper<AsStudentDestinationEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsStudentDestinationEntity::getIsValid, 1)
                .eq(AsStudentDestinationEntity::getSchoolId, enrollment.getSchoolId())
                .eq(AsStudentDestinationEntity::getServiceId, enrollment.getServiceId())
                .eq(AsStudentDestinationEntity::getStudentId, enrollment.getStudentId())
                .isNull(AsStudentDestinationEntity::getCourseClassId)
                .in(AsStudentDestinationEntity::getDestinationType, Arrays.asList(
                        CoreConstant.DESTINATION_TYPE_GO_HOME, CoreConstant.DESTINATION_TYPE_TRAINING_INSTITUTION,
                        CoreConstant.DESTINATION_TYPE_OTHER))
                .orderByDesc(AsStudentDestinationEntity::getAssignTime)
                .orderByDesc(AsStudentDestinationEntity::getId)
                .last("limit 1");
        return asStudentDestinationMapper.selectOne(wrapper);
    }

    private List<AsEnrollmentSelectionDetailVO.LogInfo> listEnrollmentLogs(AsEnrollmentEntity enrollment) {
        if (enrollment == null || enrollment.getSchoolId() == null
                || enrollment.getServiceId() == null || enrollment.getStudentId() == null) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<AsEnrollmentLogEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsEnrollmentLogEntity::getIsValid, 1)
                .eq(AsEnrollmentLogEntity::getSchoolId, enrollment.getSchoolId())
                .eq(AsEnrollmentLogEntity::getServiceId, enrollment.getServiceId())
                .eq(AsEnrollmentLogEntity::getStudentId, enrollment.getStudentId())
                .orderByDesc(AsEnrollmentLogEntity::getCreateTime)
                .orderByDesc(AsEnrollmentLogEntity::getId);
        List<AsEnrollmentLogEntity> logs = asEnrollmentLogMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(logs)) {
            return Collections.emptyList();
        }
        return logs.stream().map(this::toSelectionLogInfo).collect(Collectors.toList());
    }

    /**
     * 转换选课日志展示对象，补充状态和动作名称。
     */
    private AsEnrollmentSelectionDetailVO.LogInfo toSelectionLogInfo(AsEnrollmentLogEntity entity) {
        AsEnrollmentSelectionDetailVO.LogInfo item = new AsEnrollmentSelectionDetailVO.LogInfo();
        item.setLogId(entity.getId());
        item.setEnrollmentId(entity.getEnrollmentId());
        item.setBeforeCourseClassId(entity.getBeforeCourseClassId());
        item.setAfterCourseClassId(entity.getAfterCourseClassId());
        item.setBeforeEnrollStatus(entity.getBeforeEnrollStatus());
        item.setBeforeEnrollStatusName(AssembleUtil.getEnrollStatusName(entity.getBeforeEnrollStatus()));
        item.setAfterEnrollStatus(entity.getAfterEnrollStatus());
        item.setAfterEnrollStatusName(AssembleUtil.getEnrollStatusName(entity.getAfterEnrollStatus()));
        item.setActionType(entity.getActionType());
        item.setActionTypeName(AssembleUtil.getEnrollmentActionName(entity.getActionType(), entity.getRemark()));
        item.setOperatorType(entity.getOperatorType());
        item.setOperatorUserId(entity.getOperatorUserId());
        item.setOperatorName(entity.getOperatorName());
        item.setOperateTime(entity.getCreateTime());
        item.setRemark(entity.getRemark());
        return item;
    }


    public JsonResult<AsEnrollmentOperateResultVO> assignNonCourseDestination(Long enrollmentId, Long serviceId, Long studentId,
                                                                              Integer destinationType, String remark,
                                                                              Integer operatorType) {
        if (!isSupportedNonCourseDestination(destinationType)) {
            return JsonResult.buildError("去向类型不支持");
        }
        AsServiceEntity service = asServiceMapper.selectById(serviceId);
        AsStudentEntity student = asStudentMapper.selectById(studentId);
        if (!isValid(service) || !isValid(student)) {
            return JsonResult.buildError("服务或学生不存在");
        }
        if (!isServiceAssignableWindow(service)) {
            return JsonResult.buildError("课后服务不在分配时间内");
        }
        String scopeError = validateStudentServiceScope(service, student);
        if (scopeError != null) {
            return JsonResult.buildError(scopeError);
        }
        List<AsCourseSessionEntity> sessions = timeSlotService.listValidSessionsByService(serviceId);
        if (sessions.isEmpty()) {
            return JsonResult.buildError("课后服务尚未生成课次，无法分配去向");
        }
        List<AsCourseSessionEntity> freeSessions = timeSlotService.filterUnregisteredServiceSessions(service, studentId, sessions);
        if (freeSessions.isEmpty()) {
            return JsonResult.buildError("当前学生无可分配的空闲课后时间");
        }
        return persistNonCourseDestination(enrollmentId, service, student, freeSessions,
                destinationType, remark, operatorType);
    }


    public JsonResult<AsEnrollmentOperateResultVO> assignAllTermNonCourseDestination(
            Long enrollmentId, Long serviceId, Long studentId, Integer destinationType, String remark,
            Integer operatorType) {
        if (!isSupportedNonCourseDestination(destinationType)) {
            return JsonResult.buildError("去向类型不支持");
        }
        AsServiceEntity anchorService = asServiceMapper.selectById(serviceId);
        AsStudentEntity student = asStudentMapper.selectById(studentId);
        if (!isValid(anchorService) || !isValid(student)) {
            return JsonResult.buildError("服务或学生不存在");
        }
        if (!isAllTermNonCourseDestinationServiceAssignable(anchorService, operatorType)) {
            return JsonResult.buildError("课后服务不在分配时间内");
        }
        String scopeError = validateStudentServiceScope(anchorService, student);
        if (scopeError != null) {
            return JsonResult.buildError(scopeError);
        }

        List<AsServiceEntity> sameTermServices = listValidServicesInSameTerm(anchorService);
        Set<String> assignedSessionKeys = listActiveNonCourseSessionKeys(anchorService, studentId, sameTermServices);
        List<AsServiceEntity> services = sameTermServices.stream()
                .filter(service -> isAllTermNonCourseDestinationServiceAssignable(service, operatorType))
                .filter(service -> isStudentInServiceScope(service, student))
                .collect(Collectors.toList());
        Map<Long, AsServiceEntity> serviceMap = services.stream().collect(Collectors.toMap(
                AsServiceEntity::getId, Function.identity(), (a, b) -> a));
        Map<Long, List<EnrollmentTimeSlot>> afterSchoolSlotMap = timeSlotService.mapAfterSchoolTemplateTimeSlots(
                serviceMap.keySet(), serviceMap);
        Map<Long, List<AsCourseSessionEntity>> selectedSessionsByService = new LinkedHashMap<>();
        for (AsServiceEntity service : services) {
            Set<String> afterSchoolSlotKeys = afterSchoolSlotMap.getOrDefault(service.getId(), Collections.emptyList())
                    .stream()
                    .filter(slot -> timeSlotService.isSlotApplicableToStudent(slot, student))
                    .map(EnrollmentTimeSlot::getSlotKey)
                    .filter(StringUtils::hasText)
                    .collect(Collectors.toSet());
            if (afterSchoolSlotKeys.isEmpty()) {
                continue;
            }
            List<AsCourseSessionEntity> serviceSessions = timeSlotService.listValidSessionsByService(service.getId()).stream()
                    .filter(this::isAdminAssignableSessionTime)
                    .filter(session -> afterSchoolSlotKeys.contains(timeSlotService.buildEnrollmentTimeSlotKey(session)))
                    .collect(Collectors.toList());
            List<AsCourseSessionEntity> freeSessions = timeSlotService.filterUnregisteredConcreteServiceSessions(
                    service, studentId, serviceSessions);
            for (AsCourseSessionEntity session : freeSessions) {
                String sessionKey = timeSlotService.buildConcreteSessionTimeKey(session);
                if (!StringUtils.hasText(sessionKey) || !assignedSessionKeys.add(sessionKey)) {
                    continue;
                }
                selectedSessionsByService.computeIfAbsent(service.getId(), key -> new ArrayList<>())
                        .add(session);
            }
        }
        if (selectedSessionsByService.isEmpty()) {
            return JsonResult.buildError("当前学生无可分配的空闲课后时间");
        }

        JsonResult<AsEnrollmentOperateResultVO> firstResult = null;
        for (Map.Entry<Long, List<AsCourseSessionEntity>> entry : selectedSessionsByService.entrySet()) {
            AsServiceEntity service = serviceMap.get(entry.getKey());
            if (service == null) {
                continue;
            }
            Long currentEnrollmentId = Objects.equals(service.getId(), anchorService.getId()) ? enrollmentId : null;
            JsonResult<AsEnrollmentOperateResultVO> result = persistNonCourseDestination(currentEnrollmentId,
                    service, student, entry.getValue(), destinationType, remark, operatorType);
            if (!Boolean.TRUE.equals(result.getSuccess())) {
                return result;
            }
            if (firstResult == null) {
                firstResult = result;
            }
        }
        return firstResult == null ? JsonResult.buildError("当前学生无可分配的空闲课后时间") : firstResult;
    }

    private boolean isAllTermNonCourseDestinationServiceAssignable(AsServiceEntity service, Integer operatorType) {
        return isServiceAssignableWindow(service)
                && (!Objects.equals(operatorType, CoreConstant.ENROLLMENT_OPERATOR_TEACHER) || isServiceAssignable(service));
    }

    private List<AsServiceEntity> listValidServicesInSameTerm(AsServiceEntity anchorService) {
        if (anchorService == null || anchorService.getTermId() == null) {
            return anchorService == null ? Collections.emptyList() : Collections.singletonList(anchorService);
        }
        LambdaQueryWrapper<AsServiceEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsServiceEntity::getIsValid, 1)
                .eq(AsServiceEntity::getSchoolId, anchorService.getSchoolId())
                .eq(AsServiceEntity::getTermId, anchorService.getTermId());
        List<AsServiceEntity> services = asServiceMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(services)) {
            return Collections.singletonList(anchorService);
        }
        return services.stream()
                .sorted(Comparator.comparingInt((AsServiceEntity service) ->
                                Objects.equals(service.getId(), anchorService.getId()) ? 0 : 1)
                        .thenComparing(AsServiceEntity::getId, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    private Set<String> listActiveNonCourseSessionKeys(AsServiceEntity anchorService, Long studentId,
                                                       List<AsServiceEntity> services) {
        List<Long> serviceIds = services == null ? Collections.emptyList() : services.stream()
                .map(AsServiceEntity::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (anchorService == null || studentId == null || serviceIds.isEmpty()) {
            return new HashSet<>();
        }
        LambdaQueryWrapper<AsStudentDestinationEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsStudentDestinationEntity::getIsValid, 1)
                .eq(AsStudentDestinationEntity::getSchoolId, anchorService.getSchoolId())
                .eq(AsStudentDestinationEntity::getStudentId, studentId)
                .in(AsStudentDestinationEntity::getServiceId, serviceIds)
                .isNull(AsStudentDestinationEntity::getCourseClassId)
                .isNotNull(AsStudentDestinationEntity::getSessionId)
                .in(AsStudentDestinationEntity::getDestinationType, Arrays.asList(
                        CoreConstant.DESTINATION_TYPE_GO_HOME, CoreConstant.DESTINATION_TYPE_TRAINING_INSTITUTION,
                        CoreConstant.DESTINATION_TYPE_OTHER));
        List<AsStudentDestinationEntity> destinations = asStudentDestinationMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(destinations)) {
            return new HashSet<>();
        }
        return new HashSet<>(timeSlotService.mapSessionSlots(destinations.stream()
                        .map(AsStudentDestinationEntity::getSessionId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())).values().stream()
                .map(timeSlotService::buildConcreteSessionTimeKey)
                .filter(StringUtils::hasText)
                .collect(Collectors.toSet()));
    }

    private boolean isAdminAssignableSessionTime(AsCourseSessionEntity session) {
        return isAdminAssignableSessionTime(timeSlotService.toEnrollmentTimeSlot(session));
    }

    private boolean isAdminAssignableSessionTime(EnrollmentTimeSlot slot) {
        if (slot == null) {
            return false;
        }
        if (slot.getSessionDate() == null) {
            return true;
        }
        Calendar now = Calendar.getInstance();
        Calendar sessionDate = Calendar.getInstance();
        sessionDate.setTime(slot.getSessionDate());
        int nowDay = now.get(Calendar.YEAR) * 1000 + now.get(Calendar.DAY_OF_YEAR);
        int sessionDay = sessionDate.get(Calendar.YEAR) * 1000 + sessionDate.get(Calendar.DAY_OF_YEAR);
        if (sessionDay != nowDay) {
            return sessionDay > nowDay;
        }
        if (slot.getEndMinute() == null) {
            return true;
        }
        int nowMinute = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE);
        return slot.getEndMinute() > nowMinute;
    }

    /** 在指定服务中保存非课程代表报名、课次去向和操作日志。 */
    public JsonResult<AsEnrollmentOperateResultVO> persistNonCourseDestination(
            Long enrollmentId, AsServiceEntity service, AsStudentEntity student,
            List<AsCourseSessionEntity> freeSessions, Integer destinationType, String remark,
            Integer operatorType) {
        Long serviceId = service.getId();
        Long studentId = student.getId();
        Long operatorUserId = currentUserId();
        String operatorName = currentUserName(defaultOperatorName(operatorType));
        AsEnrollmentEntity representative = enrollmentId == null ? getCurrentEnrollment(service.getSchoolId(), serviceId, studentId)
                : resolveRepresentativeEnrollment(enrollmentId);
        Long beforeClassId = representative == null ? null : representative.getCourseClassId();
        Integer beforeStatus = representative == null ? null : representative.getEnrollStatus();
        sessionEnrollmentSupport.invalidateUnassignedPlaceholder(service.getSchoolId(), serviceId, studentId, operatorUserId);

        Date now = new Date();
        AsEnrollmentEntity base = representative == null
                ? newUnassignedEnrollment(service.getSchoolId(), serviceId, studentId)
                : representative;
        AsEnrollmentEntity assigned = buildEnrollmentTemplate(base, null, CoreConstant.ENROLL_SOURCE_MANUAL_ASSIGN,
                CoreConstant.ENROLL_STATUS_ASSIGNED, operatorType, operatorUserId, operatorName, remark);
        assigned.setEnrollTime(now);
        assigned.setCreateUser(operatorUserId);
        assigned.setUpdateUser(operatorUserId);
        assigned.setCreateTime(now);
        assigned.setUpdateTime(now);
        assigned.setIsValid(1);
        asEnrollmentMapper.insert(assigned);

        saveNonCourseDestinations(service, student, freeSessions, destinationType, operatorUserId, now);
        saveLog(assigned, beforeClassId, null, beforeStatus, CoreConstant.ENROLL_STATUS_ASSIGNED, CoreConstant.ENROLL_ACTION_MANUAL_ASSIGN,
                operatorType, operatorUserId, operatorName, buildDestinationRemark(destinationType, remark));
        return JsonResult.buildSuccess(toOperateResult(assigned));
    }

    /** 选课/分配统一写入口（默认严格容量策略）。 */
    public JsonResult<AsEnrollmentOperateResultVO> assignCourseInternal(Long enrollmentId, Long serviceId, Long studentId, Long courseClassId,
                                                                        Integer source, Integer status, Integer action,
                                                                        Integer operatorType, String remark) {
        return assignCourseInternal(enrollmentId, serviceId, studentId, courseClassId, source, status, action,
                operatorType, remark, false);
    }

    /** 选课/分配统一写入口。capacityRepairLocked 仅用于家长公开报名接近满员时校准课程班人数。 */
    public JsonResult<AsEnrollmentOperateResultVO> assignCourseInternal(Long enrollmentId, Long serviceId, Long studentId, Long courseClassId,
                                                                        Integer source, Integer status, Integer action,
                                                                        Integer operatorType, String remark,
                                                                        boolean capacityRepairLocked) {
        return assignCourseInternal(enrollmentId, serviceId, studentId, courseClassId, source, status, action,
                operatorType, remark, capacityRepairLocked, CourseCapacityPolicy.ENFORCE_LIMIT);
    }

    /**
     * 选课/分配统一写入口。
     * 负责服务范围、容量、重复报名校验，维护课程班人数、当前状态、操作日志和学生课后去向。
     * 只有学生调课审核的专用调用链能放开容量，其它调用方默认沿用严格占位。
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<AsEnrollmentOperateResultVO> assignCourseInternal(Long enrollmentId, Long serviceId, Long studentId, Long courseClassId,
                                                                        Integer source, Integer status, Integer action,
                                                                        Integer operatorType, String remark,
                                                                        boolean capacityRepairLocked,
                                                                        CourseCapacityPolicy capacityPolicy) {
        boolean studentAdjust = capacityPolicy == CourseCapacityPolicy.STUDENT_ADJUST;
        if (studentAdjust && (!Objects.equals(action, CoreConstant.ACTION_STUDENT_ADJUST)
                || !Objects.equals(operatorType, CoreConstant.OPERATOR_ADMIN))) {
            return JsonResult.buildError("仅学生调课审核允许使用超额占位");
        }
        AsServiceEntity service = asServiceMapper.selectById(serviceId);
        AsStudentEntity student = asStudentMapper.selectById(studentId);
        AsCourseClassEntity course = asCourseClassMapper.selectById(courseClassId);
        if (!isValid(service) || !isValid(student)) {
            return JsonResult.buildError("服务或学生不存在");
        }
        if (!Objects.equals(operatorType, CoreConstant.OPERATOR_GUARDIAN) && !isServiceAssignableWindow(service)) {
            return JsonResult.buildError("课后服务不在分配时间内");
        }
        if (Objects.equals(operatorType, CoreConstant.OPERATOR_GUARDIAN) && !isServiceOpenForGuardianApply(service)) {
            return JsonResult.buildError("课后服务不在报名时间内");
        }
        String scopeError = validateStudentServiceScope(service, student);
        if (scopeError != null) {
            return JsonResult.buildError(scopeError);
        }
        boolean publicGuardianApply = Objects.equals(operatorType, CoreConstant.OPERATOR_GUARDIAN)
                && Objects.equals(source, CoreConstant.ENROLL_SOURCE_PUBLIC);
        String validate = validateCourseAssignable(student, course, serviceId, !publicGuardianApply, capacityPolicy);
        if (validate != null) {
            return JsonResult.buildError(validate);
        }
        List<AsCourseSessionEntity> sessions = sessionEnrollmentSupport.listValidSessionsByCourseClass(courseClassId);
        if (sessions.isEmpty()) {
            return JsonResult.buildError("课程班尚未生成课次，无法报名");
        }
        if (!Objects.equals(operatorType, CoreConstant.OPERATOR_GUARDIAN)
                && !timeSlotService.courseCoversUnregisteredTime(service, studentId, sessions)) {
            return JsonResult.buildError("当前课程未覆盖学生剩余可分配课后时间");
        }
        AsEnrollmentEntity existingCourseEnrollment = findCourseEnrollment(courseClassId, studentId);
        if (existingCourseEnrollment != null) {
            return JsonResult.buildError("该学生已选择该课程");
        }
        ScheduleConflict conflict = timeSlotService.findScheduleConflict(service.getSchoolId(), studentId, courseClassId, sessions);
        if (conflict != null) {
            return JsonResult.buildError(conflict.message());
        }
        if (studentAdjust) {
            reserveStudentAdjustCourseSeat(course, student);
        } else if (asCourseQuotaService.isPublic(course)) {
            asCourseQuotaService.reserve(course, student);
        } else if (!reserveCourseClassSeat(courseClassId, capacityRepairLocked)) {
            return JsonResult.buildError("课程班名额已满");
        }
        AsEnrollmentEntity representative = enrollmentId == null ? null : resolveRepresentativeEnrollment(enrollmentId);
        Long beforeClassId = representative == null ? null : representative.getCourseClassId();
        Integer beforeStatus = representative == null ? null : representative.getEnrollStatus();
        Date now = new Date();
        AsEnrollmentEntity base = representative == null
                ? newUnassignedEnrollment(service.getSchoolId(), serviceId, studentId)
                : representative;
        AsEnrollmentEntity template = buildEnrollmentTemplate(
                base,
                courseClassId, source, status, operatorType, currentUserId(), currentUserName(defaultOperatorName(operatorType)), remark);
        template.setParentConfirmRequired(course.getParentConfirmRequired());
        if (Objects.equals(source, CoreConstant.ENROLL_SOURCE_INVITE)) {
            AsCourseInviteEntity invite = findInviteByCourseAndStudent(courseClassId, studentId);
            if (invite != null) {
                template.setInviteId(invite.getId());
            }
        }
        if (Objects.equals(operatorType, CoreConstant.OPERATOR_GUARDIAN)) {
            template.setGuardianConfirmUserId(currentUserId());
            template.setGuardianConfirmTime(now);
        }
        template.setEnrollTime(now);
        AsEnrollmentEntity assigned = sessionEnrollmentSupport.appendStudentToCourseSessions(template, sessions, currentUserId());
        timeSlotService.invalidateNonCourseDestinationsBySlots(service, studentId, sessions, currentUserId());
        saveLog(assigned, beforeClassId, courseClassId, beforeStatus, status, action,
                operatorType, currentUserId(), currentUserName(defaultOperatorName(operatorType)), remark);

        // 学生报名成功给家长发送成功站内通知
        sendChooseSuccessToParentNotice(studentId, course.getCourseClassName(), course.getSchoolId());
        return JsonResult.buildSuccess(toOperateResult(assigned));
    }


    /**
     * 管理端/班主任端调整已选课程班共用内核。
     * 成功后目标课程班人数加一，原课程班人数减一，并同步学生课后去向。
     */
    public JsonResult<AsEnrollmentOperateResultVO> changeCourseInternal(AsEnrollmentChangeAO ao, AsEnrollmentEntity enrollment,
                                                                        Integer operatorType) {
        if (!isValid(enrollment) || enrollment.getCourseClassId() == null) {
            return JsonResult.buildError("已选课记录不存在");
        }
        if (Objects.equals(enrollment.getCourseClassId(), ao.getTargetCourseClassId())) {
            return JsonResult.buildError("目标课程班不能与当前课程班相同");
        }
        AsCourseClassEntity target = asCourseClassMapper.selectById(ao.getTargetCourseClassId());
        AsStudentEntity student = asStudentMapper.selectById(enrollment.getStudentId());
        AsServiceEntity service = asServiceMapper.selectById(enrollment.getServiceId());
        String scopeError = validateStudentServiceScope(service, student);
        if (scopeError != null) {
            return JsonResult.buildError(scopeError);
        }
        String validate = validateCourseAssignable(student, target, enrollment.getServiceId());
        if (validate != null) {
            return JsonResult.buildError(validate);
        }
        List<AsCourseSessionEntity> targetSessions = sessionEnrollmentSupport.listValidSessionsByCourseClass(target.getId());
        if (targetSessions.isEmpty()) {
            return JsonResult.buildError("目标课程班尚未生成课次，无法改课");
        }
        ScheduleConflict conflict = timeSlotService.findScheduleConflict(enrollment.getSchoolId(), enrollment.getStudentId(),
                target.getId(), targetSessions, new HashSet<>(Arrays.asList(enrollment.getCourseClassId(), target.getId())));
        if (conflict != null) {
            return JsonResult.buildError(conflict.message());
        }
        lockPublicCourseTransition(enrollment.getCourseClassId(), target);
        if (asCourseQuotaService.isPublic(target)) {
            asCourseQuotaService.reserve(target, student);
        } else {
            increaseCourseClassCountForAdjust(target.getId());
        }
        Long beforeClassId = enrollment.getCourseClassId();
        Integer beforeStatus = enrollment.getEnrollStatus();
        Long operatorUserId = currentUserId();
        String operatorName = currentUserName(defaultOperatorName(operatorType));
        decreaseCourseClassCount(beforeClassId, enrollment.getStudentId());
        AsEnrollmentEntity template = buildEnrollmentTemplate(enrollment, target.getId(), enrollment.getEnrollSource(),
                enrollment.getEnrollStatus(), operatorType, operatorUserId, operatorName, ao.getRemark());
        sessionEnrollmentSupport.invalidateCourseSessionEnrollments(enrollment.getSchoolId(), enrollment.getServiceId(),
                enrollment.getStudentId(), beforeClassId, operatorUserId);
        AsEnrollmentEntity assigned = sessionEnrollmentSupport.appendStudentToCourseSessions(template, targetSessions, operatorUserId);
        saveLog(assigned, beforeClassId, target.getId(), beforeStatus, assigned.getEnrollStatus(), CoreConstant.ENROLL_ACTION_CHANGE_COURSE,
                operatorType, operatorUserId, operatorName, ao.getRemark());
        return JsonResult.buildSuccess(toOperateResult(assigned));
    }



    private boolean isValid(Object entity) {
        return buildHelper.isValid(entity);
    }

    private boolean isServiceAssignableWindow(AsServiceEntity service) {
        return buildHelper.isServiceAssignableWindow(service);
    }

    private boolean isServiceAssignable(AsServiceEntity service) {
        return buildHelper.isServiceAssignable(service);
    }

    private boolean isServiceOpenForGuardianApply(AsServiceEntity service) {
        return buildHelper.isServiceOpenForGuardianApply(service);
    }

    private String validateStudentServiceScope(AsServiceEntity service, AsStudentEntity student) {
        return buildHelper.validateStudentServiceScope(service, student);
    }

    private String validateCourseAssignable(AsStudentEntity student, AsCourseClassEntity course, Long serviceId) {
        return buildHelper.validateCourseAssignable(student, course, serviceId);
    }

    private String validateCourseAssignable(AsStudentEntity student, AsCourseClassEntity course, Long serviceId,
                                            boolean validateCapacity, CourseCapacityPolicy capacityPolicy) {
        return buildHelper.validateCourseAssignable(student, course, serviceId, validateCapacity, capacityPolicy);
    }

    private AsEnrollmentEntity findCourseEnrollment(Long courseClassId, Long studentId) {
        return buildHelper.findCourseEnrollment(courseClassId, studentId);
    }

    private AsEnrollmentEntity resolveRepresentativeEnrollment(Long enrollmentId) {
        return buildHelper.resolveRepresentativeEnrollment(enrollmentId);
    }

    private AsEnrollmentEntity getCurrentEnrollment(Long schoolId, Long serviceId, Long studentId) {
        return buildHelper.getCurrentEnrollment(schoolId, serviceId, studentId);
    }

    private AsEnrollmentEntity newUnassignedEnrollment(Long schoolId, Long serviceId, Long studentId) {
        return buildHelper.newUnassignedEnrollment(schoolId, serviceId, studentId);
    }

    private AsEnrollmentEntity buildEnrollmentTemplate(AsEnrollmentEntity base, Long courseClassId, Integer source,
                                                       Integer status, Integer operatorType, Long operatorUserId,
                                                       String operatorName, String remark) {
        return buildHelper.buildEnrollmentTemplate(base, courseClassId, source, status, operatorType,
                operatorUserId, operatorName, remark);
    }

    private String defaultOperatorName(Integer operatorType) {
        return buildHelper.defaultOperatorName(operatorType);
    }

    private AsCourseInviteEntity findInviteByCourseAndStudent(Long courseClassId, Long studentId) {
        return buildHelper.findInviteByCourseAndStudent(courseClassId, studentId);
    }

    private boolean isSupportedNonCourseDestination(Integer destinationType) {
        return buildHelper.isSupportedNonCourseDestination(destinationType);
    }

    private boolean isStudentInServiceScope(AsServiceEntity service, AsStudentEntity student) {
        return buildHelper.isStudentInServiceScope(service, student);
    }

    private String buildDestinationRemark(Integer destinationType, String remark) {
        return buildHelper.buildDestinationRemark(destinationType, remark);
    }

    private void saveNonCourseDestinations(AsServiceEntity service, AsStudentEntity student,
                                           List<AsCourseSessionEntity> sessions, Integer destinationType,
                                           Long operatorUserId, Date assignTime) {
        buildHelper.saveNonCourseDestinations(service, student, sessions, destinationType, operatorUserId, assignTime);
    }

    private void reserveStudentAdjustCourseSeat(AsCourseClassEntity course, AsStudentEntity student) {
        buildHelper.reserveStudentAdjustCourseSeat(course, student);
    }

    private boolean reserveCourseClassSeat(Long courseClassId, boolean capacityRepairLocked) {
        return buildHelper.reserveCourseClassSeat(courseClassId, capacityRepairLocked);
    }

    private void lockPublicCourseTransition(Long beforeCourseId, AsCourseClassEntity target) {
        buildHelper.lockPublicCourseTransition(beforeCourseId, target);
    }

    private void increaseCourseClassCountForAdjust(Long courseClassId) {
        buildHelper.increaseCourseClassCountForAdjust(courseClassId);
    }

    private void decreaseCourseClassCount(Long courseClassId, Long studentId) {
        buildHelper.decreaseCourseClassCount(courseClassId, studentId);
    }

    private AsEnrollmentOperateResultVO toOperateResult(AsEnrollmentEntity enrollment) {
        return buildHelper.toOperateResult(enrollment);
    }

    private String displayOperatorName(AsEnrollmentEntity enrollment, AsStudentEntity student,
                                       Map<String, AsGuardianStudentEntity> guardianRelationMap) {
        return buildHelper.displayOperatorName(enrollment, student, guardianRelationMap);
    }

    private Map<String, AsGuardianStudentEntity> mapGuardianRelations(List<AsEnrollmentEntity> enrollments) {
        return buildHelper.mapGuardianRelations(enrollments);
    }

    private Map<Long, AsStudentEntity> mapStudents(Collection<Long> ids) {
        return buildHelper.mapStudents(ids);
    }

    private Map<Long, AsServiceEntity> mapServices(Collection<Long> ids) {
        return buildHelper.mapServices(ids);
    }

    private Map<Long, EduclTermEntity> mapTerms(Collection<Long> ids) {
        return buildHelper.mapTerms(ids);
    }

    private Map<Long, AsCourseClassEntity> mapCourseClasses(Collection<Long> ids) {
        return buildHelper.mapCourseClasses(ids);
    }

    private Map<Long, AsCourseCategoryEntity> mapCategories(Collection<Long> ids) {
        return buildHelper.mapCategories(ids);
    }

    private Map<Long, EduclGradeEntity> mapGrades(Collection<Long> ids) {
        return buildHelper.mapGrades(ids);
    }

    private Map<Long, TEduclClass> mapClasses(Collection<Long> ids) {
        return buildHelper.mapClasses(ids);
    }

    private Map<Long, AsTeacherEntity> mapTeachers(Collection<Long> ids) {
        return buildHelper.mapTeachers(ids);
    }

    private int countActualEnrolledStudents(Long courseClassId) {
        return buildHelper.countActualEnrolledStudents(courseClassId);
    }
}
