package com.sxkj.edu.controller.admin;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.module.afterschool.domain.dto.EduclGradeAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.EduclGradeQueryAO;
import com.sxkj.edu.module.afterschool.domain.vo.EduclGradeVO;
import com.sxkj.edu.module.afterschool.service.EduclGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 年级管理 Controller - 管理端
 *
 * @author system
 * @date 2026-05-19
 */
@RestController
@RequestMapping("/admin/afterschool/grade")
@Api(tags = {SwaggerTagConst.KHFW.GRADE_MANAGEMENT})
@Slf4j
@OperateLog(type = AmqpConstant.OPERATION, value = "年级管理", businessType = "年级管理")
public class AdminEduclGradeController {

    @Autowired
    private EduclGradeService educlGradeService;

    @PostMapping("/pageQuery")
    @ApiOperation(value = "年级管理-分页查询",notes = "yh")
    @OperateLog(type = AmqpConstant.OPERATION, value = "年级管理-分页查询", businessType = "年级管理")
    public JsonResult<PageResultDTO<EduclGradeVO>> queryEduclGradePage(@RequestBody EduclGradeQueryAO qo) {
        return JsonResult.buildSuccess(educlGradeService.queryEduclGradePage(qo));
    }

    @PostMapping("/detail/{id}")
    @ApiOperation(value = "年级管理-查询详情",notes = "yh")
    @OperateLog(type = AmqpConstant.OPERATION, value = "年级管理-查询详情", businessType = "年级管理")
    public JsonResult<EduclGradeVO> getEduclGradeDetail(@PathVariable Long id) {
        return educlGradeService.getEduclGradeDetail(id);
    }

    @PostMapping("/add")
    @ApiOperation(value = "年级管理-新增",notes = "yh")
    @OperateLog(type = AmqpConstant.OPERATION, value = "年级管理-新增", businessType = "年级管理")
    public JsonResult<String> addEduclGrade(@Valid @RequestBody EduclGradeAddAO ao) {
        return educlGradeService.addEduclGrade(ao);
    }

    @PostMapping("/update")
    @ApiOperation(value = "年级管理-编辑",notes = "yh")
    @OperateLog(type = AmqpConstant.OPERATION, value = "年级管理-编辑", businessType = "年级管理")
    public JsonResult<String> updateEduclGrade(@Valid @RequestBody EduclGradeAddAO ao) {
        return educlGradeService.updateEduclGrade(ao);
    }

    @PostMapping("/delete/{id}")
    @ApiOperation(value = "年级管理-删除(逻辑删除)",notes = "yh")
    @OperateLog(type = AmqpConstant.OPERATION, value = "年级管理-删除", businessType = "年级管理")
    public JsonResult<String> deleteEduclGrade(@PathVariable Long id) {
        return educlGradeService.deleteEduclGrade(id);
    }

    @PostMapping("/import")
    @ApiOperation(value = "年级管理-导入",notes = "yh")
    @OperateLog(type = AmqpConstant.OPERATION, value = "年级管理-导入", businessType = "年级管理")
    public JsonResult<String> importEduclGrade(
                                          @RequestParam("file") MultipartFile file) {
        return educlGradeService.importEduclGrade(file);
    }

    @PostMapping("/export")
    @ApiOperation(value = "年级管理-导出",notes = "yh")
    @OperateLog(type = AmqpConstant.OPERATION, value = "年级管理-导出", businessType = "年级管理")
    public void exportEduclGrade(@RequestBody EduclGradeQueryAO qo, HttpServletResponse response) {
        educlGradeService.exportEduclGrade(qo, response);
    }

    @PostMapping("/queryList")
    @ApiOperation(value = "年级管理-条件列表查询", notes = "yh")
    @OperateLog(type = AmqpConstant.OPERATION, value = "年级管理-列表查询", businessType = "年级管理")
    public JsonResult<List<EduclGradeVO>> queryGradeList(@RequestBody EduclGradeQueryAO qo) {
        return JsonResult.buildSuccess(educlGradeService.queryGradeList(qo));
    }
}
