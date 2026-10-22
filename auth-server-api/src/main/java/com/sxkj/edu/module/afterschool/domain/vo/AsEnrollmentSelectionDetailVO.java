package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 学生选课详情对象
 *
 * @author system
 * @date 2026-05-20
 */
@Data
@ApiModel("学生选课详情对象")
public class AsEnrollmentSelectionDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("选课记录ID")
    private Long enrollmentId;

    @ApiModelProperty("学生信息")
    private StudentInfo student;

    @ApiModelProperty("课程信息")
    private CourseInfo course;

    @ApiModelProperty("操作信息")
    private OperationInfo operation;

    @ApiModelProperty("选课操作日志")
    private List<LogInfo> logs = new ArrayList<>();

    @Data
    public static class StudentInfo implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long studentId;
        private String studentNo;
        private String studentName;
        private Long gradeId;
        private String gradeName;
        private Long classId;
        private String className;
    }

    @Data
    public static class CourseInfo implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long serviceId;
        private String serviceName;
        private Long termId;
        private String termName;
        private Long courseClassId;
        private String courseClassName;
        private Long categoryId;
        private String categoryName;
        private Integer enrollType;
        private String enrollTypeName;
        private Long teacherId;
        private String teacherName;
        private String placeName;
        private Integer capacity;
        private Integer enrolledCount;
        private Integer destinationType;
        private String destinationName;
    }

    @Data
    public static class OperationInfo implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer operatorType;
        private Long operatorUserId;
        private String operatorName;
        private Date operateTime;
        private String remark;
    }

    /**
     * 选课操作日志展示项。
     */
    @Data
    public static class LogInfo implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long logId;
        private Long enrollmentId;
        private Long beforeCourseClassId;
        private Long afterCourseClassId;
        private Integer beforeEnrollStatus;
        private String beforeEnrollStatusName;
        private Integer afterEnrollStatus;
        private String afterEnrollStatusName;
        private Integer actionType;
        private String actionTypeName;
        private Integer operatorType;
        private Long operatorUserId;
        private String operatorName;
        private Date operateTime;
        private String remark;
    }
}
