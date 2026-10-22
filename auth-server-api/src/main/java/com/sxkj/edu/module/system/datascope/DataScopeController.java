package com.sxkj.edu.module.system.datascope;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.anno.RolePermission;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.module.system.datascope.domain.dto.DataScopeAndViewTypeVO;
import com.sxkj.edu.module.system.datascope.domain.dto.DataScopeBatchSetRoleDTO;
import com.sxkj.edu.module.system.datascope.domain.dto.DataScopeSelectVO;
import com.sxkj.edu.module.system.datascope.service.DataScopeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Api(tags = {SwaggerTagConst.Admin.MANAGER_DATA_SCOPE})
@RestController
@OperateLog
public class DataScopeController {

    @Autowired
    private DataScopeService dataScopeService;

    @ApiOperation(value = "获取当前系统所配置的所有数据范围")
    @GetMapping("/dataScope/list")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "获取当前系统所配置的所有数据范围", businessType = "数据范围")
    public JsonResult<List<DataScopeAndViewTypeVO>> dataScopeList() {
        return dataScopeService.dataScopeList();
    }

    @ApiOperation(value = "获取某角色所设置的数据范围")
    @GetMapping("/dataScope/listByRole/{roleId}")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "获取某角色所设置的数据范围", businessType = "数据范围")
    public JsonResult<List<DataScopeSelectVO>> dataScopeListByRole(@PathVariable Long roleId) {
        return dataScopeService.dataScopeListByRole(roleId);
    }

    @ApiOperation(value = "批量设置某角色数据范围")
    @PostMapping("/dataScope/batchSet")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "批量设置某角色数据范围", businessType = "数据范围")
    public JsonResult<String> dataScopeBatchSet(@RequestBody @Valid DataScopeBatchSetRoleDTO batchSetRoleDTO) {
        return dataScopeService.dataScopeBatchSet(batchSetRoleDTO);
    }


}
