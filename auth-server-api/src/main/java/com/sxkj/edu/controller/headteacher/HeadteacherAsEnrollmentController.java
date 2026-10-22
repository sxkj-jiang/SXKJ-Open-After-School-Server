package com.sxkj.edu.controller.headteacher;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.module.afterschool.domain.dto.AsEnrollmentAssignAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsEnrollmentAssignOptionsQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsEnrollmentChangeAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsHeadteacherEnrollmentPageQueryAO;
import com.sxkj.edu.module.afterschool.domain.vo.AsEnrollmentAssignOptionsVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsEnrollmentOperateResultVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsHeadteacherEnrollmentStatusCountVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsHeadteacherEnrollmentTermGroupVO;
import com.sxkj.edu.module.afterschool.service.AsEnrollmentHeadteacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 班主任-课后报名管理接口。
 * <p>
 * 提供班主任视角下的课后报名管理功能，包括报名列表查询、Tab 统计、分配课程候选查询、
 * 分配课后去向以及已分配课程调整。
 * 列表以学生+学期维度聚合展示，支持按全部/已报名/已分配/未分配 Tab 筛选。
 * </p>
 *
 * @author wangyouhuai
 * @date 2026-06-12
 */
@RestController
@RequestMapping("/headteacher/afterschool/enrollment")
@Api(tags = {SwaggerTagConst.TEA_LEADER.LEAD_ZHGL})
@Slf4j
@OperateLog(type = AmqpConstant.OPERATION, value = "班主任-课后报名", businessType = "班主任-课后报名")
public class HeadteacherAsEnrollmentController {

    @Resource
    private AsEnrollmentHeadteacherService asEnrollmentHeadteacherService;

    /**
     * 分页查询班主任管理的班级下学生课后报名列表。
     * <p>按学期分组展示，返回每个学期下的学生报名/待分配记录。</p>
     */
    @PostMapping("/page")
    @ApiOperation("班主任-课后报名列表")
    @OperateLog(type = AmqpConstant.OPERATION, value = "班主任-课后报名列表", businessType = "班主任-课后报名")
    public JsonResult<PageResultDTO<AsHeadteacherEnrollmentTermGroupVO>> pageHeadteacherEnrollments(
            @RequestBody AsHeadteacherEnrollmentPageQueryAO query) {
        return JsonResult.buildSuccess(asEnrollmentHeadteacherService.pageHeadteacherEnrollments(query));
    }

    /**
     * 统计班主任报名列表各 Tab 的数量。
     * <p>与分页列表接口使用相同的过滤和分组规则，返回全部/已报名/已分配/未分配四个维度的数量。</p>
     */
    @PostMapping("/status-count")
    @ApiOperation("班主任-课后报名Tab数量")
    @OperateLog(type = AmqpConstant.OPERATION, value = "班主任-课后报名Tab数量", businessType = "班主任-课后报名")
    public JsonResult<AsHeadteacherEnrollmentStatusCountVO> statusCountHeadteacherEnrollment(
            @RequestBody(required = false) AsHeadteacherEnrollmentPageQueryAO query) {
        return JsonResult.buildSuccess(asEnrollmentHeadteacherService.countHeadteacherEnrollmentStatuses(query));
    }

    /**
     * 统计班主任报名列表各 Tab 的数量。
     * <p>与分页列表接口使用相同的过滤和分组规则，返回全部/已报名/已分配/未分配四个维度的数量。</p>
     */
    @PostMapping("/status-count/v2")
    @ApiOperation("班主任-课后报名Tab数量V2")
    @OperateLog(type = AmqpConstant.OPERATION, value = "班主任-课后报名Tab数量", businessType = "班主任-课后报名")
    public JsonResult<AsHeadteacherEnrollmentStatusCountVO> statusCountHeadteacherEnrollmentV2(
            @RequestBody(required = false) AsHeadteacherEnrollmentPageQueryAO query) {
        return JsonResult.buildSuccess(asEnrollmentHeadteacherService.countHeadteacherEnrollmentStatusesV2(query));
    }


}
