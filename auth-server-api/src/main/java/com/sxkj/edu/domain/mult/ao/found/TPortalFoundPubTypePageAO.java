package com.sxkj.edu.domain.mult.ao.found;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TPortalFoundPubTypePageAO extends PageQuery {


    @ApiModelProperty(value = "学校id",required = true)
    @NotNull(message = "学校不能为空")
    private Long schoolId;

    @ApiModelProperty(value = "信息公开类型：1-收入清单，2-支出清单、3-其他",required = true)
    @NotNull(message = "信息公开类型不能为空")
    private Integer pubType;
}
