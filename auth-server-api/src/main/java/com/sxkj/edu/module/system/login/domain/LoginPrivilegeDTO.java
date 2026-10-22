package com.sxkj.edu.module.system.login.domain;

import com.sxkj.edu.common.anno.ApiModelPropertyEnum;
import com.sxkj.edu.module.system.privilege.constant.PrivilegeTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class LoginPrivilegeDTO {

    @ApiModelProperty("权限key")
    private String key;

    @ApiModelPropertyEnum(enumDesc = "菜单类型", value = PrivilegeTypeEnum.class)
    private Integer type;

    @ApiModelProperty("url")
    private String url;

    @ApiModelProperty("菜单名")
    private String name;

    @ApiModelProperty("父级key")
    private String parentKey;

}
