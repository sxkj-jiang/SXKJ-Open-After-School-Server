package com.sxkj.edu.domain.ao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class PlateVisitRecordStatisticsPageQueryAo extends PageQuery {

    /*访问设备记录-搜索条件*/
    @ApiModelProperty(value = "访问设备记录-平台类型:平台网页端 PTPC,管理端 JWPC,平台小程序 XCX")
    private String visitRecordPlateType;

    @ApiModelProperty(value = "访问设备记录-时间段类型:1-今日、2-本周、3-本月")
    private Integer visitRecordTimeType;

    @ApiModelProperty(value = "访问设备记录-开始日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date visitRecordStartDate;

    @ApiModelProperty(value = "访问设备记录-结束日期 格式:yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date visitRecordEndDate;
}
