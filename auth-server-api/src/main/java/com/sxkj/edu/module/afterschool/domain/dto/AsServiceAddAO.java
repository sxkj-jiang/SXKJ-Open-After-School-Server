package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
  * 课后服务活动AO - 用于新增/更新服务活动
 *
 * @author system
 * @date 2026-05-17
 */
@Data
public class AsServiceAddAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "服务ID，更新时必填")
    private Long id;

    @ApiModelProperty(value = "服务名称", required = true)
    @NotBlank(message = "服务名称不能为空")
    private String serviceName;

    @ApiModelProperty(value = "服务编码")
    private String serviceCode;

    @ApiModelProperty(value = "服务类型：1-作业辅导，2-兴趣课程，3-体育活动，4-艺术活动")
    @NotNull(message = "服务类型不能为空")
    private Integer serviceType;

    @ApiModelProperty(value = "服务描述")
    private String description;

    @ApiModelProperty(value = "适用年级ID列表")
    private String gradeIds;

    @ApiModelProperty(value = "最大人数")
    private Integer maxStudents;

    @ApiModelProperty(value = "收费标准")
    private BigDecimal fee;

    @ApiModelProperty(value = "授课教师ID")
    private Long teacherId;

    @ApiModelProperty(value = "上课地点")
    private String location;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "状态：0-未开始，1-进行中，2-已结束")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;
}
