package com.sxkj.edu.domain.base.mult;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TPortalInstructionslVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "操作指南id")
    private Long id;

    @ApiModelProperty(value = "操作指南名称")
    private String operName;

    @ApiModelProperty(value = "操作指南类型，1-操作类，2-体验类，3-其他类")
    private Integer operType;

    @ApiModelProperty(value = "操作指南面向角色，1-教师，2-学生、3-管理员")
    private Integer operRole;

    @ApiModelProperty(value = "操作指南状态，1-已发布(启用)，2-待发布(禁用)")
    private Integer operStatus;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


}
