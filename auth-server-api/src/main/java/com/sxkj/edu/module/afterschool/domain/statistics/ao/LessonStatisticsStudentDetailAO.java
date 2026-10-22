package com.sxkj.edu.module.afterschool.domain.statistics.ao;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class LessonStatisticsStudentDetailAO {

    @ApiModelProperty(value = "学校ID",required = true)
    private Long schoolId;

    @ApiModelProperty(value = "学期ID",required = true)
    @NotNull(message = "学期ID不能为空")
    private Long termId;

    @ApiModelProperty(value = "学生ID",required = true)
    private Long studentId;

    @ApiModelProperty(value = "学生稳定身份ID",required = true)
    @NotNull(message = "学生稳定身份ID不能为空")
    private Long studentIdentityId;

    @ApiModelProperty("出勤趋势-上课日期-开始")
    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    private Date cqqsSessionDateStart;

    @ApiModelProperty("出勤趋势-上课日期-结束")
    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    private Date cqqsSessionDateEnd;

    @ApiModelProperty("课时分布-上课日期-开始")
    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    private Date ksfbSessionDateStart;

    @ApiModelProperty("课时分布-上课日期-结束")
    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    private Date ksfbSessionDateEnd;
}
