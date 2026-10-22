package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 校历事件新增/更新参数
 *
 * @author system
 * @date 2026-05-25
 */
@Data
@ApiModel("校历事件新增/更新参数")
public class EduclCalendarEventAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("事件ID(更新时传入)")
    private Long id;

    @ApiModelProperty(value = "事件名称",required = true)
    @NotBlank(message = "事件名称不能为空")
    private String eventName;

    @NotNull(message = "事件开始日期不能为空")
    @ApiModelProperty(value = "事件开始日期",required = true)
    private Date eventDateStart;

    @NotNull(message = "事件结束日期不能为空")
    @ApiModelProperty(value = "事件结束日期",required = true)
    private Date eventDateEnd;

    @NotNull(message = "事件类型不能为空")
    @ApiModelProperty(value = "事件类型(1-教学考试、2-文体活动、3-典礼仪式、4-节假日、5-德育活动、6-教研培训)",required = true)
    private Integer eventType;

    @ApiModelProperty("备注")
    private String remark;
}
