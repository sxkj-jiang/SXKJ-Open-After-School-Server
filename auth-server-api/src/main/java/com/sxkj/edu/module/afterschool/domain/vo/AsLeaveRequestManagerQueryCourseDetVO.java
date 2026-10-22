package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("请假申请视图详情课次VO")
public class AsLeaveRequestManagerQueryCourseDetVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("课程名称")
    private String courseClassName;
    @ApiModelProperty("授课教师名称")
    private String teacherName;

    @ApiModelProperty("课次完整日期")
    private String courseResDate;

    @ApiModelProperty("报名方式：1公开报名 2邀请报名 3行政班安排")
    private Integer enrollType;

    @ApiModelProperty("课次日期")
    private String sessionDate;

    @ApiModelProperty("课次上课时间")
    private String sessionStartTime;

    @ApiModelProperty("课次下课时间")
    private String sessionEndTime;

}
