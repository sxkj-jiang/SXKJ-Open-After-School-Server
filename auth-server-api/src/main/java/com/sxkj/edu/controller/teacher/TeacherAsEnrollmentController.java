package com.sxkj.edu.controller.teacher;

import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.module.afterschool.domain.dto.AsEnrollmentAssignAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsEnrollmentAssignOptionsQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsEnrollmentSelectionPageQueryAO;
import com.sxkj.edu.module.afterschool.domain.vo.AsEnrollmentAssignOptionsVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsEnrollmentOperateResultVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsEnrollmentSelectionRowVO;
import com.sxkj.edu.module.afterschool.service.AsEnrollmentAdminQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/teacher/afterschool/enrollment")
@Api(tags = {SwaggerTagConst.TEACHER.TEA_YQXK})
@Slf4j
@OperateLog
public class TeacherAsEnrollmentController {

    @Autowired
    private AsEnrollmentAdminQueryService asEnrollmentAdminQueryService;

    @PostMapping("/unselected/page")
    @ApiOperation("分页查询未选学生")
    public JsonResult<PageResultDTO<AsEnrollmentSelectionRowVO>> unselectedPage(@RequestBody AsEnrollmentSelectionPageQueryAO query) {
        return JsonResult.buildSuccess(asEnrollmentAdminQueryService.pageUnselected(query));
    }


}
