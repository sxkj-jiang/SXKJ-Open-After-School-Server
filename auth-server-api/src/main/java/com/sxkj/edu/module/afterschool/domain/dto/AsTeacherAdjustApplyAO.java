package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 任课老师提交调课申请参数。
 */
@Data
@ApiModel("任课老师提交调课申请参数")
public class AsTeacherAdjustApplyAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @NotNull(message = "课程班ID不能为空")
    @ApiModelProperty(value = "课程班ID",required = true)
    private Long courseClassId;

    @NotEmpty(message = "调课课次不能为空")
    @ApiModelProperty(value = "调课课次ID集合",required = true)
    private List<Long> sessionIds;

    @ApiModelProperty("代课教师ID，可为空，由管理端后续分配")
    private Long substituteTeacherId;

    @ApiModelProperty("调整后地点")
    private String adjustPlace;

    @NotBlank(message = "调课原因不能为空")
    @ApiModelProperty(value = "调课原因",required = true)
    private String reason;

    @ApiModelProperty("附件文件ID集合")
    private List<Long> attachmentFileIds;

//    @ApiModelProperty("调课开始日期")
//    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
//    private Date adjustStartDate;
//
//    @ApiModelProperty("调课结束日期")
//    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
//    private Date adjustEndDate;
}
