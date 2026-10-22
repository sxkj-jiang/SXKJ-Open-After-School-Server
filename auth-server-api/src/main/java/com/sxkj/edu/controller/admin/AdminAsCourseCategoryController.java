package com.sxkj.edu.controller.admin;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.module.afterschool.domain.dto.AsCourseCategoryAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsCourseCategoryEnOrDisAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsCourseCategoryListQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsCourseCategoryQueryAO;
import com.sxkj.edu.module.afterschool.domain.vo.AsCourseCategoryVO;
import com.sxkj.edu.module.afterschool.service.AsCourseCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 课后类别管理 Controller - 管理端
 *
 * @author system
 * @date 2026-05-19
 */
@RestController
@RequestMapping("/admin/afterschool/courseCategory")
@Api(tags = {SwaggerTagConst.KHFW.KHLB})
@Slf4j
@OperateLog(type = AmqpConstant.OPERATION, value = "课后类别管理", businessType = "课后类别管理")
public class AdminAsCourseCategoryController {

    @Autowired
    private AsCourseCategoryService asCourseCategoryService;

    @PostMapping("/pageQuery")
    @ApiOperation("课后类别管理-分页查询")
    @OperateLog(type = AmqpConstant.OPERATION, value = "课后类别管理-分页查询", businessType = "课后类别管理")
    public JsonResult<PageResultDTO<AsCourseCategoryVO>> queryAsCourseCategoryPage(@RequestBody AsCourseCategoryQueryAO qo) {
        return JsonResult.buildSuccess(asCourseCategoryService.queryAsCourseCategoryPage(qo));
    }

    @PostMapping("/add")
    @ApiOperation("课后类别管理-新增")
    @OperateLog(type = AmqpConstant.OPERATION, value = "课后类别管理-新增", businessType = "课后类别管理")
    public JsonResult<String> addAsCourseCategory(@Valid @RequestBody AsCourseCategoryAddAO ao) {
        return asCourseCategoryService.addAsCourseCategory(ao);
    }

    @PostMapping("/update")
    @ApiOperation("课后类别管理-编辑")
    @OperateLog(type = AmqpConstant.OPERATION, value = "课后类别管理-编辑", businessType = "课后类别管理")
    public JsonResult<String> updateAsCourseCategory(@Valid @RequestBody AsCourseCategoryAddAO ao) {
        return asCourseCategoryService.updateAsCourseCategory(ao);
    }


    @PostMapping("/import")
    @ApiOperation("课后类别管理-导入")
    @OperateLog(type = AmqpConstant.OPERATION, value = "课后类别管理-导入", businessType = "课后类别管理")
    public JsonResult<String> importAsCourseCategory(@RequestParam Long schoolId,
                                          @RequestParam("file") MultipartFile file) {
        return asCourseCategoryService.importAsCourseCategory(schoolId, file);
    }

    @PostMapping("/export")
    @ApiOperation("课后类别管理-导出")
    @OperateLog(type = AmqpConstant.OPERATION, value = "课后类别管理-导出", businessType = "课后类别管理")
    public void exportAsCourseCategory(@RequestBody AsCourseCategoryQueryAO qo, HttpServletResponse response) {
        asCourseCategoryService.exportAsCourseCategory(qo, response);
    }

    @PostMapping("/updateStatus")
    @ApiOperation("课后类别管理-启用/禁用")
    @OperateLog(type = AmqpConstant.OPERATION, value = "课后类别管理-启用/禁用", businessType = "课后类别管理")
    public JsonResult<String> updateEnabledStatus(@RequestBody @Valid AsCourseCategoryEnOrDisAO ao) {
        return asCourseCategoryService.updateEnabledStatus(ao.getId(), ao.getEnabledStatus());
    }

    @PostMapping("/detail/{id}")
    @ApiOperation("课后类别管理-查询详情")
    @OperateLog(type = AmqpConstant.OPERATION, value = "课后类别管理-查询详情", businessType = "课后类别管理")
    public JsonResult<AsCourseCategoryVO> getCourseCategoryDetail(@PathVariable Long id) {
        return asCourseCategoryService.getAsCourseCategoryDetail(id);
    }

    @PostMapping("/dropDown")
    @ApiOperation(value = "课后类别管理-下拉列表",notes = "xyb")
    @OperateLog(type = AmqpConstant.OPERATION, value = "课后类别管理-下拉列表", businessType = "课后类别管理")
    public JsonResult<List<AsCourseCategoryVO>> queryCourseCategorySelect(@RequestBody AsCourseCategoryListQueryAO qo) {
        return asCourseCategoryService.listDropDown(qo);
    }

    @PostMapping("/categoryTree")
    @ApiOperation(value = "课后类别管理-查询所有类别树",notes = "xyb")
    @OperateLog(type = AmqpConstant.OPERATION, value = "课后类别管理-查询所有类别树", businessType = "课后类别管理")
    public JsonResult<List<AsCourseCategoryVO>> categoryTree(@RequestBody AsCourseCategoryQueryAO qo) {
        return asCourseCategoryService.listCategoryTree(qo);
    }

}
