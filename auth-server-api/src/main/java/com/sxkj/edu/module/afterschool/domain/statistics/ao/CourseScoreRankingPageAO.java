package com.sxkj.edu.module.afterschool.domain.statistics.ao;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 课程评分排行-课程评价分页 查询参数
 */
@Data
public class CourseScoreRankingPageAO extends PageQuery {

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @NotNull(message = "学期ID不能为空")
    @ApiModelProperty(value = "学期ID", required = true)
    private Long termId;

    @ApiModelProperty(value = "课程类别ID")
    private Long categoryId;

    @ApiModelProperty(value = "排序方式：1-按评价平均得分 2-按评价数量")
    private Integer sortType;

}
