package com.sxkj.edu.domain.base.khfw.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ExamQuestionEditVO {

    @ApiModelProperty("id主键  编辑必传")
    private Long id;

    @ApiModelProperty(value = "试卷名称")
    private String title;

    @ApiModelProperty(value = "试卷描述")
    private String introduce;

    @ApiModelProperty(value = "适用角色JSON（多个） 1-家长 2-教师 3-管理员")
    private String applicableRoleJson;

    @ApiModelProperty("评价指标体系ID")
    private Long evaluationIndicatorSystemId;

    @ApiModelProperty(value = "题目数量" )
    private Integer questNum;

    @ApiModelProperty("状态：0 停用，1 启用")
    private Integer enable;

    @ApiModelProperty(value = "试题库列表集合" )
    List<ExamQuestionAutoCreateVO> items;

    @ApiModelProperty("学校ID")
    private Long schoolId;
}
