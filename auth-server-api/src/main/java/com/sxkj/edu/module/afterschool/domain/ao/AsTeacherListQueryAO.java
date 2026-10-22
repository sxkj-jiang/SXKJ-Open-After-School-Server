package com.sxkj.edu.module.afterschool.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AsTeacherListQueryAO {

    @ApiModelProperty(value = "学校ID",required = true)
    private Long schoolId;

    @ApiModelProperty("教师姓名（模糊）")
    private String teacherName;
}
