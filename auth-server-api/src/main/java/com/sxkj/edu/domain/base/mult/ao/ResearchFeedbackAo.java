package com.sxkj.edu.domain.base.mult.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: ResearchFeedbackAo
 * @Author yanghui
 * @Date 2024年10月24日
 */
@Data
public class ResearchFeedbackAo {

    @ApiModelProperty(value = "调查id", required = true)
    @NotNull(message = "调查id不能为空")
    private Long researchId;

    @ApiModelProperty(value = "调查对象类型 1-单位 2-角色 3-所有", required = true)
    @NotNull(message = "调查对象类型不能为空")
    private Integer surveyType;

    @ApiModelProperty(value = "表单内容", required = true)
    @NotEmpty(message = "内容不能为空")
    private List<ResearchFeedbackItemAo> items;
}
