package com.sxkj.edu.module.support.heartbeat;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.base.domain.PageParamDTO;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Api(tags = {SwaggerTagConst.Admin.MANAGER_HEART_BEAT})
@OperateLog
@RestController
public class HeartBeatController {

    @Autowired
    private HeartBeatService heartBeatService;

    @PostMapping("/heartBeat/query")
    @ApiOperation("查询心跳记录 @author zhuoda")
    public JsonResult<PageResultDTO<HeartBeatRecordVO>> query(@RequestBody @Valid PageParamDTO pageParamDTO){
        return heartBeatService.pageQuery(pageParamDTO);
    }

}
