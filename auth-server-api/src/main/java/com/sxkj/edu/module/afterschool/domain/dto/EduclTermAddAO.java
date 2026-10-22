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
 * 学期新增/更新参数
 *
 * @author system
 * @date 2026-05-19
 */
@Data
@ApiModel("学期新增/更新参数")
public class EduclTermAddAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID(更新时必填)")
    private Long id;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "所属学校ID", required = true)
    private Long schoolId;

    @NotBlank(message = "学期名称不能为空")
    @ApiModelProperty(value = "学期名称", required = true)
    private String termName;

    @NotBlank(message = "学年不能为空")
    @ApiModelProperty(value = "学年(如2023-2024)", required = true)
    private String schoolYear;

    @NotNull(message = "学期类型不能为空")
    @ApiModelProperty(value = "学期类型(1第一学期 2第二学期 3其他)",required = true)
    private Integer termType;

    @NotNull(message = "开始日期不能为空")
    @ApiModelProperty(value = "开始日期",required = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date startDate;

    @NotNull(message = "结束日期不能为空")
    @ApiModelProperty(value = "结束日期",required = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date endDate;

    @ApiModelProperty("校历事件列表")
    private List<EduclCalendarEventAO> calendarEventList;
}
