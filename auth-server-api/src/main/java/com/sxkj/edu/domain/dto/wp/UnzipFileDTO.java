package com.sxkj.edu.domain.dto.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UnzipFileDTO {

    @ApiModelProperty("解压缩文件url")
    private String userFileId;

    @ApiModelProperty(value = "解压模式 0-解压到当前文件夹，1-自动创建该文件名目录，并解压到目录里，2-手动选择解压目录",required = true)
    private int unzipMode;

    @ApiModelProperty("解压目的文件目录，仅当 unzipMode 为 2 时必传")
    private String filePath;
}
