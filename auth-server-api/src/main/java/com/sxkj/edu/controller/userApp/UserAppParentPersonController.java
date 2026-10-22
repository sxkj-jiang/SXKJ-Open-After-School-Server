package com.sxkj.edu.controller.userApp;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.anno.RolePermission;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.afterschool.domain.ao.AsParentCourseAttendanceQueryAO;
import com.sxkj.edu.module.afterschool.domain.ao.AsParentEnrollmentPageQueryAO;
import com.sxkj.edu.module.afterschool.domain.ao.AsParentPersonDetailQueryAO;
import com.sxkj.edu.module.afterschool.domain.ao.AsParentPersonEditAO;
import com.sxkj.edu.module.afterschool.domain.vo.AsParentCourseAttendanceVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsParentEnrollmentTermVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsParentPersonDetailVO;
import com.sxkj.edu.module.afterschool.service.AsEnrollmentGuardianService;
import com.sxkj.edu.module.afterschool.service.AsGuardianStudentService;
import com.sxkj.edu.module.system.login.domain.LoginStuParentDetailVO;
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
 *@Description : 移动家长端app-我的接口
 *@Author : JIANGJJ
 *@Date : 2026-5-22 10:57
 *@Company : SXKJ
 *@Version : 1.0
 **/
@RestController
@Slf4j
@RequestMapping("/api/parent/userApp/person")
@Api(tags = {SwaggerTagConst.PARENT.PAR_PERSON})
@OperateLog
public class UserAppParentPersonController {

    @Resource
    AsGuardianStudentService asGuardianStudentService;

    @Resource
    AsEnrollmentGuardianService asEnrollmentGuardianService;

    @PostMapping("/enrollmentCourseList")
    @ApiOperation(value = "家长查询学生选课记录", notes = "根据家长ID、学校ID、学期ID(选填)查询学生选课记录，以学期为一级维度、课程为二级维度")
    public JsonResult<List<AsParentEnrollmentTermVO>> queryEnrollmentCourseList(
            @Valid @RequestBody AsParentEnrollmentPageQueryAO ao) {
        Long parentId = SmartRequestTokenUtil.getRequestUserId();
        LoginStuParentDetailVO queryStudentByParent = asGuardianStudentService.queryStudentByParent(parentId,ao.getSchoolId());
        if(null == queryStudentByParent){
            return JsonResult.buildError("当前账号未绑定家长身份");
        }
        List<AsParentEnrollmentTermVO> result = asEnrollmentGuardianService.queryParentEnrollmentByTerm(
                queryStudentByParent, ao.getSchoolId());
        return JsonResult.buildSuccess(result);
    }

    @PostMapping("/personDetail")
    @ApiOperation(value = "家长我的-个人信息详情查询", notes = "jiangjj")
    @RolePermission(roles = {2})
    public JsonResult<AsParentPersonDetailVO> queryParentPersonDetail(
            @Valid @RequestBody AsParentPersonDetailQueryAO ao) {
        Long currentUserId = SmartRequestTokenUtil.getRequestUserId();
        AsParentPersonDetailVO result = asGuardianStudentService.queryParentPersonDetail(
                ao.getSchoolId(), currentUserId,ao.getStudentIdentityId());
        if (result == null) {
            return JsonResult.buildError("当前账号未绑定家长身份");
        }
        return JsonResult.buildSuccess(result);
    }

    @PostMapping("/personEdit")
    @ApiOperation(value = "家长我的-个人信息编辑", notes = "jiangjj")
    @RolePermission(roles = {2})
    public JsonResult<String> updateAppParentPerson(
            @Valid @RequestBody AsParentPersonEditAO ao) {
        return asGuardianStudentService.updateParentPerson(ao);
    }

}
