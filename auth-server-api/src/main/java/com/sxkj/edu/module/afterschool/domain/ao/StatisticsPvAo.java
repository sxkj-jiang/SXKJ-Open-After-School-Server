package com.sxkj.edu.module.afterschool.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description: StatisticsPvRequest
 * @Author yanghui
 * @Date 2024年12月11日
 */
@Data
public class StatisticsPvAo {

    @ApiModelProperty(value = "来源类型  平台网页端 PTPC,管理端 JWPC,平台小程序 XCX",required = true)
    @NotBlank(message = "来源类型不能为空")
    private String type;

    @ApiModelProperty(value = "模块",required = true)
    @NotBlank(message = "模块不能为空")
    private String model;

    @ApiModelProperty(value = "页面",required = true)
    @NotBlank(message = "页面不能为空")
    private String sourcePage;

    @ApiModelProperty(value = "页面代码",required = true)
    @NotBlank(message = "页面代码不能为空")
    private String pageCode;

    @ApiModelProperty("用户ID")
    private Long userId;
}
