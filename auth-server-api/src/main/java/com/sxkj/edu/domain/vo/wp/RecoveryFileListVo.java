package com.sxkj.edu.domain.vo.wp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *@Description : 回收文件列表Vo
 *@Author : JIANGJJ
 *@Date : 2024-9-20 15:14
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Data
public class RecoveryFileListVo {

    @ApiModelProperty("回收文件id")
    private Long recoveryFileId;

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户文件编号")
    private String userFileId;

    @ApiModelProperty("文件id")
    private String fileId;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("文件地址")
    private String filePath;

    @ApiModelProperty("扩展名称")
    private String extendName;

    @ApiModelProperty("是否是目录(0-否,1-是)")
    private Integer isDir;

    @ApiModelProperty("删除标识(0-未删除，1-已删除)")
    private Integer deleteFlag;

    @ApiModelProperty("删除日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deleteTime;

    @ApiModelProperty("删除批次号")
    private String deleteBatchNum;

    @ApiModelProperty("分享编号")
    private String shareId;

    @ApiModelProperty("分享时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date shareTime;

    @ApiModelProperty("失效时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("提取码")
    private String extractionCode;

    @ApiModelProperty("分享批次号")
    private String shareBatchNum;

    @ApiModelProperty("分享类型(0公共,1私密,2好友)")
    private Integer shareType;

    @ApiModelProperty("分享状态(0正常,1已失效,2已撤销)")
    private Integer shareStatus;

    @ApiModelProperty("分享文件编号")
    private String shareFileId;

    @ApiModelProperty("分享文件地址")
    private String shareFilePath;

    @ApiModelProperty("上传时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;


    @ApiModelProperty("文件地址")
    private String fileUrl;

    @ApiModelProperty("文件大小")
    private Long fileSize;

    @ApiModelProperty("存储类型")
    private Integer storageType;

}
