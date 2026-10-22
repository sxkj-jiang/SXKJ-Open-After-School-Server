package com.sxkj.edu.domain.base.khfw.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName RuleCopyNameAO
 * @Description 规则副本名称查询请求
 * @Author backend-architect
 * @Date 2026-02-12
 * @Version 1.0
 **/
@Data
public class RuleCopyNameAO {
    @ApiModelProperty(value = "规则名称前缀", required = true)
    private String name;          // 规则名称前缀
}
