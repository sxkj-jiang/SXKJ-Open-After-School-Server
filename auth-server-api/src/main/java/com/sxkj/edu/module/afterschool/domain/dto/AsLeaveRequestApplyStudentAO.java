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
@ApiModel("请假申请请假人查询")
public class AsLeaveRequestApplyStudentAO implements Serializable {

    @ApiModelProperty(value = "学校ID", required = true)
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @ApiModelProperty("学期ID，未传时默认使用学校默认学期")
    private Long termId;

    @ApiModelProperty(value = "学生身份ID")
    private Long studentIdentityId;

}
