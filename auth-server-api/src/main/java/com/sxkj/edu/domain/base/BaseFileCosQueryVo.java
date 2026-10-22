package com.sxkj.edu.domain.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description: BaseFileCosQueryVo
 * @Author yanghui
 * @Date 2024年11月22日
 */
@Data
public class BaseFileCosQueryVo {

    @ApiModelProperty("主键id 更新时必传")
    private Long id;

    @ApiModelProperty(value = "文件名称",required = true)
    private String fileName;

    @ApiModelProperty(value = "文件类型 PICTURE:图片，VIDEO：视频，DOCUMENT-文档,SUBJECT_VIDEO-直播回放",required = true)
    private String fileType;

    @ApiModelProperty(value = "文件后缀",required = true)
    private String menuType;

    @ApiModelProperty(value = "cos请求id",required = true)
    private String requestId;

    @ApiModelProperty(value = "随机字符串",required = true)
    private String random;

    @ApiModelProperty(value = "文件路径",required = true)
    private String fileUrl;

    @ApiModelProperty(value = "文件大小",required = true)
    private Long fileSize;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadDate;
}
