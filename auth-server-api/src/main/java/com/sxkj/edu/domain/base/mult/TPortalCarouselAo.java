package com.sxkj.edu.domain.base.mult;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class TPortalCarouselAo extends PageQuery {

    @ApiModelProperty(value = "轮播图名称/编号")
    private String keyword;

    @ApiModelProperty(value = "轮播图名称")
    private String culName;

    @ApiModelProperty(value = "启用状态，1-启用，其他禁用")
    private Integer status;

    @ApiModelProperty(value = "开始日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "轮播图类型：1-首页/2-广告/3-校园新闻轮播")
    private Integer artType;

    @ApiModelProperty(value = "学校id")
    private Long schoolId;
}
