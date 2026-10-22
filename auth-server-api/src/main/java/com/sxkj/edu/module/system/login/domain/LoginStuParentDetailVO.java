package com.sxkj.edu.module.system.login.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class LoginStuParentDetailVO {

    @ApiModelProperty("学生id")
    private Long studentId;

    @ApiModelProperty("学生身份唯一标识id")
    private Long studentIdentityId;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("学生快照所属学期ID")
    private Long termId;

    @ApiModelProperty("学生快照所属学期名称")
    private String termName;

    @ApiModelProperty("当前学生，true表示是当前学生")
    private Boolean currentStudent;

    @ApiModelProperty("学生id集合")
    private List<Long> studentIdList;

    @Override
    public String toString() {
        return "LoginStuParentDetailVO{" +
                "studentId=" + studentId +
                ", studentIdentityId=" + studentIdentityId +
                ", studentName='" + studentName + '\'' +
                ", studentNo='" + studentNo + '\'' +
                ", termId=" + termId +
                ", termName='" + termName + '\'' +
                ", currentStudent=" + currentStudent +
                '}';
    }
}
