package com.sxkj.edu.module.system.employee.domain.dto;

import com.sxkj.edu.common.validation.PasswordAnno;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 修改密码所需参数
 *
 * @author jiangjj
 * @date 2021-02-23 下午 4:53
 */
@Data
public class EmployeeUpdatePwdDTO {


    @ApiModelProperty(value = "新密码", required = true)
    @NotNull
    @Size(min = 8, max = 64)
    private String pwd;

    @ApiModelProperty(value = "确认密码", required = true)
    @NotNull
    @Size(min = 8, max = 64)
    private String confirmPwd;

    @ApiModelProperty(value = "原密码", required = true)
    @NotNull
    @Size(min = 8, max = 64)
    private String oldPwd;

}
