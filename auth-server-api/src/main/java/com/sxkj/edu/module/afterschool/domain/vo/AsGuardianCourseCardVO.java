package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 家长端课程卡片
 *
 * @author system
 * @date 2026-05-21
 */
@Data
@ApiModel("家长端课程卡片")
public class AsGuardianCourseCardVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("课后服务ID")
    private Long serviceId;

    @ApiModelProperty("业务所属学期ID")
    private Long termId;

    @ApiModelProperty("业务所属学期名称")
    private String termName;

    @ApiModelProperty("课程类别ID")
    private Long categoryId;

    @ApiModelProperty("课程类别名称")
    private String categoryName;

    @ApiModelProperty("教师ID")
    private Long teacherId;

    @ApiModelProperty("教师姓名")
    private String teacherName;

    @ApiModelProperty("上课地点")
    private String placeName;

    @ApiModelProperty("上课时间摘要")
    private String scheduleText;

    @ApiModelProperty("上课时间安排")
    private List<AsGuardianScheduleItemVO> scheduleItems = new ArrayList<>();

    @ApiModelProperty("容量")
    private Integer capacity;

    @ApiModelProperty("公开课限额；使用quota.availableCount展示当前孩子可用名额，非公开课为空")
    private AsCourseQuotaVO quota;

    @ApiModelProperty("已报名人数")
    private Integer enrolledCount;

    @ApiModelProperty("剩余名额")
    private Integer remainingCount;

    @ApiModelProperty("报名进度百分比")
    private Integer progressPercent;

    @ApiModelProperty("是否可报名")
    private Boolean available;

    @ApiModelProperty("状态编码：available/full/applied/closed/not_scope/gender_limited/disabled/applied_other")
    private String availableStatus;

    @ApiModelProperty("状态名称")
    private String statusName;

    @ApiModelProperty("按钮文案")
    private String actionText;

    @ApiModelProperty("是否可报名")
    private Boolean canApply;

    @ApiModelProperty("是否可查看详情")
    private Boolean canDetail;

    @ApiModelProperty("是否可取消")
    private Boolean canCancel;

    @ApiModelProperty("当前学生在该服务下的报名记录ID")
    private Long enrollmentId;

    @ApiModelProperty("不可报名原因")
    private String unavailableReason;

    @ApiModelProperty("报名状态：not_started-未到报名时间/enrolling-报名中/ended-报名已结束")
    private String enrollStatus;
}
