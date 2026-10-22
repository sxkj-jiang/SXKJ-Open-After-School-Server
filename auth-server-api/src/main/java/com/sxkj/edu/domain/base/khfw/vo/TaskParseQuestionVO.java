package com.sxkj.edu.domain.base.khfw.vo;

import com.sxkj.edu.domain.base.khfw.dto.QuestionItemObjectDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TaskParseQuestionVO {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("题干")
    private String title;

    @ApiModelProperty("试题编号")
    private String examNumber;

    @ApiModelProperty("题型 1-单选 2-多选 3-文本")
    private Integer questionType;

    @ApiModelProperty("题型名称")
    private String questionTypeName;

    @ApiModelProperty("是否必填")
    private Integer isRequired;

    @ApiModelProperty("评价指标体系ID")
    private Long evaluationIndicatorSystemId;

    @ApiModelProperty("评价指标体系名称")
    private String systemName;

    @ApiModelProperty(value = "指标配置JSON")
    private String indicatorConfig;

    @ApiModelProperty("所属维度")
    private String dimension;

    @ApiModelProperty(value = "适用角色JSON（多个，角色ID数组）")
    private String applicableRoleJson;

    @ApiModelProperty("适用角色")
    private String applicableRole;

    @ApiModelProperty(value = "选项信息id")
    private Long textContentId;

    @ApiModelProperty("选项信息")
    private String textContent;

    @ApiModelProperty("题目选项")
    private List<QuestionItemObjectDto> items;

    @ApiModelProperty("学校ID")
    private Long schoolId;
}
