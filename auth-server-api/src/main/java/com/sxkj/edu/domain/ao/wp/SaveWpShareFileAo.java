package com.sxkj.edu.domain.ao.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SaveWpShareFileAo {

    @ApiModelProperty(value = "文件id",required = true)
    @NotNull(message = "文件id不能为空")
    private Long fileId;

    @ApiModelProperty(value = "文件路径",required = true)
    @NotBlank(message = "文件路径不能为空")
    private String filePath;

    @ApiModelProperty(value = "分享批次号")
    private String shareBatchNum;

    @ApiModelProperty(value = "文件MD5")
    private String fileMd5;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    public SaveWpShareFileAo(){}

    public SaveWpShareFileAo(Long fileId,Long userId, String filePath, String shareBatchNum, String fileMd5) {
        this.fileId = fileId;
        this.userId = userId;
        this.filePath = filePath;
        this.shareBatchNum = shareBatchNum;
        this.fileMd5 = fileMd5;
    }
}
