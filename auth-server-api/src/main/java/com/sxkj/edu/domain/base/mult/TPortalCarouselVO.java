package com.sxkj.edu.domain.base.mult;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TPortalCarouselVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("轮播图名称")
    private String culName;

    @ApiModelProperty("轮播图编号")
    private String culNum;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("状态，1-启用，其他禁用")
    private Integer status;

    @ApiModelProperty("地址url")
    private String culUrl;

    @ApiModelProperty("轮播图描述")
    private String culDesc;

    @ApiModelProperty("发布时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishDate;

    @ApiModelProperty(value = "轮播图类型：1-首页主业/2-广告/3-新闻轮播,4-其他")
    private Integer artType;

    @ApiModelProperty("外部链接地址")
    private String linkUrl;
}
