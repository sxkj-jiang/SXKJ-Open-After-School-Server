package com.sxkj.edu.domain.mult.ao.found;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TPortalFoundPubUptAO {

    @ApiModelProperty(value = "编号，更新时必传",required = true)
    private Long id;

    @ApiModelProperty(value = "学校ID",required = true)
    @NotNull(message = "请选择学校")
    private Long schoolId;

    @ApiModelProperty(value = "基金会编号",required = true)
    @NotNull(message = "基金会编号不能为空")
    private Long intrId;

    @ApiModelProperty(value = "学校基金会信息公开文件名称",required = true)
    @NotBlank(message = "信息公开文件名称不能为空")
    private String pubName;

    @ApiModelProperty(value = "文章类型编号",required = true)
    @NotNull(message = "文章类型不能为空")
    private Long articleTypeId;

    @ApiModelProperty(value = "信息公开类型：1-收入清单，2-支出清单、3-其他",required = true)
    @NotNull(message = "信息公开类型不能为空")
    private Integer pubType;

    @ApiModelProperty(value = "附件地址编号",required = true)
    @NotNull(message = "附件不能为空")
    private Long photoUrl;

    @ApiModelProperty(value = "信息公开介绍,富文本")
    private String introduction;

    @ApiModelProperty(value = "排序,数值越大越靠前")
    private Integer sort;
}
