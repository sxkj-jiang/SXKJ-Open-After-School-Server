package com.sxkj.edu.controller.admin;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.module.afterschool.domain.dto.EduclClassAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.EduclClassQueryAO;
import com.sxkj.edu.module.afterschool.domain.vo.EduclClassVO;
import com.sxkj.edu.module.afterschool.service.EduclClassService;
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
 * 班级管理 Controller - 管理端
 *
 * @author system
 * @date 2026-05-19
 */
@RestController
@RequestMapping("/admin/afterschool/class")
@Api(tags = {SwaggerTagConst.MULT.BASE_CLASS_MANAGEMENT})
@Slf4j
@OperateLog(type = AmqpConstant.OPERATION, value = "自然班级管理", businessType = "自然班级管理")
public class AdminEduclClassController {

    @Autowired
    private EduclClassService educlClassService;

    @PostMapping("/pageQuery")
    @ApiOperation("班级管理-分页查询")
    @OperateLog(type = AmqpConstant.OPERATION, value = "班级管理-分页查询", businessType = "班级管理")
    public JsonResult<PageResultDTO<EduclClassVO>> queryEduclClassPage(@RequestBody EduclClassQueryAO qo) {
        return JsonResult.buildSuccess(educlClassService.queryEduclClassPage(qo));
    }

    @PostMapping("/add")
    @ApiOperation("班级管理-新增")
    @OperateLog(type = AmqpConstant.OPERATION, value = "班级管理-新增", businessType = "班级管理")
    public JsonResult<String> addEduclClass(@Valid @RequestBody EduclClassAddAO ao) {
        return educlClassService.addEduclClass(ao);
    }

    @PostMapping("/update")
    @ApiOperation("班级管理-编辑")
    @OperateLog(type = AmqpConstant.OPERATION, value = "班级管理-编辑", businessType = "班级管理")
    public JsonResult<String> updateEduclClass(@Valid @RequestBody EduclClassAddAO ao) {
        return educlClassService.updateEduclClass(ao);
    }

    @PostMapping("/delete/{id}")
    @ApiOperation("班级管理-删除(逻辑删除)")
    @OperateLog(type = AmqpConstant.OPERATION, value = "班级管理-删除", businessType = "班级管理")
    public JsonResult<String> deleteEduclClass(@PathVariable Long id) {
        return educlClassService.deleteEduclClass(id);
    }

    @PostMapping("/detail/{id}")
    @ApiOperation("班级管理-根据ID查询详情")
    @OperateLog(type = AmqpConstant.OPERATION, value = "班级管理-查询详情", businessType = "班级管理")
    public JsonResult<EduclClassVO> getEduclClassDetail(@PathVariable Long id) {
        return educlClassService.getEduclClassDetail(id);
    }

    @PostMapping("/import")
    @ApiOperation("班级管理-导入")
    @OperateLog(type = AmqpConstant.OPERATION, value = "班级管理-导入", businessType = "班级管理")
    public JsonResult<String> importEduclClass(@RequestParam("file") MultipartFile file) {
        return educlClassService.importEduclClass(file);
    }

    @PostMapping("/export")
    @ApiOperation("班级管理-导出")
    @OperateLog(type = AmqpConstant.OPERATION, value = "班级管理-导出", businessType = "班级管理")
    public void exportEduclClass(@RequestBody EduclClassQueryAO qo, HttpServletResponse response) {
        educlClassService.exportEduclClass(qo, response);
    }
}
