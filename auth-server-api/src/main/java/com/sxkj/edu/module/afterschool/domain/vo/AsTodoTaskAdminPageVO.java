package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 管理端-待办任务分页视图对象
 *
 * @author system
 * @date 2026-06-08
 */
@Data
@ApiModel("管理端-待办任务分页视图对象")
public class AsTodoTaskAdminPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("待办ID")
    private Long id;

    @ApiModelProperty("待办标题")
    private String taskTitle;

    @ApiModelProperty("待办日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date todoDate;

    @ApiModelProperty("截止时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date deadlineTime;

    @ApiModelProperty("优先级（低/中/高）")
    private String priorityLevelDesc;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("待办来源")
    private String sourceBizTypeDesc;

    @ApiModelProperty("待办来源类型")
    private String sourceBizType;

    @JsonIgnore
    @ApiModelProperty(value = "来源业务ID，仅供后端解析学期", hidden = true)
    private Long sourceBizId;

    @ApiModelProperty("业务所属学期ID")
    private Long termId;

    @ApiModelProperty("业务所属学期名称")
    private String termName;

    @ApiModelProperty("状态（中文）")
    private String taskStatusDesc;

    @ApiModelProperty("最后更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
