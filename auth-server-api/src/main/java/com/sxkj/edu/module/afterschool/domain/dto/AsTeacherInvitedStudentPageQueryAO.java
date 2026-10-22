package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 教师端已邀请学生分页查询
 */
@Data
@ApiModel("教师端已邀请学生分页查询")
public class AsTeacherInvitedStudentPageQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "课程班ID不能为空")
    @ApiModelProperty(value = "课程班ID", required = true)
    private Long courseClassId;

    @ApiModelProperty("学期ID，不传时由课程班所属课后服务推导")
    private Long termId;

    @ApiModelProperty("邀请批次ID，不传则查该课程全部教师邀请学生")
    private Long inviteId;

    @ApiModelProperty("学生邀请状态：pending_confirm/enrolled/rejected")
    private String inviteStudentStatus;

    @ApiModelProperty("学号/姓名关键词")
    private String keyword;

    @ApiModelProperty("年级ID")
    private Long gradeId;

    @ApiModelProperty("班级ID")
    private Long classId;
}
