package com.sxkj.edu.domain.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description: BaseFilePageVo
 * @Author yanghui
 * @Date 2024年11月22日
 */
@Data
public class BaseFilePageVo {

    @ApiModelProperty("主键/编码")
    private Long id;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("菜单名称")
    private String menuType;

    @ApiModelProperty("文件地址")
    private String fileUrl;

    @ApiModelProperty("文件类型 PICTURE:图片，VIDEO：视频，DOCUMENT-文档,SUBJECT_VIDEO-直播回放")
    private String fileType;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadDate;
}
