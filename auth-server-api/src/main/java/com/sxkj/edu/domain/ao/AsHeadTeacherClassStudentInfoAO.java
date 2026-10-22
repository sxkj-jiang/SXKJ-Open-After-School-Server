package com.sxkj.edu.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AsHeadTeacherClassStudentInfoAO {

    @ApiModelProperty("班级ID")
    private Long classId;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("教师ID")
    private Long teacherId;

    @ApiModelProperty("姓名/学号关键词")
    private String keyword;
}
