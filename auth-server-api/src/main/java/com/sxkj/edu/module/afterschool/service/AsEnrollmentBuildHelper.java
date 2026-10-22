package com.sxkj.edu.module.afterschool.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.exception.BaseBusinessException;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.config.AssembleUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.config.handle.business.*;
import com.sxkj.edu.domain.sys.entity.mult.TEduclClass;
import com.sxkj.edu.module.afterschool.domain.dto.*;
import com.sxkj.edu.module.afterschool.domain.entity.*;
import com.sxkj.edu.module.afterschool.domain.excel.AsEnrollmentExportExcel;
import com.sxkj.edu.module.afterschool.domain.vo.*;
import com.sxkj.edu.module.afterschool.enums.CourseCapacityPolicy;
import com.sxkj.edu.module.afterschool.mapper.*;
import com.sxkj.edu.module.afterschool.support.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
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
public class AsEnrollmentBuildHelper {

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
    private AsGuardianStudentMapper asGuardianStudentMapper;
    @Resource
    private AsCourseInviteMapper asCourseInviteMapper;
    @Resource
    private AsStudentDestinationMapper asStudentDestinationMapper;
    @Resource
    private AsCourseCategoryMapper asCourseCategoryMapper;
    @Resource
    private EduclGradeMapper educlGradeMapper;
    @Resource
    private EduclClassMapper educlClassMapper;
    @Resource
    private AsTeacherMapper asTeacherMapper;
    @Resource
    private EduclTermMapper educlTermMapper;
    @Resource
    private CourseClassScopeSupport courseClassScopeSupport;
    @Resource
    private StudentTermScopeSupport studentTermScopeSupport;
    @Resource
    private StudentTermSnapshotSupport studentTermSnapshotSupport;
    @Resource
    private StudentIdentitySupport studentIdentitySupport;
    @Resource
    private SessionEnrollmentSupport sessionEnrollmentSupport;
    @Resource
    private AsCourseScheduleRuleMapper asCourseScheduleRuleMapper;
    @Resource
    private ExamRoomService examRoomService;
    @Resource
    private AsEnrollmentTimeSlotService timeSlotService;
    @Resource
    private AsCourseSessionMapper asCourseSessionMapper;

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

    /** 查询当前登录教师对应的教师实体。 */
    public AsTeacherEntity currentTeacher() {
        Long userId = currentUserId();
        if (userId == null) {
            return null;
        }
        LambdaQueryWrapper<AsTeacherEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsTeacherEntity::getIsValid, 1)
                .eq(AsTeacherEntity::getUserId, userId)
                .last("limit 1");
        return asTeacherMapper.selectOne(wrapper);
    }

    /** 判断实体是否有效（非空且未删除）。 */
    public boolean isValid(Object entity) {
        if (entity == null) {
            return false;
        }
        if (entity instanceof AsServiceEntity) {
            return Objects.equals(((AsServiceEntity) entity).getIsValid(), 1);
        }
        if (entity instanceof AsStudentEntity) {
            return Objects.equals(((AsStudentEntity) entity).getIsValid(), 1);
        }
        if (entity instanceof AsCourseClassEntity) {
            return Objects.equals(((AsCourseClassEntity) entity).getIsValid(), 1);
        }
        if (entity instanceof AsEnrollmentEntity) {
            return Objects.equals(((AsEnrollmentEntity) entity).getIsValid(), 1);
        }
        if (entity instanceof AsCourseInviteEntity) {
            AsCourseInviteEntity invite = (AsCourseInviteEntity) entity;
            return Objects.equals(invite.getIsValid(), 1)
                    && Objects.equals(invite.getInviteStatus(), 1)
                    && (invite.getExpireTime() == null || invite.getExpireTime().after(new Date()));
        }
        if (entity instanceof AsCourseSessionEntity) {
            return Objects.equals(((AsCourseSessionEntity) entity).getIsValid(), 1);
        }
        return true;
    }

    /** 判断学生是否为当前家长绑定的学生。 */
    public boolean isGuardianStudent(Long studentId) {
        if (studentId == null) {
            return false;
        }
        Long guardianUserId = currentUserId();
        if (guardianUserId == null) {
            return false;
        }
        AsStudentEntity student = asStudentMapper.selectById(studentId);
        if (!isValid(student) || student.getStudentIdentityId() == null) {
            return false;
        }
        LambdaQueryWrapper<AsGuardianStudentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsGuardianStudentEntity::getIsValid, 1)
                .eq(AsGuardianStudentEntity::getBindStatus, 1)
                .eq(AsGuardianStudentEntity::getGuardianUserId, guardianUserId)
                .eq(AsGuardianStudentEntity::getSchoolId, student.getSchoolId())
                .eq(AsGuardianStudentEntity::getStudentIdentityId, student.getStudentIdentityId())
                .last("limit 1");
        return asGuardianStudentMapper.selectOne(wrapper) != null;
    }

    /** 查询当前家长绑定的学生ID列表。 */
    public List<Long> currentGuardianStudentIds() {
        return currentGuardianStudentIds(null);
    }

    /** 查询当前家长在指定学期内可访问的学生ID列表。 */
    public List<Long> currentGuardianStudentIds(Long termId) {
        Long guardianUserId = currentUserId();
        if (guardianUserId == null) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<AsGuardianStudentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsGuardianStudentEntity::getIsValid, 1)
                .eq(AsGuardianStudentEntity::getBindStatus, 1)
                .eq(AsGuardianStudentEntity::getGuardianUserId, guardianUserId);
        Long schoolId = currentSchoolId(null);
        wrapper.eq(schoolId != null, AsGuardianStudentEntity::getSchoolId, schoolId);
        List<Long> identityIds = asGuardianStudentMapper.selectList(wrapper).stream()
                .map(AsGuardianStudentEntity::getStudentIdentityId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (identityIds.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<AsStudentEntity> studentWrapper = new LambdaQueryWrapper<>();
        studentWrapper.in(AsStudentEntity::getStudentIdentityId, identityIds)
                .eq(schoolId != null, AsStudentEntity::getSchoolId, schoolId)
                .eq(termId != null, AsStudentEntity::getTermId, termId)
                .eq(AsStudentEntity::getIsValid, 1);
        return asStudentMapper.selectList(studentWrapper).stream()
                .map(AsStudentEntity::getId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    // ======================== 批量映射 ========================

    public Map<Long, AsStudentEntity> mapStudents(Collection<Long> ids) {
        return mapById(ids, asStudentMapper::selectBatchIds, AsStudentEntity::getId);
    }

    public Map<Long, AsServiceEntity> mapServices(Collection<Long> ids) {
        return mapById(ids, asServiceMapper::selectBatchIds, AsServiceEntity::getId);
    }

    public Map<Long, AsCourseClassEntity> mapCourseClasses(Collection<Long> ids) {
        return mapById(ids, asCourseClassMapper::selectBatchIds, AsCourseClassEntity::getId);
    }

    public Map<Long, AsCourseCategoryEntity> mapCategories(Collection<Long> ids) {
        return mapById(ids, asCourseCategoryMapper::selectBatchIds, AsCourseCategoryEntity::getId);
    }

    public Map<Long, EduclGradeEntity> mapGrades(Collection<Long> ids) {
        return mapById(ids, educlGradeMapper::selectBatchIds, EduclGradeEntity::getId);
    }

    public Map<Long, TEduclClass> mapClasses(Collection<Long> ids) {
        return mapById(ids, educlClassMapper::selectBatchIds, TEduclClass::getId);
    }

    public Map<Long, AsTeacherEntity> mapTeachers(Collection<Long> ids) {
        return mapById(ids, asTeacherMapper::selectBatchIds, AsTeacherEntity::getId);
    }

    public Map<Long, EduclTermEntity> mapTerms(Collection<Long> ids) {
        return mapById(ids, educlTermMapper::selectBatchIds, EduclTermEntity::getId);
    }

    /** 通用按ID批量加载并转为Map。 */
    public <T> Map<Long, T> mapById(Collection<Long> ids, Function<Collection<Long>, List<T>> loader, Function<T, Long> idGetter) {
        List<Long> cleanIds = ids == null ? Collections.emptyList() : ids.stream()
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (cleanIds.isEmpty()) {
            return Collections.emptyMap();
        }
        List<T> rows = loader.apply(cleanIds);
        if (CollectionUtils.isEmpty(rows)) {
            return Collections.emptyMap();
        }
        return rows.stream().collect(Collectors.toMap(idGetter, Function.identity(), (a, b) -> a));
    }

    // ======================== 操作人展示 ========================

    /** 家长操作展示为学生姓名+称谓，避免展示家长姓名。 */
    public String displayOperatorName(AsEnrollmentEntity enrollment, AsStudentEntity student,
                                      Map<String, AsGuardianStudentEntity> guardianRelationMap) {
        if (enrollment == null || !Objects.equals(enrollment.getOperatorType(), CoreConstant.ENROLLMENT_OPERATOR_GUARDIAN)) {
            return enrollment == null ? null : enrollment.getOperatorName();
        }
        AsGuardianStudentEntity relation = guardianRelationMap.get(
                guardianRelationKey(enrollment.getStudentId(), enrollment.getOperatorUserId()));
        if (student != null && StringUtils.hasText(student.getStudentName())
                && relation != null && StringUtils.hasText(relation.getRelationName())) {
            return student.getStudentName() + relation.getRelationName();
        }
        return enrollment.getOperatorName();
    }

    /** 默认操作人名称。 */
    public String defaultOperatorName(Integer operatorType) {
        if (Objects.equals(operatorType, CoreConstant.ENROLLMENT_OPERATOR_GUARDIAN)) {
            return "家长";
        }
        if (Objects.equals(operatorType, CoreConstant.ENROLLMENT_OPERATOR_TEACHER)) {
            return "教师";
        }
        if (Objects.equals(operatorType, CoreConstant.ENROLLMENT_OPERATOR_SYSTEM)) {
            return "系统";
        }
        return "管理员";
    }

    /** 批量查询家长学生绑定关系，用于解析家长操作人展示称谓。 */
    public Map<String, AsGuardianStudentEntity> mapGuardianRelations(List<AsEnrollmentEntity> enrollments) {
        if (CollectionUtils.isEmpty(enrollments)) {
            return Collections.emptyMap();
        }
        List<Long> studentIds = enrollments.stream()
                .filter(e -> Objects.equals(e.getOperatorType(), CoreConstant.ENROLLMENT_OPERATOR_GUARDIAN))
                .map(AsEnrollmentEntity::getStudentId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<Long> guardianUserIds = enrollments.stream()
                .filter(e -> Objects.equals(e.getOperatorType(), CoreConstant.ENROLLMENT_OPERATOR_GUARDIAN))
                .map(AsEnrollmentEntity::getOperatorUserId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (studentIds.isEmpty() || guardianUserIds.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<Long, AsStudentEntity> studentMap = mapStudents(studentIds);
        Map<Long, List<Long>> studentIdsByIdentity = studentMap.values().stream()
                .filter(this::isValid).filter(s -> s.getStudentIdentityId() != null)
                .collect(Collectors.groupingBy(AsStudentEntity::getStudentIdentityId,
                        Collectors.mapping(AsStudentEntity::getId, Collectors.toList())));
        if (studentIdsByIdentity.isEmpty()) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<AsGuardianStudentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsGuardianStudentEntity::getIsValid, 1)
                .eq(AsGuardianStudentEntity::getBindStatus, 1)
                .in(AsGuardianStudentEntity::getStudentIdentityId, studentIdsByIdentity.keySet())
                .in(AsGuardianStudentEntity::getGuardianUserId, guardianUserIds);
        List<AsGuardianStudentEntity> relations = asGuardianStudentMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(relations)) {
            return Collections.emptyMap();
        }
        Map<String, AsGuardianStudentEntity> result = new HashMap<>();
        for (AsGuardianStudentEntity relation : relations) {
            if (relation.getStudentIdentityId() == null || relation.getGuardianUserId() == null) {
                continue;
            }
            for (Long studentId : studentIdsByIdentity.getOrDefault(relation.getStudentIdentityId(), Collections.emptyList())) {
                result.putIfAbsent(guardianRelationKey(studentId, relation.getGuardianUserId()), relation);
            }
        }
        return result;
    }

    public String guardianRelationKey(Long studentId, Long guardianUserId) {
        return studentId + ":" + guardianUserId;
    }

    // ======================== 服务状态验证 ========================

    public boolean isServiceAssignable(AsServiceEntity service) {
        return isValid(service) && (Objects.equals(service.getServiceStatus(), 1) || Objects.equals(service.getServiceStatus(), 2));
    }

    /** 判断服务是否处于家长公开报名窗口内。 */
    public boolean isServiceOpenForGuardianApply(AsServiceEntity service) {
        if (!isValid(service) || !Objects.equals(service.getServiceStatus(), 1)) {
            return false;
        }
        Date now = new Date();
        return (service.getEnrollStartTime() == null || !now.before(service.getEnrollStartTime()))
                && (service.getEnrollEndTime() == null || !now.after(service.getEnrollEndTime()));
    }

    /** 管理端驳回报名沿用报名期窗口。 */
    public boolean isServiceInEnrollmentPeriod(AsServiceEntity service) {
        return isServiceOpenForGuardianApply(service);
    }

    /** 分配窗口：报名已结束，但课程未结束。 */
    public boolean isServiceAssignableWindow(AsServiceEntity service) {
        if (!isValid(service) || Objects.equals(service.getServiceStatus(), CoreConstant.SERVICE_STATUS_FINISHED)) {
            return false;
        }
        Date now = new Date();
        boolean enrollmentClosed = service.getEnrollEndTime() == null || !now.before(service.getEnrollEndTime());
        boolean courseNotEnded = service.getCourseEndDate() == null || !now.after(service.getCourseEndDate());
        return enrollmentClosed && courseNotEnded;
    }

    public String buildAdminRejectRemark(String rejectReason) {
        return StringUtils.hasText(rejectReason) ? "管理端驳回：" + rejectReason : "管理端驳回";
    }

    // ======================== 课程分配验证 ========================

    public String validateCourseAssignable(AsStudentEntity student, AsCourseClassEntity course, Long serviceId) {
        return validateCourseAssignable(student, course, serviceId, true);
    }

    public String validateCourseAssignable(AsStudentEntity student, AsCourseClassEntity course, Long serviceId,
                                            boolean validateCapacity) {
        return validateCourseAssignable(student, course, serviceId, validateCapacity, CourseCapacityPolicy.ENFORCE_LIMIT);
    }

    /** 校验课程班是否可分配。 */
    public String validateCourseAssignable(AsStudentEntity student, AsCourseClassEntity course, Long serviceId,
                                            boolean validateCapacity, CourseCapacityPolicy capacityPolicy) {
        if (!isValid(course)) {
            return "课程班不存在";
        }
        if (!Objects.equals(course.getServiceId(), serviceId)) {
            return "课程班不属于该课后服务";
        }
        if (course.getClassStatus() != null && (course.getClassStatus() == 3 || course.getClassStatus() == 4)) {
            return "课程班已结束或停用";
        }
        if (asCourseQuotaService.isPublic(course)) {
            String quotaError = capacityPolicy == CourseCapacityPolicy.STUDENT_ADJUST
                    ? asCourseQuotaService.unavailableReasonForStudentAdjust(course, student)
                    : asCourseQuotaService.unavailableReason(course, student);
            if (quotaError != null) {
                return quotaError;
            }
        }
        if (capacityPolicy != CourseCapacityPolicy.STUDENT_ADJUST
                && !asCourseQuotaService.isPublic(course) && validateCapacity
                && course.getCapacity() != null && course.getCapacity() > 0
                && course.getEnrolledCount() != null && course.getEnrolledCount() >= course.getCapacity()) {
            return "课程班名额已满";
        }
        if (student != null && course.getGenderRequirement() != null && course.getGenderRequirement() != 0) {
            Integer requiredGender = course.getGenderRequirement() == 1 ? 0 : 1;
            if (!Objects.equals(student.getGender(), requiredGender)) {
                return "学生性别不符合课程要求";
            }
        }
        if (student != null && !courseClassScopeSupport.isStudentInCourseScope(student, course)) {
            List<Long> classIds = courseClassScopeSupport.parseLongList(course.getApplicableClassIds());
            List<Long> gradeIds = courseClassScopeSupport.parseLongList(course.getApplicableGradeIds());
            if (!classIds.isEmpty() || !gradeIds.isEmpty()) {
                return "学生不在课程适用范围内";
            }
        }
        return null;
    }

    /** 校验学生是否在课后服务适用范围内。 */
    public String validateStudentServiceScope(AsServiceEntity service, AsStudentEntity student) {
        if (!isValid(service) || !isValid(student)) {
            return "服务或学生不存在";
        }
        String termScopeError = studentTermScopeSupport.validateStudentInScope(student, serviceScope(service));
        if (termScopeError != null) {
            return termScopeError;
        }
        if (!courseClassScopeSupport.isStudentInServiceScope(
                service.getSchoolId(), service.getId(), student, asCourseClassMapper)) {
            return "学生不在课后服务适用范围内";
        }
        return null;
    }

    public boolean isStudentInServiceScope(AsServiceEntity service, AsStudentEntity student) {
        return validateStudentServiceScope(service, student) == null;
    }

    public boolean isStudentInCourseScope(AsStudentEntity student, AsCourseClassEntity course) {
        return courseClassScopeSupport.isStudentInCourseScope(student, course);
    }

    // ======================== 课程容量管理 ========================

    /** 并发安全增加课程班已报名人数。 */
    public boolean increaseCourseClassCount(Long courseClassId) {
        LambdaUpdateWrapper<AsCourseClassEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AsCourseClassEntity::getId, courseClassId)
                .eq(AsCourseClassEntity::getIsValid, 1)
                .and(w -> w.eq(AsCourseClassEntity::getCapacity, 0)
                        .or().isNull(AsCourseClassEntity::getCapacity)
                        .or().apply("enrolled_count < capacity"))
                .setSql("enrolled_count = IFNULL(enrolled_count, 0) + 1");
        return asCourseClassMapper.update(null, wrapper) > 0;
    }

    /** 原子占用一个名额，接近满员时按学生名单口径校准缓存人数。 */
    public boolean reserveCourseClassSeat(Long courseClassId, boolean capacityRepairLocked) {
        if (!capacityRepairLocked) {
            return increaseCourseClassCount(courseClassId);
        }
        AsCourseClassEntity course = asCourseClassMapper.selectById(courseClassId);
        if (!isValid(course)) {
            return false;
        }
        Integer capacity = course.getCapacity();
        if (capacity == null || capacity <= 0) {
            return increaseCourseClassCount(courseClassId);
        }
        int actualCount = countActualEnrolledStudents(courseClassId);
        if (actualCount >= capacity) {
            return false;
        }
        int storedCount = course.getEnrolledCount() == null ? 0 : course.getEnrolledCount();
        if (storedCount != actualCount
                && !compareAndSetCourseClassCount(courseClassId, storedCount, actualCount)) {
            return false;
        }
        return increaseCourseClassCount(courseClassId);
    }

    public boolean requiresCapacityRepairLock(AsCourseClassEntity course) {
        if (!isValid(course) || course.getCapacity() == null || course.getCapacity() <= 0) {
            return false;
        }
        int enrolledCount = course.getEnrolledCount() == null ? 0 : course.getEnrolledCount();
        return enrolledCount >= Math.max(course.getCapacity() - 1, 0);
    }

    public boolean compareAndSetCourseClassCount(Long courseClassId, int expectedCount, int actualCount) {
        LambdaUpdateWrapper<AsCourseClassEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AsCourseClassEntity::getId, courseClassId)
                .eq(AsCourseClassEntity::getIsValid, 1)
                .apply("IFNULL(enrolled_count, 0) = {0}", expectedCount)
                .set(AsCourseClassEntity::getEnrolledCount, actualCount);
        return asCourseClassMapper.update(null, wrapper) > 0;
    }

    /** 审核调课的独立占位入口，不能复用于普通报名、分配或直接改课。 */
    public void reserveStudentAdjustCourseSeat(AsCourseClassEntity course, AsStudentEntity student) {
        if (asCourseQuotaService.isPublic(course)) {
            asCourseQuotaService.reserveForStudentAdjust(course, student);
            return;
        }
        LambdaUpdateWrapper<AsCourseClassEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AsCourseClassEntity::getId, course.getId())
                .eq(AsCourseClassEntity::getIsValid, 1)
                .eq(AsCourseClassEntity::getEnrollType, CoreConstant.ENROLL_TYPE_INVITE)
                .in(AsCourseClassEntity::getClassStatus, CoreConstant.CLASS_STATUS_NOT_STARTED, CoreConstant.CLASS_STATUS_RUNNING)
                .setSql("enrolled_count = IFNULL(enrolled_count, 0) + 1");
        if (asCourseClassMapper.update(null, wrapper) != 1) {
            throw new BaseBusinessException(ResultCode.PARA_ERROR.code(), "调课占位失败，目标课程已变更，请刷新后重试");
        }
    }

    /** 学生调课目标课按业务要求不受剩余名额限制。 */
    public void increaseCourseClassCountForAdjust(Long courseClassId) {
        if (courseClassId == null) {
            return;
        }
        LambdaUpdateWrapper<AsCourseClassEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AsCourseClassEntity::getId, courseClassId)
                .eq(AsCourseClassEntity::getIsValid, 1)
                .setSql("enrolled_count = IFNULL(enrolled_count, 0) + 1");
        asCourseClassMapper.update(null, wrapper);
    }

    public void decreaseCourseClassCount(Long courseClassId) {
        if (courseClassId == null) {
            return;
        }
        LambdaUpdateWrapper<AsCourseClassEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AsCourseClassEntity::getId, courseClassId)
                .setSql("enrolled_count = CASE WHEN IFNULL(enrolled_count, 0) > 0 THEN enrolled_count - 1 ELSE 0 END");
        asCourseClassMapper.update(null, wrapper);
    }

    public void decreaseCourseClassCount(Long courseClassId, Long studentId) {
        if (courseClassId != null && !asCourseQuotaService.release(courseClassId, studentId)) {
            decreaseCourseClassCount(courseClassId);
        }
    }

    public void lockPublicCourseTransition(Long beforeCourseId, AsCourseClassEntity target) {
        if (asCourseQuotaService.isPublic(target)
                || asCourseQuotaService.isPublic(asCourseClassMapper.selectById(beforeCourseId))) {
            asCourseQuotaService.lockCourses(Arrays.asList(beforeCourseId, target.getId()));
        }
    }


    public int countActualEnrolledStudents(Long courseClassId) {
        LambdaQueryWrapper<AsEnrollmentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsEnrollmentEntity::getIsValid, 1)
                .eq(AsEnrollmentEntity::getCourseClassId, courseClassId)
                .in(AsEnrollmentEntity::getEnrollStatus, CoreConstant.ACTIVE_COURSE_ENROLL_STATUSES);
        List<AsEnrollmentEntity> enrollments = asEnrollmentMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(enrollments)) {
            return 0;
        }
        Set<Long> studentIds = enrollments.stream()
                .map(AsEnrollmentEntity::getStudentId).filter(Objects::nonNull).collect(Collectors.toSet());
        return studentIds.size();
    }


    /** 查询代表选课记录（聚合课次维度）。 */
    public AsEnrollmentEntity getCurrentEnrollment(Long schoolId, Long serviceId, Long studentId) {
        return sessionEnrollmentSupport.findRepresentativeEnrollment(schoolId, serviceId, studentId);
    }

    public AsEnrollmentEntity findCourseEnrollment(Long courseClassId, Long studentId) {
        return sessionEnrollmentSupport.findCourseEnrollment(courseClassId, studentId);
    }

    /** 解析代表选课记录，优先按课程班聚合。 */
    public AsEnrollmentEntity resolveRepresentativeEnrollment(Long enrollmentId) {
        AsEnrollmentEntity enrollment = asEnrollmentMapper.selectById(enrollmentId);
        if (!isValid(enrollment)) {
            return null;
        }
        if (enrollment.getCourseClassId() != null) {
            AsEnrollmentEntity courseEnrollment = findCourseEnrollment(enrollment.getCourseClassId(), enrollment.getStudentId());
            if (courseEnrollment != null) {
                return courseEnrollment;
            }
        }
        return sessionEnrollmentSupport.findRepresentativeEnrollment(
                enrollment.getSchoolId(), enrollment.getServiceId(), enrollment.getStudentId());
    }

    /** 构造选课记录模板。 */
    public AsEnrollmentEntity buildEnrollmentTemplate(AsEnrollmentEntity base, Long courseClassId, Integer source,
                                                       Integer status, Integer operatorType, Long operatorUserId,
                                                       String operatorName, String remark) {
        AsEnrollmentEntity template = new AsEnrollmentEntity();
        template.setSchoolId(base.getSchoolId());
        template.setServiceId(base.getServiceId());
        template.setStudentId(base.getStudentId());
        template.setStudentNo(base.getStudentNo());
        template.setCourseClassId(courseClassId);
        template.setEnrollSource(source);
        template.setEnrollStatus(status);
        template.setParentConfirmRequired(base.getParentConfirmRequired());
        template.setInviteId(base.getInviteId());
        template.setOperatorType(operatorType);
        template.setOperatorUserId(operatorUserId);
        template.setOperatorName(operatorName);
        template.setRemark(remark);
        template.setCancelReason(base.getCancelReason());
        template.setCancelTime(base.getCancelTime());
        return template;
    }

    /** 构造未分配选课记录。 */
    public AsEnrollmentEntity newUnassignedEnrollment(Long schoolId, Long serviceId, Long studentId) {
        AsStudentEntity student = asStudentMapper.selectById(studentId);
        AsEnrollmentEntity enrollment = new AsEnrollmentEntity();
        enrollment.setSchoolId(schoolId);
        enrollment.setServiceId(serviceId);
        enrollment.setStudentId(studentId);
        enrollment.setStudentNo(student == null ? null : student.getStudentNo());
        enrollment.setEnrollStatus(CoreConstant.ENROLL_STATUS_UNASSIGNED);
        enrollment.setIsValid(1);
        return enrollment;
    }


    public Integer normalizeDestinationType(Integer destinationType) {
        return destinationType == null ? CoreConstant.DESTINATION_TYPE_COURSE_CLASS : destinationType;
    }

    public boolean isSupportedNonCourseDestination(Integer destinationType) {
        return Objects.equals(destinationType, CoreConstant.DESTINATION_TYPE_GO_HOME)
                || Objects.equals(destinationType, CoreConstant.DESTINATION_TYPE_TRAINING_INSTITUTION)
                || Objects.equals(destinationType, CoreConstant.DESTINATION_TYPE_OTHER);
    }

    public String buildDestinationRemark(Integer destinationType, String remark) {
        String name = AssembleUtil.getDestinationTypeName(destinationType);
        return StringUtils.hasText(remark) ? name + "：" + remark : name;
    }

    /** 补充分配候选中的固定非课程去向选项。 */
    public void populateNonCourseDestinationOptions(AsEnrollmentAssignOptionsVO vo) {
        if (vo == null) {
            return;
        }
        vo.getDestinationOptions().clear();
        addNonCourseDestinationOption(vo, CoreConstant.DESTINATION_TYPE_TRAINING_INSTITUTION, "校外机构");
        addNonCourseDestinationOption(vo, CoreConstant.DESTINATION_TYPE_GO_HOME, "回家");
    }

    private AsEnrollmentAssignOptionsVO.DestinationOption buildDestinationOption(Integer destinationType, String destinationName) {
        AsEnrollmentAssignOptionsVO.DestinationOption option = new AsEnrollmentAssignOptionsVO.DestinationOption();
        option.setDestinationType(destinationType);
        option.setDestinationName(destinationName);
        return option;
    }

    private void addNonCourseDestinationOption(AsEnrollmentAssignOptionsVO vo, Integer destinationType, String destinationName) {
        vo.getDestinationOptions().add(buildDestinationOption(destinationType, destinationName));
        boolean exists = vo.getCategories().stream()
                .anyMatch(option -> Objects.equals(option.getDestinationType(), destinationType));
        if (exists) {
            return;
        }
        AsEnrollmentAssignOptionsVO.CategoryOption option = new AsEnrollmentAssignOptionsVO.CategoryOption();
        option.setCategoryId(-1L);
        option.setDestinationType(destinationType);
        option.setCategoryName(destinationName);
        vo.getCategories().add(option);
    }

    /** 批量查询学生在服务下最近一次非课程去向。 */
    public Map<String, AsStudentDestinationEntity> mapLatestNonCourseDestinations(List<AsEnrollmentEntity> enrollments) {
        if (CollectionUtils.isEmpty(enrollments)) {
            return Collections.emptyMap();
        }
        List<Long> schoolIds = enrollments.stream().map(AsEnrollmentEntity::getSchoolId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<Long> serviceIds = enrollments.stream().map(AsEnrollmentEntity::getServiceId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<Long> studentIds = enrollments.stream().map(AsEnrollmentEntity::getStudentId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (schoolIds.isEmpty() || serviceIds.isEmpty() || studentIds.isEmpty()) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<AsStudentDestinationEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsStudentDestinationEntity::getIsValid, 1)
                .in(AsStudentDestinationEntity::getSchoolId, schoolIds)
                .in(AsStudentDestinationEntity::getServiceId, serviceIds)
                .in(AsStudentDestinationEntity::getStudentId, studentIds)
                .isNull(AsStudentDestinationEntity::getCourseClassId)
                .in(AsStudentDestinationEntity::getDestinationType, Arrays.asList(
                        CoreConstant.DESTINATION_TYPE_GO_HOME, CoreConstant.DESTINATION_TYPE_TRAINING_INSTITUTION,
                        CoreConstant.DESTINATION_TYPE_OTHER))
                .orderByDesc(AsStudentDestinationEntity::getAssignTime)
                .orderByDesc(AsStudentDestinationEntity::getId);
        List<AsStudentDestinationEntity> destinations = asStudentDestinationMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(destinations)) {
            return Collections.emptyMap();
        }
        Map<String, AsStudentDestinationEntity> result = new HashMap<>();
        for (AsStudentDestinationEntity d : destinations) {
            String key = d.getSchoolId() + "_" + d.getServiceId() + "_" + d.getStudentId();
            result.putIfAbsent(key, d);
        }
        return result;
    }

    /** 查询最近一次非课程去向记录。 */
    public AsStudentDestinationEntity findLatestNonCourseDestination(AsEnrollmentEntity enrollment) {
        if (enrollment == null) {
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

    public AsStudentDestinationEntity resolveLatestNonCourseDestination(
            AsEnrollmentEntity enrollment, Map<String, AsStudentDestinationEntity> nonCourseDestinationMap) {
        if (enrollment == null) {
            return null;
        }
        if (nonCourseDestinationMap != null) {
            String key = enrollment.getSchoolId() + "_" + enrollment.getServiceId() + "_" + enrollment.getStudentId();
            return nonCourseDestinationMap.get(key);
        }
        return findLatestNonCourseDestination(enrollment);
    }

    /** 保存非课程去向记录。 */
    public void saveNonCourseDestinations(AsServiceEntity service, AsStudentEntity student,
                                           List<AsCourseSessionEntity> sessions, Integer destinationType,
                                           Long operatorUserId, Date assignTime) {
        String destinationName = AssembleUtil.getDestinationTypeName(destinationType);
        for (AsCourseSessionEntity session : sessions) {
            LambdaQueryWrapper<AsStudentDestinationEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(AsStudentDestinationEntity::getSchoolId, service.getSchoolId())
                    .eq(AsStudentDestinationEntity::getServiceId, service.getId())
                    .eq(AsStudentDestinationEntity::getStudentId, student.getId())
                    .eq(AsStudentDestinationEntity::getSessionId, session.getId());
            AsStudentDestinationEntity destination = asStudentDestinationMapper.selectOne(wrapper.last("limit 1"));
            if (destination == null) {
                destination = new AsStudentDestinationEntity();
                destination.setSchoolId(service.getSchoolId());
                destination.setServiceId(service.getId());
                destination.setStudentId(student.getId());
                destination.setStudentNo(student.getStudentNo());
                destination.setSessionId(session.getId());
                destination.setCreateUser(operatorUserId);
                destination.setCreateTime(assignTime);
            }
            destination.setIsValid(1);
            destination.setDestinationType(destinationType);
            destination.setCourseClassId(null);
            destination.setDestinationName(destinationName);
            destination.setAssignUserId(operatorUserId);
            destination.setAssignTime(assignTime);
            destination.setUpdateUser(operatorUserId);
            destination.setUpdateTime(assignTime);
            if (destination.getId() == null) {
                asStudentDestinationMapper.insert(destination);
            } else {
                asStudentDestinationMapper.updateById(destination);
            }
        }
    }


    /** 为列表行补充课后去向信息。 */
    public void fillDestinationInfo(AsEnrollmentSelectionRowVO row, AsEnrollmentEntity enrollment,
                                     Map<String, AsStudentDestinationEntity> nonCourseDestinationMap) {
        if (row == null || enrollment == null) {
            return;
        }
        if (enrollment.getCourseClassId() != null) {
            row.setDestinationType(CoreConstant.DESTINATION_TYPE_COURSE_CLASS);
            row.setDestinationName(AssembleUtil.getDestinationTypeName(CoreConstant.DESTINATION_TYPE_COURSE_CLASS));
            return;
        }
        String key = enrollment.getSchoolId() + "_" + enrollment.getServiceId() + "_" + enrollment.getStudentId();
        AsStudentDestinationEntity destination = nonCourseDestinationMap.get(key);
        if (destination != null) {
            row.setDestinationType(destination.getDestinationType());
            row.setDestinationName(destination.getDestinationName());
        }
    }


    // ======================== 转换与展示 ========================

    public AsEnrollmentOperateResultVO toOperateResult(AsEnrollmentEntity enrollment) {
        AsEnrollmentOperateResultVO vo = new AsEnrollmentOperateResultVO();
        vo.setEnrollmentId(enrollment.getId());
        vo.setCourseClassId(enrollment.getCourseClassId());
        vo.setEnrollStatus(enrollment.getEnrollStatus());
        vo.setEnrollStatusName(AssembleUtil.getEnrollStatusName(enrollment.getEnrollStatus()));
        return vo;
    }


    public AsEnrollmentExportExcel toExportExcel(AsEnrollmentSelectionRowVO row, boolean unselected) {
        AsEnrollmentExportExcel excel = new AsEnrollmentExportExcel();
        if (!unselected) {
            excel.setCourseClassName(row.getCourseClassName());
            excel.setCategoryName(row.getCategoryName());
            excel.setEnrollTypeName(row.getEnrollTypeName());
            excel.setOperatorName(row.getOperatorName());
            excel.setEnrollTime(formatDate(row.getEnrollTime()));
        } else {
            excel.setUnregisteredScheduleText(row.getUnregisteredScheduleText());
            excel.setDestinationName(row.getDestinationName());
        }
        excel.setStudentNo(row.getStudentNo());
        excel.setStudentName(row.getStudentName());
        excel.setGradeName(row.getGradeName());
        excel.setClassName(row.getClassName());
        excel.setTermName(row.getTermName());
        return excel;
    }

    public String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public String formatTime(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat("HH:mm").format(date);
    }

    public String toAvailableStatus(String reason) {
        if (!StringUtils.hasText(reason)) {
            return "available";
        }
        if (reason.contains("名额已满")) {
            return "full";
        }
        if (reason.contains("性别")) {
            return "gender_limited";
        }
        if (reason.contains("范围") || reason.contains("学期") || reason.contains("学校")) {
            return "not_scope";
        }
        if (reason.contains("结束") || reason.contains("停用")) {
            return "disabled";
        }
        return "disabled";
    }

    public String toStatusName(String reason) {
        if (!StringUtils.hasText(reason)) {
            return "可报名";
        }
        if (reason.contains("名额已满")) {
            return "已满";
        }
        if (reason.contains("结束") || reason.contains("停用")) {
            return "不可报名";
        }
        return "不可报名";
    }

    public AsEnrollmentFilterOptionsVO.Option option(Long id, Integer value, String label) {
        AsEnrollmentFilterOptionsVO.Option option = new AsEnrollmentFilterOptionsVO.Option();
        option.setId(id);
        option.setValue(value);
        option.setLabel(label);
        return option;
    }


    public <E> PageResultDTO<E> emptyPageResult(int pageIndex, int pageSize) {
        return toManualPageResult(pageIndex, pageSize, Collections.emptyList());
    }

    public <E> PageResultDTO<E> toManualPageResult(int pageIndex, int pageSize, List<E> list) {
        int safePageIndex = pageIndex <= 0 ? 1 : pageIndex;
        int safePageSize = pageSize <= 0 ? 20 : pageSize;
        int total = list == null ? 0 : list.size();
        int from = Math.min((safePageIndex - 1) * safePageSize, total);
        int to = Math.min(from + safePageSize, total);
        PageResultDTO<E> result = new PageResultDTO<>();
        result.setTotal((long) total);
        result.setPages((long) Math.ceil(total * 1.0 / safePageSize));
        result.setPageIndex((long) safePageIndex);
        result.setPageSize((long) safePageSize);
        result.setList(list == null ? Collections.emptyList() : list.subList(from, to));
        return result;
    }

    public <E> PageResultDTO<E> buildManualPageResult(int pageIndex, int pageSize, long total, List<E> pageList) {
        int safePageIndex = pageIndex <= 0 ? 1 : pageIndex;
        int safePageSize = pageSize <= 0 ? 20 : pageSize;
        PageResultDTO<E> result = new PageResultDTO<>();
        result.setTotal(total);
        result.setPages((long) Math.ceil(total * 1.0 / safePageSize));
        result.setPageIndex((long) safePageIndex);
        result.setPageSize((long) safePageSize);
        result.setList(pageList == null ? Collections.emptyList() : pageList);
        return result;
    }

    // ======================== 学期与范围 ========================

    public String resolveTermName(Long termId) {
        if (termId == null) {
            return null;
        }
        EduclTermEntity term = educlTermMapper.selectById(termId);
        return term == null ? null : term.getTermName();
    }

    public List<EduclTermEntity> listCurrentTerms(Long schoolId) {
        Date now = new Date();
        LambdaQueryWrapper<EduclTermEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduclTermEntity::getIsValid, 1)
                .eq(schoolId != null, EduclTermEntity::getSchoolId, schoolId)
                .le(EduclTermEntity::getStartDate, now)
                .ge(EduclTermEntity::getEndDate, now);
        List<EduclTermEntity> terms = educlTermMapper.selectList(wrapper);
        return terms == null ? Collections.emptyList() : terms;
    }

    public StudentTermScope serviceScope(AsServiceEntity service) {
        return new StudentTermScope(service == null ? null : service.getSchoolId(),
                service == null ? null : service.getTermId(),
                service == null ? null : service.getId(), null, null);
    }

    public List<AsServiceEntity> listCurrentTermAssignableServices(Long schoolId, List<Long> termIds) {
        if (CollectionUtils.isEmpty(termIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<AsServiceEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsServiceEntity::getIsValid, 1)
                .eq(schoolId != null, AsServiceEntity::getSchoolId, schoolId)
                .in(AsServiceEntity::getTermId, termIds)
                .in(AsServiceEntity::getServiceStatus, Arrays.asList(1, 2))
                .and(w -> w.isNull(AsServiceEntity::getCourseEndDate)
                        .or().ge(AsServiceEntity::getCourseEndDate, new Date()));
        List<AsServiceEntity> services = asServiceMapper.selectList(wrapper);
        if (services == null) {
            return Collections.emptyList();
        }
        return services.stream().filter(this::isServiceAssignableWindow).collect(Collectors.toList());
    }

    public List<AsStudentEntity> listStudentsInServiceScope(AsServiceEntity service) {
        List<Long> classIds = courseClassScopeSupport.listServiceScopeClassIds(
                service.getSchoolId(), service.getId(), asCourseClassMapper);
        if (classIds.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<AsStudentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsStudentEntity::getIsValid, 1)
                .eq(AsStudentEntity::getSchoolId, service.getSchoolId())
                .eq(AsStudentEntity::getTermId, service.getTermId())
                .in(AsStudentEntity::getClassId, classIds);
        return asStudentMapper.selectList(wrapper);
    }

    // ======================== 分配候选 ========================

    public void fillAssignOptionsTerm(AsEnrollmentAssignOptionsVO vo, AsServiceEntity service) {
        if (vo == null || !isValid(service)) {
            return;
        }
        vo.setTermId(service.getTermId());
        vo.setTermName(resolveTermName(service.getTermId()));
    }

    public void fillAssignableCourseTerm(AsAssignableCourseClassVO vo, AsServiceEntity service) {
        if (vo == null || !isValid(service)) {
            return;
        }
        vo.setTermId(service.getTermId());
        vo.setTermName(resolveTermName(service.getTermId()));
    }


    public AsCourseInviteEntity findInviteByCourseAndStudent(Long courseClassId, Long studentId) {
        LambdaQueryWrapper<AsCourseInviteEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsCourseInviteEntity::getCourseClassId, courseClassId)
                .eq(AsCourseInviteEntity::getIsValid, 1)
                .eq(AsCourseInviteEntity::getInviteStatus, 1);
        List<AsCourseInviteEntity> invites = asCourseInviteMapper.selectList(wrapper);
        for (AsCourseInviteEntity invite : invites) {
            if (parseLongList(invite.getInviteStudentIds()).contains(studentId)) {
                return invite;
            }
        }
        return null;
    }

    public List<Long> parseLongList(String json) {
        if (!StringUtils.hasText(json)) {
            return Collections.emptyList();
        }
        String cleaned = json.replace("[", "").replace("]", "").replace("\"", "");
        List<Long> result = new ArrayList<>();
        for (String part : cleaned.split(",")) {
            if (StringUtils.hasText(part.trim())) {
                try {
                    result.add(Long.valueOf(part.trim()));
                } catch (NumberFormatException ignore) {
                    log.debug("ignore invalid long value: {}", part);
                }
            }
        }
        return result;
    }

    // ======================== 键构建辅助 ========================

    /** 服务学生维度键。 */
    public String serviceStudentKey(AsEnrollmentEntity enrollment) {
        return enrollment == null ? null : serviceStudentKey(enrollment.getSchoolId(), enrollment.getServiceId(), enrollment.getStudentId());
    }

    public String serviceStudentKey(Long schoolId, Long serviceId, Long studentId) {
        return schoolId + "_" + serviceId + "_" + studentId;
    }


    public String serviceStudentDestinationKey(Long schoolId, Long serviceId, Long studentId, Integer destinationType) {
        return schoolId + "_" + serviceId + "_" + studentId + "_" + destinationType;
    }

    /** 判断学生是否为已注册学生。 */
    public boolean isApprovedStudent(AsStudentEntity student) {
        return student != null && Objects.equals(student.getRegisterStatus(), CoreConstant.STUDENT_REGISTER_STATUS_APPROVED);
    }


    /** 已选课组班按学生、服务和课程班保留一条代表课次。 */
    public List<AsEnrollmentEntity> listSelectedCourseRepresentativeEnrollments(List<AsEnrollmentEntity> enrollments) {
        if (CollectionUtils.isEmpty(enrollments)) {
            return Collections.emptyList();
        }
        LinkedHashMap<String, AsEnrollmentEntity> representativeMap = new LinkedHashMap<>();
        for (AsEnrollmentEntity enrollment : enrollments) {
            if (enrollment == null) {
                continue;
            }
            String key = enrollment.getStudentId() + ":" + enrollment.getServiceId() + ":" + enrollment.getCourseClassId();
            representativeMap.putIfAbsent(key, enrollment);
        }
        return new ArrayList<>(representativeMap.values());
    }

    /** 合并展示文本去重。 */
    public List<String> mergeTextList(Collection<String> first, Collection<String> second) {
        LinkedHashSet<String> values = new LinkedHashSet<>();
        if (!CollectionUtils.isEmpty(first)) {
            first.stream().filter(StringUtils::hasText).forEach(values::add);
        }
        if (!CollectionUtils.isEmpty(second)) {
            second.stream().filter(StringUtils::hasText).forEach(values::add);
        }
        return new ArrayList<>(values);
    }



    /**
     * 分页查询选课记录行：统一排序（学号升序）后内存分页。
     */
    public PageResultDTO<AsEnrollmentSelectionRowVO> pageSelection(AsEnrollmentSelectionPageQueryAO query,
                                                                    LambdaQueryWrapper<AsEnrollmentEntity> wrapper,
                                                                    boolean selected) {
        return pageSelection(query, wrapper, selected, false);
    }

    /**
     * 分页查询选课记录行，可选限制仅统计已进入分配窗口的服务。
     */
    public PageResultDTO<AsEnrollmentSelectionRowVO> pageSelection(AsEnrollmentSelectionPageQueryAO query,
                                                                    LambdaQueryWrapper<AsEnrollmentEntity> wrapper,
                                                                    boolean selected,
                                                                    boolean requireAssignableServiceWindow) {
        List<AsEnrollmentSelectionRowVO> rows = listSelectionRows(query, wrapper, selected, requireAssignableServiceWindow);
        rows.sort(Comparator.comparing(AsEnrollmentSelectionRowVO::getStudentNo, Comparator.nullsLast(Comparator.naturalOrder())));
        return toManualPageResult(query.getPageIndex(), query.getPageSize(), rows);
    }


    /** 统计选课记录行数，可选限制仅统计已进入分配窗口的服务。 */
    public long countSelectionRows(AsEnrollmentSelectionPageQueryAO query, boolean selected,
                                    boolean requireAssignableServiceWindow) {
        LambdaQueryWrapper<AsEnrollmentEntity> wrapper = selectionStatusWrapper(query, selected);
        return listSelectionRows(query, wrapper, selected, requireAssignableServiceWindow).size();
    }

    /**
     * 通用选课行组装链：去重代表记录 → 分配窗口过滤 → 课程条件过滤 → 行构建 → 行政班动态名单合并 → 查询条件过滤。
     */
    public List<AsEnrollmentSelectionRowVO> listSelectionRows(AsEnrollmentSelectionPageQueryAO query,
                                                               LambdaQueryWrapper<AsEnrollmentEntity> wrapper,
                                                               boolean selected,
                                                               boolean requireAssignableServiceWindow) {
        List<AsEnrollmentEntity> queriedEnrollments = asEnrollmentMapper.selectList(wrapper);
        List<AsEnrollmentEntity> enrollments = selected
                ? listSelectedCourseRepresentativeEnrollments(queriedEnrollments)
                : sessionEnrollmentSupport.listDistinctRepresentativeEnrollments(queriedEnrollments);
        enrollments = filterUnselectedEnrollmentsByAssignableServiceWindow(enrollments, selected, requireAssignableServiceWindow);
        enrollments = filterUnselectedEnrollmentsByCourseCriteria(enrollments, query, selected);
        List<AsEnrollmentSelectionRowVO> rows = selected
                ? buildRows(enrollments, true)
                : buildUnselectedRows(enrollments, requireAssignableServiceWindow);
        rows = mergeApprovedClassAssignRosterRows(query, rows, selected);
        return rows.stream()
                .filter(row -> matchSelectionQuery(row, query, selected))
                .collect(Collectors.toList());
    }

    /**
     * 统一管理端和教师端选课列表的学校与学期范围。
     */
    public void normalizeSelectionQueryTerm(AsEnrollmentSelectionPageQueryAO query) {
        Long schoolId = currentSchoolId(query.getSchoolId());
        query.setSchoolId(schoolId);
        if (query.getServiceId() != null) {
            StudentTermScope serviceScope =
                    studentTermScopeSupport.requireServiceScope(query.getServiceId());
            studentTermScopeSupport.requireTermInSchool(
                    schoolId, serviceScope.getTermId());
            if (query.getTermId() != null
                    && !Objects.equals(query.getTermId(), serviceScope.getTermId())) {
                throw new BaseBusinessException(ResultCode.PARA_ERROR.code(),
                        "查询学期与课后服务所属学期不一致");
            }
            query.setTermId(serviceScope.getTermId());
            return;
        }
        EduclTermEntity term = query.getTermId() == null
                ? studentTermScopeSupport.resolveReadableTerm(schoolId)
                : studentTermScopeSupport.requireTermInSchool(
                        schoolId, query.getTermId());
        query.setTermId(term.getId());
    }

    /**
     * 选课状态查询条件：已选口径绑定课程班且状态有效；未选口径兼容未分配、非课程去向和有效课程三种形态。
     */
    public LambdaQueryWrapper<AsEnrollmentEntity> selectionStatusWrapper(AsEnrollmentSelectionPageQueryAO query, boolean selected) {
        LambdaQueryWrapper<AsEnrollmentEntity> wrapper = baseEnrollmentQuery(query, selected);
        if (selected) {
            wrapper.isNotNull(AsEnrollmentEntity::getCourseClassId);
            wrapper.isNotNull(AsEnrollmentEntity::getSessionId);
            wrapper.in(AsEnrollmentEntity::getEnrollStatus, CoreConstant.ACTIVE_COURSE_ENROLL_STATUSES);
        } else {
            wrapper.and(status -> status
                    .and(unassigned -> unassigned.eq(AsEnrollmentEntity::getEnrollStatus, CoreConstant.ENROLL_STATUS_UNASSIGNED)
                            .isNull(AsEnrollmentEntity::getSessionId)
                            // 排除已被邀请但家长拒绝的记录，避免再次作为可分配学生出现。
                            .and(invite -> invite.isNull(AsEnrollmentEntity::getInviteId)
                                    .or().ne(AsEnrollmentEntity::getEnrollSource, CoreConstant.ENROLL_SOURCE_INVITE)))
                    .or(nonCourse -> nonCourse.eq(AsEnrollmentEntity::getEnrollStatus, CoreConstant.ENROLL_STATUS_ASSIGNED)
                            .eq(AsEnrollmentEntity::getEnrollSource, CoreConstant.ENROLL_SOURCE_MANUAL_ASSIGN)
                            .isNull(AsEnrollmentEntity::getCourseClassId)
                            .isNull(AsEnrollmentEntity::getSessionId))
                    .or(course -> course.in(AsEnrollmentEntity::getEnrollStatus, CoreConstant.ACTIVE_COURSE_ENROLL_STATUSES)
                            .isNotNull(AsEnrollmentEntity::getCourseClassId)
                            .isNotNull(AsEnrollmentEntity::getSessionId)));
        }
        wrapper.orderByDesc(AsEnrollmentEntity::getId);
        return wrapper;
    }

    /** 基础报名查询条件（默认应用课程班过滤）。 */
    public LambdaQueryWrapper<AsEnrollmentEntity> baseEnrollmentQuery(AsEnrollmentSelectionPageQueryAO query) {
        return baseEnrollmentQuery(query, true);
    }

    /** 基础报名查询条件：学校 + 学期（转换为服务范围）+ 可选服务与课程班。 */
    public LambdaQueryWrapper<AsEnrollmentEntity> baseEnrollmentQuery(AsEnrollmentSelectionPageQueryAO query, boolean applyCourseClassFilter) {
        LambdaQueryWrapper<AsEnrollmentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsEnrollmentEntity::getIsValid, 1);
        Long schoolId = currentSchoolId(query.getSchoolId());
        if (schoolId != null && schoolId > 0) {
            wrapper.eq(AsEnrollmentEntity::getSchoolId, schoolId);
        }
        applyEnrollmentTermFilter(wrapper, query.getTermId(), schoolId);
        if (query.getServiceId() != null) {
            wrapper.eq(AsEnrollmentEntity::getServiceId, query.getServiceId());
        }
        if (applyCourseClassFilter && query.getCourseClassId() != null) {
            wrapper.eq(AsEnrollmentEntity::getCourseClassId, query.getCourseClassId());
        }
        return wrapper;
    }

    /**
     * 将学期筛选转换为课后服务范围，报名表结构和原有状态条件保持不变。
     */
    private void applyEnrollmentTermFilter(LambdaQueryWrapper<AsEnrollmentEntity> enrollmentWrapper,
                                           Long termId, Long schoolId) {
        if (termId == null || termId <= 0) {
            return;
        }
        LambdaQueryWrapper<AsServiceEntity> serviceWrapper = new LambdaQueryWrapper<>();
        serviceWrapper.eq(AsServiceEntity::getTermId, termId)
                .eq(schoolId != null && schoolId > 0, AsServiceEntity::getSchoolId, schoolId);
        List<Long> serviceIds = asServiceMapper.selectList(serviceWrapper).stream()
                .map(AsServiceEntity::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (serviceIds.isEmpty()) {
            enrollmentWrapper.eq(AsEnrollmentEntity::getServiceId, -1L);
            return;
        }
        enrollmentWrapper.in(AsEnrollmentEntity::getServiceId, serviceIds);
    }


    /** 未选口径下仅保留已进入分配窗口的服务记录。 */
    private List<AsEnrollmentEntity> filterUnselectedEnrollmentsByAssignableServiceWindow(List<AsEnrollmentEntity> enrollments,
                                                                                          boolean selected,
                                                                                          boolean requireAssignableServiceWindow) {
        if (selected || !requireAssignableServiceWindow || CollectionUtils.isEmpty(enrollments)) {
            return enrollments;
        }
        Map<Long, AsServiceEntity> serviceMap = mapServices(enrollments.stream()
                .map(AsEnrollmentEntity::getServiceId)
                .collect(Collectors.toList()));
        return enrollments.stream()
                .filter(enrollment -> isServiceAssignableWindow(serviceMap.get(enrollment.getServiceId())))
                .collect(Collectors.toList());
    }

    /**
     * 未分配记录没有课程班ID，课程维度筛选需要落到服务下可分配课程和学生适用范围上。
     */
    private List<AsEnrollmentEntity> filterUnselectedEnrollmentsByCourseCriteria(List<AsEnrollmentEntity> enrollments,
                                                                                 AsEnrollmentSelectionPageQueryAO query,
                                                                                 boolean selected) {
        if (selected || CollectionUtils.isEmpty(enrollments)
                || (query.getCourseClassId() == null && query.getEnrollType() == null)) {
            return enrollments;
        }
        Map<Long, AsStudentEntity> studentMap = mapStudents(enrollments.stream()
                .map(AsEnrollmentEntity::getStudentId)
                .collect(Collectors.toList()));
        if (query.getCourseClassId() != null) {
            AsCourseClassEntity course = asCourseClassMapper.selectById(query.getCourseClassId());
            if (!isAssignableCourseForUnselectedFilter(course, query.getEnrollType())) {
                return Collections.emptyList();
            }
            return enrollments.stream()
                    .filter(enrollment -> Objects.equals(enrollment.getServiceId(), course.getServiceId()))
                    .filter(enrollment -> isStudentInCourseScope(studentMap.get(enrollment.getStudentId()), course))
                    .collect(Collectors.toList());
        }
        List<Long> serviceIds = enrollments.stream()
                .map(AsEnrollmentEntity::getServiceId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (serviceIds.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<AsCourseClassEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsCourseClassEntity::getIsValid, 1)
                .in(AsCourseClassEntity::getServiceId, serviceIds)
                .eq(AsCourseClassEntity::getEnrollType, query.getEnrollType())
                .in(AsCourseClassEntity::getClassStatus, Arrays.asList(1, 2));
        List<AsCourseClassEntity> courses = asCourseClassMapper.selectList(wrapper);
        Map<Long, List<AsCourseClassEntity>> courseMap = courses == null ? Collections.emptyMap() : courses.stream()
                .filter(course -> course.getServiceId() != null)
                .collect(Collectors.groupingBy(AsCourseClassEntity::getServiceId));
        if (courseMap.isEmpty()) {
            return Collections.emptyList();
        }
        return enrollments.stream()
                .filter(enrollment -> hasMatchedCourseForStudent(enrollment, studentMap.get(enrollment.getStudentId()), courseMap))
                .collect(Collectors.toList());
    }

    private boolean isAssignableCourseForUnselectedFilter(AsCourseClassEntity course, Integer enrollType) {
        return isValid(course)
                && (course.getClassStatus() == null || Arrays.asList(1, 2).contains(course.getClassStatus()))
                && (enrollType == null || Objects.equals(course.getEnrollType(), enrollType));
    }

    private boolean hasMatchedCourseForStudent(AsEnrollmentEntity enrollment, AsStudentEntity student,
                                               Map<Long, List<AsCourseClassEntity>> courseMap) {
        List<AsCourseClassEntity> courses = courseMap.get(enrollment.getServiceId());
        if (CollectionUtils.isEmpty(courses)) {
            return false;
        }
        return courses.stream().anyMatch(course -> isStudentInCourseScope(student, course));
    }

    /** 应用年级、班级和关键词过滤。 */
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

    /**
     * 构建已选课列表行，批量映射学生、服务、课程班、类别、年级、班级、教师及家长关系。
     */
    private List<AsEnrollmentSelectionRowVO> buildRows(List<AsEnrollmentEntity> enrollments, boolean selected) {
        if (CollectionUtils.isEmpty(enrollments)) {
            return Collections.emptyList();
        }
        Map<Long, AsStudentEntity> studentMap = mapStudents(enrollments.stream().map(AsEnrollmentEntity::getStudentId).collect(Collectors.toList()));
        Map<Long, AsServiceEntity> serviceMap = mapServices(enrollments.stream().map(AsEnrollmentEntity::getServiceId).collect(Collectors.toList()));
        Map<Long, EduclTermEntity> termMap = mapTerms(serviceMap.values().stream()
                .map(AsServiceEntity::getTermId).collect(Collectors.toList()));
        Map<Long, AsCourseClassEntity> courseMap = mapCourseClasses(enrollments.stream().map(AsEnrollmentEntity::getCourseClassId).collect(Collectors.toList()));
        Map<Long, AsCourseCategoryEntity> categoryMap = mapCategories(courseMap.values().stream().map(AsCourseClassEntity::getCategoryId).collect(Collectors.toList()));
        Map<Long, EduclGradeEntity> gradeMap = mapGrades(studentMap.values().stream().map(AsStudentEntity::getGradeId).collect(Collectors.toList()));
        Map<Long, TEduclClass> classMap = mapClasses(studentMap.values().stream().map(AsStudentEntity::getClassId).collect(Collectors.toList()));
        Map<String, AsGuardianStudentEntity> guardianRelationMap = mapGuardianRelations(enrollments);
        Map<String, AsStudentDestinationEntity> nonCourseDestinationMap = mapLatestNonCourseDestinations(enrollments);
        Map<String, Integer> learnedSessionCountMap = selected
                ? calculateStudentLearnedSessions(enrollments) : Collections.emptyMap();

        List<AsEnrollmentSelectionRowVO> rows = new ArrayList<>();
        for (AsEnrollmentEntity enrollment : enrollments) {
            AsStudentEntity student = studentMap.get(enrollment.getStudentId());
            AsCourseClassEntity course = courseMap.get(enrollment.getCourseClassId());
            if (!isVisibleCourseRosterStudent(student, course)) {
                continue;
            }
            AsServiceEntity service = serviceMap.get(enrollment.getServiceId());
            if (service == null || !Objects.equals(student.getTermId(), service.getTermId())) {
                continue;
            }
            AsCourseCategoryEntity category = course == null ? null : categoryMap.get(course.getCategoryId());
            AsEnrollmentSelectionRowVO row = new AsEnrollmentSelectionRowVO();
            row.setEnrollmentId(enrollment.getId());
            SmartBeanUtil.copyProperties(enrollment, row);
            row.setServiceName(service == null ? null : service.getServiceName());
            EduclTermEntity term = service == null ? null : termMap.get(service.getTermId());
            row.setTermId(service == null ? null : service.getTermId());
            row.setTermName(term == null ? null : term.getTermName());
            row.setCourseClassName(course == null ? null : course.getCourseClassName());
            row.setCategoryId(course == null ? null : course.getCategoryId());
            row.setCategoryName(category == null ? null : category.getCategoryName());
            row.setEnrollType(course == null ? null : course.getEnrollType());
            row.setEnrollTypeName(course == null ? null : AssembleUtil.getEnrollTypeName(course.getEnrollType()));
            row.setEnrollSourceName(AssembleUtil.getEnrollSourceName(enrollment.getEnrollSource()));
            row.setEnrollStatusName(AssembleUtil.getEnrollStatusName(enrollment.getEnrollStatus()));
            if (!selected) {
                fillDestinationInfo(row, enrollment, nonCourseDestinationMap);
            }
            if (student != null) {
                row.setStudentId(student.getId());
                row.setStudentNo(student.getStudentNo());
                row.setStudentName(student.getStudentName());
                row.setGradeId(student.getGradeId());
                row.setClassId(student.getClassId());
                row.setGender(student.getGender());
                EduclGradeEntity grade = gradeMap.get(student.getGradeId());
                TEduclClass clazz = classMap.get(student.getClassId());
                row.setGradeName(grade == null ? null : grade.getGradeName());
                row.setClassName(clazz == null ? null : clazz.getClassName());
            }
            row.setOperatorName(displayOperatorName(enrollment, student, guardianRelationMap));
            row.setEnrollTime(enrollment.getEnrollTime());
            row.setLearnedSessions(selected
                    ? learnedSessionCountMap.getOrDefault(timeSlotService.studentCourseKey(enrollment.getStudentId(), enrollment.getCourseClassId()), 0)
                    : 0);
            row.setCanDetail(true);
            row.setCanAssign(!selected && enrollment.getCourseClassId() == null);
            row.setCanEdit(selected);
            rows.add(row);
        }
        return rows;
    }


    /**
     * 构建未选课列表行：按课后服务剩余时间计算，学生已有课程或非课程去向未覆盖全部时段时仍可继续分配。
     */
    public List<AsEnrollmentSelectionRowVO> buildUnselectedRows(List<AsEnrollmentEntity> enrollments,
                                                                 boolean adminUnselectedMode) {
        return buildUnselectedRows(enrollments, adminUnselectedMode, null);
    }

    /**
     * 构建未选课列表，并允许统计场景复用已经加载的学期占用时段。
     */
    public List<AsEnrollmentSelectionRowVO> buildUnselectedRows(
            List<AsEnrollmentEntity> enrollments, boolean adminUnselectedMode,
            Map<String, Set<String>> preloadedOccupiedSlotKeyMap) {
        if (CollectionUtils.isEmpty(enrollments)) {
            return Collections.emptyList();
        }
        Map<Long, AsStudentEntity> studentMap = mapStudents(enrollments.stream()
                .map(AsEnrollmentEntity::getStudentId)
                .collect(Collectors.toList()));
        Map<Long, AsServiceEntity> serviceMap = mapServices(enrollments.stream()
                .map(AsEnrollmentEntity::getServiceId)
                .collect(Collectors.toList()));
        Map<Long, EduclTermEntity> termMap = mapTerms(serviceMap.values().stream()
                .map(AsServiceEntity::getTermId).collect(Collectors.toList()));
        Map<Long, EduclGradeEntity> gradeMap = mapGrades(studentMap.values().stream()
                .map(AsStudentEntity::getGradeId)
                .collect(Collectors.toList()));
        Map<Long, TEduclClass> classMap = mapClasses(studentMap.values().stream()
                .map(AsStudentEntity::getClassId)
                .collect(Collectors.toList()));
        Map<String, AsGuardianStudentEntity> guardianRelationMap = mapGuardianRelations(enrollments);
        Map<String, AsStudentDestinationEntity> nonCourseDestinationMap = mapLatestNonCourseDestinations(enrollments);
        List<Long> serviceIds = enrollments.stream()
                .map(AsEnrollmentEntity::getServiceId)
                .collect(Collectors.toList());
        Map<Long, List<EnrollmentTimeSlot>> serviceTimeSlotMap = adminUnselectedMode
                ? timeSlotService.mapAssignableConcreteServiceTimeSlots(serviceIds, serviceMap)
                : timeSlotService.mapServiceTimeSlots(serviceIds, serviceMap);
        Map<String, List<EnrollmentTimeSlot>> nonCourseDestinationTimeSlotMap = adminUnselectedMode
                ? mapAdminUnselectedNonCourseTimeSlots(enrollments)
                : Collections.emptyMap();
        Map<String, Set<String>> occupiedSlotKeyMap = preloadedOccupiedSlotKeyMap != null
                ? preloadedOccupiedSlotKeyMap
                : adminUnselectedMode
                ? timeSlotService.mapStudentTermOccupiedSlotKeys(enrollments, serviceMap, true)
                : mapStudentCourseSlotKeysFromCurrentEnrollments(enrollments);

        List<AsEnrollmentSelectionRowVO> rows = new ArrayList<>();
        for (AsEnrollmentEntity enrollment : enrollments) {
            AsStudentEntity student = studentMap.get(enrollment.getStudentId());
            if (!isApprovedStudent(student)) {
                continue;
            }
            AsServiceEntity service = serviceMap.get(enrollment.getServiceId());
            if (service == null || !Objects.equals(student.getTermId(), service.getTermId())) {
                continue;
            }
            List<EnrollmentTimeSlot> remainingSlots = timeSlotService.findUnregisteredTimeSlots(
                    enrollment.getSchoolId(), enrollment.getServiceId(), enrollment.getStudentId(),
                    student, serviceTimeSlotMap, occupiedSlotKeyMap, adminUnselectedMode);
            AsStudentDestinationEntity nonCourseDestination = nonCourseDestinationMap.get(serviceStudentKey(enrollment));
            boolean hasNonCourseDestination = isSelectionNonCourseDestination(nonCourseDestination);
            boolean unassignedPlaceholder = enrollment.getCourseClassId() == null
                    && enrollment.getSessionId() == null
                    && Objects.equals(enrollment.getEnrollStatus(), CoreConstant.ENROLL_STATUS_UNASSIGNED);
            if (adminUnselectedMode) {
                if (remainingSlots.isEmpty() && !hasNonCourseDestination) {
                    continue;
                }
            } else if (remainingSlots.isEmpty() && !unassignedPlaceholder) {
                continue;
            }
            AsEnrollmentSelectionRowVO row = new AsEnrollmentSelectionRowVO();
            row.setEnrollmentId(enrollment.getId());
            SmartBeanUtil.copyProperties(enrollment, row);
            row.setServiceName(null);
            EduclTermEntity term = service == null ? null : termMap.get(service.getTermId());
            row.setTermId(service == null ? null : service.getTermId());
            row.setTermName(term == null ? null : term.getTermName());
            row.setCourseClassId(null);
            row.setCourseClassName(null);
            row.setCategoryId(null);
            row.setCategoryName(null);
            row.setEnrollType(null);
            row.setEnrollTypeName(null);
            row.setEnrollSourceName(AssembleUtil.getEnrollSourceName(enrollment.getEnrollSource()));
            row.setEnrollStatusName(AssembleUtil.getEnrollStatusName(enrollment.getEnrollStatus()));
            fillNonCourseDestinationInfo(row, nonCourseDestination);
            row.setStudentId(student.getId());
            row.setStudentNo(student.getStudentNo());
            row.setStudentName(student.getStudentName());
            row.setGradeId(student.getGradeId());
            row.setClassId(student.getClassId());
            row.setGender(student.getGender());
            EduclGradeEntity grade = gradeMap.get(student.getGradeId());
            TEduclClass clazz = classMap.get(student.getClassId());
            row.setGradeName(grade == null ? null : grade.getGradeName());
            row.setClassName(clazz == null ? null : clazz.getClassName());
            row.setOperatorName(displayOperatorName(enrollment, student, guardianRelationMap));
            row.setEnrollTime(enrollment.getEnrollTime());
            row.setLearnedSessions(0);
            row.setCanDetail(true);
            row.setCanAssign(adminUnselectedMode
                    ? !hasNonCourseDestination && !remainingSlots.isEmpty()
                    : !remainingSlots.isEmpty() || unassignedPlaceholder);
            row.setCanEdit(false);
            List<String> slotTexts = timeSlotService.toSlotTexts(remainingSlots);
            if (adminUnselectedMode && hasNonCourseDestination) {
                slotTexts = mergeTextList(slotTexts, timeSlotService.toSlotTexts(
                        nonCourseDestinationTimeSlotMap.get(serviceStudentKey(enrollment))));
            }
            row.setUnregisteredScheduleItems(slotTexts);
            row.setUnregisteredScheduleText(String.join("、", slotTexts));
            rows.add(row);
        }
        return mergeUnselectedRowsByStudentAndTerm(rows, serviceMap);
    }

    /**
     * 未选课组班按学生+学期聚合展示，避免同一学期同一学生因多个课后服务重复出现在列表中。
     */
    private List<AsEnrollmentSelectionRowVO> mergeUnselectedRowsByStudentAndTerm(
            List<AsEnrollmentSelectionRowVO> rows, Map<Long, AsServiceEntity> serviceMap) {
        if (CollectionUtils.isEmpty(rows)) {
            return Collections.emptyList();
        }
        LinkedHashMap<String, AsEnrollmentSelectionRowVO> rowMap = new LinkedHashMap<>();
        for (AsEnrollmentSelectionRowVO row : rows) {
            if (row == null || row.getStudentId() == null) {
                continue;
            }
            AsServiceEntity service = serviceMap == null ? null : serviceMap.get(row.getServiceId());
            String key = row.getStudentId() + ":" + (service == null ? "0" : String.valueOf(service.getTermId()));
            AsEnrollmentSelectionRowVO existing = rowMap.get(key);
            if (existing == null) {
                row.setServiceName(null);
                rowMap.put(key, row);
                continue;
            }
            mergeUnselectedRow(existing, row);
        }
        return new ArrayList<>(rowMap.values());
    }

    /**
     * 合并同一学生多条未选课记录的剩余时间和非课程去向展示。
     */
    private void mergeUnselectedRow(AsEnrollmentSelectionRowVO target, AsEnrollmentSelectionRowVO source) {
        if (target == null || source == null) {
            return;
        }
        if (shouldUseSourceAsUnselectedOperationRow(target, source)) {
            copyUnselectedOperationFields(target, source);
        }
        target.setServiceName(null);
        target.setCanAssign(Boolean.TRUE.equals(target.getCanAssign()) || Boolean.TRUE.equals(source.getCanAssign()));
        target.setUnregisteredScheduleItems(mergeTextList(target.getUnregisteredScheduleItems(),
                source.getUnregisteredScheduleItems()));
        target.setUnregisteredScheduleText(String.join("、", target.getUnregisteredScheduleItems()));
        mergeSelectionDestination(target, source);
    }

    private boolean shouldUseSourceAsUnselectedOperationRow(AsEnrollmentSelectionRowVO target,
                                                            AsEnrollmentSelectionRowVO source) {
        boolean targetHasRemaining = !CollectionUtils.isEmpty(target.getUnregisteredScheduleItems());
        boolean sourceHasRemaining = !CollectionUtils.isEmpty(source.getUnregisteredScheduleItems());
        if (!targetHasRemaining && sourceHasRemaining) {
            return true;
        }
        if (!sourceHasRemaining || !Boolean.TRUE.equals(source.getCanAssign())) {
            return false;
        }
        if (!Boolean.TRUE.equals(target.getCanAssign())) {
            return true;
        }
        return Objects.equals(source.getEnrollStatus(), CoreConstant.ENROLL_STATUS_UNASSIGNED)
                && !Objects.equals(target.getEnrollStatus(), CoreConstant.ENROLL_STATUS_UNASSIGNED);
    }
    private void copyUnselectedOperationFields(AsEnrollmentSelectionRowVO target, AsEnrollmentSelectionRowVO source) {
        target.setEnrollmentId(source.getEnrollmentId());
        target.setServiceId(source.getServiceId());
        target.setEnrollSource(source.getEnrollSource());
        target.setEnrollSourceName(source.getEnrollSourceName());
        target.setEnrollStatus(source.getEnrollStatus());
        target.setEnrollStatusName(source.getEnrollStatusName());
        target.setOperatorName(source.getOperatorName());
        target.setEnrollTime(source.getEnrollTime());
    }

    private void mergeSelectionDestination(AsEnrollmentSelectionRowVO target, AsEnrollmentSelectionRowVO source) {
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
        target.setDestinationName(String.join("、", mergeTextList(
                Collections.singletonList(target.getDestinationName()),
                Collections.singletonList(source.getDestinationName()))));
    }



    /** 未选课行仅填充非课程去向展示。 */
    private void fillNonCourseDestinationInfo(AsEnrollmentSelectionRowVO row, AsStudentDestinationEntity destination) {
        if (row == null) {
            return;
        }
        if (destination == null) {
            return;
        }
        row.setDestinationType(destination.getDestinationType());
        row.setDestinationName(destination.getDestinationName());
    }

    private boolean isVisibleCourseRosterStudent(AsStudentEntity student, AsCourseClassEntity course) {
        if (student == null) {
            return false;
        }
        if (course != null && Objects.equals(course.getEnrollType(), CoreConstant.ENROLL_TYPE_CLASS_ASSIGN)) {
            return CoreConstant.CLASS_ASSIGN_STUDENT_REGISTER_STATUSES.contains(student.getRegisterStatus());
        }
        return isApprovedStudent(student);
    }

    private boolean isSelectionNonCourseDestination(AsStudentDestinationEntity destination) {
        return destination != null
                && Arrays.asList(CoreConstant.DESTINATION_TYPE_GO_HOME, CoreConstant.DESTINATION_TYPE_TRAINING_INSTITUTION,
                CoreConstant.DESTINATION_TYPE_OTHER)
                .contains(destination.getDestinationType());
    }

    private Map<String, Integer> calculateStudentLearnedSessions(List<AsEnrollmentEntity> enrollments) {
        if (CollectionUtils.isEmpty(enrollments)) {
            return Collections.emptyMap();
        }
        List<Long> courseClassIds = enrollments.stream()
                .map(AsEnrollmentEntity::getCourseClassId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        List<Long> studentIds = enrollments.stream()
                .map(AsEnrollmentEntity::getStudentId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (courseClassIds.isEmpty() || studentIds.isEmpty()) {
            return Collections.emptyMap();
        }

        LambdaQueryWrapper<AsCourseSessionEntity> sessionWrapper = new LambdaQueryWrapper<>();
        sessionWrapper.eq(AsCourseSessionEntity::getIsValid, 1)
                .in(AsCourseSessionEntity::getCourseClassId, courseClassIds)
                .ne(AsCourseSessionEntity::getSessionStatus, CoreConstant.SESSION_STATUS_CANCELLED);
        List<AsCourseSessionEntity> sessions = asCourseSessionMapper.selectList(sessionWrapper);
        if (CollectionUtils.isEmpty(sessions)) {
            return Collections.emptyMap();
        }
        Map<Long, AsCourseSessionEntity> sessionMap = sessions.stream()
                .filter(session -> session.getId() != null && session.getCourseClassId() != null)
                .collect(Collectors.toMap(AsCourseSessionEntity::getId, Function.identity(), (a, b) -> a));
        if (sessionMap.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Integer> result = new HashMap<>();
        return result;
    }

    /**
     * 批量查询管理端未选课列表中的回家、校外机构课次，用于补充“未报名课后时间”展示。
     */
    private Map<String, List<EnrollmentTimeSlot>> mapAdminUnselectedNonCourseTimeSlots(
            List<AsEnrollmentEntity> enrollments) {
        if (CollectionUtils.isEmpty(enrollments)) {
            return Collections.emptyMap();
        }
        List<Long> schoolIds = enrollments.stream()
                .map(AsEnrollmentEntity::getSchoolId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        List<Long> serviceIds = enrollments.stream()
                .map(AsEnrollmentEntity::getServiceId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        List<Long> studentIds = enrollments.stream()
                .map(AsEnrollmentEntity::getStudentId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (schoolIds.isEmpty() || serviceIds.isEmpty() || studentIds.isEmpty()) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<AsStudentDestinationEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsStudentDestinationEntity::getIsValid, 1)
                .in(AsStudentDestinationEntity::getSchoolId, schoolIds)
                .in(AsStudentDestinationEntity::getServiceId, serviceIds)
                .in(AsStudentDestinationEntity::getStudentId, studentIds)
                .isNull(AsStudentDestinationEntity::getCourseClassId)
                .isNotNull(AsStudentDestinationEntity::getSessionId)
                .in(AsStudentDestinationEntity::getDestinationType, Arrays.asList(
                        CoreConstant.DESTINATION_TYPE_GO_HOME, CoreConstant.DESTINATION_TYPE_TRAINING_INSTITUTION));
        List<AsStudentDestinationEntity> destinations = asStudentDestinationMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(destinations)) {
            return Collections.emptyMap();
        }
        Map<String, List<Long>> sessionIdsByKey = new HashMap<>();
        for (AsStudentDestinationEntity destination : destinations) {
            if (destination == null || destination.getSessionId() == null) {
                continue;
            }
            String key = serviceStudentKey(
                    destination.getSchoolId(), destination.getServiceId(), destination.getStudentId());
            sessionIdsByKey.computeIfAbsent(key, ignored -> new ArrayList<>()).add(destination.getSessionId());
        }
        Map<Long, EnrollmentTimeSlot> sessionSlotMap = timeSlotService.mapSessionSlots(sessionIdsByKey.values().stream()
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList()));
        if (sessionSlotMap.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, List<EnrollmentTimeSlot>> result = new HashMap<>();
        for (Map.Entry<String, List<Long>> entry : sessionIdsByKey.entrySet()) {
            List<EnrollmentTimeSlot> slots = entry.getValue().stream()
                    .distinct()
                    .map(sessionSlotMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            result.put(entry.getKey(), timeSlotService.sortSlots(slots));
        }
        return result;
    }

    private Map<String, Set<String>> mapStudentCourseSlotKeysFromCurrentEnrollments(List<AsEnrollmentEntity> enrollments) {
        if (CollectionUtils.isEmpty(enrollments)) {
            return Collections.emptyMap();
        }
        List<AsEnrollmentEntity> activeEnrollments = enrollments.stream()
                .filter(this::isActiveCourseEnrollment)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(activeEnrollments)) {
            return Collections.emptyMap();
        }
        Map<Long, EnrollmentTimeSlot> sessionSlotMap = timeSlotService.mapSessionSlots(activeEnrollments.stream()
                .map(AsEnrollmentEntity::getSessionId)
                .collect(Collectors.toList()));
        if (sessionSlotMap.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, Set<String>> result = new HashMap<>();
        for (AsEnrollmentEntity enrollment : activeEnrollments) {
            EnrollmentTimeSlot slot = sessionSlotMap.get(enrollment.getSessionId());
            if (slot == null) {
                continue;
            }
            result.computeIfAbsent(serviceStudentKey(enrollment), key -> new HashSet<>()).add(slot.getSlotKey());
        }
        return result;
    }

    /** 判断报名记录是否为绑定课程班的有效课程记录。 */
    private boolean isActiveCourseEnrollment(AsEnrollmentEntity enrollment) {
        return enrollment != null
                && CoreConstant.ACTIVE_COURSE_ENROLL_STATUSES.contains(enrollment.getEnrollStatus())
                && enrollment.getCourseClassId() != null
                && enrollment.getSessionId() != null;
    }

    public List<AsEnrollmentSelectionRowVO> mergeApprovedClassAssignRosterRows(
            AsEnrollmentSelectionPageQueryAO query,
            List<AsEnrollmentSelectionRowVO> enrollmentRows,
            boolean selected) {
        if (!selected || query == null || query.getCourseClassId() == null) {
            return enrollmentRows;
        }
        AsCourseClassEntity course = asCourseClassMapper.selectById(query.getCourseClassId());
        if (!isValid(course)
                || !Objects.equals(course.getEnrollType(), CoreConstant.ENROLL_TYPE_CLASS_ASSIGN)
                || (query.getSchoolId() != null && query.getSchoolId() > 0
                && !Objects.equals(query.getSchoolId(), course.getSchoolId()))
                || (query.getServiceId() != null
                && !Objects.equals(query.getServiceId(), course.getServiceId()))
                || (query.getEnrollType() != null
                && !Objects.equals(query.getEnrollType(), course.getEnrollType()))
                || (course.getCapacity() != null && course.getCapacity() > 0)) {
            return enrollmentRows;
        }
        AsServiceEntity service = asServiceMapper.selectById(course.getServiceId());
        if (!isValid(service)
                || !Objects.equals(service.getSchoolId(), course.getSchoolId())
                || !Objects.equals(service.getTermId(), query.getTermId())) {
            return enrollmentRows;
        }
        List<Long> classIds = courseClassScopeSupport.resolveClassIds(course.getSchoolId(), course);
        if (classIds.isEmpty()) {
            return enrollmentRows;
        }

        LambdaQueryWrapper<AsStudentEntity> studentWrapper = new LambdaQueryWrapper<>();
        studentWrapper.eq(AsStudentEntity::getIsValid, 1)
                .eq(AsStudentEntity::getRegisterStatus, CoreConstant.STUDENT_REGISTER_STATUS_APPROVED)
                .eq(AsStudentEntity::getSchoolId, course.getSchoolId())
                .eq(AsStudentEntity::getTermId, service.getTermId())
                .in(AsStudentEntity::getClassId, classIds);
        List<AsStudentEntity> students = asStudentMapper.selectList(studentWrapper);
        if (CollectionUtils.isEmpty(students)) {
            return Collections.emptyList();
        }

        List<AsEnrollmentSelectionRowVO> safeEnrollmentRows = enrollmentRows == null
                ? Collections.emptyList() : enrollmentRows;
        Map<Long, AsEnrollmentSelectionRowVO> enrollmentRowMap = safeEnrollmentRows.stream()
                .filter(Objects::nonNull)
                .filter(row -> row.getStudentId() != null)
                .collect(Collectors.toMap(AsEnrollmentSelectionRowVO::getStudentId,
                        Function.identity(), (first, ignored) -> first));
        Map<Long, AsCourseCategoryEntity> categoryMap = mapCategories(
                Collections.singletonList(course.getCategoryId()));
        Map<Long, EduclTermEntity> termMap = mapTerms(
                Collections.singletonList(service.getTermId()));
        Map<Long, EduclGradeEntity> gradeMap = mapGrades(students.stream()
                .map(AsStudentEntity::getGradeId).collect(Collectors.toList()));
        Map<Long, TEduclClass> classMap = mapClasses(students.stream()
                .map(AsStudentEntity::getClassId).collect(Collectors.toList()));

        return students.stream()
                .filter(this::isApprovedStudent)
                .filter(student -> courseClassScopeSupport.isStudentInCourseScope(student, course))
                .sorted(Comparator.comparing(AsStudentEntity::getId,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .map(student -> {
                    AsEnrollmentSelectionRowVO enrollmentRow = enrollmentRowMap.get(student.getId());
                    return enrollmentRow == null
                            ? buildClassAssignRosterRow(student, service, course,
                            categoryMap, termMap, gradeMap, classMap)
                            : enrollmentRow;
                })
                .collect(Collectors.toList());
    }

    private AsEnrollmentSelectionRowVO buildClassAssignRosterRow(
            AsStudentEntity student,
            AsServiceEntity service,
            AsCourseClassEntity course,
            Map<Long, AsCourseCategoryEntity> categoryMap,
            Map<Long, EduclTermEntity> termMap,
            Map<Long, EduclGradeEntity> gradeMap,
            Map<Long, TEduclClass> classMap) {
        AsEnrollmentSelectionRowVO row = new AsEnrollmentSelectionRowVO();
        AsCourseCategoryEntity category = categoryMap.get(course.getCategoryId());
        EduclTermEntity term = termMap.get(service.getTermId());
        EduclGradeEntity grade = gradeMap.get(student.getGradeId());
        TEduclClass clazz = classMap.get(student.getClassId());
        row.setSchoolId(course.getSchoolId());
        row.setServiceId(service.getId());
        row.setServiceName(service.getServiceName());
        row.setTermId(service.getTermId());
        row.setTermName(term == null ? null : term.getTermName());
        row.setCourseClassId(course.getId());
        row.setCourseClassName(course.getCourseClassName());
        row.setCategoryId(course.getCategoryId());
        row.setCategoryName(category == null ? null : category.getCategoryName());
        row.setEnrollType(course.getEnrollType());
        row.setEnrollTypeName(AssembleUtil.getEnrollTypeName(course.getEnrollType()));
        row.setEnrollSource(CoreConstant.ENROLL_SOURCE_CLASS_ASSIGN);
        row.setEnrollSourceName(AssembleUtil.getEnrollSourceName(CoreConstant.ENROLL_SOURCE_CLASS_ASSIGN));
        row.setEnrollStatus(CoreConstant.ENROLL_STATUS_ASSIGNED);
        row.setEnrollStatusName(AssembleUtil.getEnrollStatusName(CoreConstant.ENROLL_STATUS_ASSIGNED));
        row.setStudentId(student.getId());
        row.setStudentNo(student.getStudentNo());
        row.setStudentName(student.getStudentName());
        row.setGender(student.getGender());
        row.setGradeId(student.getGradeId());
        row.setGradeName(grade == null ? null : grade.getGradeName());
        row.setClassId(student.getClassId());
        row.setClassName(clazz == null ? null : clazz.getClassName());
        row.setOperatorName("系统");
        row.setLearnedSessions(0);
        row.setCanDetail(false);
        row.setCanAssign(false);
        row.setCanEdit(false);
        return row;
    }



    /** 批量按实际报名人数刷新课程班计数（不回写数据库，只更新内存快照）。 */
    public void applyActualEnrolledCounts(Collection<AsCourseClassEntity> courses) {
        if (CollectionUtils.isEmpty(courses)) {
            return;
        }
        asCourseQuotaService.load(courses);
        Map<Long, Integer> countMap = new HashMap<>();
        for (AsCourseClassEntity course : courses) {
            if (course != null && course.getId() != null) {
                countMap.put(course.getId(), countActualEnrolledStudents(course.getId()));
            }
        }
        for (AsCourseClassEntity course : courses) {
            if (course != null && course.getId() != null) {
                course.setEnrolledCount(countMap.getOrDefault(course.getId(), 0));
            }
        }
    }

    /**
     * 批量查询课程班上课时间项（按周几+起止时间去重），供分配候选和家长端展示共用。
     */
    public Map<Long, List<AsGuardianScheduleItemVO>> mapGuardianSchedules(Collection<Long> courseClassIds) {
        List<Long> ids = courseClassIds == null ? Collections.emptyList()
                : courseClassIds.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        QueryWrapper<AsCourseSessionEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("is_valid", 1)
                .in("course_class_id", ids)
                .ne("session_status", 3)
                .orderByAsc("week_day")
                .orderByAsc("start_time");
        Map<Long, LinkedHashMap<String, AsGuardianScheduleItemVO>> temp = new LinkedHashMap<>();
        for (AsCourseSessionEntity session : asCourseSessionMapper.selectList(wrapper)) {
            AsGuardianScheduleItemVO item = new AsGuardianScheduleItemVO();
            item.setWeekDay(session.getWeekDay());
            item.setWeekDayName(AssembleUtil.getWeekDayName(session.getWeekDay()));
            item.setStartTime(formatTime(session.getStartTime()));
            item.setEndTime(formatTime(session.getEndTime()));
            item.setSessionDate(session.getSessionDate());
            String key = item.getWeekDay() + "|" + item.getStartTime() + "|" + item.getEndTime();
            temp.computeIfAbsent(session.getCourseClassId(), k -> new LinkedHashMap<>()).putIfAbsent(key, item);
        }
        Map<Long, List<AsGuardianScheduleItemVO>> result = new HashMap<>();
        for (Map.Entry<Long, LinkedHashMap<String, AsGuardianScheduleItemVO>> entry : temp.entrySet()) {
            result.put(entry.getKey(), new ArrayList<>(entry.getValue().values()));
        }
        return result;
    }

    /**
     * 批量检测课程班是否使用自定义日期（非周期）排课规则，任一规则 ruleType=2 即视为自定义日期。
     */
    public Map<Long, Boolean> isCustomDateCourses(Collection<Long> courseClassIds) {
        List<Long> ids = courseClassIds == null ? Collections.emptyList()
                : courseClassIds.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, Boolean> result = new HashMap<>();
        if (ids.isEmpty()) {
            return result;
        }
        ids.forEach(id -> result.put(id, false));
        QueryWrapper<AsCourseScheduleRuleEntity> wrapper = new QueryWrapper<>();
        wrapper.select("DISTINCT course_class_id")
                .eq("is_valid", 1)
                .eq("rule_type", 2)
                .in("course_class_id", ids);
        for (AsCourseScheduleRuleEntity rule : asCourseScheduleRuleMapper.selectList(wrapper)) {
            if (rule != null && rule.getCourseClassId() != null) {
                result.put(rule.getCourseClassId(), true);
            }
        }
        return result;
    }

    /** 将课程班实体转换为分配候选项，并附带可选状态和不可选原因。 */
    public AsAssignableCourseClassVO toAssignableCourse(AsCourseClassEntity course, AsStudentEntity student,
                                                        Map<Long, AsCourseCategoryEntity> categoryMap,
                                                        Map<Long, AsTeacherEntity> teacherMap,
                                                        List<AsGuardianScheduleItemVO> schedules,
                                                        Map<Long, Boolean> customDateMap,
                                                        Map<Long, String> scheduleTextMap) {
        AsAssignableCourseClassVO vo = new AsAssignableCourseClassVO();
        fillAssignableCourseBase(vo, course, categoryMap, teacherMap, schedules, customDateMap, scheduleTextMap);
        fillAssignableCourseStatus(vo, course, student);
        return vo;
    }

    /** 管理端/班主任/老师分配候选基础字段，避免复用家长报名窗口状态。 */
    private void fillAssignableCourseBase(AsAssignableCourseClassVO vo, AsCourseClassEntity course,
                                          Map<Long, AsCourseCategoryEntity> categoryMap,
                                          Map<Long, AsTeacherEntity> teacherMap,
                                          List<AsGuardianScheduleItemVO> schedules,
                                          Map<Long, Boolean> customDateMap,
                                          Map<Long, String> scheduleTextMap) {
        vo.setServiceId(course.getServiceId());
        vo.setCategoryId(course.getCategoryId());
        AsCourseCategoryEntity category = categoryMap.get(course.getCategoryId());
        vo.setCategoryName(category == null ? null : category.getCategoryName());
        vo.setCourseClassId(course.getId());
        vo.setCourseClassName(course.getCourseClassName());
        vo.setTeacherId(course.getTeacherId());
        AsTeacherEntity teacher = teacherMap.get(course.getTeacherId());
        vo.setTeacherName(teacher == null ? null : teacher.getTeacherName());
        vo.setPlaceName(examRoomService.resolvePlaceName(course.getPlaceId()));
        vo.setCapacity(course.getCapacity());
        vo.setEnrolledCount(course.getEnrolledCount());
        int enrolledCount = course.getEnrolledCount() == null ? 0 : course.getEnrolledCount();
        int remaining = course.getCapacity() == null || course.getCapacity() == 0 ? 999999 : Math.max(course.getCapacity() - enrolledCount, 0);
        vo.setRemainingCount(remaining);
        vo.setProgressPercent(course.getCapacity() == null || course.getCapacity() <= 0 ? 0 : Math.min(100, enrolledCount * 100 / course.getCapacity()));
        vo.setScheduleItems(schedules == null ? Collections.emptyList() : schedules);
        boolean isCustomDate = customDateMap != null && Boolean.TRUE.equals(customDateMap.getOrDefault(course.getId(), false));
        String scheduleText = scheduleTextMap == null ? null : scheduleTextMap.get(course.getId());
        vo.setScheduleText(StringUtils.hasText(scheduleText) ? scheduleText : buildScheduleText(vo.getScheduleItems(), isCustomDate));
        resolveSingleSessionWeekDayName(vo.getScheduleItems());
        vo.setCanDetail(true);
        vo.setCanCancel(false);
        vo.setEnrollmentId(null);
        vo.setDisplayName(course.getCourseClassName() + "（" + enrolledCount
                + "/" + (course.getCapacity() == null || course.getCapacity() == 0 ? "不限" : course.getCapacity()) + "）");
    }

    private void fillAssignableCourseStatus(AsAssignableCourseClassVO vo, AsCourseClassEntity course, AsStudentEntity student) {
        vo.setQuota(asCourseQuotaService.view(course, student));
        vo.setAvailable(false);
        vo.setCanApply(false);
        vo.setActionText("分配");
        AsServiceEntity service = asServiceMapper.selectById(course.getServiceId());
        if (!isServiceAssignableWindow(service)) {
            vo.setAvailableStatus("closed");
            vo.setStatusName("不可分配");
            vo.setUnavailableReason("课后服务不在分配时间内");
            return;
        }
        String reason = validateCourseAssignable(student, course, course.getServiceId());
        if (reason != null) {
            vo.setAvailableStatus(toAvailableStatus(reason));
            vo.setStatusName(toStatusName(reason));
            vo.setUnavailableReason(reason);
            return;
        }
        List<AsCourseSessionEntity> sessions = sessionEnrollmentSupport.listValidSessionsByCourseClass(course.getId());
        if (!timeSlotService.courseCoversUnregisteredTime(service, student == null ? null : student.getId(), sessions)) {
            vo.setAvailableStatus("no_empty_time");
            vo.setStatusName("不可分配");
            vo.setUnavailableReason("当前课程未覆盖学生剩余可分配课后时间");
            return;
        }
        ScheduleConflict conflict = timeSlotService.findScheduleConflict(course.getSchoolId(), student == null ? null : student.getId(),
                course.getId(), sessions);
        if (conflict != null) {
            vo.setAvailableStatus("time_conflict");
            vo.setStatusName("时间冲突");
            vo.setUnavailableReason(conflict.message());
            return;
        }
        vo.setAvailable(true);
        vo.setCanApply(true);
        vo.setAvailableStatus("available");
        vo.setStatusName("可分配");
    }

    /** 组装课程班上课时间文案，仅一次课时使用具体日期展示。 */
    public String buildScheduleText(List<AsGuardianScheduleItemVO> schedules, boolean isCustomDate) {
        if (CollectionUtils.isEmpty(schedules)) {
            return null;
        }
        // 仅一次课时使用具体日期展示
        if (schedules.size() == 1) {
            AsGuardianScheduleItemVO item = schedules.get(0);
            if (item.getSessionDate() != null) {
                SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
                String dateStr = dateFmt.format(item.getSessionDate());
                String time = item.getStartTime() == null || item.getEndTime() == null ? "" : " " + item.getStartTime() + "-" + item.getEndTime();
                String day = item.getWeekDayName() == null ? "" : item.getWeekDayName().replace("每周", "周");
                return dateStr + "（" + day + "）" + time;
            }
        }
        return schedules.stream()
                .map(item -> {
                    if (isCustomDate && item.getSessionDate() != null) {
                        SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy年M月d日");
                        String dateStr = dateFmt.format(item.getSessionDate());
                        String day = item.getWeekDayName() == null ? "" : item.getWeekDayName().replace("每周", "周");
                        String time = item.getStartTime() == null || item.getEndTime() == null ? "" : " " + item.getStartTime() + "-" + item.getEndTime();
                        return dateStr + "（" + day + "）" + time;
                    }
                    String day = item.getWeekDayName() == null ? "" : item.getWeekDayName().replace("每周", "周");
                    String time = item.getStartTime() == null || item.getEndTime() == null ? "" : " " + item.getStartTime() + "-" + item.getEndTime();
                    return day + time;
                })
                .collect(Collectors.joining("，"));
    }

    public void resolveSingleSessionWeekDayName(List<AsGuardianScheduleItemVO> schedules) {
        if (CollectionUtils.isEmpty(schedules) || schedules.size() != 1) {
            return;
        }
        AsGuardianScheduleItemVO item = schedules.get(0);
        if (item.getSessionDate() == null) {
            return;
        }
        SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy年M月d日");
        String dateStr = dateFmt.format(item.getSessionDate());
        String dayName = item.getWeekDayName() == null ? "" : item.getWeekDayName().replace("每周", "周");
        item.setWeekDayName(dateStr + "（" + dayName + "）");
    }


    public JsonResult<AsEnrollmentAssignOptionsVO> listCurrentTermAssignOptions(
            AsEnrollmentAssignOptionsQueryAO query, AsStudentEntity student, Long schoolId) {
        return listCurrentTermAssignOptions(query, student, schoolId, false);
    }

    public JsonResult<AsEnrollmentAssignOptionsVO> listCurrentTermAssignOptions(
            AsEnrollmentAssignOptionsQueryAO query, AsStudentEntity student, Long schoolId, boolean strictAssignableCourses) {
        AsEnrollmentAssignOptionsVO vo = new AsEnrollmentAssignOptionsVO();
        List<EduclTermEntity> currentTerms = listCurrentTerms(schoolId);
        if (CollectionUtils.isEmpty(currentTerms)) {
            populateNonCourseDestinationOptions(vo);
            return JsonResult.buildSuccess(vo);
        }
        List<AsServiceEntity> services = listCurrentTermAssignableServices(schoolId,
                currentTerms.stream().map(EduclTermEntity::getId).filter(Objects::nonNull).collect(Collectors.toList()));
        services = services.stream()
                .filter(service -> isStudentInServiceScope(service, student))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(services)) {
            populateNonCourseDestinationOptions(vo);
            return JsonResult.buildSuccess(vo);
        }
        Map<Long, AsServiceEntity> serviceMap = services.stream()
                .filter(service -> service.getId() != null)
                .collect(Collectors.toMap(AsServiceEntity::getId, Function.identity(), (a, b) -> a, LinkedHashMap::new));
        LambdaQueryWrapper<AsCourseClassEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsCourseClassEntity::getIsValid, 1)
                .in(AsCourseClassEntity::getServiceId, serviceMap.keySet())
                .in(AsCourseClassEntity::getClassStatus, Arrays.asList(1, 2));
        if (strictAssignableCourses) {
            // 班主任选课分配只支持公开报名的类型
            wrapper.in(AsCourseClassEntity::getEnrollType, Arrays.asList(CoreConstant.ENROLL_TYPE_PUBLIC));
        }
        if (query.getCategoryId() != null) {
            wrapper.eq(AsCourseClassEntity::getCategoryId, query.getCategoryId());
        }
        List<AsCourseClassEntity> courses = asCourseClassMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(courses)) {
            populateNonCourseDestinationOptions(vo);
            return JsonResult.buildSuccess(vo);
        }
        courses = courses.stream()
                .filter(course -> serviceMap.containsKey(course.getServiceId()))
                .filter(course -> isStudentInCourseScope(student, course))
                .collect(Collectors.toList());
        applyActualEnrolledCounts(courses);
        if (strictAssignableCourses) {
            courses = courses.stream()
                    .filter(course -> isHeadteacherAssignableCourseCandidate(student, course))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(courses)) {
                populateNonCourseDestinationOptions(vo);
                return JsonResult.buildSuccess(vo);
            }
        }
        Map<Long, AsCourseCategoryEntity> categoryMap = mapCategories(courses.stream()
                .map(AsCourseClassEntity::getCategoryId).collect(Collectors.toList()));
        Map<Long, AsTeacherEntity> teacherMap = mapTeachers(courses.stream()
                .map(AsCourseClassEntity::getTeacherId).collect(Collectors.toList()));
        Map<Long, List<AsGuardianScheduleItemVO>> scheduleMap = mapGuardianSchedules(courses.stream()
                .map(AsCourseClassEntity::getId).collect(Collectors.toList()));
        Map<Long, Boolean> customDateMap = isCustomDateCourses(courses.stream()
                .map(AsCourseClassEntity::getId).collect(Collectors.toList()));
        Map<Long, String> scheduleTextMap = timeSlotService.mapCourseScheduleTimeDesc(courses.stream()
                .map(AsCourseClassEntity::getId).collect(Collectors.toList()), null);
        Set<Long> categoryIds = new LinkedHashSet<>();
        Set<Long> candidateTermIds = services.stream()
                .map(AsServiceEntity::getTermId)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        if (candidateTermIds.size() == 1) {
            fillAssignOptionsTerm(vo, services.get(0));
        }
        for (AsCourseClassEntity course : courses) {
            AsAssignableCourseClassVO item = toAssignableCourse(course, student, categoryMap, teacherMap,
                    scheduleMap.get(course.getId()), customDateMap, scheduleTextMap);
            if (strictAssignableCourses && !Boolean.TRUE.equals(item.getAvailable())) {
                continue;
            }
            if (course.getCategoryId() != null && categoryIds.add(course.getCategoryId())) {
                AsEnrollmentAssignOptionsVO.CategoryOption option = new AsEnrollmentAssignOptionsVO.CategoryOption();
                option.setCategoryId(course.getCategoryId());
                option.setDestinationType(CoreConstant.DESTINATION_TYPE_COURSE_CLASS);
                AsCourseCategoryEntity category = categoryMap.get(course.getCategoryId());
                option.setCategoryName(category == null ? null : category.getCategoryName());
                vo.getCategories().add(option);
            }
            AsServiceEntity service = serviceMap.get(course.getServiceId());
            item.setServiceName(service == null ? null : service.getServiceName());
            fillAssignableCourseTerm(item, service);
            vo.getCourseClasses().add(item);
        }
        populateNonCourseDestinationOptions(vo);
        return JsonResult.buildSuccess(vo);
    }

    public boolean isHeadteacherAssignableCourseCandidate(AsStudentEntity student, AsCourseClassEntity course) {
        if (!Objects.equals(course.getEnrollType(), CoreConstant.ENROLL_TYPE_PUBLIC)) {
            return false;
        }
        if (validateCourseAssignable(student, course, course.getServiceId()) != null) {
            return false;
        }
        if (findCourseEnrollment(course.getId(), student == null ? null : student.getId()) != null) {
            return false;
        }
        return !CollectionUtils.isEmpty(sessionEnrollmentSupport.listValidSessionsByCourseClass(course.getId()));
    }

    public boolean isHeadteacherAssignedEnrollment(AsEnrollmentEntity enrollment) {
        return enrollment != null
                && Objects.equals(enrollment.getEnrollStatus(), CoreConstant.ENROLL_STATUS_ASSIGNED)
                && Objects.equals(enrollment.getEnrollSource(), CoreConstant.ENROLL_SOURCE_MANUAL_ASSIGN);
    }

    public boolean hasPrecomputedUnregisteredTime(AsEnrollmentEntity enrollment,
                                                   Map<String, List<EnrollmentTimeSlot>> remainingSlotMap) {
        if (enrollment == null || remainingSlotMap == null) {
            return false;
        }
        return !CollectionUtils.isEmpty(remainingSlotMap.get(serviceStudentKey(enrollment)));
    }

    public boolean isHeadteacherRegisteredEnrollment(AsEnrollmentEntity enrollment) {
        if (enrollment == null || enrollment.getCourseClassId() == null || enrollment.getSessionId() == null) {
            return false;
        }
        boolean selfRegistered = Objects.equals(enrollment.getEnrollStatus(), CoreConstant.ENROLL_STATUS_ENROLLED)
                && Arrays.asList(CoreConstant.ENROLL_SOURCE_PUBLIC, CoreConstant.ENROLL_SOURCE_INVITE).contains(enrollment.getEnrollSource());
        boolean classAssigned = Objects.equals(enrollment.getEnrollStatus(), CoreConstant.ENROLL_STATUS_ASSIGNED)
                && Objects.equals(enrollment.getEnrollSource(), CoreConstant.ENROLL_SOURCE_CLASS_ASSIGN);
        return selfRegistered || classAssigned;
    }

    public List<AsEnrollmentEntity> listHeadteacherEnrollmentRepresentatives(List<AsEnrollmentEntity> enrollments,
                                                                              Map<Long, AsServiceEntity> serviceMap) {
        LinkedHashMap<String, AsEnrollmentEntity> grouped = new LinkedHashMap<>();
        for (AsEnrollmentEntity enrollment : enrollments) {
            AsServiceEntity service = serviceMap.get(enrollment.getServiceId());
            Long termId = service == null ? null : service.getTermId();
            String itemKey = enrollment.getCourseClassId() == null
                    ? "U:" + enrollment.getServiceId()
                    : "C:" + enrollment.getCourseClassId();
            String key = enrollment.getStudentId() + ":" + termId + ":" + itemKey;
            AsEnrollmentEntity existing = grouped.get(key);
            if (existing == null || compareEnrollmentRepresentative(enrollment, existing) > 0) {
                grouped.put(key, enrollment);
            }
        }
        return new ArrayList<>(grouped.values());
    }

    public int compareEnrollmentRepresentative(AsEnrollmentEntity left, AsEnrollmentEntity right) {
        Date leftTime = left.getEnrollTime();
        Date rightTime = right.getEnrollTime();
        if (leftTime != null && rightTime != null && !leftTime.equals(rightTime)) {
            return leftTime.compareTo(rightTime);
        }
        if (leftTime != null && rightTime == null) {
            return 1;
        }
        if (leftTime == null && rightTime != null) {
            return -1;
        }
        Long leftId = left.getId();
        Long rightId = right.getId();
        if (leftId == null && rightId == null) {
            return 0;
        }
        if (leftId == null) {
            return -1;
        }
        if (rightId == null) {
            return 1;
        }
        return leftId.compareTo(rightId);
    }

    /** 获取 SessionEnrollmentSupport 实例（供拆分Service使用）。 */
    public SessionEnrollmentSupport getSessionEnrollmentSupport() {
        return sessionEnrollmentSupport;
    }

    /** 获取 CourseClassScopeSupport 实例（供拆分Service使用）。 */
    public CourseClassScopeSupport getCourseClassScopeSupport() {
        return courseClassScopeSupport;
    }

    /** 获取 StudentTermScopeSupport 实例（供拆分Service使用）。 */
    public StudentTermScopeSupport getStudentTermScopeSupport() {
        return studentTermScopeSupport;
    }

    /** 获取 StudentTermSnapshotSupport 实例（供拆分Service使用）。 */
    public StudentTermSnapshotSupport getStudentTermSnapshotSupport() {
        return studentTermSnapshotSupport;
    }

    /** 获取 StudentIdentitySupport 实例（供拆分Service使用）。 */
    public StudentIdentitySupport getStudentIdentitySupport() {
        return studentIdentitySupport;
    }

    /** 获取 AsEnrollmentMapper 实例（供拆分Service使用）。 */
    public AsEnrollmentMapper getAsEnrollmentMapper() {
        return asEnrollmentMapper;
    }

    /** 获取 AsServiceMapper 实例（供拆分Service使用）。 */
    public AsServiceMapper getAsServiceMapper() {
        return asServiceMapper;
    }

    /** 获取 AsCourseClassMapper 实例（供拆分Service使用）。 */
    public AsCourseClassMapper getAsCourseClassMapper() {
        return asCourseClassMapper;
    }

    /** 获取 AsStudentMapper 实例（供拆分Service使用）。 */
    public AsStudentMapper getAsStudentMapper() {
        return asStudentMapper;
    }

    /** 获取 AsStudentDestinationMapper 实例（供拆分Service使用）。 */
    public AsStudentDestinationMapper getAsStudentDestinationMapper() {
        return asStudentDestinationMapper;
    }

    /** 获取 AsCourseInviteMapper 实例（供拆分Service使用）。 */
    public AsCourseInviteMapper getAsCourseInviteMapper() {
        return asCourseInviteMapper;
    }

    /** 获取 AsCourseQuotaService 实例（供拆分Service使用）。 */
    public AsCourseQuotaService getAsCourseQuotaService() {
        return asCourseQuotaService;
    }

    /** 获取 EduclTermMapper 实例（供拆分Service使用）。 */
    public EduclTermMapper getEduclTermMapper() {
        return educlTermMapper;
    }

    /** 获取 AsTeacherMapper 实例（供拆分Service使用）。 */
    public AsTeacherMapper getAsTeacherMapper() {
        return asTeacherMapper;
    }

    /** 获取 AsGuardianStudentMapper 实例（供拆分Service使用）。 */
    public AsGuardianStudentMapper getAsGuardianStudentMapper() {
        return asGuardianStudentMapper;
    }

    /** 获取 EduclGradeMapper 实例（供拆分Service使用）。 */
    public EduclGradeMapper getEduclGradeMapper() {
        return educlGradeMapper;
    }

    /** 获取 EduclClassMapper 实例（供拆分Service使用）。 */
    public EduclClassMapper getEduclClassMapper() {
        return educlClassMapper;
    }

    /** 获取 AsCourseCategoryMapper 实例（供拆分Service使用）。 */
    public AsCourseCategoryMapper getAsCourseCategoryMapper() {
        return asCourseCategoryMapper;
    }
}
