package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 家长端课程详情
 *
 * @author system
 * @date 2026-05-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("家长端课程详情")
public class AsGuardianCourseDetailVO extends AsGuardianCourseCardVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课后服务名称")
    private String serviceName;

    @ApiModelProperty("课程介绍")
    private String intro;

    @ApiModelProperty("报名截止时间")
    private Date enrollEndTime;

    @ApiModelProperty("被邀请人数")
    private Integer invitedCount;

    @ApiModelProperty("待确认人数")
    private Integer pendingConfirmCount;
}
