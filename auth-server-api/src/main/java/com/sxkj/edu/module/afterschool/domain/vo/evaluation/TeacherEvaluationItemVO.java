package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("教师评价列表项VO")
public class TeacherEvaluationItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("答案ID")
    private Long answerId;

    @ApiModelProperty("评价人账号ID")
    private Long evaluatorUserId;

    @ApiModelProperty("教师ID")
    private Long teacherId;

    @ApiModelProperty("教师姓名")
    private String teacherName;

    @ApiModelProperty("职务")
    private String positionName;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("评价状态描述")
    private String submitStatusDesc;

    @ApiModelProperty("评价总分")
    private BigDecimal totalScore;

    @ApiModelProperty("评价文字意见")
    private String commentText;

    @ApiModelProperty("提交日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;
}
