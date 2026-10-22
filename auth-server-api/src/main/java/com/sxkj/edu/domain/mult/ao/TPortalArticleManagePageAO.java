package com.sxkj.edu.domain.mult.ao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class TPortalArticleManagePageAO extends PageQuery {

    @ApiModelProperty(value = "文章名称")
    private String newsName;

    @ApiModelProperty(value = "文章类型Id")
    private Long articleTypeId;

    @ApiModelProperty(value = "创建时间开始")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeStart;

    @ApiModelProperty(value = "创建时间结束")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;

    @ApiModelProperty(value = "学校id")
    private Long schoolId;
}
