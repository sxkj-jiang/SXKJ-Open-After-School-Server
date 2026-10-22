package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.domain.base.khfw.vo.QuestionVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("任课老师端评价学生详情VO")
public class TeacherEvaluationStudentDetailVO {

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("任务ID")
    private Long taskId;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("评价开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("评价结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("是否已评价")
    private Boolean hasEvaluated;

    @ApiModelProperty("评价分数")
    private BigDecimal totalScore;

    @ApiModelProperty("作答内容JSON")
    private String answerJson;

    @ApiModelProperty("题目列表")
    private List<QuestionVO> questions;

    @ApiModelProperty("用户答题列表")
    private List<UserEvaluationAnswerVO> userEvaluationAnswerVOList;

}
