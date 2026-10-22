package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("任课老师端评价学生任务列表VO")
public class TeacherEvaluationTaskListVO {

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("任务ID")
    private Long id;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("评价开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("评价结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("任务状态：0-未发布 1-进行中 2-已结束")
    private Integer taskStatus;

    @ApiModelProperty("状态描述")
    private String statusDesc;

    @ApiModelProperty("已评价学生数")
    private Integer evaluatedCount;

    @ApiModelProperty("应评价学生总数")
    private Integer totalCount;

    @ApiModelProperty("是否可以评价")
    private Boolean canEvaluate;

    @ApiModelProperty("操作按钮文案")
    private String actionText;
}
