package com.sxkj.edu.domain.vo.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryFileLabelInfoVO {

    @ApiModelProperty("标签id")
    private Long id;

    @ApiModelProperty("标签名称")
    private String labelName;

    @ApiModelProperty("标签类型")
    private String labelType;

    @ApiModelProperty("标签颜色")
    private String colorValue;


}
