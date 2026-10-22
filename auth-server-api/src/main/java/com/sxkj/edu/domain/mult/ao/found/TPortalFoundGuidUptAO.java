package com.sxkj.edu.domain.mult.ao.found;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TPortalFoundGuidUptAO {

    @ApiModelProperty(value = "编号，更新时必传",required = true)
    private Long id;

    @ApiModelProperty(value = "学校ID",required = true)
    @NotNull(message = "请选择学校")
    private Long schoolId;

    @ApiModelProperty(value = "基金会编号",required = true)
    @NotNull(message = "基金会编号不能为空")
    private Long intrId;

    @ApiModelProperty(value = "学校基金会捐赠指南标题")
    private String guidName;

    @ApiModelProperty(value = "附件地址编号")
    private Long photoUrl;

    @ApiModelProperty(value = "基金会捐赠指南介绍,富文本",required = true)
    @NotBlank(message = "基金会捐赠指南介绍不能为空")
    private String introduction;

    @ApiModelProperty(value = "排序,数值越大越靠前")
    private Integer sort;
}
