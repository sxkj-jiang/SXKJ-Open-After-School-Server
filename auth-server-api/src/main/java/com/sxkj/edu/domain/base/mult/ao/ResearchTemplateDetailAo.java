package com.sxkj.edu.domain.base.mult.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 更新 [ 调查模板表 ]
 *
 * @author zhengqiannan
 * @version 1.0
 * @company ${company}
 * @copyright (c) 2018 ${company}Inc. All rights reserved.
 * @date 2024-03-18 21:17:13
 * @since JDK1.8
 */
@Data
public class ResearchTemplateDetailAo {

    @ApiModelProperty(value = "调查活动id", required = true)
    @NotNull(message = "活动id不能为空")
    private Long researchId;

    @ApiModelProperty("排除id")
    private Long excludeId;

}
