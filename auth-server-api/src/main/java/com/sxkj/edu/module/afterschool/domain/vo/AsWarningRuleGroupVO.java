package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 预警规则-按预警类型分组返回VO
 *
 * @author yh
 * @date 2026-06-11
 */
@Data
@ApiModel("预警规则-按预警类型分组返回VO")
public class AsWarningRuleGroupVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("预警类型：1-迟到预警 2-缺勤预警 3-早退预警")
    private Integer warningType;

    @ApiModelProperty("该类型下的预警配置数据集合")
    private List<AsWarningRuleVO> ruleList;
}
