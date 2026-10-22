package com.sxkj.edu.domain.base.khfw.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName DictItemVO
 * @Description 字典项响应
 * @Author backend-architect
 * @Date 2026-01-12
 * @Version 1.0
 **/
@Data
public class DictItemVO {
    @ApiModelProperty(value = "字典代码")
    private String dictCode;     // 字典代码

    @ApiModelProperty(value = "字典名称")
    private String dictName;     // 字典名称
    
    @ApiModelProperty(value = "默认权重值")
    private Integer defaultValue; // 默认权重值
    
    @ApiModelProperty(value = "推荐占比范围")
    private String recommendedRange; // 推荐占比范围
}
