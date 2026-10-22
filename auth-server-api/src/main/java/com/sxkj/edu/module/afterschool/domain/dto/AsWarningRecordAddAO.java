package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("预警记录新增/更新对象")
public class AsWarningRecordAddAO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("预警ID")
    private Long id;
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;
    private Long ruleId;
    @NotNull(message = "学生ID不能为空")
    private Long studentId;
    private String studentNo;
    private Long sessionId;
    @NotNull(message = "预警等级不能为空")
    private Integer warningLevel;
    private Integer warningStatus;
    private Date triggerTime;
    private String warningReason;
}
