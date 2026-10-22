package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 教师调课附件信息。
 */
@Data
@ApiModel("教师调课附件信息")
public class AsTeacherAdjustAttachmentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文件ID")
    private Long fileId;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("文件地址")
    private String fileUrl;

    @ApiModelProperty("文件大小")
    private Long fileSize;
}
