package com.sxkj.edu.domain.vo.khfw;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("年级简要视图对象")
public class EduclGradeSimpleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("年级名称")
    private String gradeName;

    @ApiModelProperty("年级id")
    private Long gradeId;

    @ApiModelProperty("年级编号")
    private String gradeCode;
}
