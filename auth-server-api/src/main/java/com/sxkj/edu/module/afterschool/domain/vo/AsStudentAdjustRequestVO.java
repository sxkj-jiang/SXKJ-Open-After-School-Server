package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 学生调课申请视图对象。
 */
@Data
@ApiModel("学生调课申请视图对象")
public class AsStudentAdjustRequestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("申请ID")
    private Long id;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("申请编号")
    private String requestNo;

    @ApiModelProperty("课后服务ID")
    private Long serviceId;

    @ApiModelProperty("课后服务名称")
    private String serviceName;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("学生头像URL")
    private String avatarFileUrl;

    @ApiModelProperty("年级ID")
    private Long gradeId;

    @ApiModelProperty("年级名称")
    private String gradeName;

    @ApiModelProperty("班级ID")
    private Long classId;

    @ApiModelProperty("班级名称")
    private String className;

    @ApiModelProperty("报名记录ID")
    private Long enrollmentId;

    @ApiModelProperty("原课程班ID")
    private Long fromCourseClassId;

    @ApiModelProperty("原课程名称")
    private String fromCourseClassName;

    @ApiModelProperty("原去向类型：1课程班 2回家 3校外机构/培训机构")
    private Integer fromDestinationType;

    @ApiModelProperty("原去向名称")
    private String fromDestinationName;

    @ApiModelProperty("目标课程班ID")
    private Long toCourseClassId;

    @ApiModelProperty("目标课程名称")
    private String toCourseClassName;

    @ApiModelProperty("目标去向类型：1课程班 2回家 3校外机构/培训机构")
    private Integer destinationType;

    @ApiModelProperty("目标去向名称")
    private String destinationName;

    @ApiModelProperty("目标去向时间")
    private String destinationScheduleText;

    @ApiModelProperty("已学课时")
    private Integer learnedSessions;

    @ApiModelProperty("班主任ID")
    private Long headTeacherId;

    @ApiModelProperty("班主任姓名")
    private String headTeacherName;

    @ApiModelProperty("申请人用户ID")
    private Long applicantUserId;

    @ApiModelProperty("申请人类型")
    private Integer applicantRole;

    @ApiModelProperty("申请人姓名")
    private String applicantName;

    @ApiModelProperty("调课原因")
    private String reason;

    @ApiModelProperty("申请状态")
    private Integer requestStatus;

    @ApiModelProperty("申请状态描述")
    private String requestStatusDesc;

    @ApiModelProperty("驳回原因")
    private String rejectReason;

    @ApiModelProperty("申请时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    @ApiModelProperty("审核人用户ID")
    private Long handleUserId;

    @ApiModelProperty("审核人姓名")
    private String handleUserName;

    @ApiModelProperty("审核时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date handleTime;

    @ApiModelProperty("审核意见")
    private String handleOpinion;

    @ApiModelProperty("原课程信息")
    private CourseInfo fromCourse;

    @ApiModelProperty("目标课程信息")
    private CourseInfo toCourse;

    @ApiModelProperty("是否可查看详情")
    private Boolean canDetail;

    @ApiModelProperty("是否可审核")
    private Boolean canHandle;

    @ApiModelProperty("是否可导出")
    private Boolean canExport;

    @Data
    @ApiModel("学生调课课程信息")
    public static class CourseInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("课程班ID")
        private Long courseClassId;

        @ApiModelProperty("课后服务名称")
        private String serviceName;

        @ApiModelProperty("学期ID")
        private Long termId;

        @ApiModelProperty("学期名称")
        private String termName;

        @ApiModelProperty("课程类别名称")
        private String categoryName;

        @ApiModelProperty("课程班名称")
        private String courseClassName;

        @ApiModelProperty("任课教师")
        private String teacherName;

        @ApiModelProperty("上课地点")
        private String placeName;

        @ApiModelProperty("上课时间摘要")
        private String scheduleText;

        @ApiModelProperty("容量")
        private Integer capacity;

        @ApiModelProperty("已报名人数")
        private Integer enrolledCount;

        @ApiModelProperty("调课详情目标公开课名额，包含当前学生所在年级限额")
        private AsCourseQuotaVO quota;

        @ApiModelProperty("调课详情的目标课程超额提示，供审核确认使用")
        private String capacityWarning;
    }
}
