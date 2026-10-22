package com.sxkj.edu.domain.mult.ao.found;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TPortalFoundOrgPageAO extends PageQuery {


    @ApiModelProperty(value = "学校id")
    private Long schoolId;
}
