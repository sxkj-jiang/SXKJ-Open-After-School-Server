package com.sxkj.edu.module.system.role.roleprivilege;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.anno.RolePermission;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.module.system.role.roleprivilege.domain.dto.RolePrivilegeDTO;
import com.sxkj.edu.module.system.role.roleprivilege.domain.dto.RolePrivilegeTreeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * @Description : 与员工权限相关：角色权限关系、权限列表
 * @Author : JIANGJJ
 * @Date : 2022-9-7 15:11
 * @Company : SXKJ
 * @Version : 1.0
 **/
@RestController
@Api(tags = {SwaggerTagConst.Admin.MANAGER_ROLE_PRIVILEGE})
@OperateLog
public class RolePrivilegeController {

    @Autowired
    private RolePrivilegeService rolePrivilegeService;

    @ApiOperation(value = "更新角色权限", notes = "更新角色权限")
    @PostMapping("/privilege/updateRolePrivilege")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "更新角色权限", businessType = "角色菜单管理")
    public JsonResult<String> updateRolePrivilege(@Valid @RequestBody RolePrivilegeDTO updateDTO) {
        return rolePrivilegeService.updateRolePrivilege(updateDTO);
    }

    @ApiOperation(value = "获取角色可选的功能权限", notes = "获取角色可选的功能权限")
    @GetMapping("/privilege/listPrivilegeByRoleId/{roleId}")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "获取角色可选的功能权限", businessType = "角色菜单管理")
    public JsonResult<RolePrivilegeTreeVO> listPrivilegeByRoleId(@PathVariable("roleId") Long roleId) {
        return rolePrivilegeService.listPrivilegeByRoleId(roleId);
    }


}
