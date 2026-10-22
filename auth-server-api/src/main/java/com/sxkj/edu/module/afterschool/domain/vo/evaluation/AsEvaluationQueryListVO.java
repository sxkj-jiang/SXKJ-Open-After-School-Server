package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 评价查询列表VO
 *
 * @author system
 * @date 2026-05-30
 */
@Data
public class AsEvaluationQueryListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("任务ID")
    private Long taskId;

    @ApiModelProperty("评价任务名称")
    private String taskName;

    @ApiModelProperty("课后服务名称")
    private String serviceName;

    @ApiModelProperty("课程名称")
    private String courseName;

    @ApiModelProperty("课程类别")
    private String categoryName;

    @ApiModelProperty("课程类别ID")
    private Long categoryId;

    @ApiModelProperty("开课周期")
    private String coursePeriod;

    @ApiModelProperty("可选年级")
    private String gradeScope;

    @ApiModelProperty("教师")
    private String teacherName;

    @ApiModelProperty("教师ID")
    private Long teacherId;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("上课地点")
    private String placeName;

    @ApiModelProperty("评价人数")
    private Integer evaluatorCount;

    @ApiModelProperty("平均评分")
    private Double averageScore;
}
