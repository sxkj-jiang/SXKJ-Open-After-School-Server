package com.sxkj.edu.domain.sys.dto;
import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;


/**
 *@Description : 用户登录日志
 *@Author : JIANGJJ
 *@Date : 2022-9-7 10:36
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Data
public class UserLoginLogDTO {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("员工id")
    private Long userId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("用户ip")
    private String remoteIp;

    @ApiModelProperty("用户端口")
    private Integer remotePort;

    @ApiModelProperty("浏览器")
    private String remoteBrowser;

    @ApiModelProperty("操作系统")
    private String remoteOs;

    @ApiModelProperty("登录状态")
    private Integer loginStatus;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


}
