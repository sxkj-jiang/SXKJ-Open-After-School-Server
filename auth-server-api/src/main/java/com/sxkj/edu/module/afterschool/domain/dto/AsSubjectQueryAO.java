package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 学科分页查询参数
 *
 * @author system
 * @date 2026-08-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("学科分页查询参数")
public class AsSubjectQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID",required = true)
    private Long schoolId;

    @ApiModelProperty("年级ID")
    private Long gradeId;

    @ApiModelProperty("学科名称(模糊)")
    private String subjectName;

    @ApiModelProperty("学科编码")
    private String subjectCode;

    @ApiModelProperty("启用状态(1启用 0停用)")
    private Integer enabled;
}
