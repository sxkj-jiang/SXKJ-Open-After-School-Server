package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 教师端邀请可选学生行
 */
@Data
@ApiModel("教师端邀请可选学生行")
public class AsTeacherInviteStudentRowVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("学生快照所属学期ID")
    private Long termId;

    @ApiModelProperty("学生快照所属学期名称")
    private String termName;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("姓名")
    private String studentName;

    @ApiModelProperty("学生注册状态：0待注册 1已通过 2已驳回 3待审核")
    private Integer registerStatus;

    @ApiModelProperty("学生注册状态名称")
    private String registerStatusName;

    @ApiModelProperty("年级ID")
    private Long gradeId;

    @ApiModelProperty("年级名称")
    private String gradeName;

    @ApiModelProperty("班级ID")
    private Long classId;

    @ApiModelProperty("班级名称")
    private String className;

    @ApiModelProperty("选课记录ID")
    private Long enrollmentId;

    @ApiModelProperty("邀请学生状态")
    private String inviteStudentStatus;

    @ApiModelProperty("邀请学生状态名称")
    private String inviteStudentStatusName;

    @ApiModelProperty("是否可勾选")
    private Boolean selectable;

    @ApiModelProperty("不可选原因")
    private String disabledReason;
}
