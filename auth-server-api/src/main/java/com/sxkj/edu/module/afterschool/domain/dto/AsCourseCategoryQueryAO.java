package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 课后类别分页查询参数
 *
 * @author system
 * @date 2026-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("课后类别分页查询参数")
public class AsCourseCategoryQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("所属学校ID")
    private Long schoolId;

    @ApiModelProperty("类别名称(模糊)")
    private String categoryName;

    @ApiModelProperty("状态(1启用 0停用)")
    private Integer enabledStatus;
}
