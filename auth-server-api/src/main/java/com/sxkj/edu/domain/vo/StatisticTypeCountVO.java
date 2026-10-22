package com.sxkj.edu.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StatisticTypeCountVO {

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("日期")
    private String date;

    @ApiModelProperty("数量")
    private Integer count;

}
