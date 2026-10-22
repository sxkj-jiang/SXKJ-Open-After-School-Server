package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 教师调课筛选项。
 */
@Data
@ApiModel("教师调课筛选项")
public class AsTeacherAdjustFilterOptionsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课程类别")
    private List<Option> categories = new ArrayList<>();

    @ApiModelProperty("教师")
    private List<TeacherOption> teachers = new ArrayList<>();

    @ApiModelProperty("状态")
    private List<Option> statuses = new ArrayList<>();

    @Data
    @ApiModel("教师调课通用选项")
    public static class Option implements Serializable {
        private static final long serialVersionUID = 1L;
        @ApiModelProperty("选项值")
        private Long value;
        @ApiModelProperty("选项编码")
        private String code;
        @ApiModelProperty("选项名称")
        private String label;
    }

    @Data
    @ApiModel("教师调课教师选项")
    public static class TeacherOption implements Serializable {
        private static final long serialVersionUID = 1L;
        @ApiModelProperty("教师ID")
        private Long teacherId;
        @ApiModelProperty("教师姓名")
        private String teacherName;
        @ApiModelProperty("教师工号")
        private String teacherNo;
    }
}
