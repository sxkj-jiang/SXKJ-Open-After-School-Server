package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/** 公开课单个年级的名额配置，不接收客户端人数。 */
@Data
public class AsCourseGradeQuotaAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "适用年级ID", required = true)
    private Long gradeId;

    @ApiModelProperty(value = "年级人数上限，必须为正整数", required = true)
    private Integer capacity;
}
