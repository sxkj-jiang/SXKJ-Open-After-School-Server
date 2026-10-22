package com.sxkj.edu.domain.vo.wp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ShareFileListVO {

    @ApiModelProperty("文件id")
    private String fileId;

    @ApiModelProperty("文件url")
    private String fileUrl;

    @ApiModelProperty("文件大小")
    private Long fileSize;

    @ApiModelProperty("存储类型")
    private Integer storageType;

    @ApiModelProperty("用户文件id")
    private String userFileId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("文件路径")
    private String filePath;

    @ApiModelProperty("文件扩展名")
    private String extendName;

    @ApiModelProperty("是否是目录 0-否， 1-是")
    private Integer isDir;

    @ApiModelProperty("上传时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;

    @ApiModelProperty("分享文件路径")
    private String shareFilePath;

    @ApiModelProperty("提取码")
    private String extractionCode;

    @ApiModelProperty("分享批次")
    private String shareBatchNum;
//    private Integer shareType;//0公共，1私密，2好友
//    private Integer shareStatus;//0正常，1已失效，2已撤销
}
