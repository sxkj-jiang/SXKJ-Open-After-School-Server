package com.sxkj.edu.module.system.employee.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;


@Data
public class EmployeeBatchUpdateRolesDTO {

    @ApiModelProperty(value = "用户id集合", required = true)
    @NotNull(message = "请选择用户")
    private List<Long> userIds;

    @ApiModelProperty(value = "角色ids", required = true)
    @NotNull(message = "请选择用户角色")
    private List<Long> roleIds;

}
