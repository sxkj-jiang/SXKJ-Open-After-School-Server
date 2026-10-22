package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.validation.ChineseAnno;
import com.sxkj.edu.common.validation.Mobile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 授课教师注册 AO
 *
 * @author system
 * @date 2026-05-25
 */
@Data
@ApiModel("授课教师注册对象")
public class TeacherRegisterAO {

    @ApiModelProperty(value = "姓名", required = true)
    @NotBlank(message = "姓名不能为空")
    @ChineseAnno
    private String teacherName;

    @ApiModelProperty(value = "手机号", required = true)
    @NotBlank(message = "手机号不能为空")
    @Mobile
    private String phone;

    @ApiModelProperty(value = "性别：0男 1女", required = true)
    @NotNull(message = "性别不能为空")
    private Integer gender;

    @ApiModelProperty(value = "学校ID", required = true)
    @NotNull(message = "学校不能为空")
    private Long schoolId;

    @ApiModelProperty(value = "学校名称")
    private String schoolName;

    @ApiModelProperty(value = "是否班主任：0否 1是", required = true)
    @NotNull(message = "是否班主任不能为空")
    private Integer isHeadTeacher;

    @ApiModelProperty(value = "年级ID,班主时必填")
    private Long gradeId;

    @ApiModelProperty(value = "班级ID，班主时必填")
    private Long classId;

    @ApiModelProperty("职位")
    private String positionName;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "二次确认密码", required = true)
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    @ApiModelProperty(value = "头像文件ID", required = true)
    private Long avatarFileId;

    @ApiModelProperty("工号")
    private String teacherNo;

    @ApiModelProperty("政治面貌")
    private String politicalStatus;
}
