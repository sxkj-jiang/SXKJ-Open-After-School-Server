package com.sxkj.edu.domain.mult.ao.found;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TPortalFoundPubQueryAO extends PageQuery {



    @ApiModelProperty(value = "信息公开类型：1-收入清单，2-支出清单、3-其他")
    private Integer pubType;
}
