package com.sxkj.edu.domain.mult.ao.found;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TPortalFoundIntrQueryAO {


    @ApiModelProperty(value = "学校id")
    private Long schoolId;

    @ApiModelProperty(value = "分类编号")
    private Long articleTypeId;
}
