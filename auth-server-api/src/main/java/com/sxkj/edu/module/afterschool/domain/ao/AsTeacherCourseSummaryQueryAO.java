package com.sxkj.edu.module.afterschool.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 教师课程汇总查询AO
 *
 * @author system
 * @date 2026-06-11
 */
@Data
public class AsTeacherCourseSummaryQueryAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @ApiModelProperty("学期ID，不传时使用学校默认学期")
    private Long termId;
}
