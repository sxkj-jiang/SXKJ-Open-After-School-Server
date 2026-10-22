package com.sxkj.edu.domain.mult.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class TPortalArticleManageDetailVO {

    @ApiModelProperty(value = "主键id，更新时必填")
    private Long id;

    @ApiModelProperty(value = "文章名称")
    private String newsName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "文章类型id")
    private Long articleTypeId;

    @ApiModelProperty(value = "文章简介")
    private String introduction;

    @ApiModelProperty(value = "文章内容")
    private String content;

    @ApiModelProperty(value = "是否展示1-是，0-否")
    private Integer isShow;

    @ApiModelProperty(value = "文章图片路径")
    private String photoUrl;

    @ApiModelProperty(value = "发布时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy年MM月dd日")
    private Date createTime;

    @ApiModelProperty(value = "是否外部站点 1-是 0-否")
    private Integer isOutside;

    @ApiModelProperty(value = "链接地址")
    private String linkUrl;
}
