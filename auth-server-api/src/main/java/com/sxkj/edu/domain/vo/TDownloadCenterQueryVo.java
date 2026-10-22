package com.sxkj.edu.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description : 下载
 * @Author : JIANGJJ
 * @Date : 2024-4-30 12:20
 * @Company : SXKJ
 * @Version : 1.0
 **/
@Data
public class TDownloadCenterQueryVo {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("编号")
    private String downloadNum;

    @ApiModelProperty(value = "下载名称")
    private String name;

    @ApiModelProperty(value = "下载地址")
    private String downloadLink;

    @ApiModelProperty("发布时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishDate;

    @ApiModelProperty(value = "是否展示,1-是，0-否")
    private Integer display;
}
