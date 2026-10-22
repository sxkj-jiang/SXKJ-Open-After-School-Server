package com.sxkj.edu.module.afterschool.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AsCourseClassQueryAO {

    @ApiModelProperty(value = "所属学校ID",required = true)
    private Long schoolId;
}
