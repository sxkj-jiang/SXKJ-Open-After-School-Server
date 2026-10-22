package com.sxkj.edu.module.afterschool.domain.ao;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AsTeacherNoticePageAO extends PageQuery {

    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    @NotNull(message = "角色ID不能为空 班主任/任课教师")
    @ApiModelProperty(value = "角色ID", required = true)
    private Long roleId;

    @NotNull(message = "状态不能为空")
    @ApiModelProperty(value = "状态 0-保存草稿 1-发布", required = true)
    private Integer status;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;
}
