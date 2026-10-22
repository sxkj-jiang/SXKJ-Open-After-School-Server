package com.sxkj.edu.domain.base.khfw.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ExamQuestionRecordPageVO {

    @ApiModelProperty("用户答题记录id")
    private Long examRecordId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("试卷名称")
    private String title;

    @ApiModelProperty("得分")
    private Integer score;

    @ApiModelProperty("提交时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    @ApiModelProperty("答题次数")
    private Integer count;

    @ApiModelProperty("最高分")
    private Integer maxScore;
}
