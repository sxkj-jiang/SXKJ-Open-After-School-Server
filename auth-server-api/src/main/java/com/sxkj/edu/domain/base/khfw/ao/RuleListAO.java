package com.sxkj.edu.domain.base.khfw.ao;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName RuleListAO
 * @Description 规则列表查询请求
 * @Author backend-architect
 * @Date 2026-01-12
 * @Version 1.0
 **/
@Data
public class RuleListAO extends PageQuery {
    @ApiModelProperty(value = "规则名称", required = false)
    private String ruleName;          // 规则名称

    @ApiModelProperty(value = "学科代码（QuestSub）", required = false)
    private String subjectCode;       // 学科代码

    @ApiModelProperty(value = "教龄代码（TeachingSeniority）", required = false)
    private String teachingAgeCode;   // 教龄代码

    @ApiModelProperty(value = "创建人", required = false)
    private Long creator;           // 创建人

    @ApiModelProperty(value = "更新时间开始", required = false)
    private String updateTimeStart;     // 更新时间开始

    @ApiModelProperty(value = "更新时间结束", required = false)
    private String updateTimeEnd;       // 更新时间结束

    @ApiModelProperty(value = "学校ID")
    private Long schoolId;
}
