package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("任课老师端课程评价列表VO")
public class TeacherCourseEvaluationListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("任务ID")
    private Long taskId;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课程名称")
    private String courseName;

    @ApiModelProperty("上课时间描述（如：每周一、三、五 16:30-18:00）")
    private String classTimeDesc;

    @ApiModelProperty("开课周期（如：2026/12/12 - 2027/12/12）")
    private String coursePeriod;

    @ApiModelProperty("上课地点")
    private String placeName;

    @ApiModelProperty(hidden = true)
    private String placeId;

    @ApiModelProperty("课程类型（类别名称）")
    private String categoryName;

    @ApiModelProperty("平均评分")
    private BigDecimal averageScore;

    @ApiModelProperty("已评人数")
    private Integer evaluationCount;
}
