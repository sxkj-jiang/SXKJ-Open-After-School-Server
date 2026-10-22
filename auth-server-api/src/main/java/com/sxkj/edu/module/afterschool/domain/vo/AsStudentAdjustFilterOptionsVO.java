package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 学生调课筛选项。
 */
@Data
@ApiModel("学生调课筛选项")
public class AsStudentAdjustFilterOptionsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("当前筛选学期ID")
    private Long termId;

    @ApiModelProperty("当前筛选学期名称")
    private String termName;

    @ApiModelProperty("学期")
    private List<Option> terms = new ArrayList<>();

    @ApiModelProperty("课后服务")
    private List<Option> services = new ArrayList<>();

    @ApiModelProperty("课程类别")
    private List<Option> categories = new ArrayList<>();

    @ApiModelProperty("课程班")
    private List<Option> courseClasses = new ArrayList<>();

    @ApiModelProperty("任课教师")
    private List<TeacherOption> teachers = new ArrayList<>();

    @ApiModelProperty("年级")
    private List<Option> grades = new ArrayList<>();

    @ApiModelProperty("行政班")
    private List<ClassOption> classes = new ArrayList<>();

    @ApiModelProperty("班主任")
    private List<TeacherOption> headTeachers = new ArrayList<>();

    @ApiModelProperty("状态")
    private List<Option> statuses = new ArrayList<>();

    @Data
    public static class Option implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long value;
        private String code;
        private String label;
        private Long termId;
        private String termName;
    }

    @Data
    public static class TeacherOption implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long teacherId;
        private String teacherNo;
        private String teacherName;
    }

    @Data
    public static class ClassOption implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long classId;
        private Long gradeId;
        private String label;
    }
}
