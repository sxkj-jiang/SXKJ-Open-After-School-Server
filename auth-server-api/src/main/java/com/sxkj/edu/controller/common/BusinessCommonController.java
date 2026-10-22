package com.sxkj.edu.controller.common;

import com.sxkj.edu.common.anno.NoNeedLogin;
import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.config.GrobProperties;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.afterschool.domain.vo.SchoolCalendarVO;
import com.sxkj.edu.module.afterschool.service.BusinessCommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *@Description : 通用业务接口
 *@Author : JIANGJJ
 *@Date : 2026-6-14 10:07
 *@Company : SXKJ
 *@Version : 1.0
 **/
@RestController
@Slf4j
@RequestMapping("/api/portal-manager/busCommon")
@Api(tags = {SwaggerTagConst.MULT.BUS_COMMON})
@OperateLog
public class BusinessCommonController {


    @Resource
    BusinessCommonService businessCommonService;

    @GetMapping("/querySchoolCalendar")
    @ApiOperation(value = "查询校历", notes = "根据学校ID查询当前学期校历，支持指定第几周")
    public JsonResult<SchoolCalendarVO> querySchoolCalendar(
            @RequestParam(value = "weekNo", required = false) Integer weekNo) {
        Long schoolId = SmartRequestTokenUtil.getRequestUserStuSchoolId();
        SchoolCalendarVO vo = businessCommonService.querySchoolCalendar(schoolId, weekNo);
        if (vo == null) {
            return JsonResult.buildError("当前时间不存在该学校的任何课后服务日历");
        }
        return JsonResult.buildSuccess(vo);
    }

}
