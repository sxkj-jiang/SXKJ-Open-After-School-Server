package com.sxkj.edu.controller.headteacher;

import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.module.afterschool.domain.dto.AsStudentQueryAO;
import com.sxkj.edu.module.afterschool.domain.vo.AsStudentVO;
import com.sxkj.edu.module.afterschool.service.AsStudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 班主任-班级管理Controller
 *
 * @author system
 * @date 2026-05-17
 */
@RestController
@RequestMapping("/headteacher/afterschool/class")
@Api(tags = "课后服务-班主任班级")
@Slf4j
public class HeadteacherAsClassController {

    @Resource
    private AsStudentService asStudentService;

    @PostMapping("/students/pageQuery")
    @ApiOperation("分页查询班级学生列表")
    public JsonResult<PageResultDTO<AsStudentVO>> queryAsStudentPage(@RequestBody AsStudentQueryAO qo) {
        return JsonResult.buildSuccess(asStudentService.queryAsStudentPage(qo));
    }

//    @GetMapping("/statistics")
//    @ApiOperation("获取班级统计信息")
//    public JsonResult<Object> getClassStatistics() {
//        return JsonResult.buildSuccess(null);
//    }
}
