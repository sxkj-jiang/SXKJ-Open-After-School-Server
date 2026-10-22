package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("巡课记录分页查询VO")
public class AsPatrolRecordVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("记录ID")
    private Long id;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("学校名称")
    private String schoolName;

    @ApiModelProperty("巡课计划ID")
    private Long planId;

    @ApiModelProperty("巡课计划名称")
    private String planName;

    @ApiModelProperty("课次ID")
    private Long sessionId;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("巡课人账号ID")
    private Long patrolUserId;

    @ApiModelProperty("巡课人姓名")
    private String patrolUserName;

    @ApiModelProperty("开始巡查时间")
    private Date startTime;

    @ApiModelProperty("提交时间")
    private Date submitTime;

    @ApiModelProperty("巡课评分总分")
    private BigDecimal totalScore;

    @ApiModelProperty("综合评价")
    private String summary;

    @ApiModelProperty("问题整改单数量")
    private Integer rectificationCount;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
