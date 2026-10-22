package com.sxkj.edu.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PlateOperateTimeStatisticsVo {

    @ApiModelProperty("日期")
    private String tjDate;

    @ApiModelProperty("使用时长")
    private Integer sumHours;

    public PlateOperateTimeStatisticsVo() {
        this.sumHours = 0;
    }
}
