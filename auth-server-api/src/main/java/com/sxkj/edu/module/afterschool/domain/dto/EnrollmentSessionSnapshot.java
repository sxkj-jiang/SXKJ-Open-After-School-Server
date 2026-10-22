package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.module.afterschool.domain.entity.AsEnrollmentEntity;

/**
 * 单个学生在课程班上的课次报名快照，保留原选课来源、状态和邀请/确认信息。
 */
public class EnrollmentSessionSnapshot {
    private final AsEnrollmentEntity enrollment;

    public EnrollmentSessionSnapshot(AsEnrollmentEntity enrollment) {
        this.enrollment = enrollment;
    }

    public AsEnrollmentEntity toTemplate() {
        AsEnrollmentEntity template = new AsEnrollmentEntity();
        template.setSchoolId(enrollment.getSchoolId());
        template.setServiceId(enrollment.getServiceId());
        template.setCourseClassId(enrollment.getCourseClassId());
        template.setStudentId(enrollment.getStudentId());
        template.setStudentNo(enrollment.getStudentNo());
        template.setEnrollSource(enrollment.getEnrollSource());
        template.setEnrollStatus(enrollment.getEnrollStatus());
        template.setInviteId(enrollment.getInviteId());
        template.setParentConfirmRequired(enrollment.getParentConfirmRequired());
        template.setGuardianConfirmUserId(enrollment.getGuardianConfirmUserId());
        template.setGuardianConfirmTime(enrollment.getGuardianConfirmTime());
        template.setOperatorType(enrollment.getOperatorType());
        template.setOperatorUserId(enrollment.getOperatorUserId());
        template.setOperatorName(enrollment.getOperatorName());
        template.setEnrollTime(enrollment.getEnrollTime());
        template.setRemark(enrollment.getRemark());
        return template;
    }
}
