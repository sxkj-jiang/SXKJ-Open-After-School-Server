package com.sxkj.edu.domain.vo.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FileUploadFastdfsVO {

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("文件地址")
    private String fileUrl;

    @ApiModelProperty("文件后缀")
    private String filePrefx;

    @ApiModelProperty("文件大小")
    private String fileSize;

    @ApiModelProperty("文件id")
    private String fileId;



}
