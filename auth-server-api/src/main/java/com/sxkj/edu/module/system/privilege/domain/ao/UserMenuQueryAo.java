package com.sxkj.edu.module.system.privilege.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 系统菜单表
 * </p>
 *
 * @author zhengqn
 * @since 2022-12-16
 */
@Data
public class UserMenuQueryAo {

    @ApiModelProperty(value = "菜单类型：1-菜单，2-功能点，默认为1")
    private Integer type;

    @ApiModelProperty(value = "父级菜单是否为空，1-是，其他否")
    private Integer isTrue;

    @ApiModelProperty(value = "是否显示,1-显示，其他不显示")
    private Integer isShow;

    @ApiModelProperty(value = "菜单来源：PTPC-平台端，JWPC-教务端，XCX-小程序")
    private String menuSource;

    @ApiModelProperty(value = "父级菜单key")
    private String menuParentKey;

    @ApiModelProperty(value = "用户菜单id集合")
    private List<String> menuKeyList;

    public UserMenuQueryAo() {
    }

    public UserMenuQueryAo(Integer type, Integer isTrue, String menuParentKey, List<String> menuKeyList) {
        this.type = type;
        this.isTrue = isTrue;
        this.menuParentKey = menuParentKey;
        this.menuKeyList = menuKeyList;
    }
}
