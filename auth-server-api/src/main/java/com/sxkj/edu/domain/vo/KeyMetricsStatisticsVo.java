package com.sxkj.edu.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class KeyMetricsStatisticsVo {

    @ApiModelProperty("页面访问量")
    private Integer pageVisitNum;

    @ApiModelProperty("独立用户数")
    private Integer alongUserNum;

    @ApiModelProperty("页面事件数")
    private Integer pageEventNum;

    @ApiModelProperty("用户登录数")
    private Integer userLoginNum;

    public KeyMetricsStatisticsVo(){
        this.pageVisitNum = 0;
        this.alongUserNum = 0;
        this.pageEventNum = 0;
        this.userLoginNum = 0;
    }
}
