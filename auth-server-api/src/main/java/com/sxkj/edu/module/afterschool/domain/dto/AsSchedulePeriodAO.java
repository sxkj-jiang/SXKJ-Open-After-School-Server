package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * 作息节次子对象 AO
 *
 * @author YH
 * @date 2026-05-22
 */
@Data
@ApiModel("作息节次参数")
public class AsSchedulePeriodAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("节次ID(编辑时有值)")
    private Long id;

    @ApiModelProperty(value = "时间段：1上午 2下午", required = true)
    @NotNull(message = "时间段不能为空")
    private Integer timeBlock;

    @ApiModelProperty(value = "第几节就传几（数字）", required = true)
    @NotNull(message = "节次编号不能为空")
    private Integer sectionNo;

    @ApiModelProperty(value = "节次名称（例如：“第一节”）", required = true)
    @NotBlank(message = "节次名称不能为空")
    private String sectionName;

    @ApiModelProperty(value = "开始时间", required = true)
    @NotNull(message = "开始时间不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm")
    private Date startTime;

    @ApiModelProperty(value = "结束时间", required = true)
    @NotNull(message = "结束时间不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm")
    private Date endTime;


}
