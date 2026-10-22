package com.sxkj.edu.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class PlateVisitRecordStatisticsVo {

    @ApiModelProperty("员工id")
    private Long userId;

    @ApiModelProperty("员工姓名")
    private String userName;

    @ApiModelProperty("用户ip")
    private String remoteIp;

    @ApiModelProperty("浏览器")
    private String remoteBrowser;

    @ApiModelProperty("操作系统")
    private String remoteOs;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("访问端 平台网页端 PTPC,管理端 JWPC,平台小程序 XCX")
    private String accessTerminal;
}
