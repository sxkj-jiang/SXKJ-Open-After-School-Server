package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 考勤编辑入参
 *
 * @author system
 * @date 2026-05-27
 */
@Data
@ApiModel("考勤编辑入参")
public class AsAttendanceUpdateAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "考勤记录ID", required = true)
    @NotNull(message = "考勤记录ID不能为空")
    private Long attendanceId;

    @ApiModelProperty(value = "考勤状态：1-出勤，2-迟到，3-早退，4-缺勤，5-请假", required = true)
    @NotNull(message = "考勤状态不能为空")
    private Integer attendanceStatus;

    @ApiModelProperty("情况说明")
    private String remark;

    @ApiModelProperty("迟到分钟数")
    private Integer lateMinutes;

    @ApiModelProperty("处理方式：1-联系家长,2-学生走错教室,3-教师留堂、4-其他")
    private Integer optionType;
}
