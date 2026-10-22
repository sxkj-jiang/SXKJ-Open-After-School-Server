package com.sxkj.edu.domain.base.webShow.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 *
 * @author zhengqn
 * @since 2022-12-16
 */
@Data
public class WebShowDeleteReq {

    @ApiModelProperty("列表主键id")
    @NotNull(message = "id不能为空")
    private Long id;

}
