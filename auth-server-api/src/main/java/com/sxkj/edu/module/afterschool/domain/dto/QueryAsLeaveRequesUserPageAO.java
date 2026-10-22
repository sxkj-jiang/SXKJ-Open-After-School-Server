package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("用户端分页查询请假")
public class QueryAsLeaveRequesUserPageAO extends PageQuery {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("请假学生ID")
    private Long studentId;

    @ApiModelProperty("请假学生学号")
    private String studentNo;

    @ApiModelProperty("请假类型 1病假 2事假 3其他'")
    private Integer leaveType;

    @ApiModelProperty("请假日期，格式yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date leaveDate;

    @ApiModelProperty("班级编号")
    private Long classId;



}
