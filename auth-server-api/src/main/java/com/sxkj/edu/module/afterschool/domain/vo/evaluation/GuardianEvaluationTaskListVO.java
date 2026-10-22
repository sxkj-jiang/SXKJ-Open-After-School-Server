package com.sxkj.edu.module.afterschool.domain.vo.evaluation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("家长端评价任务列表VO")
public class GuardianEvaluationTaskListVO {

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("学生学期快照ID")
    private Long studentId;

    @ApiModelProperty("学生稳定身份ID")
    private Long studentIdentityId;

    @ApiModelProperty("任务ID")
    private Long id;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("课程班id")
    private Long courseClassId;

    @ApiModelProperty("课程名称")
    private String courseName;

    @ApiModelProperty("授课教师")
    private String teacherName;

    @ApiModelProperty("授课教师id")
    private Long teacherId;

    @ApiModelProperty("评价开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("评价结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("开课周期开始")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date courseStartTime;

    @ApiModelProperty("开课周期结束")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date courseEndTime;

    @ApiModelProperty("提交评价时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty(value = "SQL层计算的有效状态（修正未开始）", hidden = true)
    private Integer effectiveStatus;

    @ApiModelProperty("状态描述")
    private String statusDesc;

    @ApiModelProperty("是否已评价")
    private Boolean hasEvaluated;

    @ApiModelProperty(value = "SQL层返回的是否已评价标记(0/1)", hidden = true)
    private Integer hasEvaluatedInt;

    @ApiModelProperty("是否可以评价")
    private Boolean canEvaluate;

    @ApiModelProperty("操作按钮文案")
    private String actionText;

    @ApiModelProperty(value = "创建时间", hidden = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
