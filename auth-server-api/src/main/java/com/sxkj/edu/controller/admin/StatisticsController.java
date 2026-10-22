package com.sxkj.edu.controller.admin;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.PageVO;
import com.sxkj.edu.common.utils.ExcelUtils;
import com.sxkj.edu.domain.ao.PlateOperatePvStatisticsAo;
import com.sxkj.edu.domain.ao.PlateOperateStatisticsQueryAo;
import com.sxkj.edu.domain.ao.PlateVisitRecordStatisticsPageQueryAo;
import com.sxkj.edu.domain.vo.*;
import com.sxkj.edu.module.afterschool.domain.statistics.ao.*;
import com.sxkj.edu.module.afterschool.domain.statistics.vo.*;
import com.sxkj.edu.module.afterschool.service.StatisticsService;
import com.sxkj.edu.service.UserLoginLogService;
import com.sxkj.edu.service.UserOperateLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/statistics")
@Api(tags = {SwaggerTagConst.STATISTICS.STATISTICS_MANAGER})
@OperateLog
@Slf4j
public class StatisticsController {

    @Resource
    private StatisticsService statisticsService;



    @PostMapping("/lessonStatistics/teacher")
    @ApiOperation(value = "课时统计-教师课时分页", notes = "yh")
    @OperateLog(type = AmqpConstant.OPERATION, value = "课时统计-教师课时分页", businessType = "统计报表")
    public JsonResult<PageResultDTO<LessonStatisticsTeacherPageVO>> pageTeacherLessonStatistics(@Valid @RequestBody LessonStatisticsTeacherAO ao) {
        return JsonResult.buildSuccess(statisticsService.pageTeacherLessonStatistics(ao));
    }

    @PostMapping("/lessonStatistics/teacher/export")
    @ApiOperation(value = "课时统计-教师课时-批量导出", notes = "yh")
    @OperateLog(type = AmqpConstant.OPERATION, value = "课时统计-教师课时-批量导出", businessType = "统计报表")
    public void exportTeacherLessonStatistics(@Valid @RequestBody LessonStatisticsTeacherAO ao, HttpServletResponse response) {
        statisticsService.exportTeacherLessonStatistics(ao, response);
    }

    @PostMapping("/lessonStatistics/teacherDetail")
    @ApiOperation(value = "课时统计-教师课时详情", notes = "yh")
    @OperateLog(type = AmqpConstant.OPERATION, value = "课时统计-教师课时详情", businessType = "统计报表")
    public JsonResult<LessonStatisticsTeacherDetailVO> getTeacherLessonDetail(@Valid @RequestBody LessonStatisticsTeacherDetailAO ao) {
        return JsonResult.buildSuccess(statisticsService.getTeacherLessonDetail(ao));
    }

    @PostMapping("/lessonStatistics/teacherDetail/record")
    @ApiOperation(value = "课时统计-教师课时详情-详细课时记录分页", notes = "yh")
    @OperateLog(type = AmqpConstant.OPERATION, value = "课时统计-教师课时详情-详细课时记录分页", businessType = "统计报表")
    public JsonResult<PageResultDTO<TeacherLessonRecordPageVO>> pageTeacherLessonRecord(@Valid @RequestBody TeacherLessonRecordAO ao) {
        return JsonResult.buildSuccess(statisticsService.pageTeacherLessonRecord(ao));
    }

    @PostMapping("/lessonStatistics/student")
    @ApiOperation(value = "课时统计-学生课时分页", notes = "yh")
    @OperateLog(type = AmqpConstant.OPERATION, value = "课时统计-学生课时分页", businessType = "统计报表")
    public JsonResult<PageResultDTO<LessonStatisticsStudentPageVO>> pageStudentLessonStatistics(@Valid @RequestBody LessonStatisticsStudentAO ao) {
        return JsonResult.buildSuccess(statisticsService.pageStudentLessonStatistics(ao));
    }


    @PostMapping("/lessonStatistics/stuDetail")
    @ApiOperation(value = "课时统计-学生课时详情", notes = "yh")
    @OperateLog(type = AmqpConstant.OPERATION, value = "课时统计-学生课时详情", businessType = "统计报表")
    public JsonResult<LessonStatisticsStudentDetailVO> getStudentLessonDetail(@RequestBody LessonStatisticsStudentDetailAO ao) {
        return JsonResult.buildSuccess(statisticsService.getStudentLessonDetail(ao));
    }

    @PostMapping("/lessonStatistics/stuDetail/record")
    @ApiOperation(value = "课时统计-学生课时详情-详细课时记录分页", notes = "yh")
    @OperateLog(type = AmqpConstant.OPERATION, value = "课时统计-学生课时详情-详细课时记录分页", businessType = "统计报表")
    public JsonResult<PageResultDTO<StudentLessonRecordPageVO>> pageStudentLessonRecord(@Valid @RequestBody StudentLessonRecordAO ao) {
        return JsonResult.buildSuccess(statisticsService.pageStudentLessonRecord(ao));
    }


    @PostMapping("/teacherStatistics")
    @ApiOperation(value = "教师统计", notes = "yh")
    @OperateLog(type = AmqpConstant.OPERATION, value = "教师统计", businessType = "统计报表")
    public TeacherStatisticsVO teacherStatistics(@Valid @RequestBody TeacherStatisticsAO ao) {
        return statisticsService.teacherStatistics(ao);
    }


    @PostMapping("/schoolSummary")
    @ApiOperation(value = "工作台-学校汇总统计", notes = "jiangjj")
    @OperateLog(type = AmqpConstant.OPERATION, value = "工作台-学校汇总统计", businessType = "统计报表")
    public JsonResult<SchoolSummaryStatisticsVO> getSchoolSummaryStatistics(@RequestBody SchoolSummaryStatisticsAO ao) {
        return JsonResult.buildSuccess(statisticsService.getSchoolSummaryStatistics(ao));
    }

    @PostMapping("/studentStatistics")
    @ApiOperation(value = "学生画像及统计", notes = "yh")
    @OperateLog(type = AmqpConstant.OPERATION, value = "学生画像及统计", businessType = "统计报表")
    public StudentStatisticsVO studentStatistics(@Valid @RequestBody StudentStatisticsAO ao) {
        return statisticsService.studentStatistics(ao);
    }


    @PostMapping("/studentProfile/page")
    @ApiOperation(value = "学生画像-分页查询", notes = "yh")
    @OperateLog(type = AmqpConstant.OPERATION, value = "学生画像-分页查询", businessType = "学生画像")
    public JsonResult<PageResultDTO<StudentProfilePageVO>> pageStudentProfile(@Valid @RequestBody StudentProfilePageAO ao) {
        return JsonResult.buildSuccess(statisticsService.pageStudentProfile(ao));
    }

    @PostMapping("/studentProfile/detail")
    @ApiOperation(value = "学生画像-详情", notes = "yh")
    @OperateLog(type = AmqpConstant.OPERATION, value = "学生画像-详情", businessType = "学生画像")
    public JsonResult<StudentProfileDetailVO> getStudentProfileDetail(@Valid @RequestBody StudentProfileDetailAO ao) {
        return JsonResult.buildSuccess(statisticsService.getStudentProfileDetail(ao));
    }

}