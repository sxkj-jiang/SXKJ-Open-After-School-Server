package com.sxkj.edu.module.afterschool.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("请假申请新增/更新对象")
public class AsLeaveRequestAddSubjectAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课次id", required = true)
    private Long sessionId;

}
