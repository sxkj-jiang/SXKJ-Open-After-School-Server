package com.sxkj.edu.domain.ao.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddFileForCourseSessionAo {

    @ApiModelProperty(value = "课次id", required = true)
    @NotNull(message = "课次id不能为空")
    private Long sessionId;

    @ApiModelProperty(value = "资源id", required = true)
    @NotNull(message = "资源id不能为空")
    private Long resourceId;

    @ApiModelProperty(value = "作者用户id", required = true)
    @NotNull(message = "作者用户id不能为空")
    private Long authorUserId;


}
