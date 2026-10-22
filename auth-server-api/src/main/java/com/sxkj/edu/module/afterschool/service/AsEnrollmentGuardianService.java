package com.sxkj.edu.module.afterschool.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.config.AssembleUtil;
import com.sxkj.edu.config.handle.business.GuardianAvailableCourseReadContext;
import com.sxkj.edu.config.handle.business.GuardianServiceCandidate;
import com.sxkj.edu.config.handle.business.ScheduleConflict;
import com.sxkj.edu.domain.sys.entity.mult.TEduclClass;
import com.sxkj.edu.module.afterschool.domain.dto.*;
import com.sxkj.edu.module.afterschool.domain.entity.*;
import com.sxkj.edu.module.afterschool.domain.vo.*;
import com.sxkj.edu.module.afterschool.mapper.*;
import com.sxkj.edu.module.afterschool.support.SessionEnrollmentSupport;
import com.sxkj.edu.module.afterschool.support.StudentIdentitySupport;
import com.sxkj.edu.module.afterschool.support.StudentTermSnapshotSupport;
import com.sxkj.edu.module.system.login.domain.LoginStuParentDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 家长端选课服务：首页初始化、可报名课程、报名/取消、报名记录查询。
 * @author system
 */
@Service
@Slf4j
public class AsEnrollmentGuardianService {


    @Resource
    private AsEnrollmentBuildHelper asEnrollmentBuildHelper;
    @Resource
    private AsEnrollmentTimeSlotService timeSlotService;
    @Resource
    private AsEnrollmentMapper asEnrollmentMapper;
    @Resource
    private AsServiceMapper asServiceMapper;
    @Resource
    private AsStudentMapper asStudentMapper;
    @Resource
    private AsCourseClassMapper asCourseClassMapper;
    @Resource
    private AsCourseSessionMapper asCourseSessionMapper;
    @Resource
    private AsCourseInviteMapper asCourseInviteMapper;
    @Resource
    private AsCourseQuotaService asCourseQuotaService;
    @Resource
    private SessionEnrollmentSupport sessionEnrollmentSupport;
    @Resource
    private StudentTermSnapshotSupport studentTermSnapshotSupport;
    @Resource
    private StudentIdentitySupport studentIdentitySupport;
    @Resource
    private AsEnrollmentApplyRedisSupport enrollmentApplyRedisSupport;
    @Resource
    private PlatformTransactionManager transactionManager;
    @Resource
    private ExamRoomService examRoomService;
    @Resource
    private AsEnrollmentBaseBuildHelper asEnrollmentBaseBuildHelper;

    /**
     * 家长端选课首页初始化。
     * 一次性聚合当前服务、报名截止时间、公开课类别和绑定学生，支撑客户端首屏渲染。
     */
    public JsonResult<AsGuardianEnrollmentHomeOptionsVO> getGuardianHomeOptions(AsGuardianEnrollmentHomeOptionsQueryAO query) {
        if (query == null || (query.getStudentId() == null
                && query.getStudentIdentityId() == null)) {
            return JsonResult.buildError("学生学期快照和稳定身份至少传一个");
        }
        AsStudentEntity sourceStudent = resolveGuardianSourceStudent(query);
        if (!asEnrollmentBuildHelper.isValid(sourceStudent)) {
            return JsonResult.buildError("学生不存在");
        }
        if (!asEnrollmentBuildHelper.isGuardianStudent(sourceStudent.getId())) {
            return JsonResult.buildError("当前家长未绑定该学生");
        }
        List<GuardianServiceCandidate> serviceCandidates = query.getServiceId() == null
                ? listGuardianServiceCandidates(sourceStudent) : Collections.emptyList();
        if (serviceCandidates.size() > 1) {
            AsGuardianEnrollmentHomeOptionsVO vo = new AsGuardianEnrollmentHomeOptionsVO();
            vo.setStudentId(sourceStudent.getId());
            vo.setStudentIdentityId(sourceStudent.getStudentIdentityId());
            vo.setServiceSelectionRequired(true);
            vo.setServiceOptions(buildGuardianServiceOptions(serviceCandidates));
            return JsonResult.buildSuccess(vo);
        }
        AsServiceEntity service = query.getServiceId() == null
                ? (serviceCandidates.isEmpty() ? null : serviceCandidates.get(0).service)
                : resolveGuardianService(query.getServiceId(), sourceStudent);
        if (!asEnrollmentBuildHelper.isValid(service)) {
            return JsonResult.buildError("当前暂无可选课服务");
        }
        AsStudentEntity student = studentTermSnapshotSupport.findPreparedSnapshot(
                sourceStudent, service.getSchoolId(), service.getTermId());
        if (student == null) {
            return JsonResult.buildError("目标学期学生数据尚未同步，请联系管理员");
        }
        if (!asEnrollmentBuildHelper.isGuardianStudent(student.getId())) {
            return JsonResult.buildError("当前家长未绑定目标学期学生");
        }
        AsGuardianEnrollmentHomeOptionsVO vo = new AsGuardianEnrollmentHomeOptionsVO();
        vo.setStudentId(student.getId());
        vo.setStudentIdentityId(student.getStudentIdentityId());
        vo.setServiceId(service.getId());
        vo.setServiceName(service.getServiceName());
        vo.setTermId(service.getTermId());
        vo.setTermName(asEnrollmentBuildHelper.resolveTermName(service.getTermId()));
        vo.setEnrollStartTime(service.getEnrollStartTime());
        vo.setEnrollEndTime(service.getEnrollEndTime());
        vo.setDeadlineText(service.getEnrollEndTime() == null ? null
                : "选课截止时间：" + asEnrollmentBuildHelper.formatDate(service.getEnrollEndTime()));
        List<AsCourseClassEntity> publicCourses = listGuardianPublicCourses(service.getId(), student, null, null);
        vo.setPublicAvailable(!publicCourses.isEmpty());
        vo.setInviteAvailable(false);
        vo.setSelectedTab(Boolean.TRUE.equals(vo.getPublicAvailable()) ? "public" : "invite");
        vo.setServiceSelectionRequired(false);
        vo.setServiceOptions(buildGuardianServiceOptions(
                Collections.singletonList(new GuardianServiceCandidate(service, student))));
        fillGuardianCategories(vo, publicCourses);
        vo.setStudentOptions(buildGuardianStudentOptions(resolveGuardianStudentsForService(service), service.getTermId()));
        return JsonResult.buildSuccess(vo);
    }

    public PageResultDTO<AsAssignableCourseClassVO> pageAvailableCourses(AsEnrollmentAvailableCourseQueryAO query) {
        AsStudentEntity student = asStudentMapper.selectById(query.getStudentId());
        if (!asEnrollmentBuildHelper.isGuardianStudent(query.getStudentId())
                || !asEnrollmentBuildHelper.isValid(student)) {
            return asEnrollmentBuildHelper.emptyPageResult(query.getPageIndex(), query.getPageSize());
        }
        GuardianAvailableCourseReadContext readContext = new GuardianAvailableCourseReadContext();
        List<AsCourseClassEntity> candidates = listGuardianPublicCourseCandidates(query.getServiceId(), student,
                query.getCategoryId(), query.getKeyword(), readContext);
        int total = candidates.size();
        int pageIndex = query.getPageIndex() <= 0 ? 1 : query.getPageIndex();
        int pageSize = query.getPageSize() <= 0 ? 20 : query.getPageSize();
        int from = (int) Math.min((long) (pageIndex - 1) * pageSize, total);
        int to = (int) Math.min((long) from + pageSize, total);
        // 保留完整筛选后的顺序和总数，当前页用独立集合，避免 subList 持有全部候选课程。
        List<AsCourseClassEntity> pageCourses = new ArrayList<>(candidates.subList(from, to));
        if (pageCourses.isEmpty()) {
            return asEnrollmentBuildHelper.buildManualPageResult(pageIndex, pageSize, total, Collections.emptyList());
        }
        readContext.retainPageServices(pageCourses.stream().map(AsCourseClassEntity::getServiceId)
                .collect(Collectors.toSet()));
        asEnrollmentBuildHelper.applyActualEnrolledCounts(pageCourses);
        Map<Long, AsCourseCategoryEntity> categoryMap = asEnrollmentBuildHelper.mapCategories(
                pageCourses.stream().map(AsCourseClassEntity::getCategoryId).collect(Collectors.toList()));
        Map<Long, AsTeacherEntity> teacherMap = asEnrollmentBuildHelper.mapTeachers(
                pageCourses.stream().map(AsCourseClassEntity::getTeacherId).collect(Collectors.toList()));
        GuardianServiceCandidate scheduleView = mapGuardianAvailableScheduleView(pageCourses.stream()
                .map(AsCourseClassEntity::getId).collect(Collectors.toList()));
        prepareGuardianAvailableCourseReadContext(readContext, pageCourses, student.getId());
        List<AsAssignableCourseClassVO> rows = pageCourses.stream()
                .map(course -> toGuardianAssignableCourse(course, student, categoryMap, teacherMap,
                        readContext.getEnrollments().get(course.getId()),
                        scheduleView.getScheduleItems(course.getId()), scheduleView.getScheduleText(course.getId()),
                        readContext))
                .collect(Collectors.toList());
        return asEnrollmentBuildHelper.buildManualPageResult(pageIndex, pageSize, total, rows);
    }

    /**
     * 根据学期快照或稳定身份解析当前家长选择的学生。
     */
    private AsStudentEntity resolveGuardianSourceStudent(AsGuardianEnrollmentHomeOptionsQueryAO query) {
        AsStudentEntity student = query.getStudentId() == null
                ? null : asStudentMapper.selectById(query.getStudentId());
        if (asEnrollmentBuildHelper.isValid(student)) {
            if (query.getStudentIdentityId() != null
                    && !Objects.equals(query.getStudentIdentityId(), student.getStudentIdentityId())) {
                return null;
            }
            return student;
        }
        if (query.getStudentIdentityId() == null) {
            return null;
        }
        Long schoolId = asEnrollmentBuildHelper.currentSchoolId(null);
        studentIdentitySupport.requireIdentity(query.getStudentIdentityId(), schoolId);
        LambdaQueryWrapper<AsStudentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsStudentEntity::getStudentIdentityId, query.getStudentIdentityId())
                .eq(schoolId != null, AsStudentEntity::getSchoolId, schoolId)
                .eq(AsStudentEntity::getIsValid, 1)
                .orderByDesc(AsStudentEntity::getId)
                .last("LIMIT 1");
        List<AsStudentEntity> students = asStudentMapper.selectList(wrapper);
        return CollectionUtils.isEmpty(students) ? null : students.get(0);
    }

    private AsServiceEntity resolveGuardianService(Long serviceId, AsStudentEntity student) {
        if (serviceId != null) {
            AsServiceEntity service = asServiceMapper.selectById(serviceId);
            if (!asEnrollmentBuildHelper.isValid(service) || !Objects.equals(service.getServiceStatus(), 1)
                    || student == null
                    || !Objects.equals(service.getSchoolId(), student.getSchoolId())) {
                return null;
            }
            AsStudentEntity targetStudent = studentTermSnapshotSupport.findPreparedSnapshot(
                    student, service.getSchoolId(), service.getTermId());
            return targetStudent != null
                    && isGuardianServiceEligible(service, targetStudent)
                    ? service : null;
        }
        List<GuardianServiceCandidate> candidates = listGuardianServiceCandidates(student);
        return candidates.size() == 1 ? candidates.get(0).service : null;
    }
    /**
     * 判断学生在服务下是否存在公开课
     */
    private boolean isGuardianServiceEligible(AsServiceEntity service, AsStudentEntity targetStudent) {
        if (!asEnrollmentBuildHelper.isValid(service) || !asEnrollmentBuildHelper.isValid(targetStudent)) {
            return false;
        }
        Set<Long> publicServiceIds = listGuardianPublicCourseServiceIds(
                Collections.singletonList(service), targetStudent);
        return publicServiceIds.contains(service.getId());
    }

    private Set<Long> listGuardianPublicCourseServiceIds(List<AsServiceEntity> services, AsStudentEntity student) {
        List<Long> serviceIds = services.stream()
                .map(AsServiceEntity::getId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (serviceIds.isEmpty()) {
            return Collections.emptySet();
        }
        LambdaQueryWrapper<AsCourseClassEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsCourseClassEntity::getIsValid, 1)
                .eq(AsCourseClassEntity::getEnrollType, CoreConstant.ENROLL_TYPE_PUBLIC)
                .in(AsCourseClassEntity::getClassStatus, Arrays.asList(1, 2))
                .in(AsCourseClassEntity::getServiceId, serviceIds);
        return asCourseClassMapper.selectList(wrapper).stream()
                .filter(course -> student == null || asEnrollmentBuildHelper.isStudentInCourseScope(student, course))
                .map(AsCourseClassEntity::getServiceId)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }


    /**
     * 查询当前学生真正可以报名的课后服务及目标学期学生快照。
     */
    private List<GuardianServiceCandidate> listGuardianServiceCandidates(AsStudentEntity student) {
        if (!asEnrollmentBuildHelper.isValid(student)) {
            return Collections.emptyList();
        }
        List<GuardianServiceCandidate> candidates = new ArrayList<>();
        for (AsServiceEntity service : listOpenGuardianServices(student.getSchoolId())) {
            AsStudentEntity targetStudent = studentTermSnapshotSupport.findPreparedSnapshot(
                    student, service.getSchoolId(), service.getTermId());
            if (targetStudent != null && isGuardianServiceEligible(service, targetStudent)) {
                candidates.add(new GuardianServiceCandidate(service, targetStudent));
            }
        }
        return candidates;
    }


    /**
     * 构造多服务选择项，前端选中后仍通过原有 serviceId 重新初始化报名上下文。
     */
    private List<AsGuardianEnrollmentHomeOptionsVO.ServiceOption> buildGuardianServiceOptions(
            List<GuardianServiceCandidate> candidates) {
        if (CollectionUtils.isEmpty(candidates)) {
            return Collections.emptyList();
        }
        Date now = new Date();
        List<AsGuardianEnrollmentHomeOptionsVO.ServiceOption> options = new ArrayList<>();
        for (GuardianServiceCandidate candidate : candidates) {
            AsGuardianEnrollmentHomeOptionsVO.ServiceOption option =
                    new AsGuardianEnrollmentHomeOptionsVO.ServiceOption();
            option.setServiceId(candidate.service.getId());
            option.setServiceName(candidate.service.getServiceName());
            option.setTermId(candidate.service.getTermId());
            option.setTermName(asEnrollmentBuildHelper.resolveTermName(candidate.service.getTermId()));
            option.setStudentId(candidate.student.getId());
            option.setStudentIdentityId(candidate.student.getStudentIdentityId());
            option.setEnrollStartTime(candidate.service.getEnrollStartTime());
            option.setEnrollEndTime(candidate.service.getEnrollEndTime());
            option.setEnrollStatus(resolveEnrollStatus(candidate.service, now));
            options.add(option);
        }
        return options;
    }

    /**
     * 报名状态返回：not_started / enrolling / ended。
     */
    private String resolveEnrollStatus(AsServiceEntity service, Date now) {
        if (service.getEnrollStartTime() != null && now.before(service.getEnrollStartTime())) {
            return "not_started";
        }
        if (service.getEnrollEndTime() != null && now.after(service.getEnrollEndTime())) {
            return "ended";
        }
        return "enrolling";
    }

    /**
     * 查询报名窗口内的可选服务，按报名截止时间升序。
     */
    private List<AsServiceEntity> listOpenGuardianServices(Long schoolId) {
        LambdaQueryWrapper<AsServiceEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsServiceEntity::getIsValid, 1)
                .eq(AsServiceEntity::getServiceStatus, 1);
        if (schoolId != null) {
            wrapper.eq(AsServiceEntity::getSchoolId, schoolId);
        }
        return asServiceMapper.selectList(wrapper).stream()
                .sorted(Comparator.comparing(AsServiceEntity::getEnrollEndTime, Comparator.nullsLast(Date::compareTo)))
                .collect(Collectors.toList());
    }


    /**
     * 家长端按课程班查询详情。
     * 未报名课程也可查看，详情中同步返回当前学生是否可报名、是否可取消和报名记录ID。
     */
    public JsonResult<AsGuardianCourseDetailVO> getGuardianCourseDetail(Long courseClassId, Long studentId) {
        if (!asEnrollmentBuildHelper.isGuardianStudent(studentId)) {
            return JsonResult.buildError("当前家长未绑定该学生");
        }
        AsStudentEntity student = asStudentMapper.selectById(studentId);
        AsCourseClassEntity course = asCourseClassMapper.selectById(courseClassId);
        if (!asEnrollmentBuildHelper.isValid(student) || !asEnrollmentBuildHelper.isValid(course)) {
            return JsonResult.buildError("课程不存在");
        }
        AsServiceEntity service = asServiceMapper.selectById(course.getServiceId());
        Map<Long, AsCourseCategoryEntity> categoryMap = asEnrollmentBuildHelper.mapCategories(
                Collections.singletonList(course.getCategoryId()));
        Map<Long, AsTeacherEntity> teacherMap = asEnrollmentBuildHelper.mapTeachers(
                Collections.singletonList(course.getTeacherId()));
        asEnrollmentBuildHelper.applyActualEnrolledCounts(Collections.singletonList(course));
        AsEnrollmentEntity enrollment = asEnrollmentBuildHelper.findCourseEnrollment(courseClassId, studentId);
        GuardianServiceCandidate scheduleView = mapGuardianEnrollmentScheduleView(
                Collections.singletonList(courseClassId));
        AsGuardianCourseDetailVO detail = new AsGuardianCourseDetailVO();
        copyGuardianCourseCard(toGuardianCourseCard(course, student, categoryMap, teacherMap, enrollment,
                scheduleView.getScheduleItems(courseClassId), null, scheduleView.getScheduleText(courseClassId)), detail);
        detail.setServiceName(service == null ? null : service.getServiceName());
        detail.setIntro(course.getIntro());
        detail.setEnrollEndTime(service == null ? null : service.getEnrollEndTime());
        if (Objects.equals(course.getEnrollType(), CoreConstant.ENROLL_TYPE_INVITE)) {
            detail.setInvitedCount(countInvitedStudents(courseClassId));
            detail.setPendingConfirmCount(countInviteEnrollmentStudents(courseClassId,
                    Collections.singleton(CoreConstant.ENROLL_STATUS_PENDING)));
            detail.setEnrolledCount(countInviteEnrollmentStudents(courseClassId,
                    new HashSet<>(Arrays.asList(CoreConstant.ENROLL_STATUS_ENROLLED, CoreConstant.ENROLL_STATUS_ASSIGNED))));
        } else {
            detail.setInvitedCount(0);
            detail.setPendingConfirmCount(0);
            detail.setEnrolledCount(countEnrollmentStudents(courseClassId,
                    new HashSet<>(Arrays.asList(CoreConstant.ENROLL_STATUS_ENROLLED, CoreConstant.ENROLL_STATUS_ASSIGNED))));
        }
        // 基于实际 enrolledCount 重算剩余名额和进度
        int enrolled = detail.getEnrolledCount() == null ? 0 : detail.getEnrolledCount();
        int remaining = course.getCapacity() == null || course.getCapacity() == 0
                ? 999999 : Math.max(course.getCapacity() - enrolled, 0);
        detail.setRemainingCount(remaining);
        detail.setProgressPercent(course.getCapacity() == null || course.getCapacity() <= 0
                ? 0 : Math.min(100, enrolled * 100 / course.getCapacity()));
        return JsonResult.buildSuccess(detail);
    }

    private Integer countInvitedStudents(Long courseClassId) {
        if (courseClassId == null) {
            return 0;
        }
        Set<Long> studentIds = new LinkedHashSet<>();
        LambdaQueryWrapper<AsCourseInviteEntity> inviteWrapper = new LambdaQueryWrapper<>();
        inviteWrapper.eq(AsCourseInviteEntity::getIsValid, 1)
                .eq(AsCourseInviteEntity::getInviteStatus, 1)
                .eq(AsCourseInviteEntity::getCourseClassId, courseClassId);
        List<AsCourseInviteEntity> invites = asCourseInviteMapper.selectList(inviteWrapper);
        for (AsCourseInviteEntity invite : invites == null ? Collections.<AsCourseInviteEntity>emptyList() : invites) {
            studentIds.addAll(asEnrollmentBuildHelper.parseLongList(invite.getInviteStudentIds()));
        }
        LambdaQueryWrapper<AsEnrollmentEntity> enrollmentWrapper = new LambdaQueryWrapper<>();
        enrollmentWrapper.eq(AsEnrollmentEntity::getIsValid, 1)
                .eq(AsEnrollmentEntity::getCourseClassId, courseClassId)
                .eq(AsEnrollmentEntity::getEnrollSource, CoreConstant.ENROLL_SOURCE_INVITE);
        List<AsEnrollmentEntity> enrollments = asEnrollmentMapper.selectList(enrollmentWrapper);
        for (AsEnrollmentEntity enrollment : enrollments == null ? Collections.<AsEnrollmentEntity>emptyList() : enrollments) {
            if (enrollment.getStudentId() != null) {
                studentIds.add(enrollment.getStudentId());
            }
        }
        return studentIds.size();
    }

    private Integer countInviteEnrollmentStudents(Long courseClassId, Collection<Integer> statuses) {
        if (courseClassId == null || CollectionUtils.isEmpty(statuses)) {
            return 0;
        }
        LambdaQueryWrapper<AsEnrollmentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsEnrollmentEntity::getIsValid, 1)
                .eq(AsEnrollmentEntity::getCourseClassId, courseClassId)
                .eq(AsEnrollmentEntity::getEnrollSource, CoreConstant.ENROLL_SOURCE_INVITE)
                .in(AsEnrollmentEntity::getEnrollStatus, statuses);
        List<AsEnrollmentEntity> enrollments = asEnrollmentMapper.selectList(wrapper);
        return (int) (enrollments == null ? Collections.<AsEnrollmentEntity>emptyList() : enrollments).stream()
                .map(AsEnrollmentEntity::getStudentId)
                .filter(Objects::nonNull)
                .distinct()
                .count();
    }

    /**
     * 统计课程班指定状态且已生成课次记录的去重学生数。
     */
    private Integer countEnrollmentStudents(Long courseClassId, Collection<Integer> statuses) {
        if (courseClassId == null || CollectionUtils.isEmpty(statuses)) {
            return 0;
        }
        LambdaQueryWrapper<AsEnrollmentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsEnrollmentEntity::getIsValid, 1)
                .eq(AsEnrollmentEntity::getCourseClassId, courseClassId)
                .isNotNull(AsEnrollmentEntity::getSessionId)
                .in(AsEnrollmentEntity::getEnrollStatus, statuses);
        List<AsEnrollmentEntity> enrollments = asEnrollmentMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(enrollments)) {
            return 0;
        }
        Map<Long, AsStudentEntity> studentMap = asEnrollmentBuildHelper.mapStudents(enrollments.stream()
                .map(AsEnrollmentEntity::getStudentId)
                .collect(Collectors.toList()));
        return (int) enrollments.stream()
                .map(AsEnrollmentEntity::getStudentId)
                .filter(studentId -> asEnrollmentBuildHelper.isApprovedStudent(studentMap.get(studentId)))
                .distinct()
                .count();
    }

    /**
     * 家长端查询本人绑定学生的报名记录。
     * 默认隐藏未分配记录，可通过 includeUnassigned 控制是否展示。
     */
    public PageResultDTO<AsEnrollmentSelectionRowVO> pageGuardianEnrollments(AsEnrollmentSelectionPageQueryAO query) {
        asEnrollmentBuildHelper.normalizeSelectionQueryTerm(query);
        LambdaQueryWrapper<AsEnrollmentEntity> wrapper = asEnrollmentBuildHelper.baseEnrollmentQuery(query);
        List<Long> studentIds = asEnrollmentBuildHelper.currentGuardianStudentIds(query.getTermId());
        if (studentIds.isEmpty()) {
            return asEnrollmentBuildHelper.emptyPageResult(query.getPageIndex(), query.getPageSize());
        }
        wrapper.in(AsEnrollmentEntity::getStudentId, studentIds);
        if (!Boolean.TRUE.equals(query.getIncludeUnassigned())) {
            wrapper.ne(AsEnrollmentEntity::getEnrollStatus, CoreConstant.ENROLL_STATUS_UNASSIGNED);
        }
        wrapper.orderByDesc(AsEnrollmentEntity::getId);
        return asEnrollmentBuildHelper.pageSelection(query, wrapper, true);
    }

    public AsEnrollmentSelectionDetailVO getGuardianEnrollmentDetail(Long id) {
        AsEnrollmentEntity enrollment = asEnrollmentMapper.selectById(id);
        if (!asEnrollmentBuildHelper.isValid(enrollment)
                || !asEnrollmentBuildHelper.isGuardianStudent(enrollment.getStudentId())) {
            return null;
        }
        return asEnrollmentBaseBuildHelper.getSelectionDetail(id);
    }

    public JsonResult<AsEnrollmentOperateResultVO> applyEnrollment(AsEnrollmentApplyAO ao) {
        if (!Objects.equals(ao.getParentConfirm(), 1)) {
            return JsonResult.buildError("请确认报名");
        }
        AsServiceEntity asServiceEntity = asServiceMapper.selectById(ao.getServiceId());
        if (!asEnrollmentBuildHelper.isServiceOpenForGuardianApply(asServiceEntity)) {
            return JsonResult.buildError("课后服务不在报名时间内");
        }
        String studentCourseLockKey = enrollmentApplyRedisSupport.buildStudentCourseLockKey(
                ao.getCourseClassId(), ao.getStudentId());
        if (!enrollmentApplyRedisSupport.tryLock(studentCourseLockKey)) {
            return JsonResult.buildError("报名请求处理中，请勿重复提交");
        }
        String capacityRepairLockKey = null;
        try {
            AsCourseClassEntity courseSnapshot = asCourseClassMapper.selectById(ao.getCourseClassId());
            // 新公开课由总名额与年级账本共同维护，不能再按名单人数单独改写总计数。
            if (!asCourseQuotaService.isPublic(courseSnapshot)
                    && asEnrollmentBuildHelper.requiresCapacityRepairLock(courseSnapshot)) {
                capacityRepairLockKey = enrollmentApplyRedisSupport.buildCapacityRepairLockKey(ao.getCourseClassId());
                if (!enrollmentApplyRedisSupport.tryLock(capacityRepairLockKey)) {
                    return JsonResult.buildError("课程报名人数处理中，请稍后重试");
                }
            }
            boolean capacityRepairLocked = capacityRepairLockKey != null;
            TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
            return transactionTemplate.execute(status -> applyEnrollmentInTransaction(ao, capacityRepairLocked));
        } finally {
            enrollmentApplyRedisSupport.unlock(capacityRepairLockKey);
            enrollmentApplyRedisSupport.unlock(studentCourseLockKey);
        }
    }

    /**
     * 在分布式锁保护范围内执行家长公开报名事务。
     */
    private JsonResult<AsEnrollmentOperateResultVO> applyEnrollmentInTransaction(AsEnrollmentApplyAO ao,
                                                                                 boolean capacityRepairLocked) {
        if (!asEnrollmentBuildHelper.isGuardianStudent(ao.getStudentId())) {
            return JsonResult.buildError("当前家长未绑定该学生");
        }
        AsCourseClassEntity course = asCourseClassMapper.selectById(ao.getCourseClassId());
        if (!asEnrollmentBuildHelper.isValid(course)
                || !Objects.equals(course.getEnrollType(), CoreConstant.ENROLL_TYPE_PUBLIC)) {
            return JsonResult.buildError("课程班不支持家长公开报名");
        }
        Long actualServiceId = course.getServiceId();
        AsServiceEntity service = asServiceMapper.selectById(actualServiceId);
        if (!asEnrollmentBuildHelper.isServiceOpenForGuardianApply(service)) {
            return JsonResult.buildError("课后服务不在报名时间内");
        }
        return asEnrollmentBaseBuildHelper.assignCourseInternal(null, actualServiceId, ao.getStudentId(), ao.getCourseClassId(),
                CoreConstant.ENROLL_SOURCE_PUBLIC, CoreConstant.ENROLL_STATUS_ENROLLED,
                CoreConstant.ENROLL_ACTION_PUBLIC_APPLY, CoreConstant.ENROLLMENT_OPERATOR_GUARDIAN,
                ao.getRemark(), capacityRepairLocked);
    }

    /**
     * 家长端取消公开报名。
     * 仅允许取消家长公开报名来源的记录
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> cancelGuardianEnrollment(Long enrollmentId) {
        AsEnrollmentEntity enrollment = asEnrollmentBuildHelper.resolveRepresentativeEnrollment(enrollmentId);
        if (!asEnrollmentBuildHelper.isValid(enrollment)) {
            return JsonResult.buildError("报名记录不存在");
        }
        if (!asEnrollmentBuildHelper.isGuardianStudent(enrollment.getStudentId())) {
            return JsonResult.buildError("当前家长未绑定该学生");
        }
        if (!Objects.equals(enrollment.getEnrollSource(), CoreConstant.ENROLL_SOURCE_PUBLIC)) {
            return JsonResult.buildError("仅允许取消公开报名记录");
        }
        Long beforeClassId = enrollment.getCourseClassId();
        Integer beforeStatus = enrollment.getEnrollStatus();
        if (beforeClassId != null) {
            asEnrollmentBuildHelper.decreaseCourseClassCount(beforeClassId, enrollment.getStudentId());
        }
        AsEnrollmentEntity template = asEnrollmentBuildHelper.buildEnrollmentTemplate(enrollment, null,
                enrollment.getEnrollSource(), CoreConstant.ENROLL_STATUS_UNASSIGNED,
                CoreConstant.ENROLLMENT_OPERATOR_GUARDIAN, asEnrollmentBuildHelper.currentUserId(),
                asEnrollmentBuildHelper.currentUserName("家长"), null);
        sessionEnrollmentSupport.invalidateCourseSessionEnrollments(enrollment.getSchoolId(), enrollment.getServiceId(),
                enrollment.getStudentId(), beforeClassId, asEnrollmentBuildHelper.currentUserId());
        AsEnrollmentEntity placeholder = null;
        if (asEnrollmentBuildHelper.getCurrentEnrollment(enrollment.getSchoolId(), enrollment.getServiceId(),
                enrollment.getStudentId()) == null) {
            placeholder = sessionEnrollmentSupport.createUnassignedPlaceholder(template, asEnrollmentBuildHelper.currentUserId());
        }
        asEnrollmentBaseBuildHelper.saveLog(placeholder == null ? enrollment : placeholder, beforeClassId, null,
                beforeStatus, CoreConstant.ENROLL_STATUS_UNASSIGNED, CoreConstant.ENROLL_ACTION_CANCEL,
                CoreConstant.ENROLLMENT_OPERATOR_GUARDIAN, asEnrollmentBuildHelper.currentUserId(),
                asEnrollmentBuildHelper.currentUserName("家长"), null);
        return JsonResult.buildSuccess("取消成功");
    }

    /**
     * 家长端查询本人绑定学生的全部报名记录。
     * 当前用于兼容轻量列表接口，内部按较大的 pageSize 复用分页查询。
     */
    public List<AsEnrollmentSelectionRowVO> listGuardianEnrollments() {
        AsEnrollmentSelectionPageQueryAO query = new AsEnrollmentSelectionPageQueryAO();
        query.setPageIndex(1);
        query.setPageSize(500);
        return pageGuardianEnrollments(query).getList();
    }




    /**
     * 家长端个人中心按学期查询孩子课程列表。
     */
    public List<AsParentEnrollmentTermVO> queryParentEnrollmentByTerm(
            LoginStuParentDetailVO stuParentDetailVO, Long schoolId) {
        // 1. 查询平铺数据
        List<AsParentEnrollmentFlatVO> flatList = asEnrollmentMapper.listParentEnrollmentCourses(
                stuParentDetailVO.getStudentIdList(), schoolId, stuParentDetailVO.getTermId());
        if (flatList == null || flatList.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. 按学期分组，再按课程班分组
        String[] weekDays = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        Map<Long, AsParentEnrollmentTermVO> termMap = new LinkedHashMap<>();

        for (AsParentEnrollmentFlatVO flat : flatList) {
            Long tId = flat.getTermId() != null ? flat.getTermId() : 0L;
            AsParentEnrollmentTermVO termVO = termMap.get(tId);
            if (termVO == null) {
                termVO = new AsParentEnrollmentTermVO();
                termVO.setTermId(flat.getTermId());
                termVO.setTermName(flat.getTermName() != null ? flat.getTermName() : "未知学期");
                termVO.setCourseList(new ArrayList<>());
                termMap.put(tId, termVO);
            }

            AsParentEnrollmentCourseVO courseVO = termVO.getCourseList().stream()
                    .filter(c -> c.getCourseClassId().equals(flat.getCourseClassId()))
                    .findFirst().orElse(null);

            if (courseVO == null) {
                courseVO = new AsParentEnrollmentCourseVO();
                courseVO.setCourseClassId(flat.getCourseClassId());
                courseVO.setCourseClassName(flat.getCourseClassName());
                courseVO.setPlaceName(examRoomService.resolvePlaceName(flat.getPlaceId()));
                courseVO.setCategoryName(flat.getCategoryName());
                courseVO.setEnrollType(flat.getEnrollType());
                courseVO.setEnrollTypeDesc(AssembleUtil.getEnrollTypeName(flat.getEnrollType()));
                courseVO.setClassStartTime(flat.getClassStartTime());
                courseVO.setClassEndTime(flat.getClassEndTime());
                courseVO.setStudentEnrollTime(flat.getStudentEnrollTime());
                courseVO.setClassTime(buildClassTime(flat, weekDays));
                courseVO.setCourseDate(AssembleUtil.getDateContactString(courseVO.getClassStartTime(), courseVO.getClassEndTime()));
                AsStudentEntity asStudentEntity = asStudentMapper.selectById(flat.getStudentId());
                if (null != asStudentEntity) {
                    courseVO.setStudentName(asStudentEntity.getStudentName());
                    courseVO.setStudentNo(asStudentEntity.getStudentNo());
                    courseVO.setStudentId(flat.getStudentId());
                }
                termVO.getCourseList().add(courseVO);
            } else {
                // 同一课程班的多个课次，追加上课时间
                String timeStr = buildClassTime(flat, weekDays);
                if (timeStr != null && !timeStr.isEmpty()) {
                    String existing = courseVO.getClassTime() != null ? courseVO.getClassTime() : "";
                    if (!existing.contains(timeStr)) {
                        courseVO.setClassTime(existing.isEmpty() ? timeStr : existing + "、" + timeStr);
                    }
                }
            }
        }

        return new ArrayList<>(termMap.values());
    }

    /**
     * 拼接上课时间：星期X HH:mm-HH:mm
     */
    private String buildClassTime(AsParentEnrollmentFlatVO flat, String[] weekDays) {
        if (flat.getWeekDay() == null) {
            return "";
        }
        int dayIndex = flat.getWeekDay() >= 1 && flat.getWeekDay() <= 7 ? flat.getWeekDay() - 1 : 0;
        String dayName = weekDays[dayIndex];
        String start = flat.getStartTime() != null ? flat.getStartTime() : "";
        String end = flat.getEndTime() != null ? flat.getEndTime() : "";
        return dayName + " " + start + "-" + end;
    }


    /**
     * 查询家长端公开选课可展示课程。
     * 课程必须为公开报名、处于可展示状态，并同时满足服务范围和课程班适用班级。
     */
    private List<AsCourseClassEntity> listGuardianPublicCourses(Long serviceId, AsStudentEntity student, Long categoryId, String keyword) {
        List<AsCourseClassEntity> courses = listGuardianPublicCourseCandidates(serviceId, student, categoryId, keyword, null);
        // 首页初始化仍按原口径装载全部候选课程的人数和配额。
        asEnrollmentBuildHelper.applyActualEnrolledCounts(courses);
        return courses;
    }

    /**
     * 仅按原有条件筛选可展示课程，保留查询顺序；人数和配额由调用方按所需范围装载。
     */
    private List<AsCourseClassEntity> listGuardianPublicCourseCandidates(Long serviceId, AsStudentEntity student, Long categoryId,
                                                                         String keyword, GuardianAvailableCourseReadContext readContext) {
        LambdaQueryWrapper<AsCourseClassEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsCourseClassEntity::getIsValid, 1)
                .eq(AsCourseClassEntity::getEnrollType, CoreConstant.ENROLL_TYPE_PUBLIC)
                .in(AsCourseClassEntity::getClassStatus, Arrays.asList(1, 2));
        if (serviceId != null) {
            wrapper.eq(AsCourseClassEntity::getServiceId, serviceId);
        }
        if (categoryId != null) {
            wrapper.eq(AsCourseClassEntity::getCategoryId, categoryId);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.like(AsCourseClassEntity::getCourseClassName, keyword.trim());
        }
        List<AsCourseClassEntity> courses = asCourseClassMapper.selectList(wrapper);
        Map<Long, AsServiceEntity> serviceMap = asEnrollmentBuildHelper.mapServices(
                courses.stream().map(AsCourseClassEntity::getServiceId).collect(Collectors.toList()));
        if (readContext != null) {
            readContext.getServices().putAll(serviceMap);
        }
        return courses.stream()
                .filter(course -> student == null || asEnrollmentBuildHelper.isStudentInCourseScope(student, course))
                .filter(course -> {
                    AsServiceEntity service = serviceMap.get(course.getServiceId());
                    return asEnrollmentBuildHelper.isValid(service) && Objects.equals(service.getServiceStatus(), 1)
                            && (student == null || (readContext == null
                            ? asEnrollmentBuildHelper.isStudentInServiceScope(service, student)
                            : readContext.scopeError(service.getId(),
                            () -> asEnrollmentBuildHelper.validateStudentServiceScope(service, student)) == null));
                })
                .collect(Collectors.toList());
    }

    /**
     * 根据公开课程生成首页类别胶囊。
     * 第一项固定为“全部课程”，后续按课程类别出现顺序聚合数量。
     */
    private void fillGuardianCategories(AsGuardianEnrollmentHomeOptionsVO vo, List<AsCourseClassEntity> courses) {
        AsGuardianEnrollmentHomeOptionsVO.CategoryOption all = new AsGuardianEnrollmentHomeOptionsVO.CategoryOption();
        all.setCategoryId(null);
        all.setCategoryName("全部课程");
        all.setCourseCount(courses == null ? 0 : courses.size());
        vo.getCategories().add(all);
        if (CollectionUtils.isEmpty(courses)) {
            return;
        }
        Map<Long, Long> countMap = courses.stream()
                .filter(course -> course.getCategoryId() != null)
                .collect(Collectors.groupingBy(AsCourseClassEntity::getCategoryId, LinkedHashMap::new, Collectors.counting()));
        Map<Long, AsCourseCategoryEntity> categoryMap = asEnrollmentBuildHelper.mapCategories(countMap.keySet());
        for (Map.Entry<Long, Long> entry : countMap.entrySet()) {
            AsGuardianEnrollmentHomeOptionsVO.CategoryOption option = new AsGuardianEnrollmentHomeOptionsVO.CategoryOption();
            option.setCategoryId(entry.getKey());
            AsCourseCategoryEntity category = categoryMap.get(entry.getKey());
            option.setCategoryName(category == null ? null : category.getCategoryName());
            option.setCourseCount(entry.getValue().intValue());
            vo.getCategories().add(option);
        }
    }

    /**
     * 构造当前家长绑定学生选项，供客户端多学生切换。
     */
    private List<AsGuardianStudentOptionVO> buildGuardianStudentOptions(List<AsStudentEntity> students, Long termId) {
        if (CollectionUtils.isEmpty(students)) {
            return Collections.emptyList();
        }
        Map<Long, TEduclClass> classMap = asEnrollmentBuildHelper.mapClasses(students.stream()
                .map(AsStudentEntity::getClassId).collect(Collectors.toList()));
        String termName = asEnrollmentBuildHelper.resolveTermName(termId);
        List<AsGuardianStudentOptionVO> options = new ArrayList<>();
        for (AsStudentEntity student : students) {
            if (!asEnrollmentBuildHelper.isValid(student)) {
                continue;
            }
            AsGuardianStudentOptionVO option = new AsGuardianStudentOptionVO();
            option.setStudentId(student.getId());
            option.setStudentIdentityId(student.getStudentIdentityId());
            option.setStudentNo(student.getStudentNo());
            option.setStudentName(student.getStudentName());
            option.setTermId(student.getTermId());
            option.setTermName(termName);
            TEduclClass clazz = classMap.get(student.getClassId());
            option.setClassName(clazz == null ? null : clazz.getClassName());
            options.add(option);
        }
        return options;
    }

    /**
     * 将当前家长绑定的稳定身份映射为指定课后服务学期快照。
     */
    private List<AsStudentEntity> resolveGuardianStudentsForService(AsServiceEntity service) {
        if (!asEnrollmentBuildHelper.isValid(service)) {
            return Collections.emptyList();
        }
        Map<Long, AsStudentEntity> studentMap = asEnrollmentBuildHelper.mapStudents(
                asEnrollmentBuildHelper.currentGuardianStudentIds());
        LinkedHashMap<Long, AsStudentEntity> sourceByIdentity = new LinkedHashMap<>();
        for (AsStudentEntity student : studentMap.values().stream()
                .filter(asEnrollmentBuildHelper::isValid)
                .filter(item -> Objects.equals(item.getSchoolId(), service.getSchoolId()))
                .sorted(Comparator.comparing(AsStudentEntity::getId,
                        Comparator.nullsLast(Long::compareTo)).reversed())
                .collect(Collectors.toList())) {
            if (student.getStudentIdentityId() == null) {
                continue;
            }
            AsStudentEntity current = sourceByIdentity.get(student.getStudentIdentityId());
            if (current == null || Objects.equals(student.getTermId(), service.getTermId())) {
                sourceByIdentity.put(student.getStudentIdentityId(), student);
            }
        }
        List<AsStudentEntity> targets = new ArrayList<>();
        for (AsStudentEntity source : sourceByIdentity.values()) {
            AsStudentEntity target = studentTermSnapshotSupport.findEligiblePreparedSnapshot(
                    source, service.getSchoolId(), service.getTermId());
            if (target != null && asEnrollmentBuildHelper.isGuardianStudent(target.getId())) {
                targets.add(target);
            }
        }
        return targets;
    }


    /**
     * 仅公开课程列表使用批量代表记录，不改变其他入口的单课程报名查询。
     */
    private void prepareGuardianAvailableCourseReadContext(GuardianAvailableCourseReadContext context,
                                                           List<AsCourseClassEntity> courses, Long studentId) {
        List<Long> ids = courses.stream().map(AsCourseClassEntity::getId)
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        context.getCourseClassIds().addAll(ids);
        for (List<Long> batch : Lists.partition(ids, GuardianAvailableCourseReadContext.BATCH_SIZE)) {
            List<AsEnrollmentEntity> enrollments = asEnrollmentMapper.listLatestGuardianCourseEnrollments(studentId, batch);
            if (enrollments != null) {
                enrollments.forEach(enrollment -> context.getEnrollments().put(enrollment.getCourseClassId(), enrollment));
            }
        }
    }

    /**
     * 批量读取冲突判断的目标课次，保留原查询条件和每门课程的日期、开始时间顺序。
     */
    private Map<Long, List<AsCourseSessionEntity>> mapGuardianAvailableTargetSessions(List<Long> courseClassIds) {
        Map<Long, List<AsCourseSessionEntity>> result = new HashMap<>();
        for (List<Long> batch : Lists.partition(courseClassIds, GuardianAvailableCourseReadContext.BATCH_SIZE)) {
            LambdaQueryWrapper<AsCourseSessionEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(AsCourseSessionEntity::getIsValid, 1)
                    .in(AsCourseSessionEntity::getCourseClassId, batch)
                    .ne(AsCourseSessionEntity::getSessionStatus, CoreConstant.SESSION_STATUS_CANCELLED)
                    .orderByAsc(AsCourseSessionEntity::getSessionDate)
                    .orderByAsc(AsCourseSessionEntity::getStartTime);
            for (AsCourseSessionEntity session : asCourseSessionMapper.selectList(wrapper)) {
                result.computeIfAbsent(session.getCourseClassId(), id -> new ArrayList<>()).add(session);
            }
        }
        return result;
    }

    /**
     * 聚合课程班课次为客户端时间安排。
     * 列表只需要去重后的“每周几 + 起止时间”，所以同一课程重复课次会压缩为一条展示项。
     */
    private Map<Long, List<AsGuardianScheduleItemVO>> mapGuardianSchedules(Collection<Long> courseClassIds) {
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
            item.setStartTime(asEnrollmentBuildHelper.formatTime(session.getStartTime()));
            item.setEndTime(asEnrollmentBuildHelper.formatTime(session.getEndTime()));
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
     * 组装家长端选课报名的上课时间，排课规则优先，规则缺失时沿用课次兜底结果。
     */
    private GuardianServiceCandidate mapGuardianEnrollmentScheduleView(Collection<Long> courseClassIds) {
        List<Long> ids = courseClassIds == null ? Collections.emptyList()
                : courseClassIds.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        GuardianServiceCandidate view = new GuardianServiceCandidate();
        if (ids.isEmpty()) {
            return view;
        }
        Map<Long, List<AsGuardianScheduleItemVO>> sessionScheduleMap = mapGuardianSchedules(ids);
        Map<Long, List<AsCourseScheduleRuleEntity>> ruleMap = timeSlotService.mapCourseScheduleRules(ids);
        for (Long courseClassId : ids) {
            List<AsCourseScheduleRuleEntity> rules = ruleMap.get(courseClassId);
            List<AsGuardianScheduleItemVO> ruleItems = buildGuardianScheduleItemsFromRules(rules);
            String ruleText = buildGuardianEnrollmentScheduleTextFromRules(rules);
            if (!CollectionUtils.isEmpty(ruleItems) && StringUtils.hasText(ruleText)) {
                view.put(courseClassId, ruleItems, ruleText);
                continue;
            }
            List<AsGuardianScheduleItemVO> sessionItems = sessionScheduleMap.get(courseClassId);
            view.put(courseClassId, sessionItems, asEnrollmentBuildHelper.buildScheduleText(sessionItems, false));
        }
        return view;
    }

    /**
     * 仅公开课程列表按需查询课次兜底；规则可用条件和展示格式与原时间视图保持一致。
     * 课程详情等入口继续使用 mapGuardianEnrollmentScheduleView，不改变其读取流程。
     */
    private GuardianServiceCandidate mapGuardianAvailableScheduleView(Collection<Long> courseClassIds) {
        List<Long> ids = courseClassIds == null ? Collections.emptyList()
                : courseClassIds.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        GuardianServiceCandidate view = new GuardianServiceCandidate();
        if (ids.isEmpty()) {
            return view;
        }
        Map<Long, List<AsCourseScheduleRuleEntity>> ruleMap = timeSlotService.mapCourseScheduleRules(ids);
        List<Long> fallbackIds = new ArrayList<>();
        for (Long courseClassId : ids) {
            List<AsCourseScheduleRuleEntity> rules = ruleMap.get(courseClassId);
            List<AsGuardianScheduleItemVO> ruleItems = buildGuardianScheduleItemsFromRules(rules);
            String ruleText = buildGuardianEnrollmentScheduleTextFromRules(rules);
            if (!CollectionUtils.isEmpty(ruleItems) && StringUtils.hasText(ruleText)) {
                view.put(courseClassId, ruleItems, ruleText);
            } else {
                fallbackIds.add(courseClassId);
            }
        }
        Map<Long, List<AsGuardianScheduleItemVO>> sessionScheduleMap = mapGuardianSchedules(fallbackIds);
        for (Long courseClassId : fallbackIds) {
            List<AsGuardianScheduleItemVO> sessionItems = sessionScheduleMap.get(courseClassId);
            view.put(courseClassId, sessionItems, asEnrollmentBuildHelper.buildScheduleText(sessionItems, false));
        }
        return view;
    }

    /**
     * 将周期和自定义日期规则转换为前端可直接使用的时间项。
     */
    private List<AsGuardianScheduleItemVO> buildGuardianScheduleItemsFromRules(
            List<AsCourseScheduleRuleEntity> rules) {
        if (CollectionUtils.isEmpty(rules)) {
            return Collections.emptyList();
        }
        Map<String, AsGuardianScheduleItemVO> result = new LinkedHashMap<>();
        for (AsCourseScheduleRuleEntity rule : rules) {
            if (rule == null) {
                continue;
            }
            if (Objects.equals(rule.getRuleType(), 1)) {
                for (Integer weekDay : parseWeekDays(rule.getWeekDaysJson())) {
                    AsGuardianScheduleItemVO item = buildGuardianScheduleItem(rule, weekDay, null);
                    result.put("周期|" + weekDay + "|" + item.getStartTime() + "|" + item.getEndTime(), item);
                }
                continue;
            }
            if (Objects.equals(rule.getRuleType(), 2) && rule.getSessionDate() != null) {
                Integer weekDay = resolveWeekDay(rule.getSessionDate());
                AsGuardianScheduleItemVO item = buildGuardianScheduleItem(rule, weekDay, rule.getSessionDate());
                result.put("自定义|" + rule.getSessionDate().getTime() + "|" + item.getStartTime()
                        + "|" + item.getEndTime(), item);
            }
        }
        return new ArrayList<>(result.values());
    }

    private AsGuardianScheduleItemVO buildGuardianScheduleItem(AsCourseScheduleRuleEntity rule,
                                                               Integer weekDay, Date sessionDate) {
        AsGuardianScheduleItemVO item = new AsGuardianScheduleItemVO();
        item.setWeekDay(weekDay);
        item.setWeekDayName(sessionDate == null ? AssembleUtil.getWeekDayName(weekDay)
                : new SimpleDateFormat("yyyy-MM-dd").format(sessionDate));
        item.setStartTime(asEnrollmentBuildHelper.formatTime(rule.getStartTime()));
        item.setEndTime(asEnrollmentBuildHelper.formatTime(rule.getEndTime()));
        item.setSessionDate(sessionDate);
        return item;
    }

    /**
     * 家长端周期时间展示为每周几，自定义时间展示为具体日期。
     */
    private String buildGuardianEnrollmentScheduleTextFromRules(List<AsCourseScheduleRuleEntity> rules) {
        if (CollectionUtils.isEmpty(rules)) {
            return null;
        }
        List<String> items = rules.stream()
                .map(this::formatGuardianEnrollmentScheduleRule)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
        return items.isEmpty() ? null : String.join("，", items);
    }

    private String formatGuardianEnrollmentScheduleRule(AsCourseScheduleRuleEntity rule) {
        if (rule == null) {
            return null;
        }
        if (Objects.equals(rule.getRuleType(), 1)) {
            return formatScheduleRule(rule);
        }
        if (!Objects.equals(rule.getRuleType(), 2) || rule.getSessionDate() == null) {
            return null;
        }
        String startTime = asEnrollmentBuildHelper.formatTime(rule.getStartTime());
        String endTime = asEnrollmentBuildHelper.formatTime(rule.getEndTime());
        if (!StringUtils.hasText(startTime) || !StringUtils.hasText(endTime)) {
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(rule.getSessionDate())
                + " " + startTime + "-" + endTime;
    }

    /**
     * 格式化单条排课规则（周期：每周X HH:mm-HH:mm；自定义：yyyy年M月d日（周X）HH:mm-HH:mm）。
     */
    private String formatScheduleRule(AsCourseScheduleRuleEntity rule) {
        if (rule == null) {
            return null;
        }
        String startTime = StringUtils.hasText(asEnrollmentBuildHelper.formatTime(rule.getStartTime()))
                ? asEnrollmentBuildHelper.formatTime(rule.getStartTime()) : "--:--";
        String endTime = StringUtils.hasText(asEnrollmentBuildHelper.formatTime(rule.getEndTime()))
                ? asEnrollmentBuildHelper.formatTime(rule.getEndTime()) : "--:--";
        if (Objects.equals(rule.getRuleType(), 1)) {
            List<Integer> weekDays = parseWeekDays(rule.getWeekDaysJson());
            if (CollectionUtils.isEmpty(weekDays)) {
                return null;
            }
            String weekDesc = weekDays.stream()
                    .map(this::weeklyRuleDayName)
                    .filter(StringUtils::hasText)
                    .collect(Collectors.joining("、"));
            return StringUtils.hasText(weekDesc) ? "每周" + weekDesc + " " + startTime + "-" + endTime : null;
        }
        if (Objects.equals(rule.getRuleType(), 2) && rule.getSessionDate() != null) {
            return new SimpleDateFormat("yyyy年M月d日").format(rule.getSessionDate())
                    + "（" + dateWeekDayName(rule.getSessionDate()) + "）" + startTime + "-" + endTime;
        }
        return null;
    }

    /**
     * 解析周期规则星期配置。
     */
    private List<Integer> parseWeekDays(String weekDaysJson) {
        if (!StringUtils.hasText(weekDaysJson)) {
            return Collections.emptyList();
        }
        try {
            List<Integer> weekDays = JSON.parseArray(weekDaysJson, Integer.class);
            return weekDays == null ? Collections.emptyList() : weekDays.stream()
                    .filter(Objects::nonNull)
                    .sorted()
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    /**
     * 周期规则中的星期名称，去掉“每周”前缀用于组合“每周二、四”。
     */
    private String weeklyRuleDayName(Integer weekDay) {
        String name = AssembleUtil.getWeekDayName(weekDay);
        return StringUtils.hasText(name) ? name.replace("每周", "") : null;
    }

    /**
     * 根据日期解析周几文案。
     */
    private String dateWeekDayName(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return "周一";
            case Calendar.TUESDAY:
                return "周二";
            case Calendar.WEDNESDAY:
                return "周三";
            case Calendar.THURSDAY:
                return "周四";
            case Calendar.FRIDAY:
                return "周五";
            case Calendar.SATURDAY:
                return "周六";
            case Calendar.SUNDAY:
                return "周日";
            default:
                return "";
        }
    }

    /**
     * 根据日期解析周几序号（周一=1 … 周日=7）。
     */
    private Integer resolveWeekDay(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.SUNDAY ? 7 : dayOfWeek - 1;
    }



    private AsAssignableCourseClassVO toGuardianAssignableCourse(AsCourseClassEntity course, AsStudentEntity student,
                                                                 Map<Long, AsCourseCategoryEntity> categoryMap,
                                                                 Map<Long, AsTeacherEntity> teacherMap,
                                                                 AsEnrollmentEntity currentEnrollment,
                                                                 List<AsGuardianScheduleItemVO> schedules,
                                                                 String scheduleText,
                                                                 GuardianAvailableCourseReadContext readContext) {
        AsGuardianCourseCardVO card = toGuardianCourseCard(course, student, categoryMap, teacherMap,
                currentEnrollment, schedules, null, scheduleText, readContext);
        AsAssignableCourseClassVO vo = new AsAssignableCourseClassVO();
        copyGuardianCourseCard(card, vo);
        vo.setDisplayName(course.getCourseClassName() + "（" + (course.getEnrolledCount() == null ? 0 : course.getEnrolledCount())
                + "/" + (course.getCapacity() == null || course.getCapacity() == 0 ? "不限" : course.getCapacity()) + "）");
        return vo;
    }


    private AsGuardianCourseCardVO toGuardianCourseCard(AsCourseClassEntity course, AsStudentEntity student,
                                                        Map<Long, AsCourseCategoryEntity> categoryMap,
                                                        Map<Long, AsTeacherEntity> teacherMap,
                                                        AsEnrollmentEntity currentEnrollment,
                                                        List<AsGuardianScheduleItemVO> schedules,
                                                        Map<Long, Boolean> customDateMap,
                                                        String scheduleText) {
        return toGuardianCourseCard(course, student, categoryMap, teacherMap, currentEnrollment,
                schedules, customDateMap, scheduleText, null);
    }

    private AsGuardianCourseCardVO toGuardianCourseCard(AsCourseClassEntity course, AsStudentEntity student,
                                                        Map<Long, AsCourseCategoryEntity> categoryMap,
                                                        Map<Long, AsTeacherEntity> teacherMap,
                                                        AsEnrollmentEntity currentEnrollment,
                                                        List<AsGuardianScheduleItemVO> schedules,
                                                        Map<Long, Boolean> customDateMap,
                                                        String scheduleText,
                                                        GuardianAvailableCourseReadContext readContext) {
        AsGuardianCourseCardVO vo = new AsGuardianCourseCardVO();
        vo.setQuota(asCourseQuotaService.view(course, student));
        vo.setServiceId(course.getServiceId());
        vo.setCategoryId(course.getCategoryId());
        AsCourseCategoryEntity category = categoryMap.get(course.getCategoryId());
        vo.setCategoryName(category == null ? null : category.getCategoryName());
        vo.setCourseClassId(course.getId());
        vo.setCourseClassName(course.getCourseClassName());
        vo.setTeacherId(course.getTeacherId());
        AsTeacherEntity teacher = teacherMap.get(course.getTeacherId());
        vo.setTeacherName(teacher == null ? null : teacher.getTeacherName());
        vo.setPlaceName(readContext == null ? examRoomService.resolvePlaceName(course.getPlaceId())
                : readContext.placeName(course.getPlaceId(), () -> examRoomService.resolvePlaceName(course.getPlaceId())));
        vo.setCapacity(course.getCapacity());
        vo.setEnrolledCount(course.getEnrolledCount());
        int enrolledCount = course.getEnrolledCount() == null ? 0 : course.getEnrolledCount();
        int remaining = course.getCapacity() == null || course.getCapacity() == 0
                ? 999999 : Math.max(course.getCapacity() - enrolledCount, 0);
        vo.setRemainingCount(remaining);
        vo.setProgressPercent(course.getCapacity() == null || course.getCapacity() <= 0
                ? 0 : Math.min(100, enrolledCount * 100 / course.getCapacity()));
        vo.setScheduleItems(schedules == null ? Collections.emptyList() : schedules);
        boolean isCustomDate = customDateMap != null && Boolean.TRUE.equals(customDateMap.getOrDefault(course.getId(), false));
        vo.setScheduleText(StringUtils.hasText(scheduleText)
                ? scheduleText : asEnrollmentBuildHelper.buildScheduleText(vo.getScheduleItems(), isCustomDate));
        if (!StringUtils.hasText(scheduleText)) {
            // 旧数据缺少排课规则时，保持原有单次课日期展示逻辑。
            asEnrollmentBuildHelper.resolveSingleSessionWeekDayName(vo.getScheduleItems());
        }
        vo.setCanDetail(true);
        fillGuardianCourseStatus(vo, course, student, currentEnrollment, readContext);
        return vo;
    }

    private void copyGuardianCourseCard(AsGuardianCourseCardVO source, AsGuardianCourseCardVO target) {
        target.setQuota(source.getQuota());
        SmartBeanUtil.copyProperties(source, target);
    }

    private void copyGuardianCourseCard(AsGuardianCourseCardVO source, AsAssignableCourseClassVO target) {
        target.setQuota(source.getQuota());
        SmartBeanUtil.copyProperties(source, target);

    }

    /**
     * 根据当前报名记录、报名窗口和课程校验结果推导客户端按钮状态。
     */
    private void fillGuardianCourseStatus(AsGuardianCourseCardVO vo, AsCourseClassEntity course, AsStudentEntity student,
                                          AsEnrollmentEntity currentEnrollment, GuardianAvailableCourseReadContext readContext) {
        vo.setEnrollmentId(currentEnrollment == null ? null : currentEnrollment.getId());
        vo.setCanApply(false);
        vo.setCanCancel(false);
        vo.setAvailable(false);
        AsServiceEntity service = readContext == null ? asServiceMapper.selectById(course.getServiceId())
                : readContext.getServices().get(course.getServiceId());
        if (asEnrollmentBuildHelper.isValid(service)) {
            vo.setTermId(service.getTermId());
            vo.setTermName(readContext == null ? asEnrollmentBuildHelper.resolveTermName(service.getTermId())
                    : readContext.termName(service.getTermId(),
                    () -> asEnrollmentBuildHelper.resolveTermName(service.getTermId())));
        }
        if (currentEnrollment != null && Objects.equals(currentEnrollment.getCourseClassId(), course.getId())
                && !Objects.equals(currentEnrollment.getEnrollStatus(), CoreConstant.ENROLL_STATUS_UNASSIGNED)) {
            vo.setAvailableStatus("applied");
            vo.setStatusName("已报名");
            boolean canCancelSource = Objects.equals(currentEnrollment.getEnrollSource(), CoreConstant.ENROLL_SOURCE_PUBLIC);
            vo.setActionText(canCancelSource ? "取消报名" : "已报名");
            vo.setCanCancel(canCancelSource && asEnrollmentBuildHelper.isServiceOpenForGuardianApply(service));
            return;
        }
        if (!asEnrollmentBuildHelper.isValid(service) || !Objects.equals(service.getServiceStatus(), 1)) {
            vo.setAvailableStatus("closed");
            vo.setStatusName("已截止");
            vo.setActionText("报名");
            vo.setUnavailableReason("课后服务不可用");
            vo.setEnrollStatus(resolveEnrollStatus(service, new Date()));
            return;
        }
        Date now = new Date();
        String enrollStatus = resolveEnrollStatus(service, now);
        vo.setEnrollStatus(enrollStatus);
        if ("not_started".equals(enrollStatus)) {
            vo.setAvailableStatus("not_started");
            vo.setStatusName("未到报名时间");
            vo.setActionText("报名");
            vo.setUnavailableReason("报名时间未开始");
            return;
        }
        if ("ended".equals(enrollStatus)) {
            vo.setAvailableStatus("ended");
            vo.setStatusName("报名已结束");
            vo.setActionText("报名");
            vo.setUnavailableReason("报名已结束");
            return;
        }
        String scopeError = readContext == null
                ? asEnrollmentBuildHelper.validateStudentServiceScope(service, student)
                : readContext.scopeError(service.getId(),
                () -> asEnrollmentBuildHelper.validateStudentServiceScope(service, student));
        if (scopeError != null) {
            vo.setAvailableStatus(asEnrollmentBuildHelper.toAvailableStatus(scopeError));
            vo.setStatusName(asEnrollmentBuildHelper.toStatusName(scopeError));
            vo.setActionText("报名");
            vo.setUnavailableReason(scopeError);
            return;
        }
        String reason = asEnrollmentBuildHelper.validateCourseAssignable(student, course, course.getServiceId());
        if (reason != null) {
            vo.setAvailableStatus(asEnrollmentBuildHelper.toAvailableStatus(reason));
            vo.setStatusName(asEnrollmentBuildHelper.toStatusName(reason));
            vo.setActionText("报名");
            vo.setUnavailableReason(reason);
            return;
        }
        List<AsCourseSessionEntity> targetSessions = readContext == null
                ? sessionEnrollmentSupport.listValidSessionsByCourseClass(course.getId())
                : readContext.targetSessions(course.getId(), () -> mapGuardianAvailableTargetSessions(readContext.getCourseClassIds()));
        ScheduleConflict conflict = timeSlotService.findScheduleConflict(course.getSchoolId(),
                student == null ? null : student.getId(), course.getId(), targetSessions,
                Collections.singleton(course.getId()), readContext);
        if (conflict != null) {
            vo.setAvailableStatus("time_conflict");
            vo.setStatusName("时间冲突");
            vo.setActionText("报名");
            vo.setUnavailableReason(conflict.message());
            return;
        }
        vo.setAvailable(true);
        vo.setCanApply(true);
        vo.setAvailableStatus("available");
        vo.setStatusName("可报名");
        vo.setActionText("报名");
    }
}
