package com.sxkj.edu.domain.dto.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SaveShareFileDTO {

    @ApiModelProperty(value = "文件id集合,多个用英文逗号拼接",required = true)
    @NotBlank(message = "文件id集合不能为空")
    private String fileIds;

    @ApiModelProperty(value = "文件路径",required = true)
    @NotBlank(message = "文件路径不能为空")
    private String filePath;

    @ApiModelProperty(value = "分享批次号",required = true)
    @NotBlank(message = "分享批次号不能为空")
    private String shareBatchNum;
}
