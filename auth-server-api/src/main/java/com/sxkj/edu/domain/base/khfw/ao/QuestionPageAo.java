package com.sxkj.edu.domain.base.khfw.ao;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhengqn
 * @since 2022-12-16
 */
@Data
public class QuestionPageAo extends PageQuery {

    @ApiModelProperty(value = "题型 1-单选题 2-多选题 3-文本题")
    private Integer questionType;

    @ApiModelProperty("题干关键词")
    private String title;

    @ApiModelProperty("使用角色（角色ID）")
    private Long applicableRole;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty(value = "评价指标体系ID")
    private Long evaluationIndicatorSystemId;

    @ApiModelProperty("适用维度")
    private String applicableDimension;
}
