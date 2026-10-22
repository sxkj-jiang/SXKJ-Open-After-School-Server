package com.sxkj.edu.domain.ao.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateFileNameAo {

    @ApiModelProperty(value = "文件名id，重命名时不能为空", required = true)
    @NotNull(message = "文件名id不能为空")
    private Long id;

    @ApiModelProperty(value = "文件名，格式为：1.jpg", required = true)
    @NotBlank(message = "文件名不能为空")
    private String fileName;

}
