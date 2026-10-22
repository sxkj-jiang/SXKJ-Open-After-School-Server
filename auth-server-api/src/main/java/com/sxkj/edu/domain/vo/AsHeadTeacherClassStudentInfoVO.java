package com.sxkj.edu.domain.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "班主任查询班级学生信息VO", description = "以班级为维度返回班级名称及学生列表")
public class AsHeadTeacherClassStudentInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("班级名称")
    private String className;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("性别：0男 1女")
    private Integer gender;
}
