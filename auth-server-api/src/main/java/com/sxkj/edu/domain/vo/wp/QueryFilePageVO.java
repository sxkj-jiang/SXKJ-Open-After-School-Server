package com.sxkj.edu.domain.vo.wp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.module.business.entity.wp.WpFileLabelPo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class QueryFilePageVO {

    @ApiModelProperty("主键id")
    private Long id;

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

    @ApiModelProperty("文件类型(1公开,2已分享)")
    private Integer shareType;

    @ApiModelProperty("存储类型 1-图片，2-文档，3-视频，4-音频，5-压缩文件，6-文件夹，7-其他")
    private Integer storageType;

    @ApiModelProperty("存储类型 1-图片，2-文档，3-视频，4-音频，5-压缩文件，6-文件夹，7-其他")
    private String storageName;

    @ApiModelProperty("md5唯一标识")
    private String fileMd5;

    @ApiModelProperty("图片宽度，用于放大缩小")
    private Integer imageWidth;

    @ApiModelProperty("图片高度，用于放大缩小")
    private Integer imageHeight;

    @ApiModelProperty("上传市州")
    private String cityStateName;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("删除时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deleteTime;

    @ApiModelProperty("文件标签列表")
    private List<WpFileLabelPo> labels;

}
