package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 学生调课导出结果。
 */
@Data
@ApiModel("学生调课导出结果")
public class AsStudentAdjustExportResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文件ID")
    private Long fileId;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("下载地址")
    private String downloadUrl;

    @ApiModelProperty("导出数量")
    private Integer total;
}
