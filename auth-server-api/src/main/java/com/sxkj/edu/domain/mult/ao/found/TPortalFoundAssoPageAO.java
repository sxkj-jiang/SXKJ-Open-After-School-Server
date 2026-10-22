package com.sxkj.edu.domain.mult.ao.found;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TPortalFoundAssoPageAO extends PageQuery {


    @ApiModelProperty(value = "学校id")
    private Long schoolId;

    @ApiModelProperty(value = "是否显示，1-显示，0不显示,默认为1")
    private Integer isShow;
}
