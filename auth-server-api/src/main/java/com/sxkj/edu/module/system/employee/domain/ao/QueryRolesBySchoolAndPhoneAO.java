package com.sxkj.edu.module.system.employee.domain.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 根据学校ID和手机号查询角色及账号信息的请求参数
 *
 * @author system
 * @date 2026-05-28
 */
@Data
@ApiModel("根据学校ID和手机号查询角色及账号信息的请求参数")
public class QueryRolesBySchoolAndPhoneAO {

    @ApiModelProperty(value = "学校ID", required = true)
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @ApiModelProperty(value = "手机号", required = true)
    @NotNull(message = "手机号不能为空")
    private String phone;
}
