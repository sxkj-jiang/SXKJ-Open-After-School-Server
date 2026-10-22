package com.sxkj.edu.module.afterschool.domain.statistics.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DataBoardAO {

    @NotNull(message = "类型不能为空")
    @ApiModelProperty(value = "类型：0-全部，1-数据概览，2-年级人数分布，3-课程类型人数分布，4-学生周出勤，5-教师周出勤", required = true)
    private Integer type;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @NotNull(message = "学期ID不能为空")
    @ApiModelProperty(value = "学期ID", required = true)
    private Long termId;

}
