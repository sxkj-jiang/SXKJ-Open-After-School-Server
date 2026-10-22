package com.sxkj.edu.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BaseExamRecordQueryAo {

    @ApiModelProperty(value = "审核记录编码，教师课程移交-KCB_TRANSFER",required = true)
    @NotBlank(message = "审核记录编码")
    private String examType;

    @ApiModelProperty(value = "审核编号", required = true)
    @NotNull(message = "审核编号不为空")
    private Long originId;


}
