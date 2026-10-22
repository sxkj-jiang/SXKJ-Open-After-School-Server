package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 教师今日课程分页查询请求参数
 *
 * @author system
 * @date 2026-05-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("教师今日课程分页查询请求参数")
public class AsTeacherTodayCoursePageQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @ApiModelProperty("学期ID；为空时使用学校默认学期")
    private Long termId;
}
