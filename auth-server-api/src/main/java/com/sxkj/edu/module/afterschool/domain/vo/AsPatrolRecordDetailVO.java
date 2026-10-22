package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 巡课记录详情视图对象（含整改单列表）
 *
 * @author system
 * @date 2026-05-26
 */
@Data
@ApiModel("巡课记录详情视图对象")
public class AsPatrolRecordDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("记录ID")
    private Long id;

    @ApiModelProperty("所属学校ID")
    private Long schoolId;

    @ApiModelProperty("所属学校名称")
    private String schoolName;

    @ApiModelProperty("巡课计划名称")
    private String planName;

    @ApiModelProperty("课次(第几节)")
    private String sectionName;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("巡课类型：1专项巡课 2日常巡课")
    private Integer patrolType;

    @ApiModelProperty("巡课日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date submitTime;

    @ApiModelProperty("授课教师id")
    private Long teacherId;

    @ApiModelProperty("授课教师")
    private String teacherName;

    @ApiModelProperty("教师提交巡课评分项")
    private String scoreItemsJson;

    @ApiModelProperty("综合评价")
    private String summary;

    @ApiModelProperty("上课地点")
    private String placeName;

    @ApiModelProperty("教师提交巡课评分项集合")
    private List<TeacherCheckItemVO> teacherCheckItemVOList;

    @ApiModelProperty("问题整改单列表")
    private List<AsRectificationOrderAppVO> rectificationOrders;

    @ApiModelProperty(value = "学期名称")
    private String termName;

}
