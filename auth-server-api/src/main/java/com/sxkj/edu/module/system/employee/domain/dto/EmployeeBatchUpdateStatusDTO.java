package com.sxkj.edu.module.system.employee.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 批量更新
 *
 * @author lidoudou
 * @date 2017年12月21日上午13:17:52
 */
@Data
public class EmployeeBatchUpdateStatusDTO {

    @ApiModelProperty(value = "用户ids", required = true)
    @NotNull(message = "用户ids不能为空")
    private List<Long> userIds;

    @ApiModelProperty(value = "用户状态：1-启用，0-禁用", required = true)
    @NotNull(message = "状态不能为空")
    private Integer status;

}
