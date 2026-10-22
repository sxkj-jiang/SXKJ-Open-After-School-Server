package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("教师巡课检查项")
public class TeacherCheckItemVO  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 检查项名称
     */
    @NotBlank(message = "检查项名称不能为空")
    @ApiModelProperty(value = "检查项名称", required = true)
    private String itemName;

    /**
     * 检查项选项列表
     */
    @ApiModelProperty("检查项选项列表")
    private List<String> options;

    @NotBlank(message = "用户选择不能为空")
    @ApiModelProperty(value = "用户选择", required = true)
    private String userChoose;
}
