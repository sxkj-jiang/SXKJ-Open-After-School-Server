package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 学期范围查询参数。
 */
@Data
@ApiModel("学期范围查询参数")
public class AsTermScopeQueryAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID；为空时使用学校默认学期")
    private Long termId;
}
