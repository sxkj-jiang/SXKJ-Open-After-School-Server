package com.sxkj.edu.module.afterschool.domain.ao;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class EduclCalendarEventHolidayAo {

    @NotNull(message = "开始日期不能为空")
    @ApiModelProperty(value = "开始日期",required = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date startDate;

    @NotNull(message = "结束日期不能为空")
    @ApiModelProperty(value = "结束日期",required = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date endDate;
}
