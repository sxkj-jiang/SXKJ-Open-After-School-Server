package com.sxkj.edu.domain.base.mult;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class TPortalInstructionsUptAo {

    @ApiModelProperty(value = "id，更新时必传")
    private Long id;

    @ApiModelProperty(value = "操作指南名称", required = true)
    @NotBlank(message = "操作指南名称不为空")
    private String operName;

    @ApiModelProperty(value = "操作指南类型，1-操作类，2-体验类，3-其他类", required = true)
    @NotNull(message = "操作指南类型不为空")
    private Integer operType;

    @ApiModelProperty(value = "操作指南面向角色，1-教师，2-学生、3-管理员", required = true)
    @NotNull(message = "操作指南面向角色不为空")
    private Integer operRole;

    @ApiModelProperty(value = "操作指南状态，1-已发布(启用)，2-待发布(禁用)", required = true)
    @NotNull(message = "操作指南状态不为空")
    private Integer operStatus;

    @ApiModelProperty(value = "操作指南具体内容", required = true)
    @NotBlank(message = "操作指南具体内容不为空")
    private String context;

    @ApiModelProperty(value = "操作指南附件地址 ,拼接")
    private String instUrl;

    @ApiModelProperty(value = "操作指南附件集合")
    private List<String> instUrlList;

}
