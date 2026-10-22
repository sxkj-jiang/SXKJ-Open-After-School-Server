package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StudentProfileTeacherEvaluateVO {

    @ApiModelProperty("评价id")
    private String id;

    @ApiModelProperty("教师名称")
    private String teacherName;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty(value = "提交时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    @ApiModelProperty("教师评分")
    private BigDecimal totalScore;

}
