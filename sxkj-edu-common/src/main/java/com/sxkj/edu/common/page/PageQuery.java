package com.sxkj.edu.common.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 分页查询参数
 */
@ApiModel(description = "分页条件")
@Getter
@Setter
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页，默认第一页", example = "1")
    protected int pageIndex = 1;

    @ApiModelProperty(value = "每页显示条数，默认20", example = "20")
    protected int pageSize = 20;

}
