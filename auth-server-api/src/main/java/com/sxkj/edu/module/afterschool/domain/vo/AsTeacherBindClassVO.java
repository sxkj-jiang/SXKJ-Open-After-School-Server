package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 教师任课班级VO
 *
 * @author system
 * @date 2026-09-09
 */
@Data
@ApiModel(value = "教师任课班级VO", description = "教师任课班级信息，班级名称包含年级名称")
public class AsTeacherBindClassVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("班级ID")
    private Long classId;

    @ApiModelProperty("班级名称（年级名称+班级名称）")
    private String className;
}
