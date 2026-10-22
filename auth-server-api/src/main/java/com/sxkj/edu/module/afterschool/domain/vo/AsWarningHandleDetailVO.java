package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生去向预警-处理详情查看视图对象
 */
@Data
@ApiModel("学生去向预警-处理详情查看")
public class AsWarningHandleDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("学生去向预警ID")
    private Long id;

    @ApiModelProperty("学生出勤记录ID")
    private Long attendanceRecordId;

    @ApiModelProperty("异常原因描述")
    private String exceptionDesc;

    @ApiModelProperty("处理方式：1课后留堂 2联系家长 3约谈学生 4其他")
    private Integer handleType;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("学生学号")
    private String studentNo;

    @ApiModelProperty("学生头像")
    private String studentAvatar;

    @ApiModelProperty("年级+班级")
    private String gradeClassName;

    @ApiModelProperty("考勤状态：1正常/到课 2迟到 3早退 4缺勤 5请假")
    private Integer attendanceStatus;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("上课日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date sessionDate;

    @ApiModelProperty("上课开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm")
    private Date startTime;

    @ApiModelProperty("上课结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm")
    private Date endTime;

    @ApiModelProperty(value = "预计迟到时间（分钟）")
    private Integer lateMinutes;
}
