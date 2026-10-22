package com.sxkj.edu.domain.base.khfw.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author Administrator
 * @Date 2024/10/12 12:04
 */
@Data
public class ExamQuestionItemDto {

    @ApiModelProperty(value = "习题id主键",required = true)
    @NotNull(message = "习题编号不能为空")
    private Long id;

    @NotNull(message = "题目排序不能为空")
    @ApiModelProperty(value = "题目排序",required = true)
    private Integer itemSort;
}
