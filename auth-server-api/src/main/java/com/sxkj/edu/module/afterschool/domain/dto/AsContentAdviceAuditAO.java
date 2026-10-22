package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 意见反馈新增/编辑参数
 *
 * @author system
 * @date 2026-06-20
 */
@Data
@ApiModel("意见反馈审核参数")
public class AsContentAdviceAuditAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("意见ID(编辑时必填)")
    private Long id;

    @ApiModelProperty(value = "状态,1-已处理，0-未处理",required = true)
    @NotNull(message = "审核状态不能为空")
    private Integer status;

    @ApiModelProperty(value = "学校id",required = true)
    @NotNull(message = "学校id不能为空")
    private Long schoolId;

    @ApiModelProperty("处理意见")
    private String checkContent;
}
