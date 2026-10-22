package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 巡课记录分页查询参数
 *
 * @author system
 * @date 2026-05-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("巡课记录分页查询参数")
public class AsPatrolRecordPageQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("所属学校ID")
    private Long schoolId;

    @ApiModelProperty("巡课计划ID")
    private Long planId;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("巡课人账号ID")
    private Long patrolUserId;

    @ApiModelProperty("巡查开始时间-起")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTimeFrom;

    @ApiModelProperty("巡查开始时间-止")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTimeTo;
}
