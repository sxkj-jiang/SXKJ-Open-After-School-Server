package com.sxkj.edu.module.afterschool.domain.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 学生去向预警-提交处理参数
 */
@Data
@ApiModel("学生去向预警-提交处理参数")
public class AsWarningHandleSubmitAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学生去向预警ID", required = true)
    @NotNull(message = "学生去向预警ID不能为空")
    private Long warningRecordId;

    @ApiModelProperty(value = "学生出勤记录ID",required = true)
    @NotNull(message = "学生出勤记录ID不能为空")
    private Long attendanceRecordId;

    @ApiModelProperty("异常原因描述")
    private String exceptionDesc;

    @ApiModelProperty(value = "处理方式：1课后留堂 2联系家长 3约谈学生 4其他")
    private Integer handleType;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty(value = "考勤状态：1正常/到课 2迟到 3早退 4缺勤 5请假",required = true)
    @NotNull(message = "考勤状态不能为空")
    private Integer attendanceStatus;

    @ApiModelProperty(value = "预计迟到时间（分钟）")
    private Integer lateMinutes;

    @ApiModelProperty("请假原因")
    private String leaveReason;
}
