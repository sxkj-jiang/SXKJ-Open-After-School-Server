package com.sxkj.edu.controller.khfw;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.module.afterschool.domain.ao.AsStudentScheduleQueryAO;
import com.sxkj.edu.module.afterschool.domain.ao.AsTeacherScheduleQueryAO;
import com.sxkj.edu.module.afterschool.domain.ao.CourseScheduleDetailListQueryAO;
import com.sxkj.edu.module.afterschool.domain.ao.CourseSchedulePageQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsScheduleTemplateEnOrDisAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsScheduleTemplateListQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsScheduleTemplateQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.KhfwAsScheduleTemplateAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.ZxglAsScheduleTemplateAddAO;
import com.sxkj.edu.module.afterschool.domain.vo.*;
import com.sxkj.edu.module.afterschool.service.AsCourseSessionService;
import com.sxkj.edu.module.afterschool.service.AsScheduleTemplateService;
import com.sxkj.edu.module.afterschool.service.AsServiceService;
import com.sxkj.edu.module.afterschool.service.AsTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/scheduleTemplate")
@Api(tags = {SwaggerTagConst.KHFW.ZXKHGL})
@OperateLog
@Slf4j
public class AsScheduleTemplateController {

    @Autowired
    private AsScheduleTemplateService asScheduleTemplateService;
    @Autowired
    private AsServiceService asServiceService;
    @Autowired
    private AsCourseSessionService asCourseSessionService;

    /**
     * 课表管理-分页查询
     */
    @PostMapping("kbgl/pageQuery")
    @ApiOperation("课表管理-分页查询")
    @OperateLog(type = AmqpConstant.OPERATION, value = "课表管理-分页查询", businessType = "课表管理")
    public JsonResult<PageResultDTO<CourseSchedulePageVO>> queryCourseSchedulePage(@RequestBody CourseSchedulePageQueryAO qo) {
        return JsonResult.buildSuccess(asServiceService.queryCourseSchedulePage(qo));
    }

    /**
     * 课表管理-查询详情
     */
    @PostMapping("/kbgl/detail")
    @ApiOperation("课表管理-查询详情")
    @OperateLog(type = AmqpConstant.OPERATION, value = "课表管理-查询详情(列表)", businessType = "课表管理")
    public JsonResult<ServiceCourseScheduleVO> getCourseScheduleDetail(@RequestBody CourseScheduleDetailListQueryAO ao) {
        return asServiceService.getCourseScheduleDetailList(ao);
    }

    /**
     * 作息管理-分页查询
     */
    @PostMapping("pageQuery")
    @ApiOperation("作息管理/课后时间管理-分页查询")
    @OperateLog(type = AmqpConstant.OPERATION, value = "作息管理/课后时间管理-分页查询", businessType = "作息管理/课后时间管理")
    public JsonResult<PageResultDTO<AsScheduleTemplateVO>> queryScheduleTemplatePage(@RequestBody AsScheduleTemplateQueryAO qo) {
        return JsonResult.buildSuccess(asScheduleTemplateService.queryScheduleTemplatePage(qo));
    }

    /**
     * 作息管理-新增
     */
    @PostMapping("/zxgl/add")
    @ApiOperation("作息管理-新增")
    @OperateLog(type = AmqpConstant.OPERATION, value = "作息管理-新增", businessType = "作息管理")
    public JsonResult<String> addZxglScheduleTemplate(@Valid @RequestBody ZxglAsScheduleTemplateAddAO ao) {
        return asScheduleTemplateService.addZxglScheduleTemplate(ao);
    }

    /**
     * 作息管理-编辑
     */
    @PostMapping("/zxgl/update")
    @ApiOperation("作息管理-编辑")
    @OperateLog(type = AmqpConstant.OPERATION, value = "作息管理-编辑", businessType = "作息管理")
    public JsonResult<String> updateZxglScheduleTemplate(@Valid @RequestBody ZxglAsScheduleTemplateAddAO ao) {
        return asScheduleTemplateService.updateZxglScheduleTemplate(ao);
    }


    /**
     * 作息管理-查询详情
     */
    @PostMapping("/zxgl/detail/{id}")
    @ApiOperation("作息管理-查询详情")
    @OperateLog(type = AmqpConstant.OPERATION, value = "作息管理-查询详情", businessType = "作息管理")
    public JsonResult<ZxglAsScheduleTemplateDetailVO> getZxglScheduleTemplateDetail(@PathVariable Long id) {
        return asScheduleTemplateService.getZxglScheduleTemplateDetail(id);
    }

    /**
     * 作息管理-复制
     */
    @PostMapping("/zxgl/copy/{id}")
    @ApiOperation("作息管理-复制")
    @OperateLog(type = AmqpConstant.OPERATION, value = "作息管理-复制", businessType = "作息管理")
    public JsonResult<String> copyZxglScheduleTemplate(@PathVariable Long id) {
        return asScheduleTemplateService.copyZxglScheduleTemplate(id);
    }

    /**
     * 作息管理-查询详情
     */
    @PostMapping("/zxgl/detailEnable/{schoolId}")
    @ApiOperation("作息管理-查询启用作息")
    @OperateLog(type = AmqpConstant.OPERATION, value = "作息管理-查询启用作息", businessType = "作息管理")
    public JsonResult<ZxglAsScheduleTemplateDetailVO> getZxglScheduleTemplateEnableDetail(@PathVariable Long schoolId) {
        return asScheduleTemplateService.getZxglScheduleTemplateEnableDetail(schoolId);
    }

    /**
     * 作息管理/课后时间管理-启用/禁用
     */
    @PostMapping("/updateStatus")
    @ApiOperation("作息管理/课后时间管理-启用/禁用")
    @OperateLog(type = AmqpConstant.OPERATION, value = "作息管理/课后时间管理-启用/禁用", businessType = "作息管理/课后时间管理")
    public JsonResult<String> updateZxglEnabledStatus(@Valid @RequestBody AsScheduleTemplateEnOrDisAO ao) {
        return asScheduleTemplateService.updateEnabledStatus(ao.getId(), ao.getEnabledStatus());
    }

    /**
     * 方案下拉列表查询
     */
    @PostMapping("/listQuery")
    @ApiOperation("作息管理/课后时间管理-方案下拉列表查询")
    @OperateLog(type = AmqpConstant.OPERATION, value = "方案下拉列表查询", businessType = "作息管理/课后时间管理")
    public JsonResult<List<AsScheduleTemplateListVO>> queryScheduleTemplateList(@RequestBody AsScheduleTemplateListQueryAO qo) {
        return JsonResult.buildSuccess(asScheduleTemplateService.queryScheduleTemplateList(qo));
    }

    // =============================== 课后时间管理 ===============================

    /**
     * 课后时间管理-新增
     */
    @PostMapping("/khsj/add")
    @ApiOperation("课后时间管理-新增")
    @OperateLog(type = AmqpConstant.OPERATION, value = "课后时间管理-新增", businessType = "课后时间管理")
    public JsonResult<String> addKhfwScheduleTemplate(@Valid @RequestBody KhfwAsScheduleTemplateAddAO ao) {
        return asScheduleTemplateService.addKhfwScheduleTemplate(ao);
    }

    /**
     * 课后时间管理-编辑
     */
    @PostMapping("/khsj/update")
    @ApiOperation("课后时间管理-编辑")
    @OperateLog(type = AmqpConstant.OPERATION, value = "课后时间管理-编辑", businessType = "课后时间管理")
    public JsonResult<String> updateKhfwScheduleTemplate(@Valid @RequestBody KhfwAsScheduleTemplateAddAO ao) {
        return asScheduleTemplateService.updateKhfwScheduleTemplate(ao);
    }

    /**
     * 课后时间管理-查询详情
     */
    @PostMapping("/khsj/detail/{id}")
    @ApiOperation("课后时间管理-查询详情")
    @OperateLog(type = AmqpConstant.OPERATION, value = "课后时间管理-查询详情", businessType = "课后时间管理")
    public JsonResult<KhfwAsScheduleTemplateDetailVO> getKhfwScheduleTemplateDetail(@PathVariable Long id) {
        return asScheduleTemplateService.getKhfwScheduleTemplateDetail(id);
    }

    /**
     * 课后时间管理-批量导入
     */
    @PostMapping("/khsj/import")
    @ApiOperation("课后时间管理-批量导入")
    @OperateLog(type = AmqpConstant.OPERATION, value = "课后时间管理-批量导入", businessType = "课后时间管理")
    public JsonResult<String> importKhfwScheduleTemplate(@RequestParam("file") MultipartFile file) {
        return asScheduleTemplateService.importKhfwScheduleTemplate(file);
    }

    // =============================== 学生课表查询 ===============================

    /**
     * 查询学生课表（按周/月/天）
     */
    @PostMapping("/student/schedule")
    @ApiOperation(value = "查询学生课表（按周/月/天）",notes = "jiangjj")
    @OperateLog(type = AmqpConstant.OPERATION, value = "查询学生课表", businessType = "课表管理")
    public JsonResult<List<AsStudentScheduleWeekVO>> queryStudentSchedule(@RequestBody AsStudentScheduleQueryAO ao) {
        return JsonResult.buildSuccess(asCourseSessionService.queryStudentSchedule(ao));
    }

    // =============================== 教师课表查询 ===============================

    /**
     * 查询教师课表（按周/月/天）
     */
    @PostMapping("/teacher/schedule")
    @ApiOperation(value = "查询教师课表（按周/月/天）",notes = "jiangjj")
    @OperateLog(type = AmqpConstant.OPERATION, value = "查询教师课表", businessType = "课表管理")
    public JsonResult<List<AsStudentScheduleWeekVO>> queryTeacherSchedule(@RequestBody AsTeacherScheduleQueryAO ao) {
        return JsonResult.buildSuccess(asCourseSessionService.queryTeacherSchedule(ao));
    }
}

