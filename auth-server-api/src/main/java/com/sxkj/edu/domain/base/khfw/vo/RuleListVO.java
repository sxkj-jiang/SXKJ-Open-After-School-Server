package com.sxkj.edu.domain.base.khfw.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName RuleListVO
 * @Description 规则列表响应项
 * @Author backend-architect
 * @Date 2026-01-12
 * @Version 1.0
 **/
@Data
public class RuleListVO {
    @ApiModelProperty(value = "规则ID")
    private String ruleId;              // 规则ID

    @ApiModelProperty(value = "规则名称")
    private String ruleName;            // 规则名称

    @ApiModelProperty(value = "学科名称")
    private String subjectName;         // 学科名称

    @ApiModelProperty(value = "教龄名称")
    private String teachingAgeName;     // 教龄名称

    @ApiModelProperty(value = "创建人")
    private String creator;             // 创建人

    @ApiModelProperty(value = "更新时间")
    private String updateTime;          // 更新时间

    @ApiModelProperty(value = "启用状态（true-启用，false-禁用）")
    private Boolean isEnabled;          // 启用状态

    @ApiModelProperty(value = "学校ID")
    private Long schoolId;
}
