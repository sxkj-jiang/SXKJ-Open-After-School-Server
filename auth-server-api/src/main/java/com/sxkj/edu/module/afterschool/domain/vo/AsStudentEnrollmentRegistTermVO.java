package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 学生报名记录
 * @author system
 * @date 2026-06-11
 */
@Data
public class AsStudentEnrollmentRegistTermVO implements Serializable {
    private static final long serialVersionUID = 1L;

    // ========== 学生信息 ==========

    @ApiModelProperty("排课服务ID")
    private Long serviceId;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("学生稳定身份ID")
    private Long studentIdentityId;

    @ApiModelProperty("学生快照所属学期ID")
    private Long termId;

    @ApiModelProperty("学生快照所属学期名称")
    private String termName;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("报名时间 格式：yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date enrollmentTime;

    @ApiModelProperty("报名状态")
    private Integer enrollStatus;
}
