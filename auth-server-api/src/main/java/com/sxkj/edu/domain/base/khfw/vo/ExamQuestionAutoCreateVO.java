package com.sxkj.edu.domain.base.khfw.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExamQuestionAutoCreateVO {

    @ApiModelProperty("试题主键id")
    private Long id;

    @ApiModelProperty("题干")
    private String title;

    @ApiModelProperty("试题编号")
    private String examNumber;

    @ApiModelProperty("题型 1-单选题 2-多选题 3-文本题")
    private Integer questionType;

    @ApiModelProperty("评价指标体系ID")
    private Long evaluationIndicatorSystemId;

    @ApiModelProperty("指标配置JSON")
    private String indicatorConfig;

    @ApiModelProperty("是否必填 0-否 1-是")
    private Integer isRequired;

    @ApiModelProperty("适用角色JSON")
    private String applicableRoleJson;

    @ApiModelProperty("排序")
    private Integer itemSort;
}
