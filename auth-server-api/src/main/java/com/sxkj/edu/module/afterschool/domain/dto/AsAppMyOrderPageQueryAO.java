package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 小程序-我的订单列表分页查询参数
 *
 * @author system
 * @date 2026-09-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("小程序-我的订单列表分页查询参数")
public class AsAppMyOrderPageQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    @ApiModelProperty(value = "订单状态：0-待支付 1-已支付 2-已取消 3-超时未支付（不传查全部）")
    private Integer orderStatus;

    @ApiModelProperty(value = "订单号/商品名称")
    private String keyword;
}
