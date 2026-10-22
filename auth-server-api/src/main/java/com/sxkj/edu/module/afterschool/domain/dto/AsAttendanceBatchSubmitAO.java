package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 教师批量提交考勤记录参数
 *
 * @author system
 * @date 2026-05-26
 */
@Data
@ApiModel("教师批量提交考勤记录参数")
public class AsAttendanceBatchSubmitAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学校ID", required = true)
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @ApiModelProperty(value = "教师ID", required = true)
    @NotNull(message = "教师ID不能为空")
    private Long teacherId;

    @ApiModelProperty(value = "课次ID", required = true)
    @NotNull(message = "课次ID不能为空")
    private Long sessionId;

    @ApiModelProperty(value = "上课日期,yyyy-mm-dd", required = true)
    @NotNull(message = "上课日期不能为空")
    private String attendanceDate;

    @ApiModelProperty(value = "考勤明细列表", required = true)
    @NotEmpty(message = "考勤明细列表不能为空")
    private List<AttendanceDetail> attendanceDetails;

    /**
     * 考勤明细
     */
    @Data
    @ApiModel("考勤明细")
    public static class AttendanceDetail implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "学生学号", required = true)
        @NotEmpty(message = "学生学号不能为空")
        private String studentNo;

        @ApiModelProperty(value = "考勤状态：1-出勤，2-迟到，3-早退，4-缺勤，5-请假", required = true)
        @NotNull(message = "考勤状态不能为空")
        private Integer attendanceStatus;

        @ApiModelProperty(value = "去向类型：1课程班 2回家 3培训机构 4留班 5行政课 6其他", required = true)
        @NotNull(message = "去向类型不能为空")
        private Integer destinationType;

        @ApiModelProperty(value = "备注说明")
        private String remark;
    }
}
