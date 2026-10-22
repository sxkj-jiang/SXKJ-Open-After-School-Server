package com.sxkj.edu.module.system.employee;

import com.sxkj.edu.common.anno.AccessLimit;
import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.anno.RolePermission;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.exception.BaseBusinessException;
import com.sxkj.edu.common.utils.CheckParamUtil;
import com.sxkj.edu.common.utils.ExcelUtils;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.base.UptUserLogoAo;
import com.sxkj.edu.domain.base.UserRemoveDeptAo;
import com.sxkj.edu.module.system.employee.domain.ao.QueryRolesBySchoolAndPhoneAO;
import com.sxkj.edu.module.system.employee.domain.ao.UserQueryAO;
import com.sxkj.edu.module.system.employee.domain.ao.UserQuerySingAO;
import com.sxkj.edu.module.system.employee.domain.ao.UserUpdateStatusAo;
import com.sxkj.edu.module.system.employee.domain.bo.RequestTokenBO;
import com.sxkj.edu.module.system.employee.domain.dto.*;
import com.sxkj.edu.module.system.employee.domain.vo.EmployeeSingVO;
import com.sxkj.edu.module.system.employee.domain.vo.EmployeeVO;
import com.sxkj.edu.module.system.employee.domain.vo.RoleAccountHierarchyVO;
import com.sxkj.edu.module.system.login.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.InputStream;
import java.util.List;

/**
 * 用户管理
 * <p>
 * 用户管理
 *
 * @date 2021年12月19日上午11:34:52
 */
@RestController
@Api(tags = {SwaggerTagConst.Admin.MANAGER_USER})
@OperateLog
public class TBaseUserController {

    @Autowired
    private TbaseUserService userService;
    @Resource
    private LoginService loginService;

    @PostMapping("/user/query")
    @ApiOperation(value = "用户管理查询-分页", notes = "jiangjj")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "用户管理查询-分页", businessType = "用户管理")
    public JsonResult<PageResultDTO<EmployeeVO>> queryUserPage(@RequestBody UserQueryAO query) {
        if (!StringUtils.isEmpty(query.getKeyword())) {
            query.setKeyword(query.getKeyword().trim()); // 去首尾空格
        }
        return userService.selectEmployeeList(query);
    }

    @PostMapping("/user/approve")
    @ApiOperation(value = "用户注册账号审批", notes = "jiangjj")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "用户注册账号审批", businessType = "用户管理")
    public JsonResult approve(@RequestParam("id") Long id, @RequestParam("approveStatus") Integer approveStatus) {
        return userService.approve(id, approveStatus);
    }

    @GetMapping("/user/get/all")
    @ApiOperation(value = "查询所有已通过用户，用于选择", notes = "jiangjj")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "查询所有用户基本信息", businessType = "用户管理")
    public JsonResult<List<EmployeeVO>> getAll(Integer schoolId) {
        return JsonResult.buildSuccess(userService.getAllEmployee(schoolId));
    }

    @PostMapping("/query/passUser/list")
    @ApiOperation(value = "查询所有已通过用户", notes = "jiangjj")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "查询所有已通过用户", businessType = "用户管理")
    public JsonResult<List<EmployeeSingVO>> getPassUserAll(@RequestBody UserQuerySingAO queryAO) {
        return JsonResult.buildSuccess(userService.getPassUserAll(queryAO));
    }


    @ApiOperation(value = "添加用户", notes = "jiangjj")
    @PostMapping("/user/add")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "添加用户", businessType = "用户管理")
    @AccessLimit(seconds = 15, maxCount = 2, desc = "您的注册频率过快，请稍微在试")
    public JsonResult<String> addEmployee(@Validated @RequestBody EmployeeAddDTO emp) {
        return userService.addEmployee(emp);
    }

    @ApiOperation(value = "批量导入添加员工", notes = "jiangjj")
    @PostMapping("/user/batchAdd")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "批量导入添加员工", businessType = "用户管理")
    @AccessLimit(seconds = 15, maxCount = 2, desc = "您的注册频率过快，请稍微在试")
    public JsonResult<String> batchAddEmployee(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        List<EmployeeExcelDTO> list = ExcelUtils.readExcel(inputStream, EmployeeExcelDTO.class);
        return userService.batchAddEmployee(list);
    }

    @ApiOperation(value = "批量设置角色", notes = "jiangjj")
    @PostMapping("/user/batchSetRole")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "批量设置角色", businessType = "用户管理")
    public JsonResult<String> batchSetRole(@RequestBody EmployeeBatchUpdateRolesDTO employeeBatchUpdateRolesDTO) {
        userService.batchSetRole(employeeBatchUpdateRolesDTO);
        return JsonResult.buildSuccess();
    }

    @ApiOperation(value = "禁用单个用户", notes = "用户管理")
    @PostMapping("/user/disable/updateStatus")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "禁用单个用户", businessType = "用户管理")
    public JsonResult<String> updateStatus(@RequestBody UserUpdateStatusAo userUpdateStatusAo) {
        return userService.updateStatus(userUpdateStatusAo);
    }

    @ApiOperation(value = "批量禁用", notes = "jiangjj")
    @PostMapping("/user/batchUpdateStatus")
    @RolePermission(roles = {1,4})
    @AccessLimit(seconds = 5, maxCount = 2, desc = "批量禁用用户 频率1秒内不能超过2次")
    @OperateLog(type = AmqpConstant.OPERATION, value = "批量禁用", businessType = "用户管理")
    public JsonResult<String> batchUpdateStatus(@Valid @RequestBody EmployeeBatchUpdateStatusDTO batchUpdateStatusDTO) {
        return userService.batchUpdateStatus(batchUpdateStatusDTO);
    }

    @ApiOperation(value = "管理端-更新用户信息", notes = "jiangjj")
    @PostMapping("/user/update")
    @RolePermission(roles = {1,4})
    @AccessLimit(seconds = 5, maxCount = 2, desc = "更新用户 频率1秒内不能超过2次")
    @OperateLog(type = AmqpConstant.OPERATION, value = "更新用户信息", businessType = "用户管理")
    public JsonResult<String> updateEmployee(@Valid @RequestBody EmployeeUpdateDTO employeeUpdateDto) {
        return userService.updateEmployee(employeeUpdateDto);
    }

    @ApiOperation(value = "用户端-更新用户信息", notes = "jiangjj")
    @PostMapping("/platformUser/update")
    @AccessLimit(seconds = 5, maxCount = 2, desc = "更新用户 频率1秒内不能超过2次")
    @OperateLog(type = AmqpConstant.OPERATION, value = "用户端-更新用户信息" ,businessType = "用户管理")
    public JsonResult<String> updatePlatformUser(@Valid @RequestBody EmployeePlatformUpdateDTO user) {
        return userService.updatePlatfromUser(user);
    }

    @ApiOperation(value = "删除用户信息", notes = "jiangjj")
    @PostMapping("/user/delete/{usereId}")
    @RolePermission(roles = {1,4})
    @AccessLimit(seconds = 5, maxCount = 2, desc = "删除用户 频率1秒内不能超过2次")
    @OperateLog(type = AmqpConstant.OPERATION, value = "删除用户信息", businessType = "用户管理")
    public JsonResult<String> deleteEmployeeById(@PathVariable("usereId") Long usereId) {
        CheckParamUtil.checkLongParam(usereId, "删除的用户信息不能为空");
        return userService.deleteEmployeeById(usereId);
    }

    @ApiOperation(value = "单个用户角色授权", notes = "用户管理")
    @PostMapping("/user/updateRoles")
    @RolePermission(roles = {1,4})
    @AccessLimit(seconds = 5, maxCount = 2, desc = "更新用户角色 频率1秒内不能超过2次")
    @OperateLog(type = AmqpConstant.OPERATION, value = "单个用户角色授权", businessType = "用户管理")
    public JsonResult<String> updateRoles(@Valid @RequestBody EmployeeUpdateRolesDTO updateRolesDTO) {
        return userService.updateRoles(updateRolesDTO);
    }

    @ApiOperation(value = "修改密码", notes = "用户管理")
    @PostMapping("/user/updatePwd")
    @AccessLimit(seconds = 10, maxCount = 2, desc = "更新用户密码 频率1秒内不能超过2次")
    @OperateLog(type = AmqpConstant.OPERATION, value = "修改密码", businessType = "用户管理")
    public JsonResult<String> updatePwd(@Valid @RequestBody EmployeeUpdatePwdDTO updatePwdDTO) {
        RequestTokenBO requestToken = SmartRequestTokenUtil.getRequestUser();
        return userService.updatePwd(updatePwdDTO, requestToken);
    }

//    @ApiOperation(value = "通过部门id获取当前部门的人员&没有部门的人", notes = "用户管理")
//    @GetMapping("/user/listUserByDeptId/{deptId}")
//    @OperateLog(type = AmqpConstant.OPERATION, value = "用户注册账号审批" ,businessType = "用户管理")
//    public JsonResult<List<EmployeeVO>> listEmployeeByDeptId(@PathVariable Long deptId) {
//        return userService.getEmployeeByDeptId(deptId);
//    }

    @ApiOperation(value = "用户重置密码", notes = "用户管理")
    @GetMapping("/user/resetPasswd/{employeeId}")
    @OperateLog(type = AmqpConstant.OPERATION, value = "用户重置密码", businessType = "用户管理")
    @AccessLimit(seconds = 15, maxCount = 2, desc = "您的注册频率过快，请稍微在试")
    @RolePermission(roles = {1,4})
    public JsonResult resetPasswd(@PathVariable("employeeId") Integer usereId) throws BaseBusinessException {
        CheckParamUtil.checkIntegerTypeNot0Param(usereId, "重置密码的用户信息不能为空");
        return userService.resetPasswd(usereId);
    }


    @ApiOperation(value = "批量移除用户部门")
    @PostMapping("/batch/remove/userDept")
    @OperateLog(type = AmqpConstant.OPERATION, value = "批量移除用户部门", businessType = "用户管理")
    @AccessLimit(seconds = 15, maxCount = 2, desc = "您的注册频率过快，请稍微在试")
    public JsonResult<String> batchUserDept(@RequestBody UserRemoveDeptAo userRemoveDeptAo) {
        return userService.batchUserDeptById(userRemoveDeptAo.getUserIds());
    }

    @ApiOperation(value = "更新用户头像", notes = "jiangjj")
    @PostMapping("/upt/user/logo")
    @OperateLog(type = AmqpConstant.OPERATION, value = "更新用户头像", businessType = "用户管理")
    @AccessLimit(seconds = 10, maxCount = 2, desc = "您的注册频率过快，请稍微在试")
    public JsonResult<String> uptUserLogo(@RequestBody UptUserLogoAo userLogoAo) {
        return userService.uptUserLogo(userLogoAo);
    }


    @ApiOperation(value = "查询用户多角色及账号信息", notes = "jiangjj")
    @PostMapping("/user/queryRolesBySchoolAndPhone")
    @OperateLog(type = AmqpConstant.OPERATION, value = "查询用户多角色及账号信息", businessType = "用户管理")
    public JsonResult<List<RoleAccountHierarchyVO>> queryRolesBySchoolAndPhone(@RequestBody QueryRolesBySchoolAndPhoneAO queryAO) {
        return userService.queryRolesBySchoolAndPhone(queryAO);
    }


}
