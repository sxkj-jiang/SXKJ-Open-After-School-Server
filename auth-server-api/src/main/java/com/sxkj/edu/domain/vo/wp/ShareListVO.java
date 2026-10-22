package com.sxkj.edu.domain.vo.wp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
public class ShareListVO {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("分享人名称")
    private String userName;

    @ApiModelProperty("用户文件编号")
    private String fileMd5;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("文件类型")
    private String extendName;

    @ApiModelProperty("文件地址")
    private String filePath;

    @ApiModelProperty("是否是目录(0-否,1-是)")
    private Integer isDir;

    @ApiModelProperty("回收站删除标识(0-已删除，1-未删除)")
    private Integer isValid;

    @ApiModelProperty("删除日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deleteTime;

    @ApiModelProperty("删除批次号")
    private String deleteBatchNum;

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

    @ApiModelProperty("文件访问地址")
    private String fileUrl;

    @ApiModelProperty("文件大小")
    private Long fileSize;

    @ApiModelProperty("文件大小")
    private String fileSizeStr;

   @ApiModelProperty("文件分享地址")
    private String shareUrl;
}
