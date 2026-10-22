package com.sxkj.edu.domain.base;

import com.sxkj.edu.common.validation.UserName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SaveDictItemAo {

    @ApiModelProperty(value = "字典id,更新时必填",required = true)
    private Long id;

    @ApiModelProperty(value = "字典类型名称",required = true)
    @NotBlank(message = "字典名称不能为空")
    @UserName
    private String typeName;

    @ApiModelProperty(value = "字典编码",required = true)
    @NotBlank(message = "字典编码不能为空")
    @Size(min = 4,max = 16)
    private String typeCode;

    @ApiModelProperty(value = "字典值",required = true)
    @NotBlank(message = "字典值不能为空")
    private String typeValue;

    @ApiModelProperty("排序值")
    private Integer sort;

    @ApiModelProperty("状态(1:正常;0:禁用)")
    private Integer status;

    @ApiModelProperty("备注")
    private String remark;

}
