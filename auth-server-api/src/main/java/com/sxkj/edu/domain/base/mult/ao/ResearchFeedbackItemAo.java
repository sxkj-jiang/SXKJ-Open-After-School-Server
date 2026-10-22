package com.sxkj.edu.domain.base.mult.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: ResearchFeedbackItemAo
 * @Author yanghui
 * @Date 2024年10月24日
 */
@Data
public class ResearchFeedbackItemAo {

    @ApiModelProperty("模板id")
    private Long templateId;

    @ApiModelProperty("内容")
    private String writeContent;
}
