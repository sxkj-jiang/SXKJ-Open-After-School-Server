package com.sxkj.edu.domain.dto.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShareFileListDTO {

    @ApiModelProperty("分享文件批次号")
    private String shareBatchNum;

    @ApiModelProperty("分享文件路径")
    private String shareFilePath;



}