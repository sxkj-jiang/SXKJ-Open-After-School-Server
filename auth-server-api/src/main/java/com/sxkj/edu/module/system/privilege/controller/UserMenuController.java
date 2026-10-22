package com.sxkj.edu.module.system.privilege.controller;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.anno.RolePermission;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.utils.CheckParamUtil;
import com.sxkj.edu.module.system.privilege.domain.ao.PrivilegeMenuDisableAo;
import com.sxkj.edu.module.system.privilege.domain.ao.UserMenuQueryAo;
import com.sxkj.edu.module.system.privilege.domain.dto.PrivilegeFunctionDTO;
import com.sxkj.edu.module.system.privilege.domain.dto.PrivilegeFunctionVO;
import com.sxkj.edu.module.system.privilege.domain.dto.PrivilegeMenuVO;
import com.sxkj.edu.module.system.privilege.domain.dto.UserPrivilegeMenuDTO;
import com.sxkj.edu.module.system.privilege.service.UserMenuService;
import com.sxkj.edu.module.system.role.roleprivilege.RolePrivilegeService;
import com.sxkj.edu.module.system.role.roleprivilege.domain.dto.UserRolePrivilegeVbenDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;


/**
 * @Description : 与用户权限相关：角色权限关系、权限列表
 * @Author : JIANGJJ
 * @Date : 2022-9-7 15:05
 * @Company : SXKJ
 * @Version : 1.0
 **/
@RestController
@RequestMapping("/sys/dynamic")
@Api(tags = {SwaggerTagConst.Admin.SYS_MANAGER_PRIVILEGE})
@OperateLog
public class UserMenuController {

    @Resource
    private UserMenuService userMenuService;

    @Resource
    private RolePrivilegeService rolePrivilegeService;


//    @ApiOperation(value = "初始化角色菜单", notes = "jiangjj")
//    @GetMapping("/user/privilege/init")
//    @OperateLog(type = AmqpConstant.OPERATION, value = "初始化角色菜单", businessType = "角色菜单管理")
//    public JsonResult<String> initUserPrivilegeData() {
//        return userMenuService.initUserRoleAndPrivilegeData();
//    }


    @ApiOperation(value = "菜单保存")
    @PostMapping("/privilege/menu/save")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "菜单保存", businessType = "权限管理")
    public JsonResult menuSave(@Valid @RequestBody UserPrivilegeMenuDTO privilegeMenuDTO) {
        return userMenuService.menuSave(privilegeMenuDTO);
    }

    @ApiOperation(value = "菜单更新", notes = "jiangjj")
    @PostMapping("/privilege/menu/upt")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "菜单更新", businessType = "权限管理")
    public JsonResult sysMenuUpt(@Valid @RequestBody UserPrivilegeMenuDTO privilegeMenuDTO) {
        CheckParamUtil.checkLongParam(privilegeMenuDTO.getId(), "更新时菜单主键不能为空");
        return userMenuService.sysMenuUpt(privilegeMenuDTO);
    }


    /**
     * 用户菜单管理，显示所有层级
     * @return
     */
    @ApiOperation(value = "菜单管理-查询所有菜单树", notes = "jiangjj")
    @PostMapping("/privilege/menu/managerClass/queryAll")
    @OperateLog(type = AmqpConstant.OPERATION, value = "菜单管理-查询所有菜单树", businessType = "权限管理")
    public JsonResult<List<UserRolePrivilegeVbenDto>> queryManagerAll() {
//        return JsonResult.buildSuccess(rolePrivilegeService.queryUserMenuLoginByRoleId(5L));
        List<UserRolePrivilegeVbenDto> dtoList = rolePrivilegeService.queryUserPrivilegeByRoleId(null,1);
        return JsonResult.buildSuccess(dtoList);
    }

    /**
     * 只查二级显示的菜单，排除开发专用菜单
     *
     * @return
     */
    @ApiOperation(value = "查询所有菜单项-下拉搜索", notes = "jiangjj")
    @PostMapping("/privilege/menu/queryAll")
    @OperateLog(type = AmqpConstant.OPERATION, value = "查询所有菜单项-下拉搜索", businessType = "权限管理")
    public JsonResult<List<PrivilegeMenuVO>> queryAll(@RequestBody UserMenuQueryAo userMenuQueryAo) {
        return userMenuService.menuQueryAll(userMenuQueryAo, 1);
    }

    @ApiOperation(value = "保存更新功能点", notes = "jiangjj")
    @PostMapping("/privilege/function/saveOrUpdate")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "保存更新功能点", businessType = "权限管理")
    public JsonResult<String> functionSaveOrUpdate(@Valid @RequestBody PrivilegeFunctionDTO privilegeFunctionDTO) {
        return userMenuService.functionSaveOrUpdate(privilegeFunctionDTO);
    }


    @ApiOperation(value = "查询菜单功能点", notes = "权限管理")
    @PostMapping("/privilege/function/query/{menuKey}")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "查询菜单功能点", businessType = "权限管理")
    public JsonResult<List<PrivilegeFunctionVO>> functionQuery(@PathVariable String menuKey) {
        return userMenuService.functionQuery(menuKey);
    }


    @ApiOperation(value = "菜单禁用", notes = "jiangjj")
    @PostMapping("/privilege/menu/disable")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "菜单禁用", businessType = "角色菜单管理")
    public JsonResult sysMenuUptDisable(@Valid @RequestBody PrivilegeMenuDisableAo ao) {
        return userMenuService.disablePrivilegeData(ao.getId());
    }
}
