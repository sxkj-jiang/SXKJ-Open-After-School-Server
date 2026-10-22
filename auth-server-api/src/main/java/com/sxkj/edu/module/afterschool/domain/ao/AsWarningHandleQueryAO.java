package com.sxkj.edu.module.afterschool.domain.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 学生去向预警-去处理查询条件
 */
@Data
@ApiModel("学生去向预警-去处理查询条件")
public class AsWarningHandleQueryAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课次ID", required = true)
    @NotNull(message = "课次ID不能为空")
    private Long sessionId;

    @ApiModelProperty(value = "角色id",required = true)
    private Long roleId;

    @ApiModelProperty("行政班ID")
    private Long classId;

    @ApiModelProperty("考勤状态：1正常/到课 2迟到 3早退 4缺勤 5请假")
    private Integer attendanceStatus;

    @ApiModelProperty(value = "班主任id（后台）")
    private Long headTeacherId;
}
