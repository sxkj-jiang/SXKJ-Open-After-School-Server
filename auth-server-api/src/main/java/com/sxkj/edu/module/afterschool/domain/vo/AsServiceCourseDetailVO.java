package com.sxkj.edu.module.afterschool.domain.vo;

import com.sxkj.edu.module.afterschool.domain.dto.AsCourseScheduleRuleAO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 课后服务课程班详情。
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("课后服务课程班详情")
public class AsServiceCourseDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课程班ID")
    private Long id;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("课程班编码")
    private String courseClassCode;

    @ApiModelProperty("课程类别ID")
    private Long categoryId;

    @ApiModelProperty("课程类别名称")
    private String categoryName;

    @ApiModelProperty("学习模块ID")
    private Long moduleId;

    @ApiModelProperty("报名方式：1公开选课 2邀请选课 3行政班安排")
    private Integer enrollType;

    @ApiModelProperty("报名方式文案")
    private String enrollTypeName;

    @ApiModelProperty("适用行政班ID集合JSON")
    private String applicableClassIds;

    @ApiModelProperty("适用年级ID集合JSON")
    private String applicableGradeIds;

    @ApiModelProperty("邀请报名时是否需要家长确认：0否 1是")
    private Integer parentConfirmRequired;

    @ApiModelProperty("公开报名性别要求：0不限 1男 2女")
    private Integer genderRequirement;

    @ApiModelProperty("容量")
    private Integer capacity;

    @ApiModelProperty("公开课年级限额及占用情况；非公开课为空")
    private AsCourseQuotaVO quota;

    @ApiModelProperty("已报名人数")
    private Integer enrolledCount;

    @ApiModelProperty("剩余名额")
    private Integer remainingCount;

    @ApiModelProperty("授课教师ID")
    private Long teacherId;

    @ApiModelProperty("授课教师姓名")
    private String teacherName;

    @ApiModelProperty("上课地点名称")
    private String placeName;

    @ApiModelProperty("上课地点id")
    private String placeId;

    @ApiModelProperty("课程介绍")
    private String intro;

    @ApiModelProperty("课程班状态")
    private Integer classStatus;

    @ApiModelProperty("课程班状态文案")
    private String classStatusName;

    @ApiModelProperty("排课规则")
    private List<AsCourseScheduleRuleAO> scheduleRules = new ArrayList<>();

    @ApiModelProperty("已生成课次数")
    private Integer sessionCount = 0;
}
