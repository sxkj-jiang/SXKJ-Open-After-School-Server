package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 课后服务复制结果。
 */
@Data
@ApiModel("课后服务复制结果")
public class AsServiceCopyResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("源课后服务编号")
    private Long sourceServiceId;

    @ApiModelProperty("复制后的课后服务编号")
    private Long targetServiceId;

    @ApiModelProperty("复制后的课后服务名称")
    private String targetServiceName;

    @ApiModelProperty("目标学期编号")
    private Long termId;

    @ApiModelProperty("目标学期名称")
    private String termName;

    @ApiModelProperty("课后服务状态")
    private Integer serviceStatus;

    @ApiModelProperty("课后服务状态名称")
    private String serviceStatusName;

    @ApiModelProperty("复制课程班数量")
    private Integer copiedCourseCount;

    @ApiModelProperty("复制排课规则数量")
    private Integer copiedRuleCount;

    @ApiModelProperty("是否需要确认新学期排课配置")
    private Boolean needsScheduleReview;

    @ApiModelProperty("是否为重复请求返回的已有结果")
    private Boolean idempotentReplay;

    @ApiModelProperty("复制校验问题明细")
    private List<AsServiceCopyIssueVO> issues;
}
