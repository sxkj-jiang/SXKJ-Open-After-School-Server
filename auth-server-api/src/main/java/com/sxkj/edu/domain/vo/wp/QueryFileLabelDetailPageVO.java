package com.sxkj.edu.domain.vo.wp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class QueryFileLabelDetailPageVO {

    @ApiModelProperty("文件id")
    private Long id;

    @ApiModelProperty("标签id")
    private Long labelId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("文件或目录名称")
    private String fileName;

    @ApiModelProperty("文件地址")
    private String filePath;

    @ApiModelProperty("扩展名称，文件后缀")
    private String extendName;

    @ApiModelProperty("是否是目录(0-否,1-是)")
    private Integer isDir;

    @ApiModelProperty("上传日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date uploadTime;

    @ApiModelProperty("文件url")
    private String fileUrl;

    @ApiModelProperty("文件大小")
    private Long fileSize;

    @ApiModelProperty("带单位的文件大小")
    private String fileSizeStr;

    @ApiModelProperty("存储类型 1-图片，2-文档，3-视频，4-音频，5-压缩文件，6-文件夹，7-其他")
    private Integer storageType;

    @ApiModelProperty("md5唯一标识")
    private String fileMd5;

    @ApiModelProperty("图片宽度，用于放大缩小")
    private Integer imageWidth;

    @ApiModelProperty("图片高度，用于放大缩小")
    private Integer imageHeight;

    @ApiModelProperty("标签名称列表")
    private List<String> labelNameList;
}
