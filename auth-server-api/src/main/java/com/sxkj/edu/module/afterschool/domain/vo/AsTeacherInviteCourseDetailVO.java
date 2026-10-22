package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 教师端邀请课程详情
 */
@Data
@ApiModel("教师端邀请课程详情")
public class AsTeacherInviteCourseDetailVO implements Serializable {

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

    @ApiModelProperty("报名方式名称")
    private String enrollTypeName;

    @ApiModelProperty("是否需要家长确认")
    private Integer parentConfirmRequired;

    @ApiModelProperty("上课时间摘要")
    private String scheduleSummary;

    @ApiModelProperty("时间安排")
    private List<AsGuardianScheduleItemVO> scheduleItems = new ArrayList<>();

    @ApiModelProperty("可选学生总数")
    private Integer totalStudentCount;

    @ApiModelProperty("已报名人数")
    private Integer enrolledCount;

    @ApiModelProperty("待确认人数")
    private Integer pendingConfirmCount;

    @ApiModelProperty("未邀请人数")
    private Integer notInvitedCount;

    @ApiModelProperty("最近邀请批次ID")
    private Long latestInviteId;

    @ApiModelProperty("邀请码")
    private String inviteCode;

    @ApiModelProperty("邀请链接")
    private String inviteUrl;

    @ApiModelProperty("二维码访问地址")
    private String inviteQrFileUrl;

    @ApiModelProperty("邀请过期时间")
    private Date expireTime;

    @ApiModelProperty("是否可发起邀请")
    private Boolean canInvite;

    @ApiModelProperty("是否可查看学生")
    private Boolean canViewStudents;

    @ApiModelProperty("操作按钮文案")
    private String actionText;

    @ApiModelProperty("不可操作原因")
    private String disabledReason;
}
