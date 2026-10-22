package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 根据学校查询教师
 *
 * @author system
 * @date 2026-06-19
 */
@Data
@ApiModel("根据学校查询教师")
public class AsTeacherByStudentIdVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("教师id")
    private Long teacherId;

    @ApiModelProperty("教师姓名")
    private String teacherName;

}
