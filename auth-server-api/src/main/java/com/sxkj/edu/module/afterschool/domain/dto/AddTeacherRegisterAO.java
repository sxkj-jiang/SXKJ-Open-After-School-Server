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
public class AddTeacherRegisterAO {

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
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @ApiModelProperty(value = "是否班主任：0否 1是", required = true)
    @NotNull(message = "是否班主任不能为空")
    private Integer isHeadTeacher;

    @ApiModelProperty("职位")
    private String positionName;

    @ApiModelProperty("政治面貌")
    private String politicalStatus;

    @ApiModelProperty("身份证号")
    private String idCard;


    @ApiModelProperty("工号")
    private String teacherNo;

    @ApiModelProperty("密码")
    private String password;
}
