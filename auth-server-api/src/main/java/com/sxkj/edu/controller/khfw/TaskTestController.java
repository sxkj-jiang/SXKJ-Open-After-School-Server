package com.sxkj.edu.controller.khfw;


import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.job.SyncStudentTermJobUp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/afterschool/taskTest")
@Api(tags = {SwaggerTagConst.MULT.SCHEDULE_TASK})
@Slf4j
public class TaskTestController {

    @Autowired
    private SyncStudentTermJobUp syncStudentTermJobUp;


    @PostMapping("/updateStudentTerm")
    @ApiOperation(value = "同步学校默认学期", notes = "手动执行默认学期同步任务")
    public JsonResult<String> updateStudentTerm() {
        syncStudentTermJobUp.syncStudentTerm();
        return JsonResult.buildSuccess("同步学校默认学期执行成功");
    }

}
