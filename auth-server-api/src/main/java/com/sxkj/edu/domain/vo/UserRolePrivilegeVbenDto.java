package com.sxkj.edu.domain.vo;

import com.sxkj.edu.common.anno.ApiModelPropertyEnum;
import com.sxkj.edu.module.system.privilege.constant.PrivilegeTypeEnum;
import com.sxkj.edu.module.system.privilege.domain.dto.UserMenuMetaDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 角色功能权限
 *
 * @author listen
 * @date 2018/01/03 08:48
 */
@Data
public class UserRolePrivilegeVbenDto {

    @ApiModelProperty("编号")
    private Long id;

    @ApiModelProperty("父级Key")
    private String parentKey;
    /**
     * 功能名称
     */
    @ApiModelProperty("名称")
    private String name;

    @ApiModelPropertyEnum(enumDesc = "类型", value = PrivilegeTypeEnum.class)
    private Integer type;

    @ApiModelProperty("key")
    private String key;

    @ApiModelProperty("菜单路径")
    private String path;

    @ApiModelProperty("重定向地址")
    private String redirect;

    @ApiModelProperty("菜单icon路径")
    private String icon;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("子级列表")
    private List<UserRolePrivilegeVbenDto> children;

    @ApiModelProperty("前端字段")
    private String component;

    @ApiModelProperty("前端meta对象")
    private UserMenuMetaDto meta;

    @ApiModelProperty("是否显示，1-显示，其他不显示")
    private Integer isShow;

    public UserRolePrivilegeVbenDto() {

    }
}
