package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class AsTeacherNoticeParentPageVO {

    @ApiModelProperty(value = "教师通知详情ID")
    private Long id;

    @ApiModelProperty(value = "教师通知ID")
    private Long teacherNoticeId;

    @ApiModelProperty(value = "通知标题")
    private String noticeTitle;

    @ApiModelProperty(value = "通知内容")
    private String noticeContent;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "是否已读 0-否 1-是")
    private Integer isRead;
}
