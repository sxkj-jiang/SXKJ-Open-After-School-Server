package com.sxkj.edu.domain.dto.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ShareFileDTO {

    @ApiModelProperty(value = "分享文件Id集合,多个用逗号拼接",required = true)
    @NotBlank
    private String userFileIds;

    @ApiModelProperty(value = "过期日期",example = "2020-05-23 22:10:33")
    private String endTime;

    @ApiModelProperty(value = "分享类型",example="0公共分享，1私密分享，2好友分享")
    private Integer shareType;

    @ApiModelProperty("备注")
    private String remarks;




}