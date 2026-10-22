package com.sxkj.edu.domain.mult.ao.found;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TPortalFoundArtiUptAO {

    @ApiModelProperty(value = "编号，更新时必传",required = true)
    private Long id;

    @ApiModelProperty(value = "学校ID",required = true)
    @NotNull(message = "请选择学校")
    private Long schoolId;

    @ApiModelProperty(value = "学校基金会章程标题")
    private String artiName;

    @ApiModelProperty(value = "附件地址编号")
    private Long photoUrl;

    @ApiModelProperty(value = "学校基金会章程介绍,富文本",required = true)
    @NotBlank(message = "学校基金会章程介绍不能为空")
    private String introduction;
}
