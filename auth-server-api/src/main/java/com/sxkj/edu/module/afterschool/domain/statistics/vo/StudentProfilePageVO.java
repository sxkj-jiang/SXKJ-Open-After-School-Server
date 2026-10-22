package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("学生画像-分页查询结果")
public class StudentProfilePageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("姓名")
    private String studentName;

    @ApiModelProperty("性别：0-男，1-女")
    private Integer gender;

    @ApiModelProperty("年级名称")
    private String gradeName;

    @ApiModelProperty("班级名称")
    private String className;
}
