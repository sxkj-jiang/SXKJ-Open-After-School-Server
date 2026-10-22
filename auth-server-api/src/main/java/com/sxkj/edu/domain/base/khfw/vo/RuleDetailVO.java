package com.sxkj.edu.domain.base.khfw.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName RuleDetailVO
 * @Description 规则详情响应
 * @Author backend-architect
 * @Date 2026-01-12
 * @Version 1.0
 **/
@Data
public class RuleDetailVO {
    @ApiModelProperty(value = "规则ID")
    private String ruleId;                    // 规则ID

    @ApiModelProperty(value = "规则名称")
    private String ruleName;                  // 规则名称

    @ApiModelProperty(value = "学科代码")
    private String subjectCode;               // 学科代码

    @ApiModelProperty(value = "学科名称")
    private String subjectName;               // 学科名称

    @ApiModelProperty(value = "教龄代码")
    private String teachingAgeCode;           // 教龄代码

    @ApiModelProperty(value = "教龄名称")
    private String teachingAgeName;           // 教龄名称

    @ApiModelProperty(value = "难度系数权重分配")
    private List<DifficultyWeightVO> difficultyWeights;  // 难度系数权重分配

    @ApiModelProperty(value = "维度权重分配")
    private List<DimensionWeightVO> dimensionWeights;    // 维度权重分配

    @ApiModelProperty(value = "启用状态（true-启用，false-禁用）")
    private Boolean isEnabled;                // 启用状态

    @ApiModelProperty(value = "创建人")
    private String creator;                   // 创建人

    @ApiModelProperty(value = "创建时间")
    private String createTime;                  // 创建时间

    @ApiModelProperty(value = "更新时间")
    private String updateTime;                  // 更新时间

    @ApiModelProperty(value = "学校ID")
    private Long schoolId;

    /**
     * @ClassName DifficultyWeightVO
     * @Description 难度系数权重响应
     * @Author backend-architect
     * @Date 2026-01-12
     * @Version 1.0
     **/
    @Data
    public static class DifficultyWeightVO {
        @ApiModelProperty(value = "权重ID")
        private Long id;                 // 权重ID

        @ApiModelProperty(value = "难度等级（1-5星）")
        private Integer difficultyLevel;  // 难度等级（1-5星）

        @ApiModelProperty(value = "权重百分比")
        private Integer weight;           // 权重百分比
        
        @ApiModelProperty(value = "推荐占比范围")
        private String recommendedRange;  // 推荐占比范围
    }

    /**
     * @ClassName DimensionWeightVO
     * @Description 维度权重响应
     * @Author backend-architect
     * @Date 2026-01-12
     * @Version 1.0
     **/
    @Data
    public static class DimensionWeightVO {
        @ApiModelProperty(value = "权重ID")
        private Long id;                 // 权重ID

        @ApiModelProperty(value = "维度代码")
        private String dimensionCode;    // 维度代码

        @ApiModelProperty(value = "维度名称")
        private String dimensionName;    // 维度名称

        @ApiModelProperty(value = "权重百分比")
        private Integer weight;          // 权重百分比
        
        @ApiModelProperty(value = "推荐占比范围")
        private String recommendedRange;  // 推荐占比范围
    }
}
