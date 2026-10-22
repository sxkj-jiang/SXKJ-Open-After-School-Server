package com.sxkj.edu.domain.base.khfw.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class QuestionStatisticsVO {

    @ApiModelProperty("题目题型统计")
    List<QuestionStatisticsCountVO> questTypeStatistics;

    @ApiModelProperty("题目总题数")
    Integer titleSumNum;

}
