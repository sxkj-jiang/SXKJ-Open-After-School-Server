package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AsPatrolRecordAppDetailVO {

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("上课-开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm")
    private Date sessionStartTime;

    @ApiModelProperty("上课-结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm")
    private Date sessionEndTime;

    @ApiModelProperty("授课教师姓名")
    private String teacherName;

    @ApiModelProperty("上课地点")
    private String placeName;

    @ApiModelProperty("教师提交巡课评分项")
    private String scoreItemsJson;

    @ApiModelProperty("综合评价")
    private String summary;

    @ApiModelProperty("教师提交巡课评分项集合")
    private List<TeacherCheckItemVO> teacherCheckItemVOList;

    @ApiModelProperty("问题整改单列表")
    private List<AsRectificationOrderAppVO> rectificationOrders;
}
