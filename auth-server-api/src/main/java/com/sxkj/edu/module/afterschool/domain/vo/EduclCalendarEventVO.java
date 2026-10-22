package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 校历事件视图对象
 *
 * @author system
 * @date 2026-05-25
 */
@Data
@ApiModel("校历事件视图对象")
public class EduclCalendarEventVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("事件ID")
    private Long id;

    @ApiModelProperty("所属学校ID")
    private Long schoolId;

    @ApiModelProperty("所属学期ID")
    private Long termId;

    @ApiModelProperty("事件名称")
    private String eventName;

    @ApiModelProperty("事件开始日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date eventDateStart;

    @ApiModelProperty("事件结束日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date eventDateEnd;

    @ApiModelProperty("事件类型(1-教学考试、2-文体活动、3-典礼仪式、4-节假日、5-德育活动、6-教研培训)")
    private Integer eventType;
}
