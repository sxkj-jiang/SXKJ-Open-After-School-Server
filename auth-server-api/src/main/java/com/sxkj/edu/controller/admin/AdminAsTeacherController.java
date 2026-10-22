package com.sxkj.edu.controller.admin;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.anno.RolePermission;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.utils.CheckParamUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.afterschool.domain.ao.AsTeacherListQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsStudentStatusQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsTeacherAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsTeacherAuditAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsTeacherQueryAO;
import com.sxkj.edu.module.afterschool.domain.vo.AsTeacherVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsTeacherAuditSummaryVO;
import com.sxkj.edu.module.afterschool.service.AsTeacherService;
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
 * 教师档案管理 Controller - 管理端
 *
 * @author jiangjj
 * @date 2026-05-19
 */
@RestController
@RequestMapping("/admin/afterschool/teacher")
@Api(tags = {SwaggerTagConst.KHFW.TEACH_MANAGE})
@Slf4j
@OperateLog(type = AmqpConstant.OPERATION, value = "教师档案管理", businessType = "教师档案管理")
public class AdminAsTeacherController {

    @Resource
    private AsTeacherService asTeacherService;

    @PostMapping("/pageQuery")
    @ApiOperation(value = "教师档案管理-分页查询",notes = "jiangjj")
    @OperateLog(type = AmqpConstant.OPERATION, value = "教师档案管理-分页查询", businessType = "教师档案管理")
    public JsonResult<PageResultDTO<AsTeacherVO>> queryAsTeacherPage(@RequestBody AsTeacherQueryAO qo) {
        return JsonResult.buildSuccess(asTeacherService.queryAsTeacherPage(qo));
    }

    @PostMapping("/pass/pageQuery")
    @ApiOperation(value = "已注册教师-分页查询",notes = "jiangjj")
    @OperateLog(type = AmqpConstant.OPERATION, value = "已注册教师-分页查询", businessType = "教师档案管理")
    public JsonResult<PageResultDTO<AsTeacherVO>> queryPassAsTeacherPage(@RequestBody AsTeacherQueryAO qo) {
        qo.setRegisterStatus(1);
        return JsonResult.buildSuccess(asTeacherService.queryAsTeacherPage(qo));
    }

    @PostMapping("/update")
    @ApiOperation(value = "教师档案管理-编辑",notes = "jiangjj")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "教师档案管理-编辑", businessType = "教师档案管理")
    public JsonResult<String> updateAsTeacher(@Valid @RequestBody AsTeacherAddAO ao) {
        Long schoolId = SmartRequestTokenUtil.getRequestUserStuSchoolId();
        CheckParamUtil.checkLongParam(schoolId,"请选择学校");
        return asTeacherService.updateAsTeacher(ao);
    }

    @PostMapping("/delete/{id}")
    @ApiOperation(value = "教师档案管理-删除(逻辑删除)",notes = "jiangjj")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "教师档案管理-删除", businessType = "教师档案管理")
    public JsonResult<String> deleteAsTeacher(@PathVariable Long id) {
        Long schoolId = SmartRequestTokenUtil.getRequestUserStuSchoolId();
        CheckParamUtil.checkLongParam(schoolId,"请选择学校");
        return asTeacherService.deleteAsTeacher(id);
    }

    @PostMapping("/import")
    @ApiOperation(value = "教师档案管理-导入",notes = "jiangjj")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "教师档案管理-导入", businessType = "教师档案管理")
    public JsonResult<String> importAsTeacher(@RequestParam("file") MultipartFile file, @RequestParam("schoolId") Long schoolId) {
        CheckParamUtil.checkLongParam(schoolId,"请选择学校");
        return asTeacherService.importAsTeacher(schoolId, file);
    }

    @PostMapping("/export")
    @ApiOperation(value = "教师档案管理-导出",notes = "jiangjj")
    @OperateLog(type = AmqpConstant.OPERATION, value = "教师档案管理-导出", businessType = "教师档案管理")
    public void exportAsTeacher(@RequestBody AsTeacherQueryAO qo, HttpServletResponse response) {
        asTeacherService.exportAsTeacher(qo, response);
    }

    @PostMapping("/queryList")
    @ApiOperation(value = "教师档案管理-查询下拉列表",notes = "yh")
    @OperateLog(type = AmqpConstant.OPERATION, value = "教师档案管理-查询下拉列表", businessType = "教师档案管理")
    public JsonResult<List<AsTeacherVO>> queryAsTeacherSelect(@RequestBody AsTeacherListQueryAO qo) {
        return JsonResult.buildSuccess(asTeacherService.queryList(qo));
    }

    @GetMapping("/manager/detail")
    @ApiOperation(value = "教师档案管理-教师详情", notes = "jiangjj")
    @RolePermission(roles = {1, 4})
    public JsonResult<AsTeacherVO> getTeacherDetail(@RequestParam Long schoolId,
                                                    @RequestParam Long teacherId) {
        CheckParamUtil.checkLongParam(schoolId,"请选择学校");
        CheckParamUtil.checkLongParam(teacherId,"请选择教师");
        return JsonResult.buildSuccess(asTeacherService.getTeacherDetail(schoolId, teacherId));
    }

    @PostMapping("/audit")
    @ApiOperation(value = "教师档案管理-教师审核", notes = "jiangjj")
    @RolePermission(roles = {1, 4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "教师档案管理-教师审核", businessType = "教师档案管理")
    public JsonResult<String> auditTeacher(@Valid @RequestBody AsTeacherAuditAO ao) {
        return asTeacherService.auditTeacher(ao);
    }

    @PostMapping("/auditSummary")
    @ApiOperation(value = "教师档案管理-审核状态汇总", notes = "jiangjj")
    @OperateLog(type = AmqpConstant.OPERATION, value = "教师档案管理-审核状态汇总", businessType = "教师档案管理")
    public JsonResult<AsTeacherAuditSummaryVO> queryAuditSummary(@RequestBody AsStudentStatusQueryAO ao) {
        Long schoolId = ao.getSchoolId();
        if(ao == null || !CheckParamUtil.checkLongIdParam(ao.getSchoolId())) {
            schoolId = SmartRequestTokenUtil.getRequestUserStuSchoolId();
        }
        CheckParamUtil.checkLongParam(schoolId, "请选择学校");
        return JsonResult.buildSuccess(asTeacherService.queryAuditSummary(schoolId));
    }
}
