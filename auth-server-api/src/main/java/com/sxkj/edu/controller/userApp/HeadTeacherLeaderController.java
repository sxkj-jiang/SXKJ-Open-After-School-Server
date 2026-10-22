package com.sxkj.edu.controller.userApp;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.PageVO;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.afterschool.domain.ao.evaluation.AsEvaluationAnswerAddAO;
import com.sxkj.edu.module.afterschool.domain.ao.evaluation.AsEvaluationAnswerTeacherLeaderAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.*;
import com.sxkj.edu.module.afterschool.domain.query.HeadteacherEvaluationStudentQueryAO;
import com.sxkj.edu.module.afterschool.domain.query.HeadteacherEvaluationTaskListAO;
import com.sxkj.edu.module.afterschool.domain.vo.*;
import com.sxkj.edu.module.afterschool.domain.vo.evaluation.HeadteacherEvaluationStudentVO;
import com.sxkj.edu.module.afterschool.domain.vo.evaluation.HeadteacherEvaluationTaskListVO;
import com.sxkj.edu.module.afterschool.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 *@Description : 移动班主任端app接口
 *@Author : JIANGJJ
 *@Date : 2026-5-22 10:57
 *@Company : SXKJ
 *@Version : 1.0
 **/
@RestController
@Slf4j
@RequestMapping("/api/teacherLeader/userApp")
@Api(tags = {SwaggerTagConst.TEA_LEADER.LEAD_ZHGL})
@OperateLog
public class HeadTeacherLeaderController {

    @Autowired
    private AsTeacherClassService asTeacherClassService;

    @Resource
    private AsTeacherService asTeacherService;

    @Resource
    AsCourseSessionService asCourseSessionService;
    @Autowired
    private AsStudentDestinationService asStudentDestinationService;

    @PostMapping("/classStudentList")
    @ApiOperation(value = "班主任查询名下自然班级学生列表", notes = "jiangjj")
    public JsonResult<List<AsHeadTeacherClassStudentListVO>> classStudentList(
            @RequestBody(required = false) AsTermScopeQueryAO ao) {
        Long currentTeacherId = asTeacherService.queryTeacherIdByPhone(null, null);
        Long termId = ao == null ? null : ao.getTermId();
        List<AsHeadTeacherClassStudentListVO> result = asTeacherClassService.queryHeadTeacherClassStudentList(
                currentTeacherId, SmartRequestTokenUtil.getRequestUserStuSchoolId(), termId);
        return JsonResult.buildSuccess(result);
    }

    @PostMapping("/pageStudentSessionsForTeacher")
    @ApiOperation(value = "班主任代学生请假-查询学生课次分页", notes = "jiangjj")
    @OperateLog(type = AmqpConstant.OPERATION, value = "班主任代学生请假-查询学生课次分页", businessType = "学生课次")
    public JsonResult<PageResultDTO<AsStudentSessionVO>> pageStudentSessions(@Valid @RequestBody AsStudentSessionLeaderPageQueryAO ao) {
        AsStudentSessionPageQueryAO sessionPageQueryAO = new AsStudentSessionPageQueryAO();
        SmartBeanUtil.copyProperties(ao,sessionPageQueryAO);
        return JsonResult.buildSuccess(asCourseSessionService.pageStudentSessions(sessionPageQueryAO, ao.getStudentId()));
    }

    @PostMapping("/pageHeadTeacherStudentDestination")
    @ApiOperation(value = "班主任查询自然班级学生去向", notes = "jiangjj")
    @OperateLog(type = AmqpConstant.OPERATION, value = "查询班主任班级学生去向", businessType = "学生去向")
    public JsonResult<List<AsTeacherStudentDestinationTermVO>> pageHeadTeacherStudentDestination(
            @Valid @RequestBody AsHeadTeacherStudentDestinationPageQueryAO ao) {
        ao.setHeadTeacherId(SmartRequestTokenUtil.getRequestUserId());
        ao.setSchoolId(SmartRequestTokenUtil.getRequestUserStuSchoolId());
        return JsonResult.buildSuccess(asStudentDestinationService.listHeadTeacherStudentDestination(ao));
    }
}
