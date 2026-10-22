package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 课次生成预览项。
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("课次生成预览项")
public class AsCourseSessionPreviewVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("上课日期")
    private Date sessionDate;

    @ApiModelProperty("星期几，1周一到7周日")
    private Integer weekDay;

    @ApiModelProperty("星期文案")
    private String weekDayName;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("授课教师ID")
    private Long teacherId;

    @ApiModelProperty("授课教师姓名")
    private String teacherName;

    @ApiModelProperty("上课地点")
    private String placeName;

    @ApiModelProperty("来源时间段ID")
    private Long schedulePeriodId;

    @ApiModelProperty("是否存在冲突")
    private Boolean conflict = false;

    @ApiModelProperty("冲突提示")
    private List<String> conflictMessages = new ArrayList<>();

    @ApiModelProperty("课次开始日期时间")
    private Date sessionStartDate;

    @ApiModelProperty("课次结束日期时间")
    private Date sessionEndDate;
}
