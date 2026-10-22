package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 教师端邀请课程列表行
 */
@Data
@ApiModel("教师端邀请课程列表行")
public class AsTeacherInviteCourseRowVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课程名称")
    private String courseClassName;

    @ApiModelProperty("课程类别")
    private String categoryName;

    @ApiModelProperty("课后服务ID")
    private Long serviceId;

    @ApiModelProperty("课后服务名称")
    private String serviceName;

    @ApiModelProperty("业务所属学期ID")
    private Long termId;

    @ApiModelProperty("业务所属学期名称")
    private String termName;

    @ApiModelProperty("授课教师")
    private String teacherName;

    @ApiModelProperty("上课地点")
    private String placeName;

    @ApiModelProperty("上课时间摘要")
    private String scheduleSummary;

    @ApiModelProperty("容量")
    private Integer capacity;

    @ApiModelProperty("已报名人数")
    private Integer enrolledCount;

    @ApiModelProperty("最近邀请批次选中人数")
    private Integer invitedCount;

    @ApiModelProperty("待家长确认人数")
    private Integer pendingConfirmCount;

    @ApiModelProperty("邀请状态：pending/invited")
    private String inviteCourseStatus;

    @ApiModelProperty("邀请状态名称")
    private String inviteCourseStatusName;

    @ApiModelProperty("最近有效邀请批次ID")
    private Long latestInviteId;

    @ApiModelProperty("是否可继续发起邀请")
    private Boolean canInvite;

    @ApiModelProperty("是否可查看详情")
    private Boolean canDetail;

    @ApiModelProperty("不可邀请原因")
    private String disabledReason;
}
