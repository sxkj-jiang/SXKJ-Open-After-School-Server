package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 教师档案分页查询 AO
 *
 * @author system
 * @date 2026-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("教师档案分页查询参数")
public class AsTeacherQueryAO extends PageQuery {

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("教师姓名（模糊）")
    private String teacherName;

    @ApiModelProperty("手机号（模糊）")
    private String phone;

    @ApiModelProperty("学校阶段")
    private String schoolStage;

    @ApiModelProperty("任职状态：1在职 2离职")
    private Integer employeeStatus;

    @ApiModelProperty("状态：0待注册 1已通过 2已驳回 3待审核")
    private Integer registerStatus;

    @ApiModelProperty("用户状态：0待审核 1已通过 2已驳回")
    private Integer userStatus;

    @ApiModelProperty("学科ID")
    private Long subjectId;
}
