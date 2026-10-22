package com.sxkj.edu.domain.mult.ao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description: TPortalArticleNewsAO
 * @Author yanghui
 * @Date 2024年08月09日
 */
@Data
public class TPortalArticleNewsAO extends PageQuery {


    @ApiModelProperty(value = "文章类型：1-新闻/2-公告/3-法律法规/4-其他",required = true)
    @NotNull(message = "文章类型不能为空")
    private Integer newsType;

    @ApiModelProperty(value = "新闻文章名称")
    private String newsName;

    @ApiModelProperty(value = "新闻文章简介")
    private String introduction;

    @ApiModelProperty(value = "展示状态1-是，其他否")
    private Integer isShow;

    @ApiModelProperty(value = "发布时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishDate;

    @ApiModelProperty(value = "查询发布开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishStartDate;

    @ApiModelProperty(value = "查询发布结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishEndDate;

}
