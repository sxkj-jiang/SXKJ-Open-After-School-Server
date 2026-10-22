package com.sxkj.edu.domain.base.khfw.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 *@Description : 教务-试卷详情VO
 *@Author : JIANGJJ
 *@Date : 2025-6-14 15:45
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Data
public class ExamQuestionVO {

    @ApiModelProperty("id主键")
    private Long id;

    @ApiModelProperty("试卷名称")
    private String title;

    @ApiModelProperty(value = "试卷描述")
    private String introduce;

    @ApiModelProperty(value = "适用角色JSON（多个） 1-家长 2-教师 3-管理员")
    private String applicableRoleJson;

    @ApiModelProperty("评价指标体系ID")
    private Long evaluationIndicatorSystemId;

    @ApiModelProperty("题目数量")
    private Integer questNum;

    @ApiModelProperty("状态 0 停用，1 启用")
    private Integer enable;

    @ApiModelProperty("试题库集合")
    private List<QuestionItemVO> items;

    @ApiModelProperty("学校ID")
    private Long schoolId;

}
