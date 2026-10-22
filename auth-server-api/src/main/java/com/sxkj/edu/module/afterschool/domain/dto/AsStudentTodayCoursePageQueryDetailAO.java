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
@ApiModel("学生今日课程查询请求参数")
public class AsStudentTodayCoursePageQueryDetailAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "课次id不能为空")
    @ApiModelProperty(value = "课次id", required = true)
    private Long sessionId;

    @ApiModelProperty(value = "是否当天", required = true)
    @NotNull(message = "是否当天不能为空")
    private Boolean isToday;

    @ApiModelProperty("学生稳定身份ID，家长绑定多个孩子时用于指定学生")
    private Long studentIdentityId;
}
