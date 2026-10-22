package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 家长查询选课记录-学期维度VO（一级维度）
 *
 * @author system
 * @date 2026-06-11
 */
@Data
public class AsParentEnrollmentTermVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("课程列表（二级维度）")
    private List<AsParentEnrollmentCourseVO> courseList;
}
