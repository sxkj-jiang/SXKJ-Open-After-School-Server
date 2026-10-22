package com.sxkj.edu.domain.ao.wp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(value = "文件压缩")
public class ZipFileAo {

    @ApiModelProperty(value = "文件id集合",required = true)
    @NotNull(message = "文件id集合不能为空")
    private List<Long> idList;

    @ApiModelProperty(value = "当前目录路径",required = true)
    @NotBlank(message = "当前目录路径不能为空")
    private String filePath;

}
