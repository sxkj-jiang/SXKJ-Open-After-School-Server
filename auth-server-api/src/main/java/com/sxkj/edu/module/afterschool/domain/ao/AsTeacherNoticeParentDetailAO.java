package com.sxkj.edu.module.afterschool.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AsTeacherNoticeParentDetailAO {

    @NotNull(message = "教师通知详情ID不能为空")
    @ApiModelProperty(value = "教师通知详情ID", required = true)
    private Long id;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;
}
