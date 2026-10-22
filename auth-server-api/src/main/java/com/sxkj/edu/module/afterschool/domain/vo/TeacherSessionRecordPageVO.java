package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class TeacherSessionRecordPageVO {

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("课次id")
    private Long sessionId;

    @ApiModelProperty("上课日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date sessionDate;

    @ApiModelProperty("星期")
    private Integer weekDay;

    @ApiModelProperty("星期描述")
    private String weekDayName;

    @ApiModelProperty("具体上课时间")
    private String sessionTime;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("调课状态：0未调课 1已调课")
    private Integer adjustStatus;

    @ApiModelProperty("调课状态说明")
    private String adjustStatusName;

}
