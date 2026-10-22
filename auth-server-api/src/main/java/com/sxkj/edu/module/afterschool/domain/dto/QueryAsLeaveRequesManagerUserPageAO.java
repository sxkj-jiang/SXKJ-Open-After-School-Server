package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel("管理端分页查询请假")
public class QueryAsLeaveRequesManagerUserPageAO extends PageQuery {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学校ID",required = true)
    @NotNull(message = "学校不能为空")
    private Long schoolId;

    @ApiModelProperty("请假学生ID")
    private Long studentId;

    @ApiModelProperty("请假学生姓名")
    private String studentName;

    @ApiModelProperty("请假学生学号")
    private String studentNo;

    @ApiModelProperty("请假类型 1病假 2事假 3其他'")
    private Integer leaveType;

    @ApiModelProperty("请假开始日期,yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty("请假结束日期,yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty("自然班级编号")
    private Long classId;

    @ApiModelProperty("年级编号")
    private Long gradeId;

    @ApiModelProperty("学期ID，未传时默认使用学校默认学期")
    private Long termId;

}
