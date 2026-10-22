package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("班主任端评价任务学生列表VO")
public class HeadteacherEvaluationStudentVO {

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("自然班级名称")
    private String className;

    @ApiModelProperty("评价状态：0-待评价 1-已评价")
    private Integer evalStatus;

    @ApiModelProperty("状态描述：待评价/已评价")
    private String statusDesc;

    @ApiModelProperty("操作按钮文案：评价/查看")
    private String actionText;

    @ApiModelProperty("学生账号userId（用于评价提交关联）")
    private Long studentUserId;

    @ApiModelProperty("课程班ID")
    private Long courseId;

    @ApiModelProperty("课程班名称")
    private String courseName;
}
