package com.sxkj.edu.controller.guardian;

import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.module.afterschool.domain.dto.*;
import com.sxkj.edu.module.afterschool.domain.vo.*;
import com.sxkj.edu.module.afterschool.service.AsEnrollmentGuardianService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/guardian/afterschool/enrollment")
@Api(tags = {SwaggerTagConst.PARENT.PAR_ZHGL})
@Slf4j
public class GuardianAsEnrollmentController {

    @Autowired
    private AsEnrollmentGuardianService asEnrollmentGuardianService;

    @GetMapping("/home-options")
    @ApiOperation("查询选课首页初始化信息")
    public JsonResult<AsGuardianEnrollmentHomeOptionsVO> homeOptions(@Valid AsGuardianEnrollmentHomeOptionsQueryAO query) {
        return asEnrollmentGuardianService.getGuardianHomeOptions(query);
    }

    @PostMapping("/record/page")
    @ApiOperation("分页查询我的报名记录(新版)")
    public JsonResult<PageResultDTO<AsEnrollmentSelectionRowVO>> queryEnrollmentPage(@RequestBody AsEnrollmentSelectionPageQueryAO query) {
        return JsonResult.buildSuccess(asEnrollmentGuardianService.pageGuardianEnrollments(query));
    }

    @PostMapping("/available-course/page")
    @ApiOperation("分页查询可报名课程")
    public JsonResult<PageResultDTO<AsAssignableCourseClassVO>> availableCoursePage(@Valid @RequestBody AsEnrollmentAvailableCourseQueryAO query) {
        return JsonResult.buildSuccess(asEnrollmentGuardianService.pageAvailableCourses(query));
    }

    @GetMapping("/course-detail/{courseClassId}")
    @ApiOperation("查询课程详情")
    public JsonResult<AsGuardianCourseDetailVO> courseDetail(@PathVariable Long courseClassId,
                                                            @RequestParam Long studentId) {
        return asEnrollmentGuardianService.getGuardianCourseDetail(courseClassId, studentId);
    }

    @GetMapping("/selection-detail/{id}")
    @ApiOperation("查询报名详情(新版)")
    public JsonResult<AsEnrollmentSelectionDetailVO> selectionDetail(@PathVariable Long id) {
        return JsonResult.buildSuccess(asEnrollmentGuardianService.getGuardianEnrollmentDetail(id));
    }

    @PostMapping("/apply")
    @ApiOperation("学生选课-提交选课报名")
    public JsonResult<AsEnrollmentOperateResultVO> applyEnrollmentSubmit(@Valid @RequestBody AsEnrollmentApplyAO ao) {
        return asEnrollmentGuardianService.applyEnrollment(ao);
    }

    @PostMapping("/cancel/{id}")
    @ApiOperation("学生选课-取消报名")
    public JsonResult<String> cancelEnrollment(@PathVariable Long id) {
        return asEnrollmentGuardianService.cancelGuardianEnrollment(id);
    }

    @GetMapping("/list")
    @ApiOperation("学生选课-查询我的所有报名记录")
    public JsonResult<List<AsEnrollmentSelectionRowVO>> listEnrollment() {
        return JsonResult.buildSuccess(asEnrollmentGuardianService.listGuardianEnrollments());
    }


}
