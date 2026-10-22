package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class AsTodoTaskHomePageVO {

    @ApiModelProperty("任务ID")
    private Long id;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("任务标题")
    private String taskTitle;

    @ApiModelProperty("任务内容")
    private String taskContent;

    @ApiModelProperty("任务状态：0待办 1已完成")
    private Integer taskStatus;

    @ApiModelProperty("待办日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date todoDate;

    @ApiModelProperty("截止日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date deadlineTime;

    @JsonIgnore
    @ApiModelProperty(value = "来源业务类型，仅供后端解析学期", hidden = true)
    private String sourceBizType;

    @JsonIgnore
    @ApiModelProperty(value = "来源业务ID，仅供后端解析学期", hidden = true)
    private Long sourceBizId;

    @ApiModelProperty("业务所属学期ID")
    private Long termId;

    @ApiModelProperty("业务所属学期名称")
    private String termName;

}
