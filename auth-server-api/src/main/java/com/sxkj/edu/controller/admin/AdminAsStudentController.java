package com.sxkj.edu.controller.admin;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.anno.RolePermission;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.utils.CheckParamUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.afterschool.domain.ao.AsStudentQueryListAO;
import com.sxkj.edu.module.afterschool.domain.dto.*;
import com.sxkj.edu.module.afterschool.domain.vo.AsStudentLearningAchievementDetailVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsStudentLearningAchievementVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsStudentMergedRecordVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsStudentVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsTeacherAuditSummaryVO;
import com.sxkj.edu.module.afterschool.service.AsStudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 学生档案管理 Controller - 管理端
 *
 * @author jiangjj
 * @date 2026-05-19
 */
@RestController
@RequestMapping("/admin/afterschool/student")
@Slf4j
@Api(tags = {SwaggerTagConst.KHFW.STU_MANAGE})
@OperateLog(type = AmqpConstant.OPERATION, value = "学生档案管理", businessType = "学生档案管理")
public class AdminAsStudentController {

    @Resource
    private AsStudentService asStudentService;

    @PostMapping("/pageQuery")
    @ApiOperation(value = "学生档案管理-分页查询",notes = "jiangjj")
    @OperateLog(type = AmqpConstant.OPERATION, value = "学生档案管理-分页查询", businessType = "学生档案管理")
    public JsonResult<PageResultDTO<AsStudentVO>> queryAsStudentPage(@RequestBody AsStudentQueryAO qo) {
        return JsonResult.buildSuccess(asStudentService.queryAsStudentPage(qo));
    }


    @PostMapping("/update")
    @ApiOperation(value = "学生档案管理-新增或编辑",notes = "jiangjj")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "学生档案管理-新增或编辑", businessType = "学生档案管理")
    public JsonResult<String> updateAsStudent(@Valid @RequestBody AsStudentAddAO ao) {
        return asStudentService.updateAsStudent(ao);
    }

    @PostMapping("/delete/{id}")
    @ApiOperation(value = "学生档案管理-删除",notes = "jiangjj")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "学生档案管理-删除", businessType = "学生档案管理")
    public JsonResult<String> deleteAsStudent(@PathVariable Long id) {
        return asStudentService.deleteAsStudent(id);
    }

    @PostMapping("/import")
    @ApiOperation(value = "学生档案管理-导入",notes = "jiangjj")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "学生档案管理-导入", businessType = "学生档案管理")
    public JsonResult<String> importAsStudent(@RequestParam("file") MultipartFile file,
                                              @RequestParam("schoolId") Long schoolId) {
        CheckParamUtil.checkLongParam(schoolId, "学校不能为空");
        return asStudentService.importAsStudentData(file, schoolId);
    }

    @PostMapping("/export")
    @ApiOperation(value = "学生档案管理-导出",notes = "jiangjj")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "学生档案管理-导出", businessType = "学生档案管理")
    public void exportAsStudent(@RequestBody AsStudentQueryAO qo, HttpServletResponse response) {
        asStudentService.exportAsStudent(qo, response);
    }

    @PostMapping("/mergedRecord")
    @ApiOperation(value = "学生档案-查询学生出勤/报名/请假/调课合并记录", notes = "jiangjj")
    @RolePermission(roles = {1, 4})
    public JsonResult<AsStudentMergedRecordVO> queryStudentMergedRecord(@Valid @RequestBody AsStudentMergeQueryAO ao) {
        return JsonResult.buildSuccess(asStudentService.queryStudentMergedRecord(ao.getSchoolId(), ao.getStudentId()));
    }

    @GetMapping("/manager/detail")
    @ApiOperation(value = "学生档案管理-学生详情", notes = "jiangjj")
    @RolePermission(roles = {1, 4})
    public JsonResult<AsStudentVO> getStudentDetail(@RequestParam Long schoolId,
                                                    @RequestParam Long studentId) {
        CheckParamUtil.checkLongParam(schoolId,"请选择学校");
        CheckParamUtil.checkLongParam(studentId,"请选择学生");
        return JsonResult.buildSuccess(asStudentService.getStudentDetail(schoolId, studentId));
    }

    @PostMapping("/auditParent")
    @ApiOperation(value = "学生档案管理-学生家长审核", notes = "jiangjj")
    @RolePermission(roles = {1, 4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "学生档案管理-学生家长审核", businessType = "学生档案管理")
    public JsonResult<String> auditStudentParent(@Valid @RequestBody AsStudentAuditAO ao) {
        return asStudentService.auditStudentParent(ao);
    }

    @PostMapping("/auditSummary")
    @ApiOperation(value = "学生档案管理-审核状态汇总", notes = "jiangjj")
    @OperateLog(type = AmqpConstant.OPERATION, value = "学生档案管理-审核状态汇总", businessType = "学生档案管理")
    public JsonResult<AsTeacherAuditSummaryVO> queryAuditSummary(@RequestBody AsStudentStatusQueryAO ao) {
        Long schoolId = ao == null ? null : ao.getSchoolId();
        if(!CheckParamUtil.checkLongIdParam(schoolId)) {
            schoolId = SmartRequestTokenUtil.getRequestUserStuSchoolId();
        }
        CheckParamUtil.checkLongParam(schoolId, "请选择学校");
        Long termId = ao == null ? null : ao.getTermId();
        return JsonResult.buildSuccess(asStudentService.queryStudentAuditSummary(schoolId, termId));
    }



    @PostMapping("/list")
    @ApiOperation(value = "学生档案管理-下拉列表查询",notes = "jiangjj")
    @OperateLog(type = AmqpConstant.OPERATION, value = "学生档案管理-分页查询", businessType = "学生档案管理")
    public JsonResult<List<AsStudentVO>> queryAsStudentList(@RequestBody @Valid AsStudentQueryListAO qo) {
        return JsonResult.buildSuccess(asStudentService.queryAsStudentList(qo));
    }

}
