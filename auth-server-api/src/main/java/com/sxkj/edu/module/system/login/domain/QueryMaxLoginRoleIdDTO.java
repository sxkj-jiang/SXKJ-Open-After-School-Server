package com.sxkj.edu.module.system.login.domain;

import com.sxkj.edu.common.anno.ApiModelPropertyEnum;
import com.sxkj.edu.module.system.privilege.constant.PrivilegeTypeEnum;
import com.sxkj.edu.module.system.role.basic.domain.dto.RoleSelectedVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class QueryMaxLoginRoleIdDTO {

    @ApiModelProperty("角色id,权限最大的角色")
    private Long roleId;

    private List<RoleSelectedVO> roleSelectedVOList;


}
