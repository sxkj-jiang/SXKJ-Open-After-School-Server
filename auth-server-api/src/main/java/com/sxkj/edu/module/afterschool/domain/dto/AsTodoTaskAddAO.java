package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 待办任务新增对象
 *
 * @author system
 * @date 2026-06-08
 */
@Data
@ApiModel("待办任务新增对象")
public class AsTodoTaskAddAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "待办标题不能为空")
    @ApiModelProperty(value = "待办标题", required = true)
    private String taskTitle;

    @ApiModelProperty(value = "待办内容",required = true)
    @NotBlank(message = "待办内容不能为空")
    private String taskContent;

    @NotNull(message = "待办日期不能为空")
    @ApiModelProperty(value = "待办日期", required = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date todoDate;

    @NotNull(message = "截止时间不能为空")
    @ApiModelProperty(value = "截止时间", required = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date deadlineTime;

    @NotNull(message = "优先级不能为空")
    @ApiModelProperty(value = "优先级：1低 2中 3高", required = true)
    private Integer priorityLevel;

    @ApiModelProperty(value = "提醒方式",required = true)
    @NotNull(message = "提醒方式不能为空")
    private Integer remindMode;

    @ApiModelProperty(value = "重复模式",required = true)
    @NotNull(message = "重复模式不能为空")
    private Integer repeatMode;

    @ApiModelProperty("附件ID集合JSON")
    private List<Long> fileIds;
}
