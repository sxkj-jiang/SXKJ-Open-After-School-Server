package com.sxkj.edu.domain.base.khfw.vo;

import com.sxkj.edu.domain.base.khfw.dto.QuestionItemObjectDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class EvaluationStartQuestionItemsVO {

    @ApiModelProperty("试题id")
    private Long id;

    @ApiModelProperty(value = "题干")
    private String title;

    @ApiModelProperty("试题编号")
    private String examNumber;

    @ApiModelProperty(value = "题型 1-单选题 2-多选题 3-文本题")
    private Integer questionType;

    @ApiModelProperty(value = "题型名称")
    private String questionTypeName;

    @ApiModelProperty("选项")
    private List<QuestionItemObjectDto> items;

    @ApiModelProperty("排序")
    private Integer itemSort;

    @ApiModelProperty("习题试卷分值")
    private Integer examQuestScore;

    @ApiModelProperty(value = "指标配置JSON")
    private String indicatorConfig;
}
