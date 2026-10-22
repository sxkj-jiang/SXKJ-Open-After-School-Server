package com.sxkj.edu.module.afterschool.domain.ao;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 家长查询学生选课记录分页AO
 *
 * @author system
 * @date 2026-06-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AsParentEnrollmentPageQueryAO extends PageQuery {

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

}
