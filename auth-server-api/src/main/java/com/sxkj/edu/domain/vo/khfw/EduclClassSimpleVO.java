package com.sxkj.edu.domain.vo.khfw;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("班级简要视图对象")
public class EduclClassSimpleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("班级名称")
    private String className;

    @ApiModelProperty("班级编号")
    private String classCode;
}
