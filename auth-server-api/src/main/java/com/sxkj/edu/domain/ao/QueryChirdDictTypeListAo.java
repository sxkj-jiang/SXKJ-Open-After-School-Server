package com.sxkj.edu.domain.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class QueryChirdDictTypeListAo {


    @ApiModelProperty(value = "父类字典代码List",required = true)
    private List<String> parentCodeList;

}
