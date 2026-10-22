package com.sxkj.edu.module.system.role.roleemployee;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.anno.RolePermission;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.utils.CheckParamUtil;
import com.sxkj.edu.module.system.employee.domain.vo.EmployeeVO;
import com.sxkj.edu.module.system.role.basic.domain.ao.RemoveRoleBatchAo;
import com.sxkj.edu.module.system.role.basic.domain.dto.RoleBatchDTO;
import com.sxkj.edu.module.system.role.basic.domain.dto.RoleQueryDTO;
import com.sxkj.edu.module.system.role.basic.domain.dto.RoleSelectedVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户角色管理api实现
 *
 * @author jiangjj
 * @date 2020/12/28 10:10
 */
@Api(tags = {SwaggerTagConst.Admin.MANAGER_ROLE_USER})
@RestController
@OperateLog
public class RoleUserController {

    @Autowired
    private RoleUserService roleUserService;

    @ApiOperation(value = "获取角色成员-已通过用户列表", notes = "角色用户管理")
    @PostMapping("/role/listUser")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "获取角色成员-已通过用户列表", businessType = "角色用户管理")
    public JsonResult<PageResultDTO<EmployeeVO>> listEmployeeByName(@Valid @RequestBody RoleQueryDTO queryDTO) {
        return roleUserService.listEmployeeByName(queryDTO);
    }

    @ApiOperation(value = "根据角色id获取角色用户列表(无分页)", notes = "角色用户管理")
    @GetMapping("/role/listAllUser/{roleId}")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "根据角色id获取角色用户列表", businessType = "角色用户管理")
    public JsonResult<List<EmployeeVO>> listAllEmployeeRoleId(@PathVariable Long roleId) {
        return roleUserService.getAllEmployeeByRoleId(roleId);
    }

    @ApiOperation(value = "从角色成员列表中移除用户", notes = "角色用户管理")
    @PostMapping("/role/removeUser")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "从角色成员列表中移除用户", businessType = "角色用户管理")
    public JsonResult<String> removeEmployee(@Valid @RequestBody RemoveRoleBatchAo ao) {
        CheckParamUtil.checkLongParam(ao.getUserId(),"用户不能为空");
        CheckParamUtil.checkLongParam(ao.getRoleId(), "角色不能为空");
        return roleUserService.removeEmployeeRole(ao.getUserId(), ao.getRoleId());
    }

    @ApiOperation(value = "从角色成员列表中批量移除用户", notes = "角色用户管理")
    @PostMapping("/role/removeUserList")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "从角色成员列表中批量移除用户", businessType = "角色用户管理")
    public JsonResult<String> removeEmployeeList(@Valid @RequestBody RoleBatchDTO removeDTO) {
        return roleUserService.batchRemoveEmployeeRole(removeDTO);
    }

    @ApiOperation(value = "角色成员列表中批量添加用户", notes = "角色用户管理")
    @PostMapping("/role/addUserList")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "角色成员列表中批量添加用户", businessType = "角色用户管理")
    public JsonResult<String> addEmployeeList(@Valid @RequestBody RoleBatchDTO addDTO) {
        return roleUserService.batchAddEmployeeRole(addDTO);
    }

    @ApiOperation(value = "获取用户角色", notes = "获取用户角色")
    @GetMapping("/role/getRoles/{userId}")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "获取用户角色", businessType = "角色用户管理")
    @ApiImplicitParams({@ApiImplicitParam(name = "userId", value = "用户id", paramType = "path", required = true)})
    public JsonResult<List<RoleSelectedVO>> getRoleByEmployeeId(@PathVariable Long userId) {
        return roleUserService.getRolesByEmployeeId(userId);
    }
}
