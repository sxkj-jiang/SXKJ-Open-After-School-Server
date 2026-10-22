package com.sxkj.edu.domain.mult.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TPortalArticleManageAO {

    @ApiModelProperty(value = "主键id，更新时必填")
    private Long id;

    @ApiModelProperty(value = "文章名称",required = true)
    @NotBlank(message = "文章名称不能为空")
    private String newsName;

    @ApiModelProperty(value = "文章类型id",required = true)
    @NotNull(message = "文章类型id不能为空")
    private Long articleTypeId;

    @ApiModelProperty(value = "文章简介",required = true)
    @NotBlank(message = "文章简介不能为空")
    private String introduction;

    @ApiModelProperty(value = "文章内容(外部站点为否必填)")
    private String content;

    @ApiModelProperty(value = "是否展示1-是，0-否",required = true)
    @NotNull(message = "是否展示不能为空")
    private Integer isShow;

    @ApiModelProperty(value = "是否外部站点 1-是 0-否")
    private Integer isOutside;

    @ApiModelProperty(value = "链接地址(外部站点为是必填)")
    private String linkUrl;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "文章图片路径")
    private String photoUrl;
}
