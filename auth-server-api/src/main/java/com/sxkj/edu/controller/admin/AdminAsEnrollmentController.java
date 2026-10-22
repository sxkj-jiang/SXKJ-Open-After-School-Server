package com.sxkj.edu.controller.admin;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.utils.CheckParamUtil;
import com.sxkj.edu.module.afterschool.domain.dto.*;
import com.sxkj.edu.module.afterschool.domain.vo.*;
import com.sxkj.edu.module.afterschool.service.AsEnrollmentAdminQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/admin/afterschool/enrollment")
@Api(tags = {SwaggerTagConst.KHFW.XSBM})
@OperateLog
@Slf4j
public class AdminAsEnrollmentController {

    @Autowired
    private AsEnrollmentAdminQueryService asEnrollmentAdminQueryService;

    @PostMapping("/init-selection/{serviceId}")
    @ApiOperation("初始化学生选课状态")
    public JsonResult<AsEnrollmentInitResultVO> initSelectionStudentEnrollmen(@PathVariable Long serviceId) {
        return asEnrollmentAdminQueryService.initSelection(serviceId);
    }

    @PostMapping("/selected/page")
    @ApiOperation("分页查询已选课组班")
    public JsonResult<PageResultDTO<AsEnrollmentSelectionRowVO>> selectedStudentEnrollmentPage(@RequestBody AsEnrollmentSelectionPageQueryAO query) {
        return JsonResult.buildSuccess(asEnrollmentAdminQueryService.pageSelected(query));
    }

    @PostMapping("/unselected/page")
    @ApiOperation("分页查询未选课组班")
    public JsonResult<PageResultDTO<AsEnrollmentSelectionRowVO>> unselectedStudentEnrollmenPage(@RequestBody AsEnrollmentSelectionPageQueryAO query) {
        CheckParamUtil.checkLongParam(query.getServiceId(), "课后服务编号不能为空");
        return JsonResult.buildSuccess(asEnrollmentAdminQueryService.pageAdminUnselected(query));
    }

    @PostMapping("/selection/status-count")
    @ApiOperation("统计学生选课各Tab数量")
    public JsonResult<AsEnrollmentSelectionStatusCountVO> countSelectionStatusesV2(@Valid @RequestBody AsEnrollmentSelectionCountQueryAO query) {
        return JsonResult.buildSuccess(asEnrollmentAdminQueryService.countAdminSelectionStatusesV2(query));
    }


    @GetMapping("/selection-detail/{enrollmentId}")
    @ApiOperation("查询选课详情")
    public JsonResult<AsEnrollmentSelectionDetailVO> selectionStudentEnrollmenDetail(@PathVariable Long enrollmentId) {
        return JsonResult.buildSuccess(asEnrollmentAdminQueryService.getSelectionDetail(enrollmentId));
    }

    @GetMapping("/assign-options")
    @ApiOperation("查询分配课程候选")
    public JsonResult<AsEnrollmentAssignOptionsVO> assignOptions(AsEnrollmentAssignOptionsQueryAO query) {
        return asEnrollmentAdminQueryService.listAssignOptions(query);
    }


    @PostMapping("/change-course")
    @ApiOperation("编辑已选课程")
    public JsonResult<AsEnrollmentOperateResultVO> changeCourseStudentEnrollmen(@Valid @RequestBody AsEnrollmentChangeAO ao) {
        return asEnrollmentAdminQueryService.changeCourse(ao);
    }

    @PostMapping("/cancel/{enrollmentId}")
    @ApiOperation("取消选课")
    public JsonResult<String> cancelStudentEnrollmen(@PathVariable Long enrollmentId, @RequestBody(required = false) AsEnrollmentCancelAO ao) {
        return asEnrollmentAdminQueryService.cancelEnrollment(enrollmentId, ao == null ? null : ao.getCancelReason());
    }

    @PostMapping("/export")
    @ApiOperation("导出学生选课")
    public void exportStudentEnrollmen(@RequestBody AsEnrollmentExportAO query, HttpServletResponse response) {
        asEnrollmentAdminQueryService.exportEnrollments(query,response);
    }

    @GetMapping("/filter-options")
    @ApiOperation("查询学生选课筛选项")
    public JsonResult<AsEnrollmentFilterOptionsVO> filterStudentEnrollmenOptions(@RequestParam(required = false) Long schoolId,
                                                                                 @RequestParam(required = false) Long serviceId,
                                                                                 @RequestParam(required = false) Long termId) {
        return JsonResult.buildSuccess(asEnrollmentAdminQueryService.filterOptions(schoolId, serviceId, termId));
    }
}
