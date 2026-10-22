package com.sxkj.edu.module.system.department;

import com.sxkj.edu.common.anno.NoNeedLogin;
import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.anno.RolePermission;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.module.system.department.domain.dto.DepartmentCreateDTO;
import com.sxkj.edu.module.system.department.domain.dto.DepartmentUpdateDTO;
import com.sxkj.edu.module.system.department.domain.dto.DepartmentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理路由器
 *
 * @author jiangjj
 * @date 2017/12/19 14:29
 */
@Api(tags = {SwaggerTagConst.Admin.MANAGER_DEPARTMENT})
@RestController
@OperateLog
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @ApiOperation(value = "查询部门树形列表", notes = "查询部门列表")
    @GetMapping("/department/list")
    @OperateLog(type = AmqpConstant.OPERATION, value = "查询部门树形列表", businessType = "部门管理")
    public JsonResult<List<DepartmentVO>> listDepartment() {
        return departmentService.listDepartment();
    }

//    @ApiOperation(value = "查询所有部门及员工", notes = "查询所有部门及员工")
//    @GetMapping("/department/listEmployee")
//    @OperateLog(type = AmqpConstant.OPERATION, value = "查询所有部门及员工" ,businessType = "部门管理")
//    public JsonResult<List<DepartmentVO>> listDepartmentEmployee() {
//        UserDeptSearchAo deptSearchAo = new UserDeptSearchAo();
//        return departmentService.listAllDepartmentEmployee(deptSearchAo);
//    }


//    @PostMapping("/department/queryUserPage")
//    @OperateLog(type = AmqpConstant.OPERATION, value = "部门员工查询-分页" ,businessType = "部门管理")
//    public JsonResult<PageResultDTO<EmployeeDTO>> queryDeptUserPage(@RequestBody UserDeptSearchAo searchAo) {
//        return departmentService.queryDeptUserPage(searchAo);
//    }

    @ApiOperation(value = "添加部门", notes = "添加部门")
    @PostMapping("/department/add")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "添加部门", businessType = "部门管理")
    public JsonResult<String> addDepartment(@Validated @RequestBody DepartmentCreateDTO departmentCreateDTO) {
        return departmentService.addDepartment(departmentCreateDTO);
    }

    @ApiOperation(value = "更新部门信息", notes = "更新部门信息")
    @PostMapping("/department/update")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "更新部门信息", businessType = "部门管理")
    public JsonResult<String> updateDepartment(@Validated @RequestBody DepartmentUpdateDTO departmentUpdateDTO) {
        return departmentService.updateDepartment(departmentUpdateDTO);
    }

    @ApiOperation(value = "删除部门", notes = "删除部门")
    @PostMapping("/department/delete/{deptId}")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "删除部门", businessType = "部门管理")
    public JsonResult<String> delDepartment(@PathVariable Long deptId) {
        return departmentService.delDepartment(deptId);
    }

    @ApiOperation(value = "获取部门信息", notes = "获取部门")
    @GetMapping("/department/query/{deptId}")
    @OperateLog(type = AmqpConstant.OPERATION, value = "获取部门详情", businessType = "部门管理")
    public JsonResult<DepartmentVO> getDepartment(@PathVariable Long deptId) {
        return departmentService.getDepartmentById(deptId);
    }

    @ApiOperation(value = "查询部门列表", notes = "查询部门列表")
    @GetMapping("/department/listAll")
    @OperateLog(type = AmqpConstant.OPERATION, value = "查询部门列表", businessType = "部门管理")
    public JsonResult<List<DepartmentVO>> listAll() {
        return departmentService.listAll();
    }

    @ApiOperation(value = "查询部门列表(去重版)", notes = "查询部门列表")
    @GetMapping("/department/distinct/listAll")
    @OperateLog(type = AmqpConstant.OPERATION, value = "查询部门列表", businessType = "部门管理")
    public JsonResult<List<DepartmentVO>> listAllDistinct(@RequestParam(required = false) @ApiParam(value = "部门名称") String name) {
        return departmentService.listAllDistinct(name);
    }


    @ApiOperation(value = "上下移动")
    @GetMapping("/department/upOrDown/{deptId}/{swapId}")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "部门移动", businessType = "部门管理")
    public JsonResult<String> upOrDown(@PathVariable Long deptId, @PathVariable Long swapId) {
        return departmentService.upOrDown(deptId, swapId);
    }

//    @ApiOperation(value = "升级")
//    @GetMapping("/department/upgrade/{deptId}")
//    @OperateLog(type = AmqpConstant.OPERATION, value = "部门升级" ,businessType = "部门管理")
//    public JsonResult<String> upgrade(@PathVariable Long deptId) {
//        return departmentService.upgrade(deptId);
//    }
//
//    @ApiOperation(value = "降级")
//    @GetMapping("/department/downgrade/{deptId}/{preId}")
//    @OperateLog(type = AmqpConstant.OPERATION, value = "部门降级" ,businessType = "部门管理")
//    public JsonResult<String> downgrade(@PathVariable Long deptId, @PathVariable Long preId) {
//        return departmentService.downgrade(deptId, preId);
//    }


}
