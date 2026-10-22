package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 班主任查询自然班级学生去向VO
 * 以学期名称为一级维度、自然班级名称为二级维度返回
 *
 * @author system
 * @date 2026-06-05
 */
@Data
@ApiModel("班主任查询自然班级学生去向VO")
public class AsTeacherStudentDestinationTermVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("自然班级学生汇总")
    private List<AsTeacherStudentDestinationClassVO> classVOList;
}
