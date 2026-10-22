package com.sxkj.edu.domain.base.mult;

import com.sxkj.edu.common.validation.ChineseTitleAnno;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TPortalCarouselUptAo {

    @ApiModelProperty(value = "主键id，更新时必填", required = true)
    private Long id;

    @ApiModelProperty(value = "学校id", required = true)
    @NotNull(message = "学校id不能为空")
    private Long schoolId;

    @ApiModelProperty(value = "轮播图名称", required = true)
    @ChineseTitleAnno
    private String culName;

    @ApiModelProperty(value = "排序", required = true)
    private Integer sort;

    @ApiModelProperty(value = "状态,1-启用，其他禁用", required = true)
    private Integer status;

    @ApiModelProperty(value = "地址id", required = true)
    @NotBlank(message = "封面不能为空")
    private String culUrl;

    @ApiModelProperty(value = "轮播图类型：1-首页/2-广告/3-校园新闻轮播,4-其他", required = true)
    @NotNull(message = "轮播图类型不为空")
    private Integer artType;

    @ApiModelProperty("轮播图描述")
    private String culDesc;

    @ApiModelProperty("外部链接地址")
    private String linkUrl;

}
