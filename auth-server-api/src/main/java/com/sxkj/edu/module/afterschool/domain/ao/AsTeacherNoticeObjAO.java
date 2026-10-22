package com.sxkj.edu.module.afterschool.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AsTeacherNoticeObjAO {

    @NotNull(message = "用户id不能为空")
    @ApiModelProperty(value = "用户id", required = true)
    private Long userId;

    @NotNull(message = "学校id不能为空")
    @ApiModelProperty(value = "用户id", required = true)
    private Long schoolId;

    @NotNull(message = "角色id不能为空")
    @ApiModelProperty(value = "角色id 用来区分任课老师和班主任", required = true)
    private Long roleId;
}
