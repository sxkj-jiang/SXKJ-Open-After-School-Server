package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 课后类别占比VO
 *
 * @author system
 * @date 2026-06-19
 */
@Data
public class AsCategoryRatioVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课后类别名称")
    private String categoryName;

    @ApiModelProperty("占比（百分比，保留两位小数）")
    private String ratio;
}
