package com.sxkj.edu.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description: PlateOperateEventStatisticsVo
 * @Author yanghui
 * @Date 2024年12月27日
 */
@Data
public class PlateOperateEventStatisticsVo {

    @ApiModelProperty("访问ip")
    private String ip;

    @ApiModelProperty("访问端平台 网页端 PTPC,管理端 JWPC,平台小程序 XCX")
    private String type;

    @ApiModelProperty("页面名称")
    private String model;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("事件名称")
    private String event;

    @ApiModelProperty("访问时间 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
