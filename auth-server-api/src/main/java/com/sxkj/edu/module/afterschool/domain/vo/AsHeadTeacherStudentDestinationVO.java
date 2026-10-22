package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 班主任查询自然班级学生去向VO
 * 以学期名称为一级维度、自然班级名称为二级维度返回
 *
 * @author system
 * @date 2026-06-05
 */
@Data
@ApiModel("班主任查询自然班级学生去向VO")
public class AsHeadTeacherStudentDestinationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("自然班级ID")
    private Long classId;

    @ApiModelProperty("自然班级名称")
    private String className;

    @ApiModelProperty("年级名称")
    private String gradeName;

    @ApiModelProperty("班级人数")
    private Integer classStudentCount;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("姓名")
    private String studentName;

    @ApiModelProperty("性别：0男 1女")
    private Integer gender;

    @ApiModelProperty("性别描述")
    private String genderDesc;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("课后去向类型：1课程班 2回家 3培训机构 4留班 5行政课 6其他")
    private Integer destinationType;

    @ApiModelProperty("课后去向描述")
    private String destinationDesc;

    @ApiModelProperty("课次日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date sessionDate;

    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("课次ID")
    private Long sessionId;
}
