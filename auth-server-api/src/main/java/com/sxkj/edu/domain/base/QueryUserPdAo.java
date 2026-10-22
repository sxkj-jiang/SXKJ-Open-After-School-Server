package com.sxkj.edu.domain.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Getter
@Setter
public class QueryUserPdAo {

    @ApiModelProperty(value = "用户id", required = true)
    @NotNull(message = "请选择用户")
    private Long userId;

    @ApiModelProperty(value = "用户账号")
    private String userName;
}
