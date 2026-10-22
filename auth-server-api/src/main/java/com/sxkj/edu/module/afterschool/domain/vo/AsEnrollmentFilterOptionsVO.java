package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 学生选课筛选项
 *
 * @author system
 * @date 2026-05-20
 */
@Data
@ApiModel("学生选课筛选项")
public class AsEnrollmentFilterOptionsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课后服务")
    private List<Option> services = new ArrayList<>();

    @ApiModelProperty("课程班")
    private List<Option> courseClasses = new ArrayList<>();

    @ApiModelProperty("报名方式")
    private List<Option> enrollTypes = new ArrayList<>();

    @ApiModelProperty("年级")
    private List<Option> grades = new ArrayList<>();

    @ApiModelProperty("班级")
    private List<Option> classes = new ArrayList<>();

    @Data
    public static class Option implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long id;
        private Integer value;
        private String label;
    }
}
