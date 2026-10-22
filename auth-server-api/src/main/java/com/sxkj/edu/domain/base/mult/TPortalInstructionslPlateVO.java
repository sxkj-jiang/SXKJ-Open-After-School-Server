package com.sxkj.edu.domain.base.mult;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description: TPortalInstructionslPlateVO
 * @Author yanghui
 * @Date 2024年11月27日
 */
@Data
public class TPortalInstructionslPlateVO {

    @ApiModelProperty(value = "操作指南id")
    private Long id;

    @ApiModelProperty(value = "操作指南名称")
    private String operName;

    @ApiModelProperty(value = "操作指南具体内容")
    private String context;

    @ApiModelProperty(value = "发布时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
