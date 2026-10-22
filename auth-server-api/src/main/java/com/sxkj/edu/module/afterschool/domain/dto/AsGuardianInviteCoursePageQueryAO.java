package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 家长端邀请选课分页查询对象
 *
 * @author system
 * @date 2026-05-21
 */
@Data
@ApiModel("家长端邀请选课分页查询对象")
public class AsGuardianInviteCoursePageQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学生ID，不传时查询当前家长绑定的全部学生")
    private Long studentId;

    @ApiModelProperty("课后服务ID")
    private Long serviceId;

    @ApiModelProperty("课程类别ID")
    private Long categoryId;

    @ApiModelProperty("课程名称关键词")
    private String keyword;
}
