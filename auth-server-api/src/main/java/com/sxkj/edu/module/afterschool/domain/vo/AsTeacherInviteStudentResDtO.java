package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 教师端邀请可选学生行
 */
@Data
@ApiModel("教师端邀请可选学生行")
public class AsTeacherInviteStudentResDtO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("班级ID列表")
    private Set<Long> classIdList;

    @ApiModelProperty("年级ID列表")
    private Set<Long> gradeIdList;
}
