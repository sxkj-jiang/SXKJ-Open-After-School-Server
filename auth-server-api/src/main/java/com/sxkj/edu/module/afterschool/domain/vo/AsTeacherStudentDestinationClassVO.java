package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 班主任查询自然班级学生去向-班级二级VO
 *
 * @author system
 * @date 2026-06-05
 */
@Data
@ApiModel("班主任查询自然班级学生去向-班级VO")
public class AsTeacherStudentDestinationClassVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("自然班级ID")
    private Long classId;

    @ApiModelProperty("自然班级名称")
    private String className;

    @ApiModelProperty("班级人数")
    private Integer classStudentCount;

    @ApiModelProperty("班级学生去向明细")
    private List<AsHeadTeacherStudentDestinationVO> studentList;
}
