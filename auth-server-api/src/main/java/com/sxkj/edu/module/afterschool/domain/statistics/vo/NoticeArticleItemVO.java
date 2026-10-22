package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 通知公告条目VO
 *
 * @author system
 * @date 2026-06-16
 */
@Data
@ApiModel("通知公告条目")
public class NoticeArticleItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文章ID")
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("发布日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date publishDate;

    @ApiModelProperty("文章概要")
    private String introduction;
}
