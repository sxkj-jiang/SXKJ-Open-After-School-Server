package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import com.sxkj.edu.domain.base.khfw.dto.QuestionItemObjectDto;
import com.sxkj.edu.domain.base.khfw.vo.IndicatorConfigItemVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserEvaluationAnswerVO {

    @ApiModelProperty(value = "题目唯一标识ID")
    private Integer id;

    @ApiModelProperty(value = "问题标题")
    private String title;

    @ApiModelProperty(value = "学校/机构ID")
    private Integer schoolId;

    @ApiModelProperty(value = "评价维度名称")
    private String dimension;

    @ApiModelProperty(value = "用户作答内容")
    private String userAnswer;

    @ApiModelProperty(value = "用户作答百分得分")
    private String userScore;

    @ApiModelProperty(value = "考试或问卷批次号")
    private String examNumber;

    @ApiModelProperty(value = "是否必填：1-是，0-否")
    private Integer isRequired;

    @ApiModelProperty(value = "系统名称")
    private String systemName;

    @ApiModelProperty(value = "文本内容")
    private String textContent;

    @ApiModelProperty(value = "题目类型：1-单选")
    private String questionType;

    @ApiModelProperty(value = "题目类型名称")
    private String questionTypeName;

    @ApiModelProperty(value = "文本内容ID")
    private Integer textContentId;

    @ApiModelProperty(value = "适用角色对象")
    private Object applicableRole;

    @ApiModelProperty(value = "评价指标体系ID" )
    private Integer evaluationIndicatorSystemId;

    @ApiModelProperty(value = "选项列表")
    private List<QuestionItemObjectDto> items;

    @ApiModelProperty(value = "指标配置信息（原始为JSON字符串）")
    private Object indicatorConfig;

    @ApiModelProperty(value = "适用角色ID列表（原始为JSON字符串）")
    private Object applicableRoleJson;

}
