package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 教师调课导出结果。
 */
@Data
@ApiModel("教师调课导出结果")
public class AsTeacherAdjustExportResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文件ID")
    private Long fileId;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("下载地址")
    private String downloadUrl;

    @ApiModelProperty("导出记录数")
    private Integer total;
}
