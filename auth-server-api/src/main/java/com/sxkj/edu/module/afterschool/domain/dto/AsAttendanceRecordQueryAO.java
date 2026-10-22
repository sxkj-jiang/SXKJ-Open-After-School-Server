package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 教师查询考勤记录参数
 *
 * @author system
 * @date 2026-05-26
 */
@Data
@ApiModel("教师查询考勤记录参数")
public class AsAttendanceRecordQueryAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学校ID",required = true)
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @ApiModelProperty("课次ID")
    private Long sessionId;

    @ApiModelProperty("学期ID，未传课次时默认使用学校默认学期")
    private Long termId;
}
