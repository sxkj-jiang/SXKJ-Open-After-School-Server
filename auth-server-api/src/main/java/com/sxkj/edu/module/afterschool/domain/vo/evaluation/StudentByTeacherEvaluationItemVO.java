package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("学生角色：评价教师列表")
public class StudentByTeacherEvaluationItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("任务ID")
    private Long taskId;

    @ApiModelProperty("答案ID")
    private Long answerId;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("评价人账号ID-学生")
    private Long evaluatorUserId;

    @ApiModelProperty("评价人：学生ID")
    private Long studentId;

    @ApiModelProperty("评价人：学号")
    private String studentNo;

    @ApiModelProperty("被评价人：教师id")
    private Long teacherId;

    @ApiModelProperty("被评价人：教师姓名")
    private String teacherName;

    @ApiModelProperty("评价人：学生头像")
    private Long avatarFileId;

    @ApiModelProperty("年级名称")
    private String gradeName;

    @ApiModelProperty("班级名称")
    private String className;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("评价状态描述")
    private String submitStatusDesc;

    @ApiModelProperty("评价分")
    private BigDecimal totalScore;

    @ApiModelProperty("评价文字意见")
    private String commentText;

    @ApiModelProperty("提交日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date previewDate;

    @ApiModelProperty("评价状态：0-待评价 1-已评价")
    private Integer evalStatus;

    @ApiModelProperty("操作按钮文案")
    private String actionText;

}
