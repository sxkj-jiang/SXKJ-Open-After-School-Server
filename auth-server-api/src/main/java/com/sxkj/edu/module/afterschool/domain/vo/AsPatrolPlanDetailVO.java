package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.module.afterschool.domain.dto.CheckItemVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 巡课计划详情返回VO
 *
 * @author system
 * @date 2026-05-26
 */
@Data
@ApiModel("巡课计划详情返回VO")
public class AsPatrolPlanDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("计划ID")
    private Long id;

    @ApiModelProperty("所属学校ID")
    private Long schoolId;

    @ApiModelProperty("所属学校名称")
    private String schoolName;

    @ApiModelProperty("计划名称")
    private String planName;

    @ApiModelProperty("巡课类型：1专项巡课 2日常巡课")
    private Integer patrolType;

    @ApiModelProperty("巡课人员教师ID集合JSON")
    private String patrolUserIds;

    @ApiModelProperty("巡课人员名称")
    private String patrolUserNames;

    @ApiModelProperty("巡课范围年级JSON")
    private String patrolScopeGradeJson;

    @ApiModelProperty("巡课范围年级名称")
    private String patrolScopeGradeNames;

    @ApiModelProperty("巡课范围课程类别JSON")
    private String patrolScopeCourseTypeJson;

    @ApiModelProperty("巡课范围课程类别名称")
    private String patrolScopeCourseTypeNames;

    @ApiModelProperty("巡课范围课程班JSON")
    private String patrolScopeCourseClassJson;

    @ApiModelProperty("巡课范围课程班名称")
    private String patrolScopeCourseClassNames;

    @ApiModelProperty("巡课开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date startTime;

    @ApiModelProperty("巡课结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date endTime;

    @ApiModelProperty("计划状态：0草稿 1已提交(发布) 2进行中 3已完成")
    private Integer planStatus;

    @ApiModelProperty("巡课内容")
    private String patrolContent;

    @ApiModelProperty("检查项列表（由checkItemsJson反序列化而来）")
    private List<CheckItemVO> checkItems;

    @ApiModelProperty("检查项列表（JSON结构字符串）")
    private String checkItemsJson;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty(value = "学期名称")
    private String termName;
}
