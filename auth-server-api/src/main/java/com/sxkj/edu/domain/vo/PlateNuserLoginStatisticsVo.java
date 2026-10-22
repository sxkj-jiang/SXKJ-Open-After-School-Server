package com.sxkj.edu.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class PlateNuserLoginStatisticsVo {

    @ApiModelProperty("未登录用户访问端 GLD-管理端 XCX-小程序")
    private String accessTerminal;

    @ApiModelProperty("日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date date;

    @ApiModelProperty("数量")
    private Integer count;
}
