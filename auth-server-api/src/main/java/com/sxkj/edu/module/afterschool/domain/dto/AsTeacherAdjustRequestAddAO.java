package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("教师调课申请新增/更新对象")
public class AsTeacherAdjustRequestAddAO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("申请ID")
    private Long id;
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;
    private String requestNo;
    @NotNull(message = "申请人教师ID不能为空")
    private Long applicantTeacherId;
    @NotNull(message = "课程班ID不能为空")
    private Long courseClassId;
    @NotNull(message = "原教师ID不能为空")
    private Long originalTeacherId;
    private Long substituteTeacherId;
    private String originalPlace;
    private String adjustPlace;
    @NotNull(message = "开始日期不能为空")
    private Date adjustStartDate;
    @NotNull(message = "结束日期不能为空")
    private Date adjustEndDate;
    private String adjustSessionsJson;
    private Integer totalSessions;
    @NotBlank(message = "调课原因不能为空")
    private String reason;
    private Integer requestStatus;
}
