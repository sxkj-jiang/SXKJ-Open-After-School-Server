package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("待办任务视图对象")
public class AsTodoTaskVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("任务ID")
    private Long id;
    @ApiModelProperty("学校ID")
    private Long schoolId;
    @ApiModelProperty("学校名称")
    private String schoolName;
    @ApiModelProperty("任务标题")
    private String taskTitle;
    @ApiModelProperty("任务内容")
    private String taskContent;
    @ApiModelProperty("任务类型")
    private Integer taskType;
    @ApiModelProperty("任务类型描述")
    private String taskTypeDesc;
    @ApiModelProperty("关联业务ID")
    private Long businessId;
    @ApiModelProperty("接收人用户ID")
    private Long receiverUserId;
    @ApiModelProperty("接收人姓名")
    private String receiverName;
    @ApiModelProperty("任务状态")
    private Integer taskStatus;
    @ApiModelProperty("任务状态描述")
    private String taskStatusDesc;
    @ApiModelProperty("截止时间")
    private Date deadline;
    @ApiModelProperty("处理时间")
    private Date handleTime;
    @ApiModelProperty("处理备注")
    private String handleRemark;
    @ApiModelProperty("创建时间")
    private Date createTime;
}
