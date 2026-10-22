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
public class WebShowDetailReq {

    @ApiModelProperty("类型 0-操作指南 1-学校介绍 2-来校路线")
    @NotNull(message = "类型不能为空")
    private Integer type;

}
