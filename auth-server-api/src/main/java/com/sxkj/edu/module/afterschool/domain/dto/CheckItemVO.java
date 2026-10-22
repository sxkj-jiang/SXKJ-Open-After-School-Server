package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * 巡课检查项实体（用于checkItemsJson字段序列化/反序列化）
 *
 * @author system
 * @date 2026-05-26
 */
@Data
@ApiModel("巡课检查项")
public class CheckItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 检查项名称
     */
    @NotBlank(message = "检查项名称不能为空")
    @ApiModelProperty(value = "检查项名称", required = true)
    private String itemName;

    /**
     * 检查项选项列表
     */
    @ApiModelProperty("检查项选项列表")
    private List<String> options;
}
