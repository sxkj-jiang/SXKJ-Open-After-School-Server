package com.sxkj.edu.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PlateTrafficTrendStatisticsVo {

    @ApiModelProperty("X轴关键词")
    private String xKeyWord;

    @ApiModelProperty("页面访问数")
    private Integer pvCount;

    @ApiModelProperty("独立用户数")
    private Integer uvCount;

    public PlateTrafficTrendStatisticsVo(){
        this.pvCount = 0;
        this.uvCount = 0;
    }
}
