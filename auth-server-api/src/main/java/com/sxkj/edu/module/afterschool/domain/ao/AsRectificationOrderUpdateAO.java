package com.sxkj.edu.module.afterschool.domain.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 授课教师端巡课整改-整改更新请求参数
 *
 * @author system
 * @date 2026-05-29
 */
@Data
@ApiModel("授课教师端巡课整改-整改更新请求参数")
public class AsRectificationOrderUpdateAO {

    @NotNull(message = "整改单ID不能为空")
    @ApiModelProperty(value = "整改单ID", required = true)
    private Long id;

    @NotNull(message = "整改状态不能为空")
    @ApiModelProperty(value = "整改状态：0待整改 1整改中 2已整改 3已完成 4已逾期", required = true)
    private Integer rectifyStatus;

    @ApiModelProperty("整改措施")
    private String rectifyMeasure;

    @ApiModelProperty("整改附件id的JSON")
    private String rectifyFileIds;
}
