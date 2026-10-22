package com.sxkj.edu.domain.ao.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CheckShareCodeAo {


    @ApiModelProperty(value="批次号",required = true)
    @NotBlank
    private String shareBatchNum;

    @ApiModelProperty(value="提取码",required = true)
    @NotBlank
    private String extractionCode;
}
