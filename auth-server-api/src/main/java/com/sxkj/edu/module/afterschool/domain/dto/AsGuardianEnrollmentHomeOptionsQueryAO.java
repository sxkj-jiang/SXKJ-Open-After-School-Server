package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 家长端选课首页初始化查询对象
 *
 * @author system
 * @date 2026-05-21
 */
@Data
@ApiModel("家长端选课首页初始化查询对象")
public class AsGuardianEnrollmentHomeOptionsQueryAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学生学期快照ID，与稳定身份ID至少传一个")
    private Long studentId;

    @ApiModelProperty("学生跨学期稳定身份ID，与学生学期快照ID至少传一个")
    private Long studentIdentityId;

    @ApiModelProperty("课后服务ID，不传时取当前报名中且最近截止的服务")
    private Long serviceId;
}
