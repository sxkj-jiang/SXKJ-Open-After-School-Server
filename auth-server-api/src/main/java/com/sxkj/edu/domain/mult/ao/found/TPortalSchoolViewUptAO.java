package com.sxkj.edu.domain.mult.ao.found;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TPortalSchoolViewUptAO {

    @ApiModelProperty(value = "编号，更新时必传",required = true)
    private Long id;

    @ApiModelProperty(value = "学校ID",required = true)
    @NotNull(message = "请选择学校")
    private Long schoolId;

    @ApiModelProperty(value = "相册名称",required = true)
    @NotBlank(message = "相册名称不能为空")
    private String viewName;

    @ApiModelProperty(value = "附件地址编号 多个用,连接",required = true)
    @NotBlank(message = "附件不能为空")
    private String photoUrl;

    @ApiModelProperty(value = "排序,数值越大越靠前")
    private Integer sort;

    @ApiModelProperty(value = "是否显示，1-显示，0不显示,默认为1")
    private Integer isShow;
}
