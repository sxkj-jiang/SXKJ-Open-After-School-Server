package com.sxkj.edu.domain.mult.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PortalArticleManageListAo {

    @ApiModelProperty(value = "学校id")
    private Long schoolId;
}
