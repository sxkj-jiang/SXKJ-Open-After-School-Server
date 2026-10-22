package com.sxkj.edu.module.afterschool.domain.ao;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 授课教师端巡课整改-巡课记录分页查询请求参数
 *
 * @author system
 * @date 2026-05-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("授课教师端巡课整改-巡课记录分页查询请求参数")
public class AsPatrolRecordTeacherPageQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "所属学校ID不能为空")
    @ApiModelProperty(value = "所属学校ID", required = true)
    private Long schoolId;

    @ApiModelProperty("用户id")
    private Long userId;
}
