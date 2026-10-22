package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 家长端绑定学生选项
 *
 * @author system
 * @date 2026-05-21
 */
@Data
@ApiModel("家长端绑定学生选项")
public class AsGuardianStudentOptionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("学生跨学期稳定身份ID")
    private Long studentIdentityId;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("学生快照所属学期ID")
    private Long termId;

    @ApiModelProperty("学生快照所属学期名称")
    private String termName;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("学生性别：0男 1女")
    private Integer studentGender;

    @ApiModelProperty("学生身份证(脱密后)")
    private String idCard;

    @ApiModelProperty("学生头像地址")
    private String imgUrl;

    @ApiModelProperty("年级名称")
    private String gradeName;

    @ApiModelProperty("班级名称")
    private String className;

    @ApiModelProperty("当前学生，true表示是当前学生")
    private Boolean currentStudent;

    @ApiModelProperty("学籍状态：1在籍 2已毕业")
    private Integer enrollmentStatus;
}
