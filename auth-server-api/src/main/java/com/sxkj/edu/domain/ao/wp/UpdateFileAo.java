package com.sxkj.edu.domain.ao.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateFileAo {

    @ApiModelProperty(value = "文件名id，重命名时不能为空",required = true)
    private Long id;

    @ApiModelProperty("文件名，格式为：1.jpg")
    @NotBlank
    private String fileName;

    @ApiModelProperty("上传文件路径,格式为/测试 ")
    @NotBlank
    private String filePath;


}
