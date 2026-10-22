package com.sxkj.edu.domain.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("教师查询任课学生入参")
public class AsTeacherStudentQueryAO {

    @ApiModelProperty("教师ID")
    private Long teacherId;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("姓名/学号关键词")
    private String keyword;
}
