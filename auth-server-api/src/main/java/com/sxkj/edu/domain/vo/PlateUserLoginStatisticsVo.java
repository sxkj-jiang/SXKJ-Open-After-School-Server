package com.sxkj.edu.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PlateUserLoginStatisticsVo {

    @ApiModelProperty("日期")
    private String date;

    @ApiModelProperty("管理端数量")
    private Integer gldCount;

    @ApiModelProperty("官网端数量")
    private Integer gwdCount;

    @ApiModelProperty("移动端数量")
    private Integer yddCount;

    public PlateUserLoginStatisticsVo(){
        this.gldCount = 0;
        this.gwdCount = 0;
        this.yddCount = 0;
    }
}
