package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 管理后台-巡课记录分页查询参数
 *
 * @author system
 * @date 2026-05-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("管理后台-巡课记录分页查询参数")
public class AsPatrolRecordAdminPageQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "所属学校ID不能为空")
    @ApiModelProperty(value = "所属学校ID",required = true)
    private Long schoolId;

    @ApiModelProperty("巡课计划名称")
    private String planName;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("巡课类型：1专项巡课 2日常巡课")
    private Integer patrolType;

    @ApiModelProperty("巡课日期-起")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date patrolDateFrom;

    @ApiModelProperty("巡课日期-止")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date patrolDateTo;

    @ApiModelProperty("巡课人员ID（用户id）")
    private Long patrolUserId;

    @ApiModelProperty("学期ID")
    private Long termId;
}
