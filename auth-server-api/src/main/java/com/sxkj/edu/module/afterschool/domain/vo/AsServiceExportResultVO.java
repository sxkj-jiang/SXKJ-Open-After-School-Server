package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 排课管理-课后服务导出结果。
 *
 * @author wangyouhuai
 * @date 2026-06-25
 */
@Data
@ApiModel("课后服务导出结果")
public class AsServiceExportResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文件ID")
    private Long fileId;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("下载地址")
    private String downloadUrl;

    @ApiModelProperty("导出条数")
    private Integer total;
}
