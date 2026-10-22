package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 小程序-我的订单列表项VO
 *
 * @author system
 * @date 2026-09-09
 */
@Data
@ApiModel("小程序-我的订单列表项")
public class AsAppMyOrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("订单状态：0-待支付 1-已支付 2-已取消 3-超时未支付 4-已退款")
    private Integer status;

    @ApiModelProperty("订单状态展示文案")
    private String statusText;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("订单金额（元）")
    private BigDecimal amount;

    @ApiModelProperty("下单时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
