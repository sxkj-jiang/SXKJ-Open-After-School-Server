package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 课时完成情况-课时详情分页列表VO
 *
 * @author system
 * @date 2026-06-25
 */
@Data
@ApiModel("课时完成情况-课时详情分页列表VO")
public class CourseSessionDetailPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("课程班类别名称")
    private String categoryName;

    @ApiModelProperty("总课时数")
    private Integer totalSessions;

    @ApiModelProperty("已上课课时数")
    private Integer completedSessions;

    @ApiModelProperty("剩余课时数")
    private Integer remainingSessions;

    @ApiModelProperty("完成率（百分比数值，如：75.5）")
    private Double completionRate;

    @ApiModelProperty("完成率（百分比字符串，如：75.5%）")
    private String completionRateRes;

    @ApiModelProperty("授课教师名称")
    private String teacherName;

    @ApiModelProperty("课程状态：0-未开始 1-进行中 2-已结束")
    private Integer courseStatus;

    @ApiModelProperty("课程状态描述")
    private String courseStatusDesc;
}
