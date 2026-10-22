package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class AsTeacherNoticePageVO {

    @ApiModelProperty(value = "教师通知ID")
    private Long id;

    @ApiModelProperty(value = "通知标题")
    private String noticeTitle;

    @ApiModelProperty(value = "通知内容")
    private String noticeContent;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createTime;

    @ApiModelProperty(value = "状态 0-保存草稿 1-发布")
    private Integer status;

    @ApiModelProperty(value = "已读数量")
    private Integer readNum;

    @ApiModelProperty(value = "未读数量")
    private Integer noReadNum;
}
