package com.sxkj.edu.controller;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.domain.sys.dto.UserOperateLogDTO;
import com.sxkj.edu.domain.sys.dto.UserOperateLogQueryDTO;
import com.sxkj.edu.service.UserOperateLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@Api(tags = {SwaggerTagConst.Admin.MANAGER_USER_OPERATE_LOG})
@OperateLog
public class UserOperateLogController {

    @Autowired
    private UserOperateLogService userOperateLogService;

    @ApiOperation(value = "分页查询",notes = "@author yandanyang")
    @PostMapping("/userOperateLog/page/query")
    @OperateLog(type = AmqpConstant.OPERATION, value = "操作日志查询" ,businessType = "日志管理")
    public JsonResult<PageResultDTO<UserOperateLogDTO>> queryByPage(@RequestBody UserOperateLogQueryDTO queryDTO) {
        return userOperateLogService.queryByPage(queryDTO);
    }

    @ApiOperation(value="详情",notes = "@author yandanyang")
    @GetMapping("/userOperateLog/detail/{id}")
    @OperateLog(type = AmqpConstant.OPERATION, value = "操作日志详情" ,businessType = "日志管理")
    public JsonResult<UserOperateLogDTO> detail(@PathVariable("id") Long id){
        return userOperateLogService.detail(id);
    }


}
