 package com.sxkj.edu.module.afterschool.domain.ao.evaluation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

 @Data
 @ApiModel("家长端-评价任务详情AO")
 public class AppAsEvaluationTaskParentQueryAO implements Serializable {

     private static final long serialVersionUID = 1L;

     @ApiModelProperty(value = "评价类型：1-我收到的，2-我评价的",required = true)
     @NotNull(message = "评价类型不能为空")
     private Integer evaluationType;

     @NotNull(message = "任务ID不能为空")
     @ApiModelProperty(value = "任务ID", required = true)
     private Long taskId;

     @ApiModelProperty(value = "课程班ID")
     private Long courseId;

     @ApiModelProperty("学生稳定身份ID，家长绑定多个孩子时用于指定学生")
     private Long studentIdentityId;

 //    @ApiModelProperty(value = "被评价人id:学生角色时被评价人传教师id,教师角色时被评价人传学生id，学校管理评价教师传教师id")
 //    private Long targetId;
 }
