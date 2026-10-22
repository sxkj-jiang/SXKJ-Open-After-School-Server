package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 去向轨迹时间轴VO
 */
@Data
@ApiModel("去向轨迹时间轴VO")
public class DestinationTrajectoryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("去向ID")
    private Long destinationId;

    @ApiModelProperty("考勤ID")
    private Long attendanceId;

    @ApiModelProperty("考勤点到时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date attendanceTime;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("考勤状态：1正常/到课 2迟到 3早退 4缺勤 5请假 (特殊去向的则取去向名称赋值)")
    private Integer attendanceStatus;

    @ApiModelProperty("考勤状态描述")
    private String attendanceStatusDesc;

    @ApiModelProperty("课后去向类型：1课程班 2回家 3培训机构/校外机构 4其他")
    private Integer destinationType;

    @ApiModelProperty("去向名称")
    private String destinationName;

    @ApiModelProperty("课次时间")
    private String sessionDate;

    @ApiModelProperty("课次ID")
    private Long sessionId;

    @ApiModelProperty("请假ID")
    private Long leaveId;

    @ApiModelProperty("原因")
    private String reason;
}
