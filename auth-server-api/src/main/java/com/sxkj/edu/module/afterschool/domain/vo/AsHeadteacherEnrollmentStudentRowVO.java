package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 班主任课后报名学生行对象。
 * <p>以学生+学期维度聚合，包含学生基本信息、学期信息、报名状态分类以及该学期下的课程列表。
 * 状态分类支持全部/已报名/已分配/未分配四种类型。</p>
 *
 * @author wangyouhuai
 * @date 2026-06-12
 */
@Data
@ApiModel("班主任课后报名学生行对象")
public class AsHeadteacherEnrollmentStudentRowVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("年级ID")
    private Long gradeId;

    @ApiModelProperty("年级名称")
    private String gradeName;

    @ApiModelProperty("班级ID")
    private Long classId;

    @ApiModelProperty("班级名称")
    private String className;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("Tab类型：registered已报名 assigned已分配 unassigned未分配")
    private String tabType;

    @ApiModelProperty("状态名称")
    private String statusName;

    @ApiModelProperty("已分配或已报名课程数量")
    private Integer courseCount = 0;

    @ApiModelProperty("待分配课后服务数量")
    private Integer unassignedCount = 0;

    @ApiModelProperty("本条报名/待分配服务信息")
    private CourseInfo courseInfo;

    @Data
    @ApiModel("班主任课后报名课程对象")
    public static class CourseInfo implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty("报名记录ID")
        private Long enrollmentId;

        @ApiModelProperty("服务ID")
        private Long serviceId;

        @ApiModelProperty("服务名称")
        private String serviceName;

        @ApiModelProperty("课程班ID，未分配时为空")
        private Long courseClassId;

        @ApiModelProperty("课程班名称，未分配时为空")
        private String courseClassName;

        @ApiModelProperty("课程真实上课时间，已报名/已分配课程使用；未分配剩余时间请使用unregisteredScheduleText")
        private String scheduleTimeDesc;

        @ApiModelProperty("课程类别ID")
        private Long categoryId;

        @ApiModelProperty("课程类别名称")
        private String categoryName;

        @ApiModelProperty("报名方式")
        private Integer enrollType;

        @ApiModelProperty("报名方式名称")
        private String enrollTypeName;

        @ApiModelProperty("报名来源")
        private Integer enrollSource;

        @ApiModelProperty("报名来源名称")
        private String enrollSourceName;

        @ApiModelProperty("报名状态")
        private Integer enrollStatus;

        @ApiModelProperty("报名状态名称")
        private String enrollStatusName;

        @ApiModelProperty("课后去向类型：1课程班 2回家 3培训机构/校外机构 4其他")
        private Integer destinationType;

        @ApiModelProperty("课后去向名称")
        private String destinationName;

        @ApiModelProperty("未报名课程的课后时间摘要")
        private String unregisteredScheduleText;

        @ApiModelProperty("未报名课程的课后时间明细")
        private List<String> unregisteredScheduleItems = new ArrayList<>();

        @ApiModelProperty("课程是否已经开课")
        private Boolean courseStarted;

        @ApiModelProperty("报名/分配时间")
        private Date enrollTime;

        @ApiModelProperty("是否可分配")
        private Boolean canAssign;

        @ApiModelProperty("是否可调整")
        private Boolean canAdjust;
    }
}
