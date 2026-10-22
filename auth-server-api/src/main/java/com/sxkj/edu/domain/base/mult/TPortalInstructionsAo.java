package com.sxkj.edu.domain.base.mult;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class TPortalInstructionsAo extends PageQuery {

    @ApiModelProperty(value = "操作指南名称")
    private String operName;

    @ApiModelProperty(value = "操作指南类型,1-操作类，2-体验类，3-其他类")
    private Integer operType;

    @ApiModelProperty(value = "操作指南面向角色，1-教师，2-学生、3-管理员")
    private Integer operRole;

    @ApiModelProperty(value = "更新时间-开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "更新时间-结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;


}
