  package com.sxkj.edu.module.afterschool.domain.query;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("课程质量分页查询参数")
public class CourseQualityPageAO extends PageQuery {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课程名称")
    private String courseName;

    @ApiModelProperty("课程类别ID")
    private String categoryId;

    @ApiModelProperty("年级ID")
    private Long gradeId;

    @ApiModelProperty("教师ID")
    private Long teacherId;

    @ApiModelProperty("学期ID")
    private Long termId;
}
