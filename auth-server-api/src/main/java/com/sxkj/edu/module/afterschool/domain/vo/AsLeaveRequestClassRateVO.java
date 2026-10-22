package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 班级请假率统计视图对象
 *
 * @author system
 * @date 2026-05-22
 */
@Data
public class AsLeaveRequestClassRateVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("班级ID")
    private Long classId;

    @ApiModelProperty("班级名称")
    private String className;

    @ApiModelProperty("请假人数")
    private Integer leaveStudentCount;

    @ApiModelProperty("班级总人数")
    private Integer totalStudentCount;

    @ApiModelProperty("请假率")
    private String leaveRate;
}
