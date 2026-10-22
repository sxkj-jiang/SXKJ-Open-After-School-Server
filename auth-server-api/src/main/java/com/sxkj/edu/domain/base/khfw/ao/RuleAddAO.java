package com.sxkj.edu.domain.base.khfw.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @ClassName RuleAddAO
 * @Description 规则新增请求
 * @Author backend-architect
 * @Date 2026-01-12
 * @Version 1.0
 **/
@Data
public class RuleAddAO {
    @ApiModelProperty(value = "规则名称", required = true)
    @NotBlank(message = "规则名称不能为空")
    private String ruleName;                  // 规则名称

    @ApiModelProperty(value = "学科代码（QuestSub）", required = true)
    @NotBlank(message = "学科代码不能为空")
    private String subjectCode;               // 学科代码

    @ApiModelProperty(value = "教龄代码（TeachingSeniority）", required = true)
    @NotBlank(message = "教龄代码不能为空")
    private String teachingAgeCode;           // 教龄代码

    @ApiModelProperty(value = "状态：1-启用，0-禁用", required = true)
    private Integer status;                   // 状态：1-启用，0-禁用

    @ApiModelProperty(value = "难度系数权重分配", required = true)
    @NotEmpty(message = "难度系数权重分配不能为空")
    private List<DifficultyWeight> difficultyWeights;  // 难度系数权重分配

    @ApiModelProperty(value = "维度权重分配", required = true)
    @NotEmpty(message = "维度权重分配不能为空")
    private List<DimensionWeight> dimensionWeights;    // 维度权重分配

    @ApiModelProperty(value = "学校ID")
    private Long schoolId;

    /**
     * @ClassName DifficultyWeight
     * @Description 难度系数权重
     * @Author backend-architect
     * @Date 2026-01-12
     * @Version 1.0
     **/
    @Data
    public static class DifficultyWeight {
        @ApiModelProperty(value = "难度等级（1-5星）", required = true)
        private Integer difficultyLevel;  // 难度等级（1-5星）

        @ApiModelProperty(value = "权重百分比", required = true)
        private Integer weight;           // 权重百分比
    }

    /**
     * @ClassName DimensionWeight
     * @Description 维度权重
     * @Author backend-architect
     * @Date 2026-01-12
     * @Version 1.0
     **/
    @Data
    public static class DimensionWeight {
        @ApiModelProperty(value = "维度代码（Dimension）", required = true)
        private String dimensionCode;  // 维度代码

        @ApiModelProperty(value = "权重百分比", required = true)
        private Integer weight;        // 权重百分比
    }
}
