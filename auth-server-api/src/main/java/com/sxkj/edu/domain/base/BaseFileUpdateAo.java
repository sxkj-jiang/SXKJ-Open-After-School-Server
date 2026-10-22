package com.sxkj.edu.domain.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description: BaseFileUpdateAo
 * @Author yanghui
 * @Date 2024年11月22日
 */
@Data
public class BaseFileUpdateAo {

    @ApiModelProperty("主键id 更新时必传")
    private Long id;

    @ApiModelProperty(value = "文件名称",required = true)
    @NotBlank(message = "文件名称不能为空")
    private String fileName;

    @ApiModelProperty(value = "文件类型 PICTURE:图片，VIDEO：视频，DOCUMENT-文档,SUBJECT_VIDEO-直播回放",required = true)
    @NotBlank(message = "文件类型不能为空")
    private String fileType;

    @ApiModelProperty(value = "菜单名称",required = true)
    @NotBlank(message = "菜单名称不能为空")
    private String menuType;

    @ApiModelProperty(value = "文件路径")
    private String fileUrl;

}
