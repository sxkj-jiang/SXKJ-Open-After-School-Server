package com.sxkj.edu.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description: PlateOperatePvStatisticsVo
 * @Author yanghui
 * @Date 2024年12月27日
 */
@Data
public class PlateOperatePvStatisticsVo {

    @ApiModelProperty("访问ip")
    private String ip;

    @ApiModelProperty("访问端 平台网页端 PTPC,管理端 JWPC,平台小程序 XCX")
    private String type;

    @ApiModelProperty("页面名称")
    private String model;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("访问时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
