package com.sxkj.edu.domain.base;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryDictTypeAo extends PageQuery {

    @ApiModelProperty("字典名称")
    private String typeName;

    @ApiModelProperty("字典类型编码")
    private String typeCode;


}
