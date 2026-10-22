package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 教师端邀请课程分页查询
 */
@Data
@ApiModel("教师端邀请课程分页查询")
public class AsTeacherInviteCoursePageQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课后服务ID")
    private Long serviceId;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("课程名称关键词")
    private String keyword;

    @ApiModelProperty("邀请状态：pending待邀请 / invited已邀请")
    private String inviteCourseStatus;
}
