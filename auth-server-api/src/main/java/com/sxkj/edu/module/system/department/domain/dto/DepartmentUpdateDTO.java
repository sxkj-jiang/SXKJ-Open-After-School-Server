package com.sxkj.edu.module.system.department.domain.dto;

import com.sxkj.edu.common.validation.NumberAnno;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
public class DepartmentUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "部门id", required = true)
    @NotNull(message = "部门id不能为空")
    private Long id;

    @ApiModelProperty(value = "部门名称", required = true)
    @Length(min = 2, max = 50, message = "请输入正确的部门名称(1-50个字符)")
    @NotBlank(message = "请输入正确的部门名称(1-50个字符)")
    private String name;

    @ApiModelProperty(value = "部门编码", required = true)
    @NumberAnno
    private String code;

    @ApiModelProperty("部门简称")
    private String shortName;

    @ApiModelProperty("部门负责人id")
    private Long managerId;

    @ApiModelProperty("上级部门id (可选)")
    private Long parentId;

    @ApiModelProperty("部门描述")
    private String description;

    @ApiModelProperty("部门排序")
    private Integer sort;

}
