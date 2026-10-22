package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 管理端-待办任务分页查询参数
 *
 * @author system
 * @date 2026-06-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("管理端-待办任务分页查询参数")
public class AsTodoTaskAdminPageQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @ApiModelProperty("业务所属学期ID")
    private Long termId;

    @ApiModelProperty("待办标题（模糊匹配）")
    private String taskTitle;

    @ApiModelProperty("优先级：1低 2中 3高")
    private Integer priorityLevel;

    @ApiModelProperty("待办来源")
    private String sourceBizType;

    @ApiModelProperty("待办日期-起")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date todoDateFrom;

    @ApiModelProperty("待办日期-止")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date todoDateTo;

    @ApiModelProperty("截止时间-起")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date deadlineTimeFrom;

    @ApiModelProperty("截止时间-止")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date deadlineTimeTo;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("任务状态：0待办 1已完成")
    private Integer taskStatus;
}
