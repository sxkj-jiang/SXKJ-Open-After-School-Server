package com.sxkj.edu.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PlateOperateFrequencyStatisticsVo {

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("菜单编码")
    private String pageCode;

    @ApiModelProperty("菜单访问次数")
    private Integer menuVisitCount;
}
