package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AsCourseCategoryListQueryAO {

    @ApiModelProperty(value = "所属学校ID",required = true)
    private Long schoolId;

    @ApiModelProperty("类别名称(模糊)")
    private String categoryName;

    @ApiModelProperty("父类别ID")
    private Long parentId;

    @ApiModelProperty("是否只查询父类 0-否 1-是")
    private Integer onlyParent;
}
