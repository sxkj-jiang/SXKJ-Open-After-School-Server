package com.sxkj.edu.domain.ao.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 新建文件 AO
 */
@Data
public class SaveFileAo {

    @ApiModelProperty(value = "文件名，没有后缀名，例如：file1",required = true)
    @NotBlank(message = "文件名不能为空")
    private String fileName;

    @ApiModelProperty(value ="文件类型，0-Word，1-excel，2-ppt", required = true)
    @NotNull(message = "文件类型不能为空")
    private Integer fileType;
}
