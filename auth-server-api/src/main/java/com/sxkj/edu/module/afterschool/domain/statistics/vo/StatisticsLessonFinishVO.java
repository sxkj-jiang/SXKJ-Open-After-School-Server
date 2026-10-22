package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StatisticsLessonFinishVO {

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("总课时")
    private Integer totalClassHours;

    @ApiModelProperty("已完成课时")
    private Integer finishedClassHours;

    @ApiModelProperty("未完成课时")
    private Integer unfinishedClassHours;

}
