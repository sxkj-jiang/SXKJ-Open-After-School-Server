package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 巡课计划分页查询返回VO
 *
 * @author system
 * @date 2026-05-26
 */
@Data
@ApiModel("巡课计划分页查询返回VO")
public class AsPatrolPlanVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("计划ID")
    private Long id;

    @ApiModelProperty("所属学校ID")
    private Long schoolId;

    @ApiModelProperty("所属学校名称")
    private String schoolName;

    @ApiModelProperty("计划名称")
    private String planName;

    @ApiModelProperty("巡课类型：1专项巡课 2日常巡课")
    private Integer patrolType;

    @ApiModelProperty("巡课人员教师ID集合JSON")
    private String patrolUserIds;

    @ApiModelProperty("巡课人员姓名")
    private String patrolUserNames;

    @ApiModelProperty("巡课范围课程类别JSON")
    private String patrolScopeCourseTypeJson;

    @ApiModelProperty("巡课范围课程类别")
    private String patrolScopeCourseTypeNames;

    @ApiModelProperty("巡课开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date startTime;

    @ApiModelProperty("巡课结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date endTime;

    @ApiModelProperty("计划状态：0草稿(待发布) 1已提交(发布)")
    private Integer planStatus;

    @ApiModelProperty("巡课记录数")
    private Integer recordNum;

    @ApiModelProperty(value = "学期名称")
    private String termName;
}
