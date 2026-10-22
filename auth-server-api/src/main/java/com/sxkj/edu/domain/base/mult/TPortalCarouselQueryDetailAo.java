package com.sxkj.edu.domain.base.mult;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TPortalCarouselQueryDetailAo {

    @ApiModelProperty(value = "轮播图id")
    private Long id;

    @ApiModelProperty(value = "轮播图编号")
    private String culNum;

    @ApiModelProperty(value = "轮播图类型：1-首页/2-广告/3-校园新闻轮播,4-其他")
    private Integer artType;
}
