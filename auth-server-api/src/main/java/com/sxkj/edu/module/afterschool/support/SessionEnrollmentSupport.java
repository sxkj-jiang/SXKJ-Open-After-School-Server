package com.sxkj.edu.module.afterschool.support;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sxkj.edu.module.afterschool.domain.entity.*;
import com.sxkj.edu.module.afterschool.mapper.AsCourseClassMapper;
import com.sxkj.edu.module.afterschool.mapper.AsCourseSessionMapper;
import com.sxkj.edu.module.afterschool.mapper.AsEnrollmentMapper;
import com.sxkj.edu.module.afterschool.mapper.AsStudentDestinationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 课次级选课与学生去向同步支持。
 * <p>
 * 业务原则：已选课时按课次展开 enrollment/destination；未分配时使用 session_id=NULL 的占位记录。
 */
@Component
@Slf4j
public class SessionEnrollmentSupport {

    @Resource
    private AsEnrollmentMapper asEnrollmentMapper;

    @Resource
    private AsStudentDestinationMapper asStudentDestinationMapper;

    @Resource
    private AsCourseSessionMapper asCourseSessionMapper;

    @Resource
    private AsCourseClassMapper asCourseClassMapper;

    /**
     * 查询课程班下全部有效且未取消的课次。
     */
    public List<AsCourseSessionEntity> listValidSessionsByCourseClass(Long courseClassId) {
        if (courseClassId == null) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<AsCourseSessionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsCourseSessionEntity::getIsValid, 1)
                .eq(AsCourseSessionEntity::getCourseClassId, courseClassId)
                .ne(AsCourseSessionEntity::getSessionStatus, 3)
                .orderByAsc(AsCourseSessionEntity::getSessionDate)
                .orderByAsc(AsCourseSessionEntity::getStartTime);
        return asCourseSessionMapper.selectList(wrapper);
    }

    /**
     * 查询学生在服务下的未分配占位记录（session_id 为空）。
     */
    public AsEnrollmentEntity findUnassignedPlaceholder(Long schoolId, Long serviceId, Long studentId) {
        LambdaQueryWrapper<AsEnrollmentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsEnrollmentEntity::getIsValid, 1)
                .eq(AsEnrollmentEntity::getSchoolId, schoolId)
                .eq(AsEnrollmentEntity::getServiceId, serviceId)
                .eq(AsEnrollmentEntity::getStudentId, studentId)
                .isNull(AsEnrollmentEntity::getSessionId)
                .eq(AsEnrollmentEntity::getEnrollStatus, 4);
        return asEnrollmentMapper.selectOne(wrapper.last("limit 1"));
    }

    /**
     * 查询学生在服务下已分配课程班 ID；无分配时返回 null。
     */
    public Long findAssignedCourseClassId(Long schoolId, Long serviceId, Long studentId) {
        LambdaQueryWrapper<AsEnrollmentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsEnrollmentEntity::getIsValid, 1)
                .eq(AsEnrollmentEntity::getSchoolId, schoolId)
                .eq(AsEnrollmentEntity::getServiceId, serviceId)
                .eq(AsEnrollmentEntity::getStudentId, studentId)
                .isNotNull(AsEnrollmentEntity::getSessionId)
                .isNotNull(AsEnrollmentEntity::getCourseClassId)
                .last("limit 1");
        AsEnrollmentEntity enrollment = asEnrollmentMapper.selectOne(wrapper);
        return enrollment == null ? null : enrollment.getCourseClassId();
    }

    /**
     * 获取列表/详情展示用的代表选课记录：优先已分配课次，其次未分配占位。
     */
    public AsEnrollmentEntity findRepresentativeEnrollment(Long schoolId, Long serviceId, Long studentId) {
        LambdaQueryWrapper<AsEnrollmentEntity> assignedWrapper = new LambdaQueryWrapper<>();
        assignedWrapper.eq(AsEnrollmentEntity::getIsValid, 1)
                .eq(AsEnrollmentEntity::getSchoolId, schoolId)
                .eq(AsEnrollmentEntity::getServiceId, serviceId)
                .eq(AsEnrollmentEntity::getStudentId, studentId)
                .isNotNull(AsEnrollmentEntity::getSessionId)
                .orderByAsc(AsEnrollmentEntity::getSessionId)
                .last("limit 1");
        AsEnrollmentEntity assigned = asEnrollmentMapper.selectOne(assignedWrapper);
        if (assigned != null) {
            return assigned;
        }
        return findUnassignedPlaceholder(schoolId, serviceId, studentId);
    }

    /**
     * 查询学生跨所有服务的有效课次报名，用于统一判断真实时间占用。
     */
    public List<AsEnrollmentEntity> listActiveSessionEnrollments(Long schoolId, Long studentId) {
        if (schoolId == null || studentId == null) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<AsEnrollmentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsEnrollmentEntity::getIsValid, 1)
                .eq(AsEnrollmentEntity::getSchoolId, schoolId)
                .eq(AsEnrollmentEntity::getStudentId, studentId)
                .isNotNull(AsEnrollmentEntity::getSessionId)
                .isNotNull(AsEnrollmentEntity::getCourseClassId)
                .in(AsEnrollmentEntity::getEnrollStatus, 0, 1, 3)
                .orderByAsc(AsEnrollmentEntity::getSessionId);
        return filterActiveSessionEnrollments(asEnrollmentMapper.selectList(wrapper));
    }

    /**
     * 过滤孤儿或失效的课次报名记录，避免已删除课程班、失效课次继续占用学生时间。
     */
    private List<AsEnrollmentEntity> filterActiveSessionEnrollments(List<AsEnrollmentEntity> enrollments) {
        if (CollectionUtils.isEmpty(enrollments)) {
            return Collections.emptyList();
        }
        List<Long> sessionIds = enrollments.stream()
                .map(AsEnrollmentEntity::getSessionId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        List<Long> courseClassIds = enrollments.stream()
                .map(AsEnrollmentEntity::getCourseClassId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (sessionIds.isEmpty() || courseClassIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<AsCourseSessionEntity> sessions = asCourseSessionMapper.selectBatchIds(sessionIds);
        List<AsCourseClassEntity> courses = asCourseClassMapper.selectBatchIds(courseClassIds);
        Set<Long> activeSessionIds = (sessions == null ? Collections.<AsCourseSessionEntity>emptyList() : sessions).stream()
                .filter(this::isActiveSession)
                .map(AsCourseSessionEntity::getId)
                .collect(Collectors.toCollection(HashSet::new));
        Set<Long> activeCourseClassIds = (courses == null ? Collections.<AsCourseClassEntity>emptyList() : courses).stream()
                .filter(this::isActiveCourseClass)
                .map(AsCourseClassEntity::getId)
                .collect(Collectors.toCollection(HashSet::new));

        return enrollments.stream()
                .filter(enrollment -> activeSessionIds.contains(enrollment.getSessionId()))
                .filter(enrollment -> activeCourseClassIds.contains(enrollment.getCourseClassId()))
                .collect(Collectors.toList());
    }

    /**
     * 判断课次是否仍可作为学生时间占用：课次必须有效且未取消。
     */
    private boolean isActiveSession(AsCourseSessionEntity session) {
        return session != null
                && Objects.equals(session.getIsValid(), 1)
                && !Objects.equals(session.getSessionStatus(), 3);
    }

    /**
     * 判断课程班是否仍可作为学生时间占用：课程班记录必须存在且有效。
     */
    private boolean isActiveCourseClass(AsCourseClassEntity course) {
        return course != null && Objects.equals(course.getIsValid(), 1);
    }

    /**
     * 查询学生在指定课程班下的有效课次报名，避免多课程模式下用服务代表记录误判。
     */
    public AsEnrollmentEntity findCourseEnrollment(Long courseClassId, Long studentId) {
        if (courseClassId == null || studentId == null) {
            return null;
        }
        LambdaQueryWrapper<AsEnrollmentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsEnrollmentEntity::getIsValid, 1)
                .eq(AsEnrollmentEntity::getCourseClassId, courseClassId)
                .eq(AsEnrollmentEntity::getStudentId, studentId)
                .isNotNull(AsEnrollmentEntity::getSessionId)
                .in(AsEnrollmentEntity::getEnrollStatus, 0, 1, 3)
                .orderByDesc(AsEnrollmentEntity::getId)
                .last("limit 1");
        return asEnrollmentMapper.selectOne(wrapper);
    }

    /**
     * 按学生+服务聚合选课记录，供列表/卡片展示（避免一课次一条重复行）。
     */
    public List<AsEnrollmentEntity> listDistinctRepresentativeEnrollments(List<AsEnrollmentEntity> enrollments) {
        if (CollectionUtils.isEmpty(enrollments)) {
            return Collections.emptyList();
        }
        LinkedHashMap<String, AsEnrollmentEntity> grouped = new LinkedHashMap<>();
        for (AsEnrollmentEntity enrollment : enrollments) {
            String key = enrollment.getServiceId() + ":" + enrollment.getStudentId();
            AsEnrollmentEntity existing = grouped.get(key);
            if (existing == null) {
                grouped.put(key, enrollment);
                continue;
            }
            if (existing.getSessionId() == null && enrollment.getSessionId() != null) {
                grouped.put(key, enrollment);
            }
        }
        return new ArrayList<>(grouped.values());
    }

    /**
     * 统计服务下已选/未选学生人数（按学生去重，不按课次条数）。
     */
    public EnrollmentDistinctStat countDistinctEnrollmentStat(Long serviceId, List<AsEnrollmentEntity> enrollments) {
        EnrollmentDistinctStat stat = new EnrollmentDistinctStat();
        if (serviceId == null || CollectionUtils.isEmpty(enrollments)) {
            return stat;
        }
        LinkedHashMap<Long, Integer> studentStatus = new LinkedHashMap<>();
        for (AsEnrollmentEntity enrollment : enrollments) {
            if (enrollment.getStudentId() == null) {
                continue;
            }
            Integer current = studentStatus.get(enrollment.getStudentId());
            int next = resolveStudentStatusPriority(enrollment);
            if (current == null || next > current) {
                studentStatus.put(enrollment.getStudentId(), next);
            }
        }
        for (Integer status : studentStatus.values()) {
            if (status >= 3) {
                stat.selectedCount++;
            } else if (status == 4) {
                stat.unselectedCount++;
            } else if (status == 0) {
                stat.pendingCount++;
            } else if (status == 2) {
                stat.cancelledCount++;
            }
        }
        return stat;
    }

    private int resolveStudentStatusPriority(AsEnrollmentEntity enrollment) {
        if (enrollment.getCourseClassId() != null
                && enrollment.getEnrollStatus() != null
                && (enrollment.getEnrollStatus() == 0 || enrollment.getEnrollStatus() == 1 || enrollment.getEnrollStatus() == 3)) {
            return 3;
        }
        if (Objects.equals(enrollment.getEnrollStatus(), 4)) {
            return 4;
        }
        if (Objects.equals(enrollment.getEnrollStatus(), 0)) {
            return 0;
        }
        if (Objects.equals(enrollment.getEnrollStatus(), 2)) {
            return 2;
        }
        return enrollment.getEnrollStatus() == null ? 0 : enrollment.getEnrollStatus();
    }

    /**
     * 失效学生在服务下的全部课次选课与去向；可选保留未分配占位。
     */
    public void invalidateSessionEnrollments(Long schoolId, Long serviceId, Long studentId, Long operatorUserId) {
        Date now = new Date();
        LambdaUpdateWrapper<AsEnrollmentEntity> enrollmentWrapper = new LambdaUpdateWrapper<>();
        enrollmentWrapper.eq(AsEnrollmentEntity::getIsValid, 1)
                .eq(AsEnrollmentEntity::getSchoolId, schoolId)
                .eq(AsEnrollmentEntity::getServiceId, serviceId)
                .eq(AsEnrollmentEntity::getStudentId, studentId)
                .isNotNull(AsEnrollmentEntity::getSessionId)
                .set(AsEnrollmentEntity::getIsValid, 0)
                .set(AsEnrollmentEntity::getUpdateUser, operatorUserId)
                .set(AsEnrollmentEntity::getUpdateTime, now);
        asEnrollmentMapper.update(null, enrollmentWrapper);

        LambdaUpdateWrapper<AsStudentDestinationEntity> destinationWrapper = new LambdaUpdateWrapper<>();
        destinationWrapper.eq(AsStudentDestinationEntity::getIsValid, 1)
                .eq(AsStudentDestinationEntity::getSchoolId, schoolId)
                .eq(AsStudentDestinationEntity::getServiceId, serviceId)
                .eq(AsStudentDestinationEntity::getStudentId, studentId)
                .isNotNull(AsStudentDestinationEntity::getSessionId)
                .set(AsStudentDestinationEntity::getIsValid, 0)
                .set(AsStudentDestinationEntity::getUpdateUser, operatorUserId)
                .set(AsStudentDestinationEntity::getUpdateTime, now);
        asStudentDestinationMapper.update(null, destinationWrapper);
    }

    /**
     * 使指定课程班下的课次选课记录失效
     */
    public void invalidateCourseSessionEnrollments(Long schoolId, Long serviceId, Long studentId, Long courseClassId, Long operatorUserId) {
        Date now = new Date();
        LambdaUpdateWrapper<AsEnrollmentEntity> enrollmentWrapper = new LambdaUpdateWrapper<>();
        enrollmentWrapper.eq(AsEnrollmentEntity::getIsValid, 1)
                .eq(AsEnrollmentEntity::getSchoolId, schoolId)
                .eq(AsEnrollmentEntity::getServiceId, serviceId)
                .eq(AsEnrollmentEntity::getStudentId, studentId)
                .eq(AsEnrollmentEntity::getCourseClassId, courseClassId)
                .isNotNull(AsEnrollmentEntity::getSessionId)
                .set(AsEnrollmentEntity::getIsValid, 0)
                .set(AsEnrollmentEntity::getUpdateUser, operatorUserId)
                .set(AsEnrollmentEntity::getUpdateTime, now);
        asEnrollmentMapper.update(null, enrollmentWrapper);

        LambdaUpdateWrapper<AsStudentDestinationEntity> destinationWrapper = new LambdaUpdateWrapper<>();
        destinationWrapper.eq(AsStudentDestinationEntity::getIsValid, 1)
                .eq(AsStudentDestinationEntity::getSchoolId, schoolId)
                .eq(AsStudentDestinationEntity::getServiceId, serviceId)
                .eq(AsStudentDestinationEntity::getStudentId, studentId)
                .eq(AsStudentDestinationEntity::getCourseClassId, courseClassId)
                .isNotNull(AsStudentDestinationEntity::getSessionId)
                .set(AsStudentDestinationEntity::getIsValid, 0)
                .set(AsStudentDestinationEntity::getUpdateUser, operatorUserId)
                .set(AsStudentDestinationEntity::getUpdateTime, now);
        asStudentDestinationMapper.update(null, destinationWrapper);
    }

    /**
     * 失效未分配占位记录。
     */
    public void invalidateUnassignedPlaceholder(Long schoolId, Long serviceId, Long studentId, Long operatorUserId) {
        Date now = new Date();
        LambdaUpdateWrapper<AsEnrollmentEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AsEnrollmentEntity::getIsValid, 1)
                .eq(AsEnrollmentEntity::getSchoolId, schoolId)
                .eq(AsEnrollmentEntity::getServiceId, serviceId)
                .eq(AsEnrollmentEntity::getStudentId, studentId)
                .isNull(AsEnrollmentEntity::getSessionId)
                .set(AsEnrollmentEntity::getIsValid, 0)
                .set(AsEnrollmentEntity::getUpdateUser, operatorUserId)
                .set(AsEnrollmentEntity::getUpdateTime, now);
        asEnrollmentMapper.update(null, wrapper);
    }


    /**
     * 追加写入指定课程的课次选课记录，保留同一服务下其他课程的有效课次。
     */
    public AsEnrollmentEntity appendStudentToCourseSessions(AsEnrollmentEntity template, List<AsCourseSessionEntity> sessions,
                                                            Long operatorUserId) {
        if (template == null || CollectionUtils.isEmpty(sessions)) {
            return template;
        }
        invalidateCourseSessionEnrollments(template.getSchoolId(), template.getServiceId(), template.getStudentId(),
                template.getCourseClassId(), operatorUserId);
        invalidateUnassignedPlaceholder(template.getSchoolId(), template.getServiceId(), template.getStudentId(), operatorUserId);
        AsEnrollmentEntity first = null;
        Date now = new Date();
        for (AsCourseSessionEntity session : sessions) {
            AsEnrollmentEntity enrollment = buildSessionEnrollment(template, session, operatorUserId, now);
            LambdaQueryWrapper<AsEnrollmentEntity> existsWrapper = new LambdaQueryWrapper<>();
            existsWrapper.eq(AsEnrollmentEntity::getSchoolId, template.getSchoolId())
                    .eq(AsEnrollmentEntity::getServiceId, template.getServiceId())
                    .eq(AsEnrollmentEntity::getStudentId, template.getStudentId())
                    .eq(AsEnrollmentEntity::getSessionId, session.getId());
            AsEnrollmentEntity existing = asEnrollmentMapper.selectOne(existsWrapper.last("limit 1"));
            if (existing != null) {
                enrollment.setId(existing.getId());
                enrollment.setCreateUser(existing.getCreateUser());
                enrollment.setCreateTime(existing.getCreateTime());
                asEnrollmentMapper.updateById(enrollment);
            } else {
                asEnrollmentMapper.insert(enrollment);
            }
            syncDestination(enrollment);
            if (first == null) {
                first = enrollment;
            }
        }
        return first;
    }
    /**
     * 重建课次后迁移学生课次报名：只失效指定旧课次，不影响已开始或已完成的历史课次记录。
     */
    public AsEnrollmentEntity replaceStudentCourseSessions(AsEnrollmentEntity template, List<Long> oldSessionIds,
            List<AsCourseSessionEntity> newSessions, Long operatorUserId) {
        if (template == null || CollectionUtils.isEmpty(newSessions)) {
            return template;
        }
        invalidateStudentCourseSessionIds(template.getSchoolId(), template.getServiceId(), template.getStudentId(),
                template.getCourseClassId(), oldSessionIds, operatorUserId);
        AsEnrollmentEntity first = null;
        Date now = new Date();
        for (AsCourseSessionEntity session : newSessions) {
            AsEnrollmentEntity enrollment = buildSessionEnrollment(template, session, operatorUserId, now);
            LambdaQueryWrapper<AsEnrollmentEntity> existsWrapper = new LambdaQueryWrapper<>();
            existsWrapper.eq(AsEnrollmentEntity::getSchoolId, template.getSchoolId())
                    .eq(AsEnrollmentEntity::getServiceId, template.getServiceId())
                    .eq(AsEnrollmentEntity::getStudentId, template.getStudentId())
                    .eq(AsEnrollmentEntity::getSessionId, session.getId());
            AsEnrollmentEntity existing = asEnrollmentMapper.selectOne(existsWrapper.last("limit 1"));
            if (existing != null) {
                enrollment.setId(existing.getId());
                enrollment.setCreateUser(existing.getCreateUser());
                enrollment.setCreateTime(existing.getCreateTime());
                asEnrollmentMapper.updateById(enrollment);
            } else {
                asEnrollmentMapper.insert(enrollment);
            }
            syncDestination(enrollment);
            if (first == null) {
                first = enrollment;
            }
        }
        return first;
    }

    /**
     * 只失效指定旧课次的报名和去向，保留同课程班下其它历史课次数据。
     */
    private void invalidateStudentCourseSessionIds(Long schoolId, Long serviceId, Long studentId, Long courseClassId,
            List<Long> sessionIds, Long operatorUserId) {
        if (CollectionUtils.isEmpty(sessionIds)) {
            return;
        }
        Date now = new Date();
        LambdaUpdateWrapper<AsEnrollmentEntity> enrollmentWrapper = new LambdaUpdateWrapper<>();
        enrollmentWrapper.eq(AsEnrollmentEntity::getIsValid, 1)
                .eq(AsEnrollmentEntity::getSchoolId, schoolId)
                .eq(AsEnrollmentEntity::getServiceId, serviceId)
                .eq(AsEnrollmentEntity::getStudentId, studentId)
                .eq(AsEnrollmentEntity::getCourseClassId, courseClassId)
                .in(AsEnrollmentEntity::getSessionId, sessionIds)
                .set(AsEnrollmentEntity::getIsValid, 0)
                .set(AsEnrollmentEntity::getUpdateUser, operatorUserId)
                .set(AsEnrollmentEntity::getUpdateTime, now);
        asEnrollmentMapper.update(null, enrollmentWrapper);

        LambdaUpdateWrapper<AsStudentDestinationEntity> destinationWrapper = new LambdaUpdateWrapper<>();
        destinationWrapper.eq(AsStudentDestinationEntity::getIsValid, 1)
                .eq(AsStudentDestinationEntity::getSchoolId, schoolId)
                .eq(AsStudentDestinationEntity::getServiceId, serviceId)
                .eq(AsStudentDestinationEntity::getStudentId, studentId)
                .eq(AsStudentDestinationEntity::getCourseClassId, courseClassId)
                .in(AsStudentDestinationEntity::getSessionId, sessionIds)
                .set(AsStudentDestinationEntity::getIsValid, 0)
                .set(AsStudentDestinationEntity::getUpdateUser, operatorUserId)
                .set(AsStudentDestinationEntity::getUpdateTime, now);
        asStudentDestinationMapper.update(null, destinationWrapper);
    }

    /**
     * 同步单条课次选课对应的学生去向。
     */
    public void syncDestination(AsEnrollmentEntity enrollment) {
        if (enrollment == null || enrollment.getSessionId() == null) {
            return;
        }
        LambdaQueryWrapper<AsStudentDestinationEntity> wrapper = new LambdaQueryWrapper<>();
        // 按学生去向表唯一键查找历史记录，取消后再次报名时恢复旧记录，避免重复插入触发唯一键冲突。
        wrapper.eq(AsStudentDestinationEntity::getSchoolId, enrollment.getSchoolId())
                .eq(AsStudentDestinationEntity::getServiceId, enrollment.getServiceId())
                .eq(AsStudentDestinationEntity::getStudentId, enrollment.getStudentId())
                .eq(AsStudentDestinationEntity::getSessionId, enrollment.getSessionId());
        AsStudentDestinationEntity destination = asStudentDestinationMapper.selectOne(wrapper.last("limit 1"));
        Date now = new Date();
        if (destination == null) {
            destination = new AsStudentDestinationEntity();
            destination.setSchoolId(enrollment.getSchoolId());
            destination.setServiceId(enrollment.getServiceId());
            destination.setStudentId(enrollment.getStudentId());
            destination.setStudentNo(enrollment.getStudentNo());
            destination.setSessionId(enrollment.getSessionId());
            destination.setCreateUser(enrollment.getUpdateUser());
            destination.setCreateTime(now);
            destination.setIsValid(1);
        }
        destination.setSessionId(enrollment.getSessionId());
        destination.setIsValid(1);
        destination.setDestinationType(enrollment.getCourseClassId() == null ? 4 : 1);
        destination.setCourseClassId(enrollment.getCourseClassId());
        destination.setDestinationName(enrollment.getCourseClassId() == null ? "未分配" : "课程班");
        destination.setAssignUserId(enrollment.getOperatorUserId());
        destination.setAssignTime(now);
        destination.setUpdateUser(enrollment.getUpdateUser());
        destination.setUpdateTime(now);
        if (destination.getId() == null) {
            asStudentDestinationMapper.insert(destination);
        } else {
            asStudentDestinationMapper.updateById(destination);
        }
    }

    /**
     * 创建未分配占位选课记录（session_id 为空）。
     */
    public AsEnrollmentEntity createUnassignedPlaceholder(AsEnrollmentEntity template, Long operatorUserId) {
        if (findUnassignedPlaceholder(template.getSchoolId(), template.getServiceId(), template.getStudentId()) != null) {
            return findUnassignedPlaceholder(template.getSchoolId(), template.getServiceId(), template.getStudentId());
        }
        if (findAssignedCourseClassId(template.getSchoolId(), template.getServiceId(), template.getStudentId()) != null) {
            return null;
        }
        Date now = new Date();
        AsEnrollmentEntity entity = new AsEnrollmentEntity();
        entity.setSchoolId(template.getSchoolId());
        entity.setServiceId(template.getServiceId());
        entity.setStudentId(template.getStudentId());
        entity.setStudentNo(template.getStudentNo());
        entity.setEnrollStatus(4);
        entity.setOperatorType(template.getOperatorType());
        entity.setOperatorUserId(template.getOperatorUserId());
        entity.setOperatorName(template.getOperatorName());
        entity.setCreateUser(operatorUserId);
        entity.setUpdateUser(operatorUserId);
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        entity.setIsValid(1);
        asEnrollmentMapper.insert(entity);
        return entity;
    }


    private AsEnrollmentEntity buildSessionEnrollment(AsEnrollmentEntity template, AsCourseSessionEntity session,
                                                      Long operatorUserId, Date now) {
        AsEnrollmentEntity enrollment = new AsEnrollmentEntity();
        enrollment.setSchoolId(template.getSchoolId());
        enrollment.setServiceId(template.getServiceId());
        enrollment.setStudentId(template.getStudentId());
        enrollment.setStudentNo(template.getStudentNo());
        enrollment.setSessionId(session.getId());
        enrollment.setCourseClassId(template.getCourseClassId());
        enrollment.setEnrollSource(template.getEnrollSource());
        enrollment.setEnrollStatus(template.getEnrollStatus());
        enrollment.setParentConfirmRequired(template.getParentConfirmRequired());
        enrollment.setInviteId(template.getInviteId());
        enrollment.setGuardianConfirmUserId(template.getGuardianConfirmUserId());
        enrollment.setGuardianConfirmTime(template.getGuardianConfirmTime());
        enrollment.setOperatorType(template.getOperatorType());
        enrollment.setOperatorUserId(template.getOperatorUserId());
        enrollment.setOperatorName(template.getOperatorName());
        enrollment.setEnrollTime(template.getEnrollTime() == null ? now : template.getEnrollTime());
        enrollment.setRemark(template.getRemark());
        enrollment.setCreateUser(operatorUserId);
        enrollment.setUpdateUser(operatorUserId);
        enrollment.setCreateTime(now);
        enrollment.setUpdateTime(now);
        enrollment.setIsValid(1);
        return enrollment;
    }

    public static class EnrollmentDistinctStat {
        private int selectedCount;
        private int unselectedCount;
        private int pendingCount;
        private int cancelledCount;

        public int getSelectedCount() {
            return selectedCount;
        }

        public int getUnselectedCount() {
            return unselectedCount;
        }

        public int getPendingCount() {
            return pendingCount;
        }

        public int getCancelledCount() {
            return cancelledCount;
        }
    }
}
