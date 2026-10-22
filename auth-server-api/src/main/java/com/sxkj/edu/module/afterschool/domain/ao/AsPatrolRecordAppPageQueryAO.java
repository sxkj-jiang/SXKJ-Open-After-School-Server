package com.sxkj.edu.module.afterschool.domain.ao;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 教师端巡课记录分页查询请求参数
 *
 * @author system
 * @date 2026-05-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("教师端巡课记录分页查询请求参数")
public class AsPatrolRecordAppPageQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "所属学校ID不能为空")
    @ApiModelProperty(value = "所属学校ID", required = true)
    private Long schoolId;

    @NotNull(message = "巡课计划ID不能为空")
    @ApiModelProperty(value = "巡课计划ID", required = true)
    private Long planId;
}
