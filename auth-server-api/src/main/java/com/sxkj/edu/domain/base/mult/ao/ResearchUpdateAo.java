package com.sxkj.edu.domain.base.mult.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * 新建 [ 调查活动表 ]
 *
 * @author zhengqiannan
 * @version 1.0
 * @company ${company}
 * @copyright (c) 2018 ${company}Inc. All rights reserved.
 * @date 2024-03-18 20:56:52
 * @since JDK1.8
 */
@Data
public class ResearchUpdateAo {

    @ApiModelProperty("调查id 更新时必传")
    private Long id;

    @ApiModelProperty(value = "活动名称", required = true)
    @NotBlank(message = "名称不能为空")
    private String surveyName;

    @ApiModelProperty("问卷描述")
    private String surveyDesc;

    @ApiModelProperty(value = "调查对象类型 1-单位 2-角色 3-所有", required = true)
    @NotNull(message = "调查对象类型不能为空")
    private Integer surveyType;

    @ApiModelProperty("单位id数组")
    private Set<Long> unitSet;

    @ApiModelProperty("角色id数组")
    private Set<Long> roleSet;

}
