package com.sxkj.edu.domain.base.suggest.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SuggestVO {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("意见类型名称")
    private String typeName;

    @ApiModelProperty(value = "反馈人")
    private String author;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "反馈时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishDate;

    @ApiModelProperty(value = "状态 0-未处理；1-已处理")
    private Integer status;

    @ApiModelProperty(value = "操作人")
    private String feedBackUser;

    @ApiModelProperty(value = "回复意见")
    private String checkContent;

    @ApiModelProperty(value = "意见内容")
    private String adviceName;

    @ApiModelProperty(value = "意见来源，平台端、移动端")
    private String sourceName;

}
