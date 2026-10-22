package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 学生选课操作结果
 *
 * @author system
 * @date 2026-05-20
 */
@Data
@ApiModel("学生选课操作结果")
public class AsEnrollmentOperateResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("选课记录ID")
    private Long enrollmentId;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("报名状态")
    private Integer enrollStatus;

    @ApiModelProperty("报名状态名称")
    private String enrollStatusName;
}
