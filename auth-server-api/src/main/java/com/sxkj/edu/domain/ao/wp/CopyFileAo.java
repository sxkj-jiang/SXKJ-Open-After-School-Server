package com.sxkj.edu.domain.ao.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CopyFileAo {

    @ApiModelProperty(value = "文件id",required = true)
    @NotNull(message = "文件编号不能为空")
    private Long id;

    @ApiModelProperty(value = "复制/移动的目标文件路径,格式为/测试",required = true)
    @NotBlank(message = "复制目标路径不能为空")
    private String filePath;


}
