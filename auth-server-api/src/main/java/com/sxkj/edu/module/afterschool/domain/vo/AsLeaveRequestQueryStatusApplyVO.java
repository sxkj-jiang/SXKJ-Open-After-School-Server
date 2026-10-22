package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("请假申请视图对象")
public class AsLeaveRequestQueryStatusApplyVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("请假ID")
    private Long id;

    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("学生学号")
    private String studentNo;

    @ApiModelProperty("请假类型:1病假 2事假 3其他")
    private Integer leaveType;

    /**
     * 请假开始时间
     */
    @ApiModelProperty("请假开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date leaveStartTime;

    @ApiModelProperty("请假结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date leaveEndTime;

    @ApiModelProperty("请假课次idJSON")
    private String leaveSessionsJson;

    @ApiModelProperty("请假原因")
    private String leaveReason;

}
