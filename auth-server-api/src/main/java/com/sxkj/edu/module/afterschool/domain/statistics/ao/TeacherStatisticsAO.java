package com.sxkj.edu.module.afterschool.domain.statistics.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TeacherStatisticsAO {

    @NotNull(message = "类型不能为空")
    @ApiModelProperty(value = "类型：0-全部，1-出勤率分布，2-月度趋势，3-课程类型分布，4-月度课时趋势，5-点到率统计，6-评价分数分布", required = true)
    private Integer type;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID",required = true)
    private Long schoolId;

    @ApiModelProperty(value = "出勤率分布-学期ID",required = true)
    private Long cqlTermId;

    @ApiModelProperty(value = "月度趋势-学期ID",required = true)
    private Long ydqsTermId;

    @ApiModelProperty(value = "课程类型分布-学期ID",required = true)
    private Long kclxTermId;

    @ApiModelProperty(value = "月度课时趋势-学期ID",required = true)
    private Long ydksTermId;

    @ApiModelProperty(value = "点到率统计-学期ID",required = true)
    private Long ddlTermId;

    @ApiModelProperty(value = "评价分数分布-学期ID",required = true)
    private Long pjfsTermId;
}


