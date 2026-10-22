package com.sxkj.edu.module.system.login.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class LoginStuParentDataVO {

    @ApiModelProperty("学生id")
    private Long studentId;

    @ApiModelProperty("学生监护人ID")
    private Long guardianId;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("家长姓名")
    private String parentName;

    @ApiModelProperty("家长关系")
    private String relationName;

    @ApiModelProperty("联系方式")
    private String phone;

    @ApiModelProperty("家长id")
    private Long parentId;
}
