package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 教师端巡课记录分页查询返回VO
 *
 * @author system
 * @date 2026-05-27
 */
@Data
@ApiModel("教师端巡课记录分页查询返回VO")
public class AsPatrolRecordAppVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("巡课记录ID")
    private Long id;

    @ApiModelProperty("巡课时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    @ApiModelProperty("记录综合评价")
    private String summary;

    @ApiModelProperty("上课-开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm")
    private Date sessionStartTime;

    @ApiModelProperty("上课-结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm")
    private Date sessionEndTime;

    @ApiModelProperty("上课地点")
    private String placeName;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("授课教师姓名")
    private String teacherName;

    @ApiModelProperty("巡课老师姓名")
    private String patrolUserName;
}
