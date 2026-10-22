package com.sxkj.edu.domain.mult.ao.found;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description: TPortalArticleNewsAO
 * @Author yanghui
 * @Date 2024年08月09日
 */
@Data
public class TPortalFoundIntrDisableUptAO {

    @ApiModelProperty(value = "主键id，更新时必填",required = true)
    @NotNull(message = "编号不能为空")
    private Long id;

    @ApiModelProperty(value = "展示状态1-是，其他否,默认为1",required = true)
    @NotNull(message = "展示状态不能为空")
    private Integer isShow;

}
