package com.sxkj.edu.domain.base.mult;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TPortalLinkVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("来源分类")
    private String linkType;

    @ApiModelProperty("链接名称")
    private String linkName;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("状态，1-启用，其他禁用")
    private Integer status;

    @ApiModelProperty("链接地址")
    private String linkUrl;

    @ApiModelProperty("发布时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishDate;

    @ApiModelProperty("链接说明")
    private String linkDesc;

}
