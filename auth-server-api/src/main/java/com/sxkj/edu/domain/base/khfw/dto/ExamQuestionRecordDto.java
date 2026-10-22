package com.sxkj.edu.domain.base.khfw.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ExamQuestionRecordDto {

    @ApiModelProperty(value = "专题名称")
    private String specialTopicName;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "试卷名称")
    private String title;

    @ApiModelProperty(value = "答题时间")
    private String answerTime;

    @ApiModelProperty(value = "提交时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    @ApiModelProperty(value = "用户得分")
    private Integer userScore;

    @ApiModelProperty(value = "试卷总分")
    private Integer score;

    @ApiModelProperty(value = "题目内容")
    private String content;

    @ApiModelProperty(value = "用户答案")
    private String stuAnswer;

    @ApiModelProperty(value = "排序")
    private Integer itemSort;

    @ApiModelProperty(value = "单题得分")
    private Integer itemScore;
}
