package com.sxkj.edu.domain.base.suggest.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 * @author zhengqn
 * @since 2022-12-16
 */
@Data
public class SuggestUpdateReq implements Serializable {

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "处理意见",required = true)
    @NotBlank(message = "处理意见不能为空")
    private String checkContent;

    @ApiModelProperty(value = "处理人id", required = true)
    @NotNull(message = "处理人id不能为空")
    private Long authorUpdate;

}
