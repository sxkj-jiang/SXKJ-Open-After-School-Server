package com.sxkj.edu.module.afterschool.support;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseClassEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsEnrollmentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsGuardianStudentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsServiceEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsServiceStudentNoticeEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.EduclTermEntity;
import com.sxkj.edu.module.afterschool.mapper.AsCourseClassMapper;
import com.sxkj.edu.module.afterschool.mapper.AsEnrollmentMapper;
import com.sxkj.edu.module.afterschool.mapper.AsGuardianStudentMapper;
import com.sxkj.edu.module.afterschool.mapper.AsServiceMapper;
import com.sxkj.edu.module.afterschool.mapper.AsServiceStudentNoticeMapper;
import com.sxkj.edu.module.afterschool.mapper.AsStudentMapper;
import com.sxkj.edu.module.afterschool.mapper.EduclTermMapper;
import com.sxkj.edu.module.system.employee.TBaseUserDao;
import com.sxkj.edu.module.system.employee.domain.entity.TBaseUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 报名开放期新增学生和新绑定家长提醒组件。
 *
 * <p>该组件只对已经准备完成的学生学期快照补发消息，
 * 不创建学生快照，也不修改服务、报名和邀请状态。</p>
 */
@Component
@Slf4j
public class ServiceEnrollmentNoticeSupport {

    private static final Integer NOTICE_TYPE_SUPPLEMENT = 1;
    private static final Integer ENROLL_SOURCE_INVITE = 2;
    private static final Integer STUDENT_REGISTER_STATUS_APPROVED = 1;
    private static final Integer SCHOOL_ROLL_ACTIVE = 1;
    private static final Long SYSTEM_USER_ID = 1L;

    @Resource
    private AsServiceMapper asServiceMapper;
    @Resource
    private AsCourseClassMapper asCourseClassMapper;
    @Resource
    private AsEnrollmentMapper asEnrollmentMapper;
    @Resource
    private AsStudentMapper asStudentMapper;
    @Resource
    private AsGuardianStudentMapper asGuardianStudentMapper;
    @Resource
    private AsServiceStudentNoticeMapper asServiceStudentNoticeMapper;
    @Resource
    private EduclTermMapper educlTermMapper;
    @Resource
    private TBaseUserDao tBaseUserDao;
    @Resource
    private CourseClassScopeSupport courseClassScopeSupport;
    private PlatformTransactionManager transactionManager;

    /**
     * 为指定学生快照补发报名开放提醒。
     */
    public void notifyOpenServicesForStudent(AsStudentEntity student) {
        try {
            executeAfterCurrentTransactionCommit(() -> {
                try {
                    executeInIndependentTransaction(
                            () -> doNotifyOpenServicesForStudent(student));
                } catch (Exception exception) {
                    log.error("补发学生报名开放提醒失败，studentId={}",
                            student == null ? null : student.getId(), exception);
                }
            });
        } catch (Exception exception) {
            log.error("登记学生报名开放提醒失败，studentId={}",
                    student == null ? null : student.getId(), exception);
        }
    }

    /**
     * 执行指定学生快照的报名开放提醒，异常由外层统一隔离。
     */
    private void doNotifyOpenServicesForStudent(AsStudentEntity student) {
        if (!canReceiveNotice(student)) {
            return;
        }
        Date now = new Date();
        LambdaQueryWrapper<AsServiceEntity> serviceWrapper = new LambdaQueryWrapper<>();
        serviceWrapper.eq(AsServiceEntity::getSchoolId, student.getSchoolId())
                .eq(AsServiceEntity::getTermId, student.getTermId())
                .eq(AsServiceEntity::getServiceStatus,
                        CoreConstant.SERVICE_STATUS_ENROLLING)
                .eq(AsServiceEntity::getIsValid, 1)
                .le(AsServiceEntity::getEnrollStartTime, now)
                .ge(AsServiceEntity::getEnrollEndTime, now);
        List<AsServiceEntity> services = asServiceMapper.selectList(serviceWrapper);
        if (CollectionUtils.isEmpty(services)) {
            return;
        }
        for (AsServiceEntity service : services) {
            if (hasEnrollmentOpportunity(service.getId(), student)) {
                sendOnce(service, student);
            }
        }
    }

    /**
     * 为指定稳定身份补齐开放服务目标快照，并补发报名开放提醒。
     */
    public void notifyOpenServicesForIdentity(Long schoolId, Long studentIdentityId) {
        try {
            executeAfterCurrentTransactionCommit(() -> {
                try {
                    executeInIndependentTransaction(
                            () -> doNotifyOpenServicesForIdentity(
                                    schoolId, studentIdentityId));
                } catch (Exception exception) {
                    log.error("补齐稳定学生身份报名提醒失败，schoolId={}，studentIdentityId={}",
                            schoolId, studentIdentityId, exception);
                }
            });
        } catch (Exception exception) {
            log.error("登记稳定学生身份报名提醒失败，schoolId={}，studentIdentityId={}",
                    schoolId, studentIdentityId, exception);
        }
    }

    /**
     * 对稳定学生身份已有的学期快照执行提醒，异常由外层统一隔离。
     */
    private void doNotifyOpenServicesForIdentity(Long schoolId,
                                                 Long studentIdentityId) {
        if (schoolId == null || studentIdentityId == null) {
            return;
        }
        LambdaQueryWrapper<AsStudentEntity> studentWrapper = new LambdaQueryWrapper<>();
        studentWrapper.eq(AsStudentEntity::getSchoolId, schoolId)
                .eq(AsStudentEntity::getStudentIdentityId, studentIdentityId)
                .eq(AsStudentEntity::getIsValid, 1);
        List<AsStudentEntity> students = asStudentMapper.selectList(studentWrapper);
        if (CollectionUtils.isEmpty(students)) {
            return;
        }
        students.forEach(this::doNotifyOpenServicesForStudent);
    }

    /**
     * 在独立事务中执行快照补齐和提醒，避免失败污染调用方业务事务。
     */
    private void executeInIndependentTransaction(Runnable action) {
        if (transactionManager == null) {
            action.run();
            return;
        }
        TransactionTemplate template =
                new TransactionTemplate(transactionManager);
        template.setPropagationBehavior(
                TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        template.execute(status -> {
            action.run();
            return null;
        });
    }

    /**
     * 当前存在业务事务时在提交成功后执行，否则立即执行。
     */
    private void executeAfterCurrentTransactionCommit(Runnable action) {
        if (TransactionSynchronizationManager.isActualTransactionActive()
                && TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            action.run();
                        }
                    });
            return;
        }
        action.run();
    }

    /**
     * 判断学生是否满足补发提醒的基础条件。
     */
    private boolean canReceiveNotice(AsStudentEntity student) {
        return student != null
                && student.getStudentIdentityId() != null
                && Objects.equals(student.getIsValid(), 1)
                && Objects.equals(student.getRegisterStatus(),
                STUDENT_REGISTER_STATUS_APPROVED)
                && Objects.equals(student.getSchoolRollStatus(), SCHOOL_ROLL_ACTIVE);
    }

    /**
     * 判断服务下是否存在学生可报名的公开课程。
     */
    private boolean hasPublicCourseForStudent(Long serviceId, AsStudentEntity student) {
        LambdaQueryWrapper<AsCourseClassEntity> courseWrapper = new LambdaQueryWrapper<>();
        courseWrapper.eq(AsCourseClassEntity::getServiceId, serviceId)
                .eq(AsCourseClassEntity::getEnrollType,
                        CoreConstant.ENROLL_TYPE_PUBLIC)
                .in(AsCourseClassEntity::getClassStatus, 1, 2)
                .eq(AsCourseClassEntity::getIsValid, 1);
        List<AsCourseClassEntity> courses = asCourseClassMapper.selectList(courseWrapper);
        return !CollectionUtils.isEmpty(courses)
                && courses.stream().anyMatch(course ->
                courseClassScopeSupport.isStudentInCourseScope(student, course));
    }

    /**
     * 判断学生在服务下是否存在公开报名或待确认邀请入口。
     */
    private boolean hasEnrollmentOpportunity(Long serviceId, AsStudentEntity student) {
        if (hasPublicCourseForStudent(serviceId, student)) {
            return true;
        }
        LambdaQueryWrapper<AsEnrollmentEntity> enrollmentWrapper =
                new LambdaQueryWrapper<>();
        enrollmentWrapper.eq(AsEnrollmentEntity::getSchoolId, student.getSchoolId())
                .eq(AsEnrollmentEntity::getServiceId, serviceId)
                .eq(AsEnrollmentEntity::getStudentId, student.getId())
                .eq(AsEnrollmentEntity::getEnrollSource, ENROLL_SOURCE_INVITE)
                .eq(AsEnrollmentEntity::getEnrollStatus,
                        CoreConstant.ENROLL_STATUS_PENDING)
                .eq(AsEnrollmentEntity::getIsValid, 1);
        return asEnrollmentMapper.selectCount(enrollmentWrapper) > 0;
    }

    /**
     * 按服务和稳定身份占位并发送一次补发提醒。
     */
    private void sendOnce(AsServiceEntity service, AsStudentEntity student) {
        List<Long> guardianUserIds = listGuardianUserIds(
                student.getSchoolId(), student.getStudentIdentityId());
        if (guardianUserIds.isEmpty()) {
            return;
        }
        AsServiceStudentNoticeEntity notice = new AsServiceStudentNoticeEntity();
        notice.setSchoolId(student.getSchoolId());
        notice.setServiceId(service.getId());
        notice.setStudentIdentityId(student.getStudentIdentityId());
        notice.setGuardianUserIds(JSON.toJSONString(guardianUserIds));
        notice.setNoticeType(NOTICE_TYPE_SUPPLEMENT);
        notice.setIsValid(1);
        notice.setCreateTime(new Date());
        notice.setCreateUser(currentOperatorId());
        try {
            if (asServiceStudentNoticeMapper.insert(notice) != 1) {
                log.warn("课后服务补发提醒占位失败，serviceId={}，studentIdentityId={}",
                        service.getId(), student.getStudentIdentityId());
                return;
            }
        } catch (DuplicateKeyException exception) {
            return;
        }

        try {
            LambdaQueryWrapper<TBaseUserEntity> userWrapper = new LambdaQueryWrapper<>();
            userWrapper.in(TBaseUserEntity::getId, guardianUserIds)
                    .eq(TBaseUserEntity::getIsValid, 1);
            List<TBaseUserEntity> users = tBaseUserDao.selectList(userWrapper);
            if (CollectionUtils.isEmpty(users)) {
                releaseNotice(notice);
                return;
            }
            EduclTermEntity term = educlTermMapper.selectById(service.getTermId());
            releaseNotice(notice);
        } catch (Exception exception) {
            releaseNotice(notice);
            log.error("课后服务补发提醒失败，serviceId={}，studentIdentityId={}",
                    service.getId(), student.getStudentIdentityId(), exception);
        }
    }

    /**
     * 释放发送失败的去重占位，允许后续重新补发。
     */
    private void releaseNotice(AsServiceStudentNoticeEntity notice) {
        if (notice == null || notice.getId() == null) {
            return;
        }
        try {
            asServiceStudentNoticeMapper.deleteById(notice.getId());
        } catch (Exception exception) {
            log.error("释放课后服务补发提醒占位失败，noticeId={}",
                    notice.getId(), exception);
        }
    }

    /**
     * 查询稳定身份当前有效绑定的家长用户。
     */
    private List<Long> listGuardianUserIds(Long schoolId, Long studentIdentityId) {
        LambdaQueryWrapper<AsGuardianStudentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsGuardianStudentEntity::getSchoolId, schoolId)
                .eq(AsGuardianStudentEntity::getStudentIdentityId, studentIdentityId)
                .eq(AsGuardianStudentEntity::getBindStatus, 1)
                .eq(AsGuardianStudentEntity::getIsValid, 1)
                .isNotNull(AsGuardianStudentEntity::getGuardianUserId);
        List<AsGuardianStudentEntity> relations =
                asGuardianStudentMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(relations)) {
            return Collections.emptyList();
        }
        return relations.stream()
                .map(AsGuardianStudentEntity::getGuardianUserId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * 取得当前操作人，后台任务没有登录上下文时使用系统用户。
     */
    private Long currentOperatorId() {
        Long operatorId = SmartRequestTokenUtil.getRequestUserId();
        return operatorId == null ? SYSTEM_USER_ID : operatorId;
    }
}
