package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 班主任端-学生调课-提交调课申请参数
 *
 * @author wangyouhuai
 * @date 2026-06-12
 */
@Data
@ApiModel("班主任-提交调课申请参数")
public class AsHeadteacherStudentAdjustSubmitAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "学生ID不能为空")
    @ApiModelProperty(value = "学生ID", required = true)
    private Long studentId;

    @ApiModelProperty("当前课程班ID（原去向为课程班时必填）")
    private Long fromCourseClassId;

    @ApiModelProperty("报名记录ID")
    private Long enrollmentId;

    @ApiModelProperty("课后服务ID（原去向为回家/校外机构时可选，未传时按目标课程所属服务推导）")
    private Long serviceId;

    @ApiModelProperty("原去向类型：1课程班 2回家 3校外机构/培训机构")
    private Integer fromDestinationType;

    @ApiModelProperty("目标去向类型：1课程班 2回家 3校外机构/培训机构")
    private Integer destinationType;

    @ApiModelProperty("目标课程班ID（目标去向为课程班时必填）")
    private Long toCourseClassId;

    @ApiModelProperty("调课原因")
    private String reason;
}
