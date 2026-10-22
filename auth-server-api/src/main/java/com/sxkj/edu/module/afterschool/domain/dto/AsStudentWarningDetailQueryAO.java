package com.sxkj.edu.module.afterschool.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 根据学生查询预警详情请求参数
 *
 * @author system
 * @date 2026-06-05
 */
@Data
@ApiModel("根据学生查询预警详情请求参数")
public class AsStudentWarningDetailQueryAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "学生ID不能为空")
    @ApiModelProperty(value = "学生ID", required = true)
    private Long studentId;

    @ApiModelProperty("课次日期（选填）yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date sessionDate;

    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;
}
