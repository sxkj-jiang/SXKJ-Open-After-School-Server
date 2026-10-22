package com.sxkj.edu.controller.mult;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.PageVO;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.domain.mult.ao.*;
import com.sxkj.edu.domain.mult.vo.BaseTemplatePageCodeVO;
import com.sxkj.edu.domain.mult.vo.BaseTemplateVO;
import com.sxkj.edu.domain.mult.vo.LabelVO;
import com.sxkj.edu.service.BaseTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/base/template")
@Api(tags = {SwaggerTagConst.MULT.TEMPLATE})
public class TemplateController {

    @Autowired
    private BaseTemplateService baseTemplateService;

    @GetMapping("/menuList")
    @ApiOperation("基础-模板应用菜单列表")
    @OperateLog(type = AmqpConstant.OPERATION, value = "基础-模板应用菜单列表", businessType = "模板管理")
    public JsonResult<List<LabelVO>> menuList() {
        return JsonResult.buildSuccess(baseTemplateService.menuList());
    }

    @PostMapping("/pageQuery")
    @ApiOperation("基础-模板分页查询")
    @OperateLog(type = AmqpConstant.OPERATION, value = "基础-模板分页查询", businessType = "模板管理")
    public JsonResult<PageVO<BaseTemplateVO>> pageQuery(@RequestBody BaseTemplateQueryAO ao) {
        return JsonResult.buildSuccess(baseTemplateService.pageQuery(ao));
    }

    @PostMapping("/queryByPageCode")
    @ApiOperation("模板管理-根据页面code查询模板列表")
    @OperateLog(type = AmqpConstant.OPERATION, value = "模板管理-根据页面code查询模板列表", businessType = "模板管理")
    public JsonResult<List<BaseTemplatePageCodeVO>> queryByPageCode(@Valid @RequestBody BaseTemplatePageCodeQueryAO ao) {
        return JsonResult.buildSuccess(baseTemplateService.queryByPageCode(ao));
    }


    @PostMapping("/queryByMenu")
    @ApiOperation(value = "模板管理-根据菜单查询",notes = "jiangjj")
    @OperateLog(type = AmqpConstant.OPERATION, value = "模板管理-根据菜单查询", businessType = "模板管理")
    public JsonResult<BaseTemplateVO> queryManagerTemplByMenu(@Valid @RequestBody BaseTemplateChildQueryAO ao) {
        return baseTemplateService.queryByMenu(ao);
    }

    @PostMapping("/create")
    @ApiOperation("基础-新增模板")
    @OperateLog(type = AmqpConstant.OPERATION, value = "基础-新增模板", businessType = "模板管理")
    public JsonResult<String> create(@RequestBody BaseTemplateCreateAO ao) {
        BaseTemplateUpdateAO updateAO = new BaseTemplateUpdateAO();
        SmartBeanUtil.copyProperties(ao,updateAO);
        return baseTemplateService.saveOrUpdateBaseTemplate(updateAO);
    }

    @PostMapping("/update")
    @ApiOperation("基础-编辑模板")
    @OperateLog(type = AmqpConstant.OPERATION, value = "基础-编辑模板", businessType = "模板管理")
    public JsonResult<String> update(@RequestBody BaseTemplateUpdateAO ao) {
        return baseTemplateService.saveOrUpdateBaseTemplate(ao);
    }

    @PostMapping("/delete")
    @ApiOperation("基础-模板删除")
    @OperateLog(type = AmqpConstant.OPERATION, value = "基础-模板删除", businessType = "模板管理")
    public JsonResult<Void> delete(@RequestParam @ApiParam(value = "校友ID", required = true) Long id) {
        baseTemplateService.removeById(id);
        return JsonResult.buildSuccess();
    }

    @PostMapping("/exportExcel")
    @ApiOperation("基础-模板批量导出")
    @OperateLog(type = AmqpConstant.OPERATION, value = "基础-模板批量导出", businessType = "模板管理")
    public void exportExcel(HttpServletResponse response, @RequestBody SchoolMateExportAO ao) {
        baseTemplateService.exportExcel(response, ao.getIds());
    }
}
