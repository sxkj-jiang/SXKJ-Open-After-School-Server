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
@ApiModel("家长端评价任务详情VO")
public class GuardianEvaluationTaskDetailVO {

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("学生学期快照ID")
    private Long studentId;

    @ApiModelProperty("学生稳定身份ID")
    private Long studentIdentityId;

    @ApiModelProperty("任务ID")
    private Long id;

    @ApiModelProperty("教师ID")
    private Long teacherId;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("课程名称")
    private String courseName;

    @ApiModelProperty("授课教师")
    private String teacherName;

    @ApiModelProperty("评价开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("评价结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("开课周期开始")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date courseStartTime;

    @ApiModelProperty("开课周期结束")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date courseEndTime;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("状态描述")
    private String statusDesc;

    @ApiModelProperty("是否已评价")
    private Boolean hasEvaluated;

    @ApiModelProperty("作答内容JSON")
    private String answerJson;

    @ApiModelProperty("题目列表")
    private List<QuestionVO> questions;

    @ApiModelProperty("用户答题列表")
    private List<UserEvaluationAnswerVO> userEvaluationAnswerVOList;

    @ApiModelProperty("评价得分")
    private BigDecimal totalScore;
}
