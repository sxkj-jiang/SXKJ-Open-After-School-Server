package com.sxkj.edu.domain.mult.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ArticleManageCarouselAO {

    @ApiModelProperty(value = "主键id，更新时必填")
    private Long id;

    @ApiModelProperty(value = "通知公告标题",required = true)
    @NotBlank(message = "通知公告标题不能为空")
    private String newsName;

    @ApiModelProperty(value = "是否展示1-是，0-否",required = true)
    @NotNull(message = "是否展示不能为空")
    private Integer isShow;

    @ApiModelProperty(value = "文章内容",required = true)
    @NotBlank(message = "文章内容不能为空")
    private String content;
}
