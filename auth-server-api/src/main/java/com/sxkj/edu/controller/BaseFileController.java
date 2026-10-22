package com.sxkj.edu.controller;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.PageVO;
import com.sxkj.edu.common.utils.ExcelUtils;
import com.sxkj.edu.domain.base.*;
import com.sxkj.edu.service.BaseFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 *@Description : 文件管理
 *@Author : JIANGJJ
 *@Date : 2024-6-13 17:32
 *@Company : SXKJ
 *@Version : 1.0
 **/
@RestController
@RequestMapping("/baseFile")
@AllArgsConstructor
@Slf4j
@Api(tags = {SwaggerTagConst.Admin.FILE})
@OperateLog
public class BaseFileController {

    private final BaseFileService baseFileService;

    @ApiOperation(value = "文件管理-分页查询")
    @PostMapping(value = "/page")
    @OperateLog(type = AmqpConstant.OPERATION, value = "文件管理-分页查询" ,businessType = "文件管理")
    public JsonResult<PageVO<BaseFilePageVo>> page(@RequestBody BaseFilePageAo pageAo) {
        return JsonResult.buildSuccess(baseFileService.queryPage(pageAo));
    }

    @ApiOperation(value = "文件管理-新增/编辑")
    @PostMapping(value = "/inOrUp")
    @OperateLog(type = AmqpConstant.OPERATION, value = "文件管理-新增/编辑" ,businessType = "文件管理")
    public JsonResult<String> inOrUpFileInfo( @Valid  @RequestBody BaseFileUpdateAo ao,@RequestBody MultipartFile file) throws Exception{
        return baseFileService.inOrUpFileInfo(ao,file);
    }

    @ApiOperation(value = "文件管理-回调更新")
    @PostMapping(value = "/callback/saveFile")
    @OperateLog(type = AmqpConstant.OPERATION, value = "文件管理-回调更新" ,businessType = "文件管理")
    public JsonResult<BaseFileCosQueryVo> inOrUpFileInfoForCos(@Valid @RequestBody BaseFileUpdateCosAo ao) throws Exception{
        return baseFileService.inOrUpFileInfoForCos(ao);
    }

    @ApiOperation(value = "文件管理-批量选择导出")
    @PostMapping(value = "/export")
    @OperateLog(type = AmqpConstant.OPERATION, value = "文件管理-批量选择导出" ,businessType = "文件管理")
    public void exportFileInfo(HttpServletResponse response, @RequestBody List<Long> fileIds) {
        List<BaseFileExportVo> queryExportVos = baseFileService.exportFileInfo(fileIds);
        ExcelUtils.exportExcel(response, queryExportVos, BaseFileExportVo.class, "文件管理查询导出", "文件管理查询导出");
    }

    @ApiOperation(value = "文件管理-删除")
    @PostMapping("/delete")
    @OperateLog(type = AmqpConstant.OPERATION, value = "文件管理-删除" ,businessType = "文件管理")
    public JsonResult<String> delete(@RequestParam("id") Long id) {
        return baseFileService.deleteFile(id);
    }

}
