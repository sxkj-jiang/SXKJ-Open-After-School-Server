package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 教师巡课任务分页查询请求参数
 *
 * @author system
 * @date 2026-05-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("教师巡课任务分页查询请求参数")
public class AsTeacherPatrolTaskPageQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "学校id不能为空")
    @ApiModelProperty(value = "学校id",required = true)
    private Long schoolId;

    @NotNull(message = "教师用户id不能为空")
    @ApiModelProperty(value = "教师用户id",required = true)
    private Long teacherUserId;
}
