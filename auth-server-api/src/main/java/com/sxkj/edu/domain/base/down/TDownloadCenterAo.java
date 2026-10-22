package com.sxkj.edu.domain.base.down;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 *@Description : 下载
 *@Author : JIANGJJ
 *@Date : 2024-4-30 12:20
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Data
public class TDownloadCenterAo {

    /**
     * 编号
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 编号
     */
    @ApiModelProperty("编号")
    private String downloadNum;

    /**
     * 下载名称
     */
    @ApiModelProperty(value = "下载名称",required = true)
    @NotBlank(message = "下载名称不能为空")
    private String name;

    /**
     * 下载地址
     */
    @ApiModelProperty(value = "下载地址",required = true)
    @NotBlank(message = "下载地址不能为空")
    private String downloadLink;

    /**
     * 发布时间
     */
    @ApiModelProperty("发布时间")
    private Date publishDate;

    /**
     * 是否展示
     */
    @ApiModelProperty(value = "是否展示",required = true)
    @NotBlank(message = "是否展示不能为空")
    private String display;
}
