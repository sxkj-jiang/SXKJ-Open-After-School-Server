package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 教师新增AO - 接口入参对象
 *
 * @author system
 * @date 2026-05-17
 */
@Data
public class AsTeacherAddAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "教师ID(编辑时必填)")
    private Long id;

    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @ApiModelProperty(value = "教师工号", required = true)
    @NotBlank(message = "教师工号不能为空")
    private String teacherNo;

    @ApiModelProperty(value = "教师姓名", required = true)
    @NotBlank(message = "教师姓名不能为空")
    private String teacherName;

    @ApiModelProperty(value = "性别：0男 1女", required = true)
    @NotNull(message = "性别不能为空")
    private Integer gender;

    @ApiModelProperty(value = "手机号", required = true)
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "学校阶段")
    private String schoolStage;

    @ApiModelProperty(value = "政治面貌")
    private String politicalStatus;

    @ApiModelProperty(value = "岗位名称")
    private String positionName;

    @ApiModelProperty(value = "任职状态：1在职 2离职", required = true)
    @NotNull(message = "任职状态不能为空")
    private Integer employeeStatus;

    @ApiModelProperty(value = "注册状态：0待注册 1已注册")
    private Integer registerStatus;

    @ApiModelProperty(value = "是否班主任：0否 1是")
    private Integer isHeadTeacher;

    @ApiModelProperty("头像文件ID")
    private Long avatarFileId;

    @ApiModelProperty("学科ID列表")
    private List<Long> subjectIds;

    @ApiModelProperty("任课班级（多个）")
    private List<Long> classIdList;
}
