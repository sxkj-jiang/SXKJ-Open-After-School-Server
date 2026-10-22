package com.sxkj.edu.module.system.privilege.domain.ao;

import com.sxkj.edu.common.base.domain.PageParamDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class QueryUserListByRoleAo extends PageParamDTO {

    @ApiModelProperty(value = "角色编号")
    private Long roleId;

    @ApiModelProperty(value = "用户名")
    private String userName;


    public QueryUserListByRoleAo() {
    }

}
