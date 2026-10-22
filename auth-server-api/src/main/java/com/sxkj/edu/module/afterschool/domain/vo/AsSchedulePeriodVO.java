package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 作息节次视图对象
 *
 * @author YH
 * @date 2026-05-22
 */
@Data
@ApiModel("作息节次视图对象")
public class AsSchedulePeriodVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("节次ID")
    private Long id;

    @ApiModelProperty("方案ID")
    private Long templateId;

    @ApiModelProperty("所属学校ID")
    private Long schoolId;

    @ApiModelProperty("时间段：1上午 2下午")
    private Integer timeBlock;

    @ApiModelProperty("第几节 序号")
    private Integer sectionNo;

    @ApiModelProperty("节次名称")
    private String sectionName;

    @ApiModelProperty("开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm:ss")
    private Date endTime;
}
