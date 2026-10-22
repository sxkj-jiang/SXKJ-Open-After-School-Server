package com.sxkj.edu.controller.admin;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.utils.CheckParamUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.afterschool.domain.ao.AsCourseClassDropdownQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.*;
import com.sxkj.edu.module.afterschool.domain.vo.*;
import com.sxkj.edu.module.afterschool.service.AsCourseClassService;
import com.sxkj.edu.module.afterschool.service.AsServicePublishAsyncService;
import com.sxkj.edu.module.afterschool.service.AsServiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 课后服务活动管理Controller - 管理端
 *
 * @author xyb
 * @date 2026-05-17
 */
@RestController
@RequestMapping("/admin/afterschool/service")
@Api(tags = {SwaggerTagConst.KHFW.JSPK})
@OperateLog
@Slf4j
public class AdminAsServiceController {

    @Autowired
    private AsServiceService asServiceService;


    @Autowired
    private AsServicePublishAsyncService asServicePublishAsyncService;

    @Autowired
    private AsCourseClassService asCourseClassService;

    @PostMapping("/page")
    @ApiOperation(value = "分页查询课后服务列表",notes = "xyb")
    public JsonResult<PageResultDTO<AsServiceVO>> pageAsService(@RequestBody(required = false) AsServicePageQueryAO query) {
        return JsonResult.buildSuccess(asServiceService.pageServices(query));
    }

    @PostMapping("/status-count")
    @ApiOperation(value = "统计课后服务各Tab状态数量", notes = "xyb")
    public JsonResult<AsServiceStatusCountVO> countServiceStatuses(@RequestBody(required = false) AsServicePageQueryAO query) {
        return JsonResult.buildSuccess(asServiceService.countServiceStatuses(query));
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出课后服务列表", notes = "yh")
    public JsonResult<AsServiceExportResultVO> exportServices(@RequestBody(required = false) AsServicePageQueryAO query, HttpServletResponse response) {
        return JsonResult.buildSuccess(asServiceService.exportServices(query,response));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询课后服务详情",notes = "xyb")
    public JsonResult<AsServiceDetailVO> getAsServiceById(@PathVariable Long id) {
        return JsonResult.buildSuccess(asServiceService.getServiceDetail(id));
    }

    @PostMapping("/save-basic")
    @ApiOperation(value = "保存课后服务基本信息",notes = "xyb")
    public JsonResult<AsServiceDetailVO> saveAsServiceBasic(@Valid @RequestBody AsServiceBasicSaveAO ao) {
        return asServiceService.saveBasic(ao);
    }

    @PostMapping("/save-courses")
    @ApiOperation(value = "保存课后服务课程和排课规则",notes = "xyb")
    public JsonResult<AsServiceDetailVO> saveAsServiceCourses(@Valid @RequestBody AsServiceCourseSaveAO ao) {
        return asServiceService.saveCourses(ao);
    }

    @PostMapping("/preview-sessions")
    @ApiOperation(value = "预览批量生成课次",notes = "xyb")
    public JsonResult<AsServiceSessionPreviewResultVO> previewSessions(@Valid @RequestBody AsServiceSessionPreviewAO ao) {
        return asServiceService.previewSessions(ao);
    }

    @PostMapping("/publish/{id}")
    @ApiOperation(value = "发布课后服务",notes = "xyb")
    public JsonResult<AsServicePublishResultVO> publishAsService(@PathVariable Long id,
                                                        @RequestBody(required = false) AsServicePublishAO ao) {
        CheckParamUtil.checkLongParam(id, "服务ID不能为空");
        CheckParamUtil.checkObjectParam(ao, "发布对象不能为空");
        return asServicePublishAsyncService.start(id, ao, SmartRequestTokenUtil.getRequestUserId());
    }

    @PostMapping("/withdraw/{id}")
    @ApiOperation(value = "撤回课后服务",notes = "xyb")
    public JsonResult<String> withdrawAsService(@PathVariable Long id) {
        return asServiceService.withdrawService(id);
    }

    @PostMapping("/finish/{id}")
    @ApiOperation(value = "结束课后服务",notes = "xyb")
    public JsonResult<String> finishAsService(@PathVariable Long id) {
        return asServiceService.finishService(id);
    }

    @PostMapping("/courseClassDropdown")
    @ApiOperation(value = "课后服务课程班下拉筛选", notes = "yh")
    public JsonResult<List<AsCourseClassDropdownVO>> courseClassDropdown(@RequestBody AsCourseClassDropdownQueryAO ao) {
        return JsonResult.buildSuccess(asCourseClassService.listDropdownByServiceDateRange(ao));
    }

}
