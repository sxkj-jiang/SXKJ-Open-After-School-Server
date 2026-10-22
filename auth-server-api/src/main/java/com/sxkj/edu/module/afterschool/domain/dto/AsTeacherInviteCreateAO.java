package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 教师端确认邀请请求
 */
@Data
@ApiModel("教师端确认邀请请求")
public class AsTeacherInviteCreateAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "课程班ID不能为空")
    @ApiModelProperty(value = "邀请目标课程班ID", required = true)
    private Long courseClassId;

    @NotEmpty(message = "至少选择一名学生")
    @ApiModelProperty(value = "选中学生ID列表", required = true)
    private List<Long> studentIds;

    @ApiModelProperty("备注")
    private String remark;
}
