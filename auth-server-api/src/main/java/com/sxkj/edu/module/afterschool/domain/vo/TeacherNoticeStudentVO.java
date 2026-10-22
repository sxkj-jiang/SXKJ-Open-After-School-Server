package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TeacherNoticeStudentVO {

    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("学生用户ID")
    private Long userId;
}
