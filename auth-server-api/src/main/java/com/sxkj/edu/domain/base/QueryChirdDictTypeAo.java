package com.sxkj.edu.domain.base;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QueryChirdDictTypeAo {

    @ApiModelProperty(value = "父类字典id")
    private Long parentId;

    @ApiModelProperty(value = "父类字典代码")
    private String parentCode;

}
