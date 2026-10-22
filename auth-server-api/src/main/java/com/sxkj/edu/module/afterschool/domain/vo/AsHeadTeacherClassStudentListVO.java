package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 班主任查询名下自然班级学生列表VO
 *
 * @author system
 * @date 2026-06-13
 */
@Data
@ApiModel(value = "班主任自然班级学生列表VO", description = "以自然班级为维度返回班级信息及学生列表")
public class AsHeadTeacherClassStudentListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("班级ID")
    private Long classId;

    @ApiModelProperty("班级名称")
    private String className;

    @ApiModelProperty("班级学生列表")
    private List<StudentItem> students;

    /**
     * 学生信息项
     */
    @Data
    @ApiModel(value = "学生信息项", description = "学生基本信息")
    public static class StudentItem implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty("学生ID")
        private Long studentId;

        @ApiModelProperty("学期ID")
        private Long termId;

        @ApiModelProperty("学期名称")
        private String termName;

        @ApiModelProperty("学生姓名")
        private String studentName;

        @ApiModelProperty("学生学号")
        private String studentNo;

        @ApiModelProperty("性别：0男 1女")
        private Integer gender;
    }
}
