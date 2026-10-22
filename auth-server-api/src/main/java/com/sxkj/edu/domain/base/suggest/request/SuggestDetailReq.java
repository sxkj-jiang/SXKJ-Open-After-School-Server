package com.sxkj.edu.domain.base.suggest.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 *
 * @author zhengqn
 * @since 2022-12-16
 */
@Data
public class SuggestDetailReq {

    @ApiModelProperty(value = "列表主键id",required = true)
    @NotNull(message = "id不能为空")
    private Long id;

}
