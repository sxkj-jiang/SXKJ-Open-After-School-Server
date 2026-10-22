package com.sxkj.edu.controller.admin;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.utils.CheckParamUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.afterschool.domain.dto.AsSubjectAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsSubjectEnOrDisAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsSubjectQueryAO;
import com.sxkj.edu.module.afterschool.domain.vo.AsSubjectGradeTreeVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsSubjectVO;
import com.sxkj.edu.service.AsSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 学科管理Controller - 管理端
 *
 * @author yh
 * @date 2026-08-26
 */
@RestController
@RequestMapping("/admin/afterschool/subject")
@Api(tags = {SwaggerTagConst.MULT.SUBJECT_MANAGER})
@OperateLog(type = AmqpConstant.OPERATION, value = "学科管理", businessType = "学科管理")
@Slf4j
public class AdminAsSubjectController {

    @Resource
    private AsSubjectService asSubjectService;

    @PostMapping("/addOrUpdate")
    @ApiOperation("学科管理-新增/编辑")
    @OperateLog(type = AmqpConstant.OPERATION, value = "学科管理-新增/编辑", businessType = "学科管理")
    public JsonResult<String> addOrUpdateSubject(@Valid @RequestBody AsSubjectAddAO ao) {
        return asSubjectService.addOrUpdateSubject(ao);
    }

    @PostMapping("/updateStatus")
    @ApiOperation("学科管理-启用/禁用")
    @OperateLog(type = AmqpConstant.OPERATION, value = "学科管理-启用/禁用", businessType = "学科管理")
    public JsonResult<String> updateEnabledStatus(@Valid @RequestBody AsSubjectEnOrDisAO ao) {
        return asSubjectService.updateEnabledStatus(ao.getId(), ao.getEnabled());
    }

    @PostMapping("/pageQuery")
    @ApiOperation("学科管理-分页查询")
    @OperateLog(type = AmqpConstant.OPERATION, value = "学科管理-分页查询", businessType = "学科管理")
    public JsonResult<PageResultDTO<AsSubjectVO>> querySubjectPage(@RequestBody AsSubjectQueryAO qo) {
        return JsonResult.buildSuccess(asSubjectService.querySubjectPage(qo));
    }

    @GetMapping("/tree")
    @ApiOperation("学科管理-查询启用学科(下拉)")
    @OperateLog(type = AmqpConstant.OPERATION, value = "学科管理-下拉查询", businessType = "学科管理")
    public JsonResult<List<AsSubjectGradeTreeVO>> listSubjectTree(@RequestParam Long schoolId) {
        CheckParamUtil.checkLongParam(schoolId, "请选择学校");
        return JsonResult.buildSuccess(asSubjectService.listSubjectTreeBySchoolId(schoolId));
    }

    @GetMapping("/detail/{id}")
    @ApiOperation(value = "学科管理-详情查询", notes = "yh")
    @OperateLog(type = AmqpConstant.OPERATION, value = "学科管理-详情查询", businessType = "学科管理")
    public JsonResult<AsSubjectVO> getDetailById(@PathVariable Long id) {
        CheckParamUtil.checkLongParam(id, "学科ID不能为空");
        return asSubjectService.getDetailById(id);
    }

    @PostMapping("/delete/{id}")
    @ApiOperation("学科管理-删除")
    @OperateLog(type = AmqpConstant.OPERATION, value = "学科管理-删除", businessType = "学科管理")
    public JsonResult<String> deleteSubject(@PathVariable Long id) {
        CheckParamUtil.checkLongParam(id, "学科ID不能为空");
        return asSubjectService.deleteSubject(id);
    }

    @PostMapping("/import")
    @ApiOperation("学科管理-批量导入")
    @OperateLog(type = AmqpConstant.OPERATION, value = "学科管理-批量导入", businessType = "学科管理")
    public JsonResult<String> importSubject(@RequestParam("file") MultipartFile file) {
        Long schoolId = SmartRequestTokenUtil.getRequestUserStuSchoolId(); // 获取当前用户学校ID
        if(schoolId == null) return JsonResult.buildError("请选择学校");
        return asSubjectService.importSubject(schoolId, file);
    }
}
