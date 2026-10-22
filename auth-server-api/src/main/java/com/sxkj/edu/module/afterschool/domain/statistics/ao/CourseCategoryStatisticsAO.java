package com.sxkj.edu.module.afterschool.domain.statistics.ao;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("课程类别统计查询参数")
public class CourseCategoryStatisticsAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID",required = true)
    private Long schoolId;

    @NotNull(message = "学期ID不能为空")
    @ApiModelProperty(value = "学期ID",required = true)
    private Long termId;

    @ApiModelProperty(value = "课程类别ID")
    private Long categoryId;

    @ApiModelProperty(value = "授课教师姓名")
    private String teacherName;

    @ApiModelProperty(value = "年级id")
    private Long gradeId;
}
