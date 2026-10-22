package com.sxkj.edu.domain.sys.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ChangePwdDTO",description = "修改密码")
public class ChangePwdDTO {


    @ApiModelProperty(value = "旧密码", required = true)
    private String loginPwd;

    @ApiModelProperty(value = "新密码", required = true)
    private String pwd;

    @ApiModelProperty(value = "确认密码", required = true)
    private String confirmPwd;


}
