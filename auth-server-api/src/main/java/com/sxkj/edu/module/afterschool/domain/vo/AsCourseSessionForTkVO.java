package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class AsCourseSessionForTkVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课次ID")
    private Long id;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("课次序号")
    private Integer sessionIndex;

    @ApiModelProperty("上课日期")
    private String sessionDate;

    @ApiModelProperty("星期")
    private String weekDay;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("课时(小时)")
    private Double durationHours;

    @ApiModelProperty("上课地点")
    private String placeName;

    @ApiModelProperty("授课教师ID")
    private Long teacherId;

    @ApiModelProperty("授课教师姓名")
    private String teacherName;

    @ApiModelProperty("课程内容")
    private String content;

    @ApiModelProperty("课次状态：1正常 2已取消 3已完成")
    private Integer sessionStatus;

    @ApiModelProperty("课次状态描述")
    private String sessionStatusDesc;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("课次开始日期时间")
    private Date sessionStartDate;

    @ApiModelProperty("课次结束日期时间")
    private Date sessionEndDate;
}
