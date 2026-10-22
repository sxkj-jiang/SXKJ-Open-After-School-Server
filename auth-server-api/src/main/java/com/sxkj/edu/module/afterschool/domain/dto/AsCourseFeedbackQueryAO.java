package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 课堂表现查询参数
 */
@Data
@ApiModel("课堂表现查询参数")
public class AsCourseFeedbackQueryAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课堂表现反馈记录ID（与课次ID二选一）")
    private Long id;

    @ApiModelProperty("课次ID（课堂表现与反馈记录ID二选一）")
    private Long sessionId;
}
