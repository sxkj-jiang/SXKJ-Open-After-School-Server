package com.sxkj.edu.controller.admin;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.anno.RolePermission;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.afterschool.domain.dto.ExamRoomAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.ExamRoomQueryAO;
import com.sxkj.edu.module.afterschool.domain.vo.ExamRoomVO;
import com.sxkj.edu.module.afterschool.service.ExamRoomService;
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
 * 教室管理 Controller - 管理端
 *
 * @author system
 * @date 2026-05-25
 */
@RestController
@RequestMapping("/admin/afterschool/examRoom")
@Api(tags = {SwaggerTagConst.MULT.EXAM_ROOM_MANAGEMENT})
@Slf4j
@OperateLog(type = AmqpConstant.OPERATION, value = "教室管理", businessType = "教室管理")
public class AdminExamRoomController {

    @Autowired
    private ExamRoomService examRoomService;

    @PostMapping("/pageQuery")
    @ApiOperation(value = "教室管理-分页查询", notes = "jiangjj")
    @OperateLog(type = AmqpConstant.OPERATION, value = "教室管理-分页查询", businessType = "教室管理")
    public JsonResult<PageResultDTO<ExamRoomVO>> queryExamRoomPage(@RequestBody ExamRoomQueryAO qo) {
        return JsonResult.buildSuccess(examRoomService.queryExamRoomPage(qo));
    }

    @PostMapping("/detail/{id}")
    @ApiOperation(value = "教室管理-查询详情", notes = "jiangjj")
    @OperateLog(type = AmqpConstant.OPERATION, value = "教室管理-查询详情", businessType = "教室管理")
    public JsonResult<ExamRoomVO> getExamRoomDetail(@PathVariable Long id) {
        return examRoomService.getExamRoomDetail(id);
    }

    @PostMapping("/add")
    @ApiOperation(value = "教室管理-新增", notes = "jiangjj")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "教室管理-新增", businessType = "教室管理")
    public JsonResult<String> addExamRoom(@Valid @RequestBody ExamRoomAddAO ao) {
        return examRoomService.addExamRoom(ao);
    }

    @PostMapping("/update")
    @ApiOperation(value = "教室管理-编辑", notes = "jiangjj")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "教室管理-编辑", businessType = "教室管理")
    public JsonResult<String> updateExamRoom(@Valid @RequestBody ExamRoomAddAO ao) {
        return examRoomService.updateExamRoom(ao);
    }

    @PostMapping("/delete/{id}")
    @ApiOperation(value = "教室管理-删除(逻辑删除)", notes = "jiangjj")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "教室管理-删除", businessType = "教室管理")
    public JsonResult<String> deleteExamRoom(@PathVariable Long id) {
        return examRoomService.deleteExamRoom(id);
    }

    @PostMapping("/import")
    @ApiOperation(value = "教室管理-导入", notes = "jiangjj")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "教室管理-导入", businessType = "教室管理")
    public JsonResult<String> importExamRoom(@RequestParam("file") MultipartFile file) {
        Long schoolId = SmartRequestTokenUtil.getRequestUserStuSchoolId();
        return examRoomService.importExamRoom(schoolId, file);
    }

    @PostMapping("/export")
    @ApiOperation(value = "教室管理-导出", notes = "jiangjj")
    @RolePermission(roles = {1,4})
    @OperateLog(type = AmqpConstant.OPERATION, value = "教室管理-导出", businessType = "教室管理")
    public void exportExamRoom(@RequestBody ExamRoomQueryAO qo, HttpServletResponse response) {
        examRoomService.exportExamRoom(qo, response);
    }
}
