package com.sxkj.edu.module.afterschool.domain.statistics.ao;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CourseSessionPageStatisticsAO extends PageQuery {

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @NotNull(message = "学期ID不能为空")
    @ApiModelProperty(value = "学期ID", required = true)
    private Long termId;

    @ApiModelProperty(value = "课程类别ID")
    private Long categoryId;

    @ApiModelProperty(value = "查询信息（课程班名称或教师名称）")
    private String queryInfo;

    @ApiModelProperty(value = "排序方式：1-剩余课时数 2-完成进度")
    private Integer sortOrder;
}
