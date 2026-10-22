package com.sxkj.edu.module.afterschool.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AsStudentQueryListAO {

    @ApiModelProperty(value = "学校ID",required = true)
    @NotNull(message = "请选择学校")
    private Long schoolId;

    @ApiModelProperty("学期ID")
    private Long termId;
}
