package com.sxkj.edu.domain.ao.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateDirFileAo {

    @ApiModelProperty(value = "目录名id，重命名时不能为空",required = true)
    @NotNull(message = "目录名id不能为空")
    private Long id;

    @ApiModelProperty(value = "目录名，格式为：1.jpg",required = true)
    @NotBlank(message = "目录名不能为空")
    private String fileName;

    @ApiModelProperty(value = "目录路径,格式为/测试 ")
    private String filePath;
}
