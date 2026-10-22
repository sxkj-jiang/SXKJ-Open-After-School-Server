package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 学生信息AO - 用于新增/更新学生档案
 *
 * @author system
 * @date 2026-05-17
 */
@Data
public class AsStudentAddAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学生ID(编辑时必填)")
    private Long id;

    @ApiModelProperty(value = "学生姓名", required = true)
    @NotBlank(message = "学生姓名不能为空")
    private String studentName;

    @ApiModelProperty(value = "学号", required = true)
    @NotBlank(message = "学号不能为空")
    private String studentNo;

    @ApiModelProperty(value = "一年级基准入学年份；未传时从班级资料中读取")
    private Integer enrollmentYear;

    @ApiModelProperty(value = "学期ID", required = true)
    @NotNull(message = "学期不能为空")
    private Long termId;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "性别：0-男，1-女")
    private Integer gender;

    @ApiModelProperty(value = "出生日期")
    private String birthday;

    @ApiModelProperty(value = "年级ID", required = true)
    @NotNull(message = "年级ID不能为空")
    private Long gradeId;

    @ApiModelProperty(value = "班级ID",required = true)
    @NotNull(message = "班级不能为空")
    private Long classId;

    @ApiModelProperty(value = "家长姓名")
    private String parentName;

    @ApiModelProperty(value = "家长手机号,多个用英文;隔开", required = true)
    @NotBlank(message = "家长手机号不能为空")
    private String parentPhone;

    @ApiModelProperty(value = "亲属关系")
    private String relationName;

    @ApiModelProperty(value = "家庭住址")
    private String address;

    @ApiModelProperty("头像id")
    private Long avatarFileId;

    @ApiModelProperty("学生监护人ID,更新时必填")
    private Long guardianId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "学籍状态：1在籍 2毕业 3休学 4转学")
    private Integer schoolRollStatus;

    @ApiModelProperty("学号发生变化时必填的纠错原因")
    private String studentNoChangeReason;

    @ApiModelProperty("已有业务时年级、班级或学籍变更的生效时间")
    private Date effectiveTime;

    @ApiModelProperty("已有业务时年级、班级或学籍变更的原因")
    private String changeReason;

    @ApiModelProperty("毕业年份")
    private Integer graduationYear;

    @ApiModelProperty("学生ID")
    private Long schoolId;
}
