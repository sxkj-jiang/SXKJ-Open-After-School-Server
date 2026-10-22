package com.sxkj.edu.controller.admin;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.module.afterschool.domain.ao.EduclCalendarEventHolidayAo;
import com.sxkj.edu.module.afterschool.domain.dto.EduclTermAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.EduclTermQueryAO;
import com.sxkj.edu.module.afterschool.domain.vo.EduclCalendarEventVO;
import com.sxkj.edu.module.afterschool.domain.vo.EduclTermVO;
import com.sxkj.edu.module.afterschool.domain.vo.TermWeekMonthDropdownVO;
import com.sxkj.edu.module.afterschool.service.EduclTermService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 学期管理 Controller - 管理端
 *
 * @author system
 * @date 2026-05-19
 */
@RestController
@RequestMapping("/admin/afterschool/term")
@Api(tags = {SwaggerTagConst.KHFW.XQGL})
@Slf4j
@OperateLog(type = AmqpConstant.OPERATION, value = "学期管理", businessType = "学期管理")
public class AdminEduclTermController {

    @Resource
    private EduclTermService educlTermService;

    @PostMapping("/pageQuery")
    @ApiOperation("学期管理-分页查询")
    @OperateLog(type = AmqpConstant.OPERATION, value = "学期管理-分页查询", businessType = "学期管理")
    public JsonResult<PageResultDTO<EduclTermVO>> queryEduclTermPage(@RequestBody EduclTermQueryAO qo) {
        return JsonResult.buildSuccess(educlTermService.queryEduclTermPage(qo));
    }

    @PostMapping("/add")
    @ApiOperation("学期管理-新增")
    @OperateLog(type = AmqpConstant.OPERATION, value = "学期管理-新增", businessType = "学期管理")
    public JsonResult<String> addEduclTerm(@Valid @RequestBody EduclTermAddAO ao) {
        return educlTermService.addEduclTerm(ao);
    }

    @PostMapping("/update")
    @ApiOperation("学期管理-编辑")
    @OperateLog(type = AmqpConstant.OPERATION, value = "学期管理-编辑", businessType = "学期管理")
    public JsonResult<String> updateEduclTerm(@Valid @RequestBody EduclTermAddAO ao) {
        return educlTermService.updateEduclTerm(ao);
    }

    @PostMapping("/detail/{id}")
    @ApiOperation("学期管理-查询详情")
    @OperateLog(type = AmqpConstant.OPERATION, value = "学期管理-查询详情", businessType = "学期管理")
    public JsonResult<EduclTermVO> getEduclTermDetail(@PathVariable Long id) {
        return educlTermService.getEduclTermDetail(id);
    }

    @PostMapping("/delete/{id}")
    @ApiOperation("学期管理-删除(逻辑删除)")
    @OperateLog(type = AmqpConstant.OPERATION, value = "学期管理-删除", businessType = "学期管理")
    public JsonResult<String> deleteEduclTerm(@PathVariable Long id) {
        return educlTermService.deleteEduclTerm(id);
    }

//    @PostMapping("/import")
//    @ApiOperation("学期管理-导入")
//    @OperateLog(type = AmqpConstant.OPERATION, value = "学期管理-导入", businessType = "学期管理")
//    public JsonResult<String> importEduclTerm(@RequestParam Long schoolId,
//                                         @RequestParam("file") MultipartFile file) {
//        return educlTermService.importEduclTerm(schoolId, file);
//    }

    @PostMapping("/export")
    @ApiOperation("学期管理-导出")
    @OperateLog(type = AmqpConstant.OPERATION, value = "学期管理-导出", businessType = "学期管理")
    public void exportEduclTerm(@RequestBody EduclTermQueryAO qo, HttpServletResponse response) {
        educlTermService.exportEduclTerm(qo, response);
    }

    @PostMapping("/holiday-events")
    @ApiOperation("学期管理-查询节假日事件")
    @OperateLog(type = AmqpConstant.OPERATION, value = "学期管理-查询节假日事件", businessType = "学期管理")
    public JsonResult<List<EduclCalendarEventVO>> queryHolidayEvents(@RequestBody @Valid EduclCalendarEventHolidayAo holidayAo) {
        return JsonResult.buildSuccess(educlTermService.queryHolidayEvents(holidayAo.getStartDate(), holidayAo.getEndDate()));
    }

    @PostMapping("/weekMonthDropdown")
    @ApiOperation("学期管理-当前学期周数/月份下拉")
    @OperateLog(type = AmqpConstant.OPERATION, value = "学期管理-当前学期周数/月份下拉", businessType = "学期管理")
    public JsonResult<List<TermWeekMonthDropdownVO>> getCurrentTermWeekMonthDropdown() {
        return JsonResult.buildSuccess(educlTermService.getCurrentTermWeekMonthDropdown());
    }
}
