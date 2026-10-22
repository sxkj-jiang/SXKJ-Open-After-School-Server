package com.sxkj.edu.domain.ao.wp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 分享文件请求类。
 * 目前不支持同时分享多个文件（原型没有该需求）
 */
@Data
public class ShareFileAo {

    @ApiModelProperty(value = "分享文件Id",required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(value = "过期日期", example = "2020-05-23 22:10:33", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "是否需要提取码", example = "true")
    private boolean isExtractCode;

}