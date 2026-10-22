package com.sxkj.edu.domain.base.webShow.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 * @author zhengqn
 * @since 2022-12-16
 */
@Data
public class WebShowUpdateReq implements Serializable {

    @ApiModelProperty(value = "id")
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "文本内容")
    private String content;

}
