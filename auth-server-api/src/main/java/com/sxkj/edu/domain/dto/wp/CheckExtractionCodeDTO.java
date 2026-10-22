package com.sxkj.edu.domain.dto.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CheckExtractionCodeDTO {

    @ApiModelProperty(value = "批次号",required = true)
    @NotBlank(message = "批次号不能为空")
    private String shareBatchNum;

    @ApiModelProperty(value = "提取码",required = true)
    @NotBlank(message = "提取码不能为空")
    private String extractionCode;




}