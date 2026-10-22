package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 后勤安全检查记录新增对象
 *
 * @author system
 * @date 2026-06-08
 */
@Data
@ApiModel("后勤安全检查记录新增对象")
public class AsLogisticsSafetyRecordAddAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @NotNull(message = "课次ID不能为空")
    @ApiModelProperty(value = "课次ID", required = true)
    private Long sessionId;

    @NotNull(message = "课程班ID不能为空")
    @ApiModelProperty(value = "课程班ID", required = true)
    private Long courseClassId;

    @NotNull(message = "授课教师ID不能为空")
    @ApiModelProperty(value = "授课教师ID", required = true)
    private Long teacherId;

    @ApiModelProperty("上课地点")
    private String placeName;
}
