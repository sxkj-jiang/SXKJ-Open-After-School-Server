package com.sxkj.edu.module.afterschool.domain.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.sxkj.edu.common.base.domain.PageParamDTO;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("我的评价分页查询对象")
public class MyEvaluationTaskPageAO extends PageQuery {
    
    @ApiModelProperty("课程名称(评价对象)")
    private String courseName;
    
    @ApiModelProperty("课程类别")
    private String courseCategory;
    
    @ApiModelProperty("教师ID")
    private Long teacherId;

    @ApiModelProperty("任务状态：0-未开始 1-进行中 2-已结束")
    private Integer taskStatus;

    @ApiModelProperty("任务状态：1-未评价 2-已评价")
    private Integer taskAnswStatus;

    @ApiModelProperty("评价任务名称")
    private String taskName;

}
