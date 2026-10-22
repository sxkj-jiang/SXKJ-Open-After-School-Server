package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("我的评价任务列表VO")
public class MyEvaluationTaskListVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("任务ID")
    private Long id;
    
    @ApiModelProperty("评价任务名称")
    private String taskName;
    
    @ApiModelProperty("评价对象（教师名称）")
    private String targetName;
    
    @ApiModelProperty("截止时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("评价时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    @ApiModelProperty("评分")
    private BigDecimal score;
    
    @ApiModelProperty("状态：1-未评价 2-已评价")
    private Integer status;
    
    @ApiModelProperty("状态描述")
    private String statusDesc;

    @ApiModelProperty("课程班Id")
    private Long courseClassId;

    @ApiModelProperty("课程班名称")
    private String courseClassName;
    
    @ApiModelProperty("课程类别")
    private String courseCategory;
    
    @ApiModelProperty("年级")
    private String grade;
    
    @ApiModelProperty("教师ID")
    private Long teacherId;
    
    @ApiModelProperty("教师名称")
    private String teacherName;

    @ApiModelProperty(value = "SQL层计算的有效任务状态", hidden = true)
    private Integer effectiveTaskStatus;

    @ApiModelProperty(value = "原始任务状态", hidden = true)
    private Integer taskStatus;
    
    @ApiModelProperty("是否可评价")
    private Boolean canEvaluate;
}
