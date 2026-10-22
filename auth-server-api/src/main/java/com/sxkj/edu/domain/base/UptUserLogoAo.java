package com.sxkj.edu.domain.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Getter
@Setter
public class UptUserLogoAo {

    @ApiModelProperty(value = "用户id集合", required = true)
    @NotNull(message = "请选择用户")
    private List<Long> userIds;

    @ApiModelProperty(value = "用户头像地址", required = true)
    @NotBlank(message = "用户头像地址不能为空")
    private String logoUrl;
}
