package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 教师端已邀请学生行
 */
@Data
@ApiModel("教师端已邀请学生行")
public class AsTeacherInvitedStudentRowVO implements Serializable {

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

    @ApiModelProperty("年级名称")
    private String gradeName;

    @ApiModelProperty("班级名称")
    private String className;

    @ApiModelProperty("邀请学生状态")
    private String inviteStudentStatus;

    @ApiModelProperty("邀请学生状态名称")
    private String inviteStudentStatusName;

    @ApiModelProperty("邀请发起时间")
    private Date inviteTime;

    @ApiModelProperty("家长确认时间")
    private Date confirmTime;
}
