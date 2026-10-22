package com.sxkj.edu.domain.mult.ao.found;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TPortalFoundPubPageAO extends PageQuery {


    @ApiModelProperty(value = "学校id",required = true)
    @NotNull(message = "学校不能为空")
    private Long schoolId;

    @ApiModelProperty(value = "信息公开类型：1-收入清单，2-支出清单、3-其他")
    private Integer pubType;

    @ApiModelProperty(value = "是否显示，1-显示，0不显示,默认为1")
    private Integer isShow;

}
