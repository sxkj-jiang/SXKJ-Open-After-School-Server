package com.sxkj.edu.module.afterschool.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 家长个人信息详情查询AO
 *
 * @author system
 * @date 2026-06-13
 */
@Data
public class AsParentPersonDetailQueryAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @ApiModelProperty(value = "学生身份ID")
    private Long studentIdentityId;
}
