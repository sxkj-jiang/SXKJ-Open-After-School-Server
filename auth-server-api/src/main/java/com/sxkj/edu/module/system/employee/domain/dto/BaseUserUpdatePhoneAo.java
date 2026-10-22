package com.sxkj.edu.module.system.employee.domain.dto;

import com.sxkj.edu.common.validation.Mobile;
import com.sxkj.edu.common.validation.PasswordAnno;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
public class BaseUserUpdatePhoneAo {


    @ApiModelProperty(value = "需要修改的手机号", required = true)
    @Mobile
    private String userPhone;


}
