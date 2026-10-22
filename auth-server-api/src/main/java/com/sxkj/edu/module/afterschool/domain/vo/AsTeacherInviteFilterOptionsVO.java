package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 教师端邀请选课筛选项
 */
@Data
@ApiModel("教师端邀请选课筛选项")
public class AsTeacherInviteFilterOptionsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("业务所属学期ID")
    private Long termId;

    @ApiModelProperty("业务所属学期名称")
    private String termName;

    @ApiModelProperty("年级选项")
    private List<Option> grades = new ArrayList<>();

    @ApiModelProperty("班级选项")
    private List<ClassOption> classes = new ArrayList<>();

    @Data
    public static class Option implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long id;
        private String label;
    }

    @Data
    public static class ClassOption implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long id;
        private Long gradeId;
        private String label;
    }
}
