package com.sxkj.edu.domain.base.khfw.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class RuleDownListVO {

    @ApiModelProperty(value = "规则ID")
    private Long id;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @ApiModelProperty(value = "学科类型")
    private String subjectType;

    @ApiModelProperty(value = "难度系数权重分配")
    private List<RuleDetailVO.DifficultyWeightVO> difficultyWeights;  // 难度系数权重分配

    @ApiModelProperty(value = "维度权重分配")
    private List<RuleDetailVO.DimensionWeightVO> dimensionWeights;    // 维度权重分配
}
