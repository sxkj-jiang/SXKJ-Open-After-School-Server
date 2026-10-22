package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.validation.ChineseAnno;
import com.sxkj.edu.common.validation.ChineseTitleAnno;
import com.sxkj.edu.common.validation.Mobile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 家长注册 AO
 *
 * @author system
 * @date 2026-05-25
 */
@Data
@ApiModel("家长注册对象")
public class ParentRegisterAO {

    @ApiModelProperty(value = "家长信息-姓名", required = true)
    @NotBlank(message = "家长姓名不能为空")
    @ChineseAnno
    private String parentName;

    @ApiModelProperty(value = "家长信息-与学生的关系", required = true)
    @NotBlank(message = "与学生的关系不能为空")
    private String relationName;

    @ApiModelProperty(value = "家长信息-手机号", required = true)
    @NotBlank(message = "手机号不能为空")
    @Mobile
    private String phone;

    @ApiModelProperty(value = "学生信息-学生姓名", required = true)
    @NotBlank(message = "学生姓名不能为空")
    @ChineseAnno
    private String studentName;

    @ApiModelProperty(value = "学生信息-学生学号", required = true)
    @NotBlank(message = "学生学号不能为空")
    @Length(min = 6, max = 16, message = "请输入正确的学生学号")
    private String studentNo;

    @ApiModelProperty(value = "学生信息-年级ID", required = true)
    @NotNull(message = "年级ID不能为空")
    private Long gradeId;

    @ApiModelProperty(value = "学生信息-班级ID", required = true)
    @NotNull(message = "班级ID不能为空")
    private Long classId;

    @ApiModelProperty(value = "学生信息-性别：0男 1女", required = true)
    @NotNull(message = "学生性别不能为空")
    private Integer studentGender;

    @ApiModelProperty(value = "其他信息-学校ID", required = true)
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @ApiModelProperty(value = "学校名称")
    private String schoolName;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "二次确认密码", required = true)
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    @ApiModelProperty(value = "头像文件ID", required = true)
    private Long avatarFileId;

    @ApiModelProperty(value = "身份证")
    private String idCard;

    @ApiModelProperty(value = "来源课后服务ID，普通注册不传，未来服务或邀请入口注册时传入")
    private Long serviceId;
}
