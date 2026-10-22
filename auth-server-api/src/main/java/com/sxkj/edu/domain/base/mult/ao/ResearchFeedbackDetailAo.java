package com.sxkj.edu.domain.base.mult.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: ResearchFeedbackDetailAo
 * @Author yanghui
 * @Date 2024年10月24日
 */
@Data
public class ResearchFeedbackDetailAo {

    @ApiModelProperty(value = "调查id", required = true)
    @NotNull(message = "调查id不能为空")
    private Long researchId;

    @ApiModelProperty(value = "调查对象类型 1-单位 2-角色 3-所有", required = true)
    @NotNull(message = "调查对象类型不能为空")
    private Integer surveyType;

    @ApiModelProperty(value = "单位id", required = true)
    private Long unitId;

    @ApiModelProperty(value = "角色id数组", required = true)
    private List<Long> roleIds;
}
