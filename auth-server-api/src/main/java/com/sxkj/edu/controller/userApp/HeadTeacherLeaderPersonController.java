package com.sxkj.edu.controller.userApp;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.ao.AsHeadTeacherClassStudentInfoAO;
import com.sxkj.edu.domain.vo.AsHeadTeacherClassStudentInfoVO;
import com.sxkj.edu.module.afterschool.domain.dto.*;
import com.sxkj.edu.module.afterschool.domain.vo.*;
import com.sxkj.edu.module.afterschool.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 *@Description : 移动班主任端app-我的接口
 *@Author : JIANGJJ
 *@Date : 2026-5-22 10:57
 *@Company : SXKJ
 *@Version : 1.0
 **/
@RestController
@Slf4j
@RequestMapping("/api/teacherLeader/userApp/person")
@Api(tags = {SwaggerTagConst.TEA_LEADER.LEAD_PERSON})
@OperateLog
public class HeadTeacherLeaderPersonController {

    @Resource
    AsTeacherService asTeacherService;

    @Resource
    AsTeacherClassService asTeacherClassService;

//    @Resource
//    AsAttendanceRecordService asAttendanceRecordService;

    @PostMapping("/classStudentInfo")
    @ApiOperation(value = "班主任查询班级学生信息", notes = "yh")
    public JsonResult<List<AsHeadTeacherClassStudentInfoVO>> classStudentInfo(@RequestBody AsHeadTeacherClassStudentInfoAO ao) {
        String phone = SmartRequestTokenUtil.getRequestUser().getEmployeeBO().getPhone();
        Long currentTeacherId = asTeacherService.queryTeacherIdByPhone(phone, SmartRequestTokenUtil.getRequestUserStuSchoolId());
        ao.setTeacherId(currentTeacherId);
        List<AsHeadTeacherClassStudentInfoVO> result = asTeacherClassService.queryClassStudentInfo(ao);
        return JsonResult.buildSuccess(result);
    }

//    @PostMapping("/pageHeadTeacherStudentDestination")
//    @ApiOperation(value = "班主任查询自然班级学生去向", notes = "jiangjj")
//    @OperateLog(type = AmqpConstant.OPERATION, value = "查询班主任班级学生去向", businessType = "学生去向")
//    public JsonResult<List<AsTeacherStudentDestinationTermVO>> pageHeadTeacherStudentDestination(
//            @Valid @RequestBody AsHeadTeacherStudentDestinationPageQueryAO ao) {
//        ao.setHeadTeacherId(SmartRequestTokenUtil.getRequestUserId());
//        ao.setSchoolId(SmartRequestTokenUtil.getRequestUserStuSchoolId());
//        return JsonResult.buildSuccess(asAttendanceRecordService.listHeadTeacherStudentDestination(ao));
//    }

}
