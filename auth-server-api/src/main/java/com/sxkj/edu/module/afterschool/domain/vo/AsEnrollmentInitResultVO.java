package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生选课初始化结果
 *
 * @author system
 * @date 2026-05-20
 */
@Data
@ApiModel("学生选课初始化结果")
public class AsEnrollmentInitResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("服务ID")
    private Long serviceId;

    @ApiModelProperty("新建数量")
    private Integer createdCount;

    @ApiModelProperty("已存在数量")
    private Integer existsCount;

    @ApiModelProperty("初始化时间")
    private Date initTime;
}
