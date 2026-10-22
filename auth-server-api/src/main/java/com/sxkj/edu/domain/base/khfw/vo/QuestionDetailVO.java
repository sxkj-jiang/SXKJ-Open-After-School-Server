package com.sxkj.edu.domain.base.khfw.vo;

import com.sxkj.edu.domain.base.khfw.dto.QuestionItemObjectDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class QuestionDetailVO {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty(value = "题干")
    private String title;

    @ApiModelProperty("试题编号")
    private String examNumber;

    @ApiModelProperty(value = "题型 1-单选题 2-多选题 3-文本题")
    private Integer questionType;

    @ApiModelProperty(value = "评价指标体系ID")
    private Long evaluationIndicatorSystemId;

    @ApiModelProperty(value = "评价指标体系名称")
    private String systemName;

    @ApiModelProperty(value = "指标配置JSON")
    private String indicatorConfig;

    @ApiModelProperty(value = "是否必填 0-否 1-是")
    private Integer isRequired;

    @ApiModelProperty(value = "适用角色JSON")
    private String applicableRoleJson;

    @ApiModelProperty("选项")
    private List<QuestionItemObjectDto> items;

    @ApiModelProperty(value = "选项信息id")
    private Long textContentId;

    @ApiModelProperty("学校ID")
    private Long schoolId;
}
