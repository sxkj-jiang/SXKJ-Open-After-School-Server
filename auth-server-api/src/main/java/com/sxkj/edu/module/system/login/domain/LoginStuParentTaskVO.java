package com.sxkj.edu.module.system.login.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class LoginStuParentTaskVO {

    @ApiModelProperty("学生id")
    private Long studentId;

    @ApiModelProperty("学生监护人账号ID")
    private Long guardianUserId;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("学生学号")
    private String studentNo;

    @ApiModelProperty("家长姓名")
    private String parentName;

    @ApiModelProperty("家长关系")
    private String relationName;

    @ApiModelProperty("学生年级")
    private String gradeName;

    @ApiModelProperty("学生班级")
    private String className;

    @ApiModelProperty("联系方式")
    private String phone;

}
