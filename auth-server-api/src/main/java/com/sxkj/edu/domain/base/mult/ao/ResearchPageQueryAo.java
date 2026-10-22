package com.sxkj.edu.domain.base.mult.ao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class ResearchPageQueryAo extends PageQuery {

    @ApiModelProperty("活动名称")
    private String surveyName;

    @ApiModelProperty("发布状态 0-未发起 1-已发起")
    private Integer publishStatus;

    @ApiModelProperty("填写状态 0-未填写 1-已填写")
    private Integer writeStatus;

    @ApiModelProperty(hidden = true)
    private Set<Long> ids;

    @ApiModelProperty("创建时间-开始")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeBegin;

    @ApiModelProperty("创建时间-截止")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;
}
