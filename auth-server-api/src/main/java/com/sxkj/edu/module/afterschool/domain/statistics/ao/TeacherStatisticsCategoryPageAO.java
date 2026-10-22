package com.sxkj.edu.module.afterschool.domain.statistics.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TeacherStatisticsCategoryPageAO {

    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @ApiModelProperty(value = "学习周期ID（学期ID）", required = true)
    private Long termId;
}
