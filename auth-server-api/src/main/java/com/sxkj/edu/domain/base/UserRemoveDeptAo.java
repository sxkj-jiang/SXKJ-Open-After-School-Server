package com.sxkj.edu.domain.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Getter
@Setter
public class UserRemoveDeptAo {

    @ApiModelProperty(value = "用户id集合", required = true)
    @NotNull(message = "请选择用户")
    private List<Long> userIds;
}
