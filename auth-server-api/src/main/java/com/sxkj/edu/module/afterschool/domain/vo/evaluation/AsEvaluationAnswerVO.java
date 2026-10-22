package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("评价答案视图对象")
public class AsEvaluationAnswerVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("答案ID")
    private Long id;
    
    @ApiModelProperty("所属学校")
    private Long schoolId;
    
    @ApiModelProperty("任务ID")
    private Long taskId;
    
    @ApiModelProperty("课程班ID")
    private Long courseClassId;
    
    @ApiModelProperty("评价人账号ID")
    private Long evaluatorUserId;
    
    @ApiModelProperty("被评价教师ID")
    private Long targetTeacherId;
    
    @ApiModelProperty("评价总分")
    private java.math.BigDecimal totalScore;
    
    @ApiModelProperty("评价作答明细JSON")
    private String answerJson;
    
    @ApiModelProperty("评价文字意见")
    private String commentText;
    
    @ApiModelProperty("提交状态：0-未提交 1-已提交")
    private Integer submitStatus;
    
    @ApiModelProperty("提交时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;
    
    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("用户答题列表")
    private List<UserEvaluationAnswerVO> userEvaluationAnswerVOList;
}
