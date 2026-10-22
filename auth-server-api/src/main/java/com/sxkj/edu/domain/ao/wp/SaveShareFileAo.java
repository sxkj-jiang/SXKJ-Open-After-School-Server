package com.sxkj.edu.domain.ao.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SaveShareFileAo {

    @ApiModelProperty(value = "分享文件id",required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(value = "1-过期，2-不过期",required = true)
    @NotNull
    private Integer isOver;

    @ApiModelProperty(value = "提取码",required = true)
    @NotBlank
    private String extractionCode;

    @ApiModelProperty(value = "分享地址",required = true)
    @NotBlank
    private String fileUrl;
}
