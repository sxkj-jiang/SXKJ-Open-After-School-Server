package com.sxkj.edu.module.afterschool.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 家长查询学生课程考勤统计AO
 *
 * @author system
 * @date 2026-06-11
 */
@Data
public class AsParentCourseAttendanceQueryAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @ApiModelProperty(value = "学期ID（选填）")
    private Long termId;

    @ApiModelProperty("学生稳定身份ID，家长绑定多个孩子时用于指定学生")
    private Long studentIdentityId;
}
