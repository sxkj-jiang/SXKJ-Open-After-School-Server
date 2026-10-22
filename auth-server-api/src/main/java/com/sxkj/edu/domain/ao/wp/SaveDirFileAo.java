package com.sxkj.edu.domain.ao.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SaveDirFileAo {


    @ApiModelProperty(value = "目录名，格式：测试",required = true)
    @NotBlank(message = "目录名不能为空")
    private String dirName;

    @ApiModelProperty(value = "目录路径，格式为:/xxx/ ",required = true)
    @NotBlank(message = "目录路径不能为空")
    private String dirPath;
}
