package com.sxkj.edu.domain.mult.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class LabelVO {

    @ApiModelProperty(value = "标签")
    private String label;

    @ApiModelProperty(value = "值")
    private Object value;
}
