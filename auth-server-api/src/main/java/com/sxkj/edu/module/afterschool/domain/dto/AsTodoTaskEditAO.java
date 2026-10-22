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
 * 待办任务编辑对象
 */
@Data
@ApiModel("待办任务编辑对象")
public class AsTodoTaskEditAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务ID",required = true)
    @NotNull(message = "任务ID不能为空")
    private Long id;

    @ApiModelProperty(value = "任务标题",required = true)
    @NotBlank(message = "任务标题不能为空")
    private String taskTitle;

    @ApiModelProperty(value = "优先级：1低 2中 3高",required = true)
    @NotNull(message = "优先级不能为空")
    private Integer priorityLevel;

    @ApiModelProperty(value = "待办日期",required = true)
    @NotNull(message = "待办日期不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date todoDate;

    @ApiModelProperty(value = "截止时间",required = true)
    @NotNull(message = "截止时间不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date deadlineTime;

    @ApiModelProperty(value = "任务内容",required = true)
    @NotBlank(message = "任务内容不能为空")
    private String taskContent;

    @ApiModelProperty(value = "提醒方式",required = true)
    @NotNull(message = "提醒方式不能为空")
    private Integer remindMode;

    @ApiModelProperty(value = "重复模式",required = true)
    @NotNull(message = "重复模式不能为空")
    private Integer repeatMode;

    @ApiModelProperty("附件文件ID集合")
    private List<Long> fileIds;

    @ApiModelProperty("任务状态：0待办 1已完成")
    private Integer taskStatus;
}
