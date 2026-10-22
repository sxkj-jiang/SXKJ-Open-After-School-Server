package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 管理端处理教师调课申请参数。
 */
@Data
@ApiModel("管理端处理教师调课申请参数")
public class AsTeacherAdjustHandleAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "调课申请ID不能为空")
    @ApiModelProperty("调课申请ID")
    private Long requestId;

    @NotNull(message = "处理结果不能为空")
    @ApiModelProperty("处理结果：1同意 2拒绝")
    private Integer handleResult;

    @ApiModelProperty("代课教师ID，申请时未指定代课教师且同意时必填")
    private Long substituteTeacherId;

    @ApiModelProperty("管理员最终批准代课的课次ID集合；为空时默认使用教师原申请课次")
    private List<Long> approvedSessionIds;

    @ApiModelProperty("处理意见")
    private String handleOpinion;
}
