package com.sxkj.edu.domain.mult.ao.found;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TPortalSchoolContactUptAO {

    @ApiModelProperty(value = "编号，更新时必传",required = true)
    private Long id;

    @ApiModelProperty(value = "学校ID",required = true)
    @NotNull(message = "请选择学校")
    private Long schoolId;

    @ApiModelProperty(value = "联系人",required = true)
    @NotBlank(message = "联系人不能为空")
    private String contactName;

    @ApiModelProperty(value = "联系电话",required = true)
    @NotBlank(message = "联系电话不能为空")
    private String contactMobile;

    @ApiModelProperty(value = "联系邮箱",required = true)
    @NotBlank(message = "联系邮箱不能为空")
    private String contactEmail;

    @ApiModelProperty(value = "联系地址",required = true)
    @NotBlank(message = "联系地址不能为空")
    private String contactAddress;

    @ApiModelProperty(value = "附件地址编号")
    private Long photoUrl;

    @ApiModelProperty(value = "详细内容,富文本")
    private String introduction;

    @ApiModelProperty(value = "排序,数值越大越靠前")
    private Integer sort;
}
