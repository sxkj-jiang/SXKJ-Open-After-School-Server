package com.sxkj.edu.domain.mult.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PortalArticleCategoryVO {

    @ApiModelProperty("文章类型主键id")
    private Long id;

    @ApiModelProperty("文章类型名称")
    private String articleTypeName;

    @ApiModelProperty("文章数据集合")
    private List<TPortalArticleManageVO> articleManageVOS;

}
