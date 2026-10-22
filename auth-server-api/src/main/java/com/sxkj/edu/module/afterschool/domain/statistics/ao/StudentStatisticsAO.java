package com.sxkj.edu.module.afterschool.domain.statistics.ao;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class StudentStatisticsAO {

    @ApiModelProperty("统一查询学期ID；为空时使用学校默认学期")
    private Long termId;

    @NotNull(message = "类型不能为空")
    @ApiModelProperty(value = "类型：0-全部，1-学生考勤分布，2-月度趋势，3-特色课程分布，4-行政课程分布，5-兴趣发展，6-学生选课分布，7-学生性别分布", required = true)
    private Integer type;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID",required = true)
    private Long schoolId;

    @ApiModelProperty(value = "考勤分布日期-开始")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date kqfbSessionStartDate;

    @ApiModelProperty(value = "考勤分布日期-结束")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date kqfbSessionEndDate;

    @ApiModelProperty(value = "月度趋势-学期ID，兼容旧版入参")
    private Long ydqsTermId;

    @ApiModelProperty(value = "特色课程分布-学期ID，兼容旧版入参")
    private Long tskcfbTermId;

    @ApiModelProperty(value = "行政课程分布-学期ID，兼容旧版入参")
    private Long xzkcfbTermId;

    @ApiModelProperty(value = "兴趣发展-学期ID，兼容旧版入参")
    private Long xqfzTermId;

    @ApiModelProperty(value = "学生性别分布-年级ID",required = true)
    private Long xbfbGradeId;

    @ApiModelProperty(value = "学生选课分布-学期ID",required = true)
    private Long xsxkfTermId;

    @ApiModelProperty(value = "学生id集合")
    private List<Long> stuIds;
}
