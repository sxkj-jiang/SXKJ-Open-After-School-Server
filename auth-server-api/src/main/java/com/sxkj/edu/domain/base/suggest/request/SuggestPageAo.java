package com.sxkj.edu.domain.base.suggest.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
public class SuggestPageAo extends PageQuery {

    @ApiModelProperty(value = "反馈人/手机号")
    private String keyword;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "反馈用户id   查询个人反馈列表")
    private Long userId;

    @ApiModelProperty(value = "意见来源，平台端、移动端")
    private String sourceName;

    @ApiModelProperty(value = "操作人")
    private String feedBackUser;

    @ApiModelProperty("意见类型名称")
    private String typeName;
}
