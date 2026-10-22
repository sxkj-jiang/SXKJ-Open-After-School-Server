package com.sxkj.edu.domain.mult.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: TPortalArticleNewsAO
 * @Author yanghui
 * @Date 2024年08月09日
 */
@Data
public class TPortalArticleNewsDisableUptAO {

    @ApiModelProperty(value = "主键id，更新时必填")
    private Long id;

    @ApiModelProperty(value = "展示状态1-是，其他否,默认为1")
    private Integer isShow;

}
