package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 班主任查询名下自然班级学生考勤统计汇总参数
 *
 * @author system
 * @date 2026-06-01
 */
@Data
@ApiModel("班主任查询名下自然班级学生考勤统计汇总参数")
public class AsTeacherLeaderAttendanceStatsQueryAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学校ID")
    private Long schoolId;

    @ApiModelProperty(value = "班主任ID")
    private Long headTeacherId;

    @ApiModelProperty("学期ID，未传时默认使用学校默认学期")
    private Long termId;

    @ApiModelProperty(value = "开始日期，格式：yyyy-MM-dd HH:mm:ss", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startDate;

    @ApiModelProperty(value = "结束日期，格式：yyyy-MM-dd HH:mm:ss", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;
}
