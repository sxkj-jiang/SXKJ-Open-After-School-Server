package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CourseSchedulePageVO {

    @ApiModelProperty(value = "课后服务id")
    private Long id;

    @ApiModelProperty(value = "课后服务名称")
    private String serviceName;

    @ApiModelProperty(value = "学期ID")
    private Long termId;

    @ApiModelProperty(value = "学期名称")
    private String termName;

    @ApiModelProperty("学期开始日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date startDate;

    @ApiModelProperty("学期结束日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date endDate;
}
