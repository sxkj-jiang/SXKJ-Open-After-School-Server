package com.sxkj.edu.domain.mult.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "SchoolMateExportAO对象", description = "校友导出Excel参数")
public class SchoolMateExportAO {

    @ApiModelProperty(value = "主键集合")
    private List<Long> ids;

}