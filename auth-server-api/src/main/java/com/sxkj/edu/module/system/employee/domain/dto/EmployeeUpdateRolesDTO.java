package com.sxkj.edu.module.system.employee.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;


@Data
public class EmployeeUpdateRolesDTO {

    @ApiModelProperty(value = "用户id", required = true)
    @NotNull(message = "用户id不能为空")
    private Long userId;

    @ApiModelProperty(value = "角色ids", required = true)
    @NotNull(message = "请选择用户角色")
    private List<Long> roleIds;

}
