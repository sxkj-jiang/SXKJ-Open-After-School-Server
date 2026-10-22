package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 学生今日课程分页查询请求参数
 *
 * @author system
 * @date 2026-05-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("学生今日课程分页查询请求参数")
public class AsStudentTodayCoursePageQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @NotNull(message = "家长ID不能为空")
    @ApiModelProperty(value = "家长ID", required = true)
    private Long guardianId;

    @ApiModelProperty("学期ID，不传时使用学校默认学期")
    private Long termId;

    @ApiModelProperty("学生稳定身份ID，家长绑定多个孩子时用于指定学生")
    private Long studentIdentityId;

}
