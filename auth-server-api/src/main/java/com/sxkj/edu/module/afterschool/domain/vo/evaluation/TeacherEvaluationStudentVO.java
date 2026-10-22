package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("任课老师端评价学生列表VO")
public class TeacherEvaluationStudentVO {

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("任务ID")
    private Long taskId;

    @ApiModelProperty("课程班ID")
    private Long courseId;

    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("班级名称")
    private String className;

    @ApiModelProperty("学生头像文件ID")
    private Long avatarFileId;

    @ApiModelProperty("评价状态：0-待评价 1-已评价")
    private Integer evalStatus;

    @ApiModelProperty("状态描述")
    private String statusDesc;

    @ApiModelProperty("操作按钮文案")
    private String actionText;

    @ApiModelProperty("学生账号userId")
    private Long studentUserId;
}
