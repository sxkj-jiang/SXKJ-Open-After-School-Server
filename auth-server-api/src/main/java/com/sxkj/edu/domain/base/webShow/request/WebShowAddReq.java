package com.sxkj.edu.domain.base.webShow.request;

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
public class WebShowAddReq implements Serializable {

    @ApiModelProperty(value = "文本内容")
    @NotBlank(message = "内容不能为空")
    private String content;

    @ApiModelProperty(value = "类型  0-操作指南 1-学校介绍 2-来校路线", required = true)
    @NotNull(message = "类型不能为空")
    private Integer type;

}
