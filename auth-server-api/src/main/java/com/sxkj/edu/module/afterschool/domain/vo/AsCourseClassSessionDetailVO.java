package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 课程班课次详情VO
 *
 * @author system
 * @date 2026-06-12
 */
@Data
@ApiModel("课程班课次详情")
public class AsCourseClassSessionDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课次ID")
    private Long sessionId;

    @ApiModelProperty("课次所属学期ID")
    private Long termId;

    @ApiModelProperty("课次所属学期名称")
    private String termName;

    @ApiModelProperty("课次日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date sessionDate;

    @ApiModelProperty("节次编号")
    private Integer sectionNo;

    @ApiModelProperty("第几节课")
    private String sectionName;

    @ApiModelProperty("星期几")
    private String weekDayName;

    @ApiModelProperty("上课开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm")
    private Date startTime;

    @ApiModelProperty("上课结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm")
    private Date endTime;

    @ApiModelProperty("教师姓名")
    private String teacherName;

    @ApiModelProperty("完整课次时间")
    private String classTimeRes;
}
