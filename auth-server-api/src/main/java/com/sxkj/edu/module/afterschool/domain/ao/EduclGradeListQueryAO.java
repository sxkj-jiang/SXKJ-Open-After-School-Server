package com.sxkj.edu.module.afterschool.domain.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("年级列表查询参数")
public class EduclGradeListQueryAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "所属学校ID", required = true)
    @NotNull(message = "所属学校ID不能为空")
    private Long schoolId;
}
