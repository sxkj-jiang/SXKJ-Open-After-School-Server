package com.sxkj.edu.domain.base.down;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 *@Description : 编辑/查看 论文管理页面VO
 *@Author : JIANGJJ
 *@Date : 2024-4-30 12:20
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Data
public class TDownloadUptAo {

    /**
     * 编号
     */
    @NotBlank(message = "id不能为空")
    @ApiModelProperty(value = "id",required = true)
    private Long id;


    /**
     * 是否展示 0:不显示，1：显示
     */
    @NotBlank(message = "是否展示不能为空")
    @ApiModelProperty("是否展示")
    private String display;
}
