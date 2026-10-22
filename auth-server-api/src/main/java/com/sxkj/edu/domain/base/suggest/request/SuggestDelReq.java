package com.sxkj.edu.domain.base.suggest.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 *
 * @author zhengqn
 * @since 2022-12-16
 */
@Data
public class SuggestDelReq {

    @ApiModelProperty("id集合")
    @NotEmpty(message = "id不能为空")
    private List<Long> ids;

}
