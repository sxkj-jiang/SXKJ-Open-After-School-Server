package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("课程质量列表VO")
public class CourseQualityListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课程名称")
    private String courseName;

    @ApiModelProperty("课程类别")
    private String categoryName;

    @ApiModelProperty("开课周期")
    private String coursePeriod;

    @ApiModelProperty("教师名称")
    private String teacherName;

    @ApiModelProperty("上课地点")
    private String placeName;

    @ApiModelProperty("总出勤率")
    private String totalAttendanceRate;

    @ApiModelProperty("学生家长评分")
    private BigDecimal parentScore;

    @ApiModelProperty("管理员评分")
    private BigDecimal adminScore;

    @ApiModelProperty("选课类型")
    private String enrollTypeDesc;
}
