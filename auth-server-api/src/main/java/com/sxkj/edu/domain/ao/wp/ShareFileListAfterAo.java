package com.sxkj.edu.domain.ao.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ShareFileListAfterAo {

    @ApiModelProperty(value = "分享文件批次号",required = true)
    @NotBlank(message = "分享文件批次号不能为空")
    private String shareBatchNum;

    @ApiModelProperty(value = "文件路径,默认为/",required = true)
    @NotBlank(message = "文件路径不能为空")
    private String shareFilePath;



}