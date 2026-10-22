package com.sxkj.edu.domain.sys.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class BaseFileDTO {

    @ApiModelProperty(value = "附件名称")
    @NotBlank(message = "附件名称不能为空")
    private String fileName;

    @ApiModelProperty(value = "cos存储桶")
    @NotBlank(message = "cos存储桶不能为空")
    private String bucket;

    @ApiModelProperty(value = "cos地域")
    @NotBlank(message = "cos地域不能为空")
    private String region;

    @ApiModelProperty(value = "fileKey")
    @NotBlank(message = "key不能为空")
    private String fileKey;

    @ApiModelProperty(value = "附件类型")
    @NotBlank(message = "fileType不能为空")
    private String fileType;

    @ApiModelProperty(value = "菜单名称，来源")
    private String menuType;

}
