package com.sxkj.edu.module.afterschool.domain.statistics.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class StudentProfileDetailAO {

    @NotNull(message = "类型不能为空")
    @ApiModelProperty(value = "类型：0-全部，1-出勤趋势", required = true)
    private Integer type;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @NotNull(message = "学号不能为空")
    @ApiModelProperty(value = "学号", required = true)
    private String studentNo;

    @NotNull(message = "学期id不能为空")
    @ApiModelProperty(value = "（出勤情况）学期ID",required = true)
    private Long termId;

}
