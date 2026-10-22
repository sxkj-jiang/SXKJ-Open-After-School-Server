package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 学科新增/编辑参数
 *
 * @author system
 * @date 2026-08-26
 */
@Data
@ApiModel("学科新增/编辑参数")
public class AsSubjectAddAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学科ID(编辑时必填)")
    private Long id;

    @NotBlank(message = "学科编码不能为空")
    @ApiModelProperty(value = "学科编码", required = true)
    private String subjectCode;

    @NotBlank(message = "学科名称不能为空")
    @ApiModelProperty(value = "学科名称", required = true)
    private String subjectName;

    @ApiModelProperty("学科描述")
    private String subjectDesc;

    @NotNull(message = "年级ID不能为空")
    @ApiModelProperty(value = "年级ID", required = true)
    private Long gradeId;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @ApiModelProperty(value = "状态(1启用 0停用)", required = true)
    @NotNull(message = "启用状态不能为空")
    private Integer enabled;
}
