package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 家长端邀请选课列表行
 *
 * @author system
 * @date 2026-05-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("家长端邀请选课列表行")
public class AsGuardianInviteCourseRowVO extends AsGuardianCourseCardVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("邀请ID")
    private Long inviteId;

    @ApiModelProperty("邀请码")
    private String inviteCode;

    @ApiModelProperty("邀请过期时间")
    private Date expireTime;

    @ApiModelProperty("被邀请学生ID")
    private Long studentId;

    @ApiModelProperty("被邀请人数")
    private Integer invitedCount;

    @ApiModelProperty("邀请报名状态：enrolled/已报名 not_enrolled/未报名")
    private String inviteEnrollmentStatus;

    @ApiModelProperty("邀请状态名称")
    private String inviteStatusName;
}
