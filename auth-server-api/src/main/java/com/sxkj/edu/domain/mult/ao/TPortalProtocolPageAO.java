package com.sxkj.edu.domain.mult.ao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class TPortalProtocolPageAO extends PageQuery {

    @ApiModelProperty(value = "协议名称")
    private String protocolName;

    @ApiModelProperty(value = "协议编号")
    private String protocolCode;

    @ApiModelProperty(value = "协议类型 1-用户协议 2-隐私政策")
    private Integer protocolType;

    @ApiModelProperty(value = "最后更新时间-开始")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTimeStart;

    @ApiModelProperty(value = "最后更新时间-结束")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTimeEnd;
}
