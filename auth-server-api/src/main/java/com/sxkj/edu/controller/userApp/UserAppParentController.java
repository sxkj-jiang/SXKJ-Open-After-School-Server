package com.sxkj.edu.controller.userApp;

import com.sxkj.edu.common.anno.AccessLimit;
import com.sxkj.edu.common.anno.NoNeedLogin;
import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.utils.CheckParamUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.vo.khfw.EduclClassSimpleVO;
import com.sxkj.edu.domain.vo.khfw.EduclGradeSimpleVO;
import com.sxkj.edu.module.afterschool.domain.ao.*;
import com.sxkj.edu.module.afterschool.domain.dto.*;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentEntity;
import com.sxkj.edu.module.afterschool.domain.vo.*;
import com.sxkj.edu.module.afterschool.service.*;
import com.sxkj.edu.module.afterschool.support.StudentTermScope;
import com.sxkj.edu.module.afterschool.support.StudentTermScopeSupport;
import com.sxkj.edu.service.CacheServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 *@Description : 移动家长端app接口
 *@Author : JIANGJJ
 *@Date : 2026-5-22 10:57
 *@Company : SXKJ
 *@Version : 1.0
 **/
@RestController
@Slf4j
@RequestMapping("/api/parent/userApp")
@Api(tags = {SwaggerTagConst.PARENT.PAR_ZHGL})
@OperateLog
public class UserAppParentController {

    @Resource
    AsGuardianStudentService asGuardianStudentService;
    @Resource
    EduclGradeService educlGradeService;
    @Resource
    EduclClassService educlClassService;
    @Resource
    AsCourseSessionService asCourseSessionService;

    @Resource
    private StudentTermScopeSupport studentTermScopeSupport;


    @Resource
    AsStudentService asStudentService;


    @NoNeedLogin
    @PostMapping("/parentRegister")
    @ApiOperation(value = "家长注册", notes = "jiangjj")
    @AccessLimit(seconds = 4, maxCount = 5, desc = "您的频率过快，请稍微在试")
    public JsonResult<String> parentRegister(@Valid @RequestBody ParentRegisterAO ao) {
        return asGuardianStudentService.registerParent(ao);
    }


    @PostMapping("/common/queryGradePage")
    @ApiOperation(value = "根据学校查询年级分页", notes = "jiangjj")
    @NoNeedLogin
    @AccessLimit(seconds = 10, maxCount = 5, desc = "您的频率过快，请稍微在试")
    public JsonResult<PageResultDTO<EduclGradeVO>> queryGradePage(@Valid @RequestBody EduclGradeQueryAO qo) {
        return JsonResult.buildSuccess(educlGradeService.queryEduclGradePage(qo));
    }

    @PostMapping("/common/queryGradeList")
    @ApiOperation(value = "注册查询-根据学校查询年级列表", notes = "jiangjj")
    @NoNeedLogin
    @AccessLimit(seconds = 10, maxCount = 5, desc = "您的频率过快，请稍微在试")
    public JsonResult<List<EduclGradeSimpleVO>> queryGradeList(@Valid @RequestBody EduclGradeListQueryAO qo) {
        return JsonResult.buildSuccess(educlGradeService.queryGradeSimpleList(qo));
    }

    @PostMapping("/common/queryClassPage")
    @ApiOperation(value = "根据学校和年级查询班级分页", notes = "jiangjj")
    @NoNeedLogin
    @AccessLimit(seconds = 10, maxCount = 5, desc = "您的频率过快，请稍微在试")
    public JsonResult<PageResultDTO<EduclClassVO>> queryClassPage(@Valid @RequestBody EduclClassQueryAO qo) {
        return JsonResult.buildSuccess(educlClassService.queryEduclClassPage(qo));
    }

    @PostMapping("/common/queryClassList")
    @ApiOperation(value = "注册查询-根据学校、年级编号和班级名称查询班级列表", notes = "jiangjj")
    @NoNeedLogin
    @AccessLimit(seconds = 10, maxCount = 5, desc = "您的频率过快，请稍微在试")
    public JsonResult<List<EduclClassSimpleVO>> queryClassList(@Valid @RequestBody EduclClassListQueryAO qo) {
        return JsonResult.buildSuccess(educlClassService.queryClassSimpleList(qo));
    }


    @PostMapping("/pageStudentTodayCourse")
    @ApiOperation(value = "首页-查询学生今日课程分页", notes = "jiangjj")
    @OperateLog(type = AmqpConstant.OPERATION, value = "查询学生今日课程", businessType = "学生课表")
    public JsonResult<PageResultDTO<AsStudentTodayCourseVO>> pageStudentTodayCourse(@Valid @RequestBody AsStudentTodayCoursePageQueryAO ao) {
        Long termId = resolveParentQueryTerm(ao.getSchoolId(), ao.getTermId());
        ao.setTermId(termId);
        Long currentUserId = SmartRequestTokenUtil.getRequestUserId();
        Long studentId = resolveParentStudentId(currentUserId, ao.getSchoolId(), termId,
                ao.getStudentIdentityId());
        if(null == studentId){
            return JsonResult.buildError("您好，未找到有效的学生身份数据");
        }
        return JsonResult.buildSuccess(asCourseSessionService.pageStudentTodayCourse(ao, studentId));
    }




    @PostMapping("/queryClass/byStuNo")
    @ApiOperation(value = "根据年级班级查询学号", notes = "jiangjj")
    @OperateLog(type = AmqpConstant.OPERATION, value = "根据年级班级查询学号", businessType = "班级查询")
    @NoNeedLogin
    @AccessLimit(seconds = 10, maxCount = 5, desc = "您的频率过快，请稍微在试")
    public JsonResult<List<AsQueryStudentNoByGradeClassVO>> queryClassByStuNo(@RequestBody @Valid AsStudentNoByGradeClassAO query) {
        return JsonResult.buildSuccess(asStudentService.queryStudentNoByGradeClass(query));
    }

    /**
     * 根据稳定学生身份解析目标学期学生快照，未指定时保持原有默认学生逻辑。
     */
    Long resolveParentStudentId(Long parentId, Long schoolId, Long termId, Long studentIdentityId) {
        if (studentIdentityId == null) {
            return asGuardianStudentService.queryStudentIdByParent(parentId, schoolId);
        }
        AsStudentEntity student = studentTermScopeSupport.resolveGuardianStudent(
                parentId, schoolId, termId, studentIdentityId);
        return student == null ? null : student.getId();
    }


    /**
     * 解析家长普通查询使用的学期，日期空档时允许只读回看最近学期。
     */
    private Long resolveParentQueryTerm(Long schoolId, Long termId) {
        return termId == null
                ? studentTermScopeSupport.resolveReadableTerm(schoolId).getId()
                : studentTermScopeSupport.requireTermInSchool(schoolId, termId).getId();
    }



}
