package com.sxkj.edu.module.system.role.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.common.anno.AccessLimit;
import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.anno.RolePermission;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.utils.CheckParamUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.system.role.basic.domain.ao.QueryRolePageAo;
import com.sxkj.edu.module.system.role.basic.domain.ao.RoleEnableAo;
import com.sxkj.edu.module.system.role.basic.domain.dto.RoleAddDTO;
import com.sxkj.edu.module.system.role.basic.domain.dto.RolePageVO;
import com.sxkj.edu.module.system.role.basic.domain.dto.RoleUpdateDTO;
import com.sxkj.edu.module.system.role.basic.domain.dto.RoleVO;
import com.sxkj.edu.module.system.role.basic.domain.entity.RoleEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 角色管理路由
 *
 * @author listen
 * @date 2017/12/28 10:10
 */
@RestController
@Api(tags = {SwaggerTagConst.Admin.MANAGER_ROLE})
@OperateLog
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleDao roleDao;

    @ApiOperation(value = "添加角色", notes = "角色管理")
    @PostMapping("/role/add")
    @RolePermission(roles = {1})
    @AccessLimit(seconds = 3, maxCount = 2, desc = "添加角色防止暴力攻击处理")
    @OperateLog(type = AmqpConstant.OPERATION, value = "添加角色", businessType = "角色管理")
    public JsonResult addRole(@Valid @RequestBody RoleAddDTO roleAddDTO) {
        return roleService.addRole(roleAddDTO);
    }

    @ApiOperation(value = "删除角色", notes = "根据id删除角色")
    @GetMapping("/role/delete/{roleId}")
    @RolePermission(roles = {1})
    @OperateLog(type = AmqpConstant.OPERATION, value = "删除角色", businessType = "角色管理")
    public JsonResult<String> deleteRole(@PathVariable("roleId") Long roleId) {
        CheckParamUtil.checkLongParam(roleId, "删除角色不能为空");
        if(CoreConstant.SYS_DEFAULT_ROLE.contains(roleId)){
            return JsonResult.buildError("默认角色不能删除");
        }
        return roleService.deleteRole(roleId);
    }

    @ApiOperation(value = "启用禁用", notes = "jiangjj")
    @GetMapping("/role/forbidden")
    @RolePermission(roles = {1})
    @OperateLog(type = AmqpConstant.OPERATION, value = "角色-启用禁用", businessType = "角色管理")
    public JsonResult<RoleEntity> forbidden(@Valid @RequestBody RoleEnableAo ao) {
        if(CoreConstant.SYS_DEFAULT_ROLE.contains(ao.getRoleId())){
            return JsonResult.buildError("默认角色不能操作");
        }
        RoleEntity role = roleDao.selectById(ao.getRoleId());
        role.setStatus(ao.getStatus());
        role.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        role.setUpdateTime(new Date());
        roleDao.updateById(role);
        return JsonResult.buildSuccess(role);
    }

    @ApiOperation(value = "更新角色", notes = "角色管理")
    @PostMapping("/role/update")
    @RolePermission(roles = {1})
    @OperateLog(type = AmqpConstant.OPERATION, value = "更新角色", businessType = "角色管理")
    public JsonResult<String> updateRole(@Valid @RequestBody RoleUpdateDTO roleUpdateDTO) {
        return roleService.updateRole(roleUpdateDTO);
    }

    @ApiOperation(value = "获取角色数据", notes = "角色管理")
    @GetMapping("/role/get/{roleId}")
    @OperateLog(type = AmqpConstant.OPERATION, value = "获取角色数据", businessType = "角色管理")
    public JsonResult<RoleVO> getRole(@PathVariable("roleId") Long roleId) {
        return roleService.getRoleById(roleId);
    }

    @ApiOperation(value = "获取所有角色", notes = "角色管理")
    @GetMapping("/role/getAll")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "获取所有角色", businessType = "角色管理")
    public JsonResult<List<RoleVO>> getAllRole() {
        return roleService.getAllRole();
    }

    @ApiOperation(value = "获取评价角色", notes = "角色管理")
    @GetMapping("/role/getTaskList")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "获取评价角色", businessType = "角色管理")
    public JsonResult<List<RoleVO>> getTaskRoleList() {
        return roleService.getTaskRoleList();
    }

    @ApiOperation(value = "角色分页", notes = "角色管理")
    @PostMapping("/role/page")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "角色分页", businessType = "角色管理")
    public JsonResult<Page<RolePageVO>> rolePage(
            @RequestBody QueryRolePageAo ao) {
        Page<RolePageVO> page = new Page<>(ao.getPageIndex(), ao.getPageSize());
        return roleService.rolePage(page, ao);
    }



}
