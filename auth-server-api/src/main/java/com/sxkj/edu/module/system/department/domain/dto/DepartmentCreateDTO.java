package com.sxkj.edu.module.system.department.domain.dto;

import com.sxkj.edu.common.validation.NumberAnno;
import com.sxkj.edu.common.validation.UserName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class DepartmentCreateDTO {


    @ApiModelProperty(value = "部门名称", required = true)
    @UserName
    @NotBlank(message = "请输入正确的部门名称(2-50个字符)")
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

    @ApiModelProperty("部门排序")
    private Integer sort;

    @ApiModelProperty("部门描述")
    private String description;

    @ApiModelProperty("校友id")
    private Long mateId;

}
