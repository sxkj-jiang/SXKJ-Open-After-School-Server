package com.sxkj.edu.module.system.privilege.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class UserMenuQueryTypeAo {

    @ApiModelProperty(value = "菜单类型：1-菜单，2-功能点，默认为1")
    private Integer type;

    @ApiModelProperty(value = "是否显示,1-显示，其他不显示")
    private Integer isShow;

    public UserMenuQueryTypeAo() {
    }

}
