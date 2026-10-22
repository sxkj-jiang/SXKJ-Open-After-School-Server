package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 教师今日课程VO
 *
 * @author system
 * @date 2026-05-30
 */
@Data
@ApiModel("教师今日课程VO")
public class AsTeacherTodayCourseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("课次ID")
    private Long sessionId;

    @ApiModelProperty("上课开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm")
    private Date startTime;

    @ApiModelProperty("上课结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm")
    private Date endTime;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("年级")
    private String gradeName;

    @ApiModelProperty("上课地点")
    private String placeName;

    @ApiModelProperty("课堂状态：0未开始 1进行中 2已结束")
    private Integer classStatus;

    @ApiModelProperty("课堂状态描述")
    private String classStatusDesc;

    @ApiModelProperty("开课日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date sessionDate;
}
