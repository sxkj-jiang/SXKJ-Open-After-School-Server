package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 学期周数/月份下拉选项VO
 *
 * @author system
 * @date 2026-06-09
 */
@Data
@ApiModel("学期周数/月份下拉选项")
public class TermWeekMonthDropdownVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("类型：周数、月份")
    private String type;

    @ApiModelProperty("选项列表")
    private List<String> items;
}
