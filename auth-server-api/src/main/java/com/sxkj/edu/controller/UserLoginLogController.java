package com.sxkj.edu.controller;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.domain.sys.dto.UserLoginLogDTO;
import com.sxkj.edu.domain.sys.dto.UserLoginLogQueryDTO;
import com.sxkj.edu.module.system.employee.domain.ao.UserQueryAO;
import com.sxkj.edu.module.system.employee.domain.vo.EmployeeVO;
import com.sxkj.edu.service.UserLoginLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 *@Description : 用户登录日志
 *@Author : JIANGJJ
 *@Date : 2022-9-7 14:35
 *@Company : SXKJ
 *@Version : 1.0
 **/
@RestController
@Api(tags = {SwaggerTagConst.Admin.MANAGER_USER_LOGIN_LOG})
@OperateLog
public class UserLoginLogController {

    @Autowired
    private UserLoginLogService userLoginLogService;

    @ApiOperation(value = "分页查询用户登录日志", notes = "日志管理")
    @PostMapping("/userLoginLog/page/query")
    @OperateLog(type = AmqpConstant.OPERATION, value = "分页查询用户登录日志" ,businessType = "系统管理")
    public JsonResult<PageResultDTO<UserLoginLogDTO>> queryByPage(@RequestBody UserLoginLogQueryDTO queryDTO) {
        return userLoginLogService.queryByPage(queryDTO);
    }


    @ApiOperation(value = "查询员工在线状态", notes = "日志管理")
    @PostMapping("/userOnLine/query")
    @OperateLog(type = AmqpConstant.OPERATION, value = "查询员工在线状态" ,businessType = "系统管理")
    public JsonResult<PageResultDTO<EmployeeVO>> queryUserOnLine(@RequestBody @Valid UserQueryAO queryDTO) {
        return userLoginLogService.queryUserOnLine(queryDTO);
    }

}
