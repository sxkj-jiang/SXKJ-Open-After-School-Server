package com.sxkj.edu.controller.userApp;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.anno.RolePermission;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.ao.AsTeacherStudentQueryAO;
import com.sxkj.edu.domain.vo.AsTeacherCourseStudentVO;
import com.sxkj.edu.module.afterschool.domain.ao.*;
import com.sxkj.edu.module.afterschool.domain.vo.AsTeacherCourseSummaryVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsTeacherPersonDetailVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsTeacherSessionStatsVO;
import com.sxkj.edu.module.afterschool.domain.vo.TeacherSessionRecordPageVO;
import com.sxkj.edu.module.afterschool.service.AsTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 *@Description : 移动教师端app 我的接口
 *@Author : JIANGJJ
 *@Date : 2026-5-22 10:57
 *@Company : SXKJ
 *@Version : 1.0
 **/
@RestController
@Slf4j
@RequestMapping("/api/teacher/userApp/person")
@Api(tags = {SwaggerTagConst.TEACHER.TEA_PERSON})
@OperateLog
public class UserAppTeacherPersonController {

    @Resource
    AsTeacherService asTeacherService;


    @PostMapping("/personDetail")
    @ApiOperation(value = "教师我的-个人信息详情查询", notes = "jiangjj")
    @RolePermission(roles = {3, 5})
    public JsonResult<AsTeacherPersonDetailVO> queryPersonDetail(
            @Valid @RequestBody AsTeacherPersonDetailQueryAO ao) {
        Long currentUserId = SmartRequestTokenUtil.getRequestUserId();
        AsTeacherPersonDetailVO result = asTeacherService.queryTeacherPersonDetail(
                ao.getSchoolId(), currentUserId);
        if (result == null) {
            return JsonResult.buildError("当前账号未绑定授课老师身份");
        }
        return JsonResult.buildSuccess(result);
    }

    @PostMapping("/personEdit")
    @ApiOperation(value = "教师我的-个人信息编辑", notes = "jiangjj")
    @RolePermission(roles = {3, 5})
    public JsonResult<String> updatePerson(
            @Valid @RequestBody AsTeacherPersonEditAO ao) {
        return asTeacherService.updateTeacherPerson(ao);
    }

    @PostMapping("/courseStudents")
    @ApiOperation(value = "教师查询任课学生信息", notes = "yh")
    public JsonResult<List<AsTeacherCourseStudentVO>> listCourseStudents(
            @RequestBody(required = false) AsTeacherStudentQueryAO ao) {
        return JsonResult.buildSuccess(asTeacherService.listTeacherCourseStudents(ao));
    }

}
