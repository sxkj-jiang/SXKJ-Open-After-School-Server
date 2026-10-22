package com.sxkj.edu.controller.admin;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.sxkj.edu.module.afterschool.domain.ao.AsCourseClassTransferAO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.anno.RolePermission;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.module.afterschool.domain.ao.AsCourseClassCountQueryAO;
import com.sxkj.edu.module.afterschool.domain.ao.AsCourseClassPageQueryAO;
import com.sxkj.edu.module.afterschool.domain.ao.AsSessionAttendancePageQueryAO;
import com.sxkj.edu.module.afterschool.domain.vo.AsCourseClassCountVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsCourseClassPageVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsCourseClassVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsSessionAttendancePageVO;
import com.sxkj.edu.module.afterschool.service.AsCourseClassService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 课后服务班级管理 Controller - 管理端
 *
 * @author jiangjj
 * @date 2026-05-19
 */
@RestController
@RequestMapping("/admin/afterschool/aftClass")
@Api(tags = {SwaggerTagConst.KHFW.CLASS_MANAGEMENT})
@Slf4j
@OperateLog(type = AmqpConstant.OPERATION, value = "课后服务班级", businessType = "课后服务班级")
public class AdminAsClassController {


    @Resource
    private AsCourseClassService asCourseClassService;



    @PostMapping("/pageQueryCourseClass")
    @ApiOperation(value = "课程班分页查询", notes = "jiangjj")
    @RolePermission(roles = {1, 4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "课程班分页查询", businessType = "课程班管理")
    public JsonResult<PageResultDTO<AsCourseClassPageVO>> pageQueryCourseClass(@RequestBody @Valid AsCourseClassPageQueryAO qo) {
        return JsonResult.buildSuccess(asCourseClassService.pageQueryCourseClass(qo));
    }

    @PostMapping("/countCourseClass")
    @ApiOperation(value = "统计课程班各报名方式数量", notes = "wangyouhuai")
    @RolePermission(roles = {1, 4})
    public JsonResult<AsCourseClassCountVO> countCourseClass(@RequestBody(required = false) AsCourseClassCountQueryAO qo) {
        return JsonResult.buildSuccess(asCourseClassService.countCourseClass(qo));
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出课程班列表", notes = "wangyouhuai")
    @RolePermission(roles = {1, 4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "课程班导出", businessType = "课程班管理")
    public void exportCourseClass(@RequestBody AsCourseClassPageQueryAO qo, HttpServletResponse response) {
        asCourseClassService.exportCourseClass(qo, response);
    }

    @PostMapping("/courseClassDetail/{id}")
    @ApiOperation(value = "课程班-详情", notes = "yh")
    @OperateLog(type = AmqpConstant.OPERATION, value = "课程班-详情", businessType = "课程班管理")
    public JsonResult<AsCourseClassVO> pageQueryCourseClass(@PathVariable Long id) {
        return JsonResult.buildSuccess(asCourseClassService.getCourseClassDetail(id));
    }

    @PostMapping("/pageQuerySessionAttendance")
    @ApiOperation(value = "课程班-课次考勤分页查询", notes = "jiangjj")
    @RolePermission(roles = {1, 4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "课程班课次考勤分页查询", businessType = "课程班管理")
    public JsonResult<PageResultDTO<AsSessionAttendancePageVO>> pageQuerySessionAttendance(
            @RequestBody @Valid AsSessionAttendancePageQueryAO qo) {
        return JsonResult.buildSuccess(asCourseClassService.pageQuerySessionAttendance(qo));
    }

    @PostMapping("/transfer/teacherSubject")
    @ApiOperation(value = "课程班-教师课程移交", notes = "jiangjj")
    @RolePermission(roles = {1, 3})
    @OperateLog(type = AmqpConstant.OPERATION, value = "课程班-教师课程移交", businessType = "课程班管理")
    public JsonResult<String> transferTeacherSubjectApi(
            @RequestBody @Valid AsCourseClassTransferAO ao) {
        return asCourseClassService.transferTeacherSubject(ao);
    }

}
