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
 * 巡课计划分页查询请求参数
 *
 * @author system
 * @date 2026-05-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("巡课计划分页查询请求参数")
public class AsPatrolPlanPageQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("所属学校ID")
    private Long schoolId;

    @ApiModelProperty("计划名称（模糊搜索）")
    private String planName;

    @ApiModelProperty("巡课类型：1专项巡课 2日常巡课")
    private Integer patrolType;

    @ApiModelProperty("巡课人员ID")
    private Long patrolUserId;

    @ApiModelProperty("巡课周期-开始")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date patrolCycleStart;

    @ApiModelProperty("巡课周期-结束")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date patrolCycleEnd;

    @ApiModelProperty("学期ID")
    private Long termId;
}
