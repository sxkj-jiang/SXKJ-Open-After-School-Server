package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 教师端问题整改分页查询请求参数
 *
 * @author system
 * @date 2026-05-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("教师端问题整改分页查询请求参数")
public class AsRectificationOrderAppPageQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "所属学校ID不能为空")
    @ApiModelProperty(value = "所属学校ID", required = true)
    private Long schoolId;

    @ApiModelProperty(value = "巡课记录ID")
    private Long patrolRecordId;

    @ApiModelProperty(value = "巡课计划ID")
    private Long planId;

    @ApiModelProperty("整改状态：0待整改 1整改中 2已整改 3已完成 4已逾期")
    private Integer rectifyStatus;
}
