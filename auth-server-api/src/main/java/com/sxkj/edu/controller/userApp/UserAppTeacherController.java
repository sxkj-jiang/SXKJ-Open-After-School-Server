package com.sxkj.edu.controller.userApp;

import com.sxkj.edu.common.anno.AccessLimit;
import com.sxkj.edu.common.anno.NoNeedLogin;
import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.anno.RolePermission;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.PageVO;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.base.mult.TPortalCarouselAo;
import com.sxkj.edu.domain.base.mult.TPortalCarouselVO;
import com.sxkj.edu.domain.mult.ao.PortalArticleManageListAo;
import com.sxkj.edu.domain.mult.vo.PortalArticleCategoryVO;
import com.sxkj.edu.module.afterschool.domain.dto.AsTeacherTodayCoursePageQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.ExamRoomQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.TeacherRegisterAO;
import com.sxkj.edu.module.afterschool.domain.vo.AsTeacherTodayCourseDetailVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsTeacherTodayCourseVO;
import com.sxkj.edu.module.afterschool.domain.vo.ExamRoomVO;
import com.sxkj.edu.module.afterschool.service.*;
import com.sxkj.edu.service.TPortalArticleManageService;
import com.sxkj.edu.service.TPortalCarouselService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 *@Description : 移动教师端app接口
 *@Author : JIANGJJ
 *@Date : 2026-5-22 10:57
 *@Company : SXKJ
 *@Version : 1.0
 **/
@RestController
@Slf4j
@RequestMapping("/api/teacher/userApp")
@Api(tags = {SwaggerTagConst.TEACHER.TEA_QJGL})
@OperateLog
public class UserAppTeacherController {

    @Resource
    AsTeacherService asTeacherService;


    @Resource
    AsCourseSessionService asCourseSessionService;

    @Autowired
    private TPortalCarouselService tPortalCarouselService;

    @Autowired
    private TPortalArticleManageService tPortalArticleManageService;

    @Resource
    private ExamRoomService examRoomService;


    @NoNeedLogin
    @PostMapping("/teacherRegister")
    @ApiOperation(value = "授课教师注册", notes = "jiangjj")
    @AccessLimit(seconds = 4, maxCount = 4, desc = "您的频率过快，请稍微在试")
    public JsonResult<String> teacherRegister(@Valid @RequestBody TeacherRegisterAO ao) {
        return asTeacherService.registerTeacher(ao);
    }

    @PostMapping("/pageTeacherTodayCourse")
    @ApiOperation(value = "首页-查询教师今日课程分页", notes = "jiangjj")
    @OperateLog(type = AmqpConstant.OPERATION, value = "查询教师今日课程", businessType = "教师课表")
    public JsonResult<PageResultDTO<AsTeacherTodayCourseVO>> pageTeacherTodayCourse(@Valid @RequestBody AsTeacherTodayCoursePageQueryAO ao) {
        // 获取当前登录教师ID
        Long currentTeacherId = SmartRequestTokenUtil.getRequestUserId();
        return JsonResult.buildSuccess(asCourseSessionService.pageTeacherTodayCourse(ao, currentTeacherId));
    }

    @PostMapping("/getTeacherTodayCourseDetail/{sessionId}")
    @ApiOperation(value = "首页-查询教师今日课程详情", notes = "jiangjj")
    @OperateLog(type = AmqpConstant.OPERATION, value = "查询教师今日课程详情", businessType = "教师课表")
    public JsonResult<AsTeacherTodayCourseDetailVO> getTeacherTodayCourseDetail(@PathVariable Long sessionId) {
        // 获取当前登录教师ID
        Long currentTeacherId = SmartRequestTokenUtil.getRequestUserId();
        AsTeacherTodayCourseDetailVO detail = asCourseSessionService.getTeacherTodayCourseDetail(sessionId, currentTeacherId);
        if (detail == null) {
            return JsonResult.buildError("课程不存在或无权限访问");
        }
        return JsonResult.buildSuccess(detail);
    }

    @PostMapping("/carousel/page")
    @ApiOperation(value = "首页-轮播图分页查询", notes = "yh")
    @NoNeedLogin
    @OperateLog(type = AmqpConstant.OPERATION, value = "首页-轮播图分页查询", businessType = "首页")
    public JsonResult<PageVO<TPortalCarouselVO>> carouselPageList(@RequestBody TPortalCarouselAo query) {
        return JsonResult.buildSuccess(tPortalCarouselService.queryPortalCarouselList(query));
    }

    @PostMapping("/articleManage/categoryList")
    @ApiOperation(value = "首页-文章管理分类查询", notes = "yh")
    @NoNeedLogin
    @OperateLog(type = AmqpConstant.OPERATION, value = "首页-文章管理分类查询", businessType = "首页")
    public JsonResult<List<PortalArticleCategoryVO>> articleManageeMobileMore(@RequestBody PortalArticleManageListAo query) {
        return tPortalArticleManageService.articleManageeMobile(query);
    }


    @PostMapping("/app/pageRoomQuery")
    @ApiOperation(value = "移动端教室管理-分页查询", notes = "jiangjj")
    @OperateLog(type = AmqpConstant.OPERATION, value = "移动端教室管理-分页查询", businessType = "教室管理")
    @RolePermission(roles = {3, 5})
    public JsonResult<PageResultDTO<ExamRoomVO>> queryAppExamRoomPage(@RequestBody ExamRoomQueryAO qo) {
        return JsonResult.buildSuccess(examRoomService.queryExamRoomPage(qo));
    }

}
