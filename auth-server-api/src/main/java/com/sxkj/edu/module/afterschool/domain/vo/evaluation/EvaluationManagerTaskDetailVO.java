package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.domain.base.khfw.vo.TaskParseQuestionVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("评价任务完整详情VO")
public class EvaluationManagerTaskDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("任务信息")
    private AsEvaluationTaskVO task;
    
    @ApiModelProperty("课程班信息")
    private CourseVO courseClass;
    
    @ApiModelProperty("模板信息")
    private Object template;
    
    @ApiModelProperty("题目列表")
    private List<TaskParseQuestionVO> questions;

    @ApiModelProperty("是否已评价")
    private Boolean hasEvaluated;

    @ApiModelProperty("作答内容JSON")
    private String answerJson;

    @ApiModelProperty("评价文字意见")
    private String commentText;

    @ApiModelProperty("评价总分")
    private BigDecimal totalScore;

    @ApiModelProperty("提交时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    @ApiModelProperty("评价角色编码(portal_role_stu/portal_role_teacher/portal_school_admin)")
    private String evaluatorRoleCode;

    @ApiModelProperty("评价角色描述")
    private String evaluatorRoleDesc;

    @ApiModelProperty("学生评价列表(角色为学生时返回)")
    private List<StudentEvaluationItemVO> studentEvaluations;

    @ApiModelProperty("教师评价列表(角色为教师/班主任时返回)")
    private List<TeacherEvaluationItemVO> teacherEvaluations;

    @ApiModelProperty("管理员评价列表(角色为管理员时返回)")
    private List<AdminEvaluationItemVO> adminEvaluations;
}
