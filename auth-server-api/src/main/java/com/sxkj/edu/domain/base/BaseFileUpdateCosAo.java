package com.sxkj.edu.domain.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description: BaseFileUpdateAo
 * @Author yanghui
 * @Date 2024年11月22日
 */
@Data
public class BaseFileUpdateCosAo {

    @ApiModelProperty("主键id 更新时必传")
    private Long id;

    @ApiModelProperty(value = "文件名称",required = true)
    @NotBlank(message = "文件名称不能为空")
    private String fileName;

    @ApiModelProperty(value = "文件类型 PICTURE:图片，VIDEO：视频，DOCUMENT-文档,SUBJECT_VIDEO-直播回放",required = true)
    @NotBlank(message = "文件类型不能为空")
    private String fileType;

    @ApiModelProperty(value = "文件后缀",required = true)
    @NotBlank(message = "文件后缀不能为空")
    private String menuType;

    @ApiModelProperty(value = "cos请求id",required = true)
    @NotBlank(message = "cos请求id不能为空")
    private String requestId;

    @ApiModelProperty(value = "随机字符串",required = true)
    private String random;

    @ApiModelProperty(value = "文件路径",required = true)
    @NotBlank(message = "文件路径不能为空")
    private String fileUrl;

    @ApiModelProperty(value = "文件大小",required = true)
    @NotNull(message = "文件大小不能为空")
    private Long fileSize;

}
