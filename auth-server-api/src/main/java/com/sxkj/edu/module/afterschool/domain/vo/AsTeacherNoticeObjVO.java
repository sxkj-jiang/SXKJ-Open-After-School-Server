package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AsTeacherNoticeObjVO {

    @ApiModelProperty("班级列表")
    List<EduclClassVO> classList;

    @ApiModelProperty("课程班列表")
    List<AsCourseClassDropdownVO> courseClassList;

    @ApiModelProperty("学生列表")
    List<AsStudentVO> studentList;
}
