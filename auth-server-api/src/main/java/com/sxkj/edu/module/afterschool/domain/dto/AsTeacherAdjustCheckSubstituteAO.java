package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 代课教师可用性校验参数。
 */
@Data
@ApiModel("代课教师可用性校验参数")
public class AsTeacherAdjustCheckSubstituteAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "代课教师不能为空")
    @ApiModelProperty("代课教师ID")
    private Long substituteTeacherId;

    @NotEmpty(message = "课次不能为空")
    @ApiModelProperty("课次ID集合")
    private List<Long> sessionIds;
}
