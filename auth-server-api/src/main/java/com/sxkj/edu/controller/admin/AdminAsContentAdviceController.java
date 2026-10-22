package com.sxkj.edu.controller.admin;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.anno.RolePermission;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.utils.CheckParamUtil;
import com.sxkj.edu.module.afterschool.domain.dto.AsContentAdviceAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsContentAdviceAuditAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsContentAdviceQueryAO;
import com.sxkj.edu.module.afterschool.domain.vo.AsContentAdviceVO;
import com.sxkj.edu.module.afterschool.service.AsContentAdviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 意见反馈管理 Controller - 管理端
 *
 * @author system
 * @date 2026-06-20
 */
@RestController
@RequestMapping("/admin/afterschool/contentAdvice")
@Api(tags = {SwaggerTagConst.MULT.BUS_COMMON})
@Slf4j
@OperateLog(type = AmqpConstant.OPERATION, value = "意见反馈管理", businessType = "意见反馈管理")
public class AdminAsContentAdviceController {

    @Autowired
    private AsContentAdviceService asContentAdviceService;

    @PostMapping("/pageQuery")
    @ApiOperation("意见反馈管理-分页查询")
    @OperateLog(type = AmqpConstant.OPERATION, value = "意见反馈管理-分页查询", businessType = "意见反馈管理")
    public JsonResult<PageResultDTO<AsContentAdviceVO>> queryAsContentAdvicePage(@RequestBody AsContentAdviceQueryAO qo) {
        return JsonResult.buildSuccess(asContentAdviceService.queryAsContentAdvicePage(qo));
    }

    @PostMapping("/add")
    @ApiOperation("意见反馈管理-新增")
    @OperateLog(type = AmqpConstant.OPERATION, value = "意见反馈管理-新增", businessType = "意见反馈管理")
    public JsonResult<String> addAsContentAdvice(@Valid @RequestBody AsContentAdviceAddAO ao) {
        return asContentAdviceService.addAsContentAdvice(ao);
    }

//    @PostMapping("/update")
//    @ApiOperation("意见反馈管理-编辑")
//    @OperateLog(type = AmqpConstant.OPERATION, value = "意见反馈管理-编辑", businessType = "意见反馈管理")
//    public JsonResult<String> updateAsContentAdvice(@Valid @RequestBody AsContentAdviceAddAO ao) {
//        return asContentAdviceService.updateAsContentAdvice(ao);
//    }

    @PostMapping("/audit")
    @ApiOperation("意见反馈管理-审核")
    @OperateLog(type = AmqpConstant.OPERATION, value = "意见反馈管理-审核", businessType = "意见反馈管理")
    @RolePermission(roles = {1, 4})
    public JsonResult<String> auditAsContentAdvice(@Valid @RequestBody AsContentAdviceAuditAO ao) {
        return asContentAdviceService.auditAsContentAdvice(ao);
    }

    @PostMapping("/delete/{id}")
    @ApiOperation("意见反馈管理-删除(逻辑删除)")
    @OperateLog(type = AmqpConstant.OPERATION, value = "意见反馈管理-删除", businessType = "意见反馈管理")
    public JsonResult<String> deleteAsContentAdvice(@PathVariable Long id) {
        return asContentAdviceService.deleteAsContentAdvice(id);
    }

    @PostMapping("/export")
    @ApiOperation("意见反馈管理-导出")
    @OperateLog(type = AmqpConstant.OPERATION, value = "意见反馈管理-导出", businessType = "意见反馈管理")
    public void exportAsContentAdvice(@RequestBody AsContentAdviceQueryAO qo, HttpServletResponse response) {
        asContentAdviceService.exportAsContentAdvice(qo, response);
    }

    @PostMapping("/detail/{id}")
    @ApiOperation("意见反馈管理-查询详情")
    @OperateLog(type = AmqpConstant.OPERATION, value = "意见反馈管理-查询详情", businessType = "意见反馈管理")
    public JsonResult<AsContentAdviceVO> getContentAdviceDetail(@PathVariable Long id) {
        return asContentAdviceService.getAsContentAdviceDetail(id);
    }

}
