package com.sxkj.edu.domain.base.khfw.ao;

import com.sxkj.edu.domain.base.khfw.dto.QuestionTypeNumDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ExamQuestionAutoCreateAo {

    @ApiModelProperty(value = "组卷规则id",required = true)
    @NotNull(message = "组卷规则id不能为空")
    private Long ruleId;

    @ApiModelProperty(value = "题目数量",required = true)
    @NotNull(message = "题目数量不能为空")
    private Integer titleNum;

    @ApiModelProperty(value = "题目类型与数量集合",required = true)
    @NotEmpty(message = "题目类型与数量集合不能为空")
    private List<QuestionTypeNumDto> typeNumAoList;
}
