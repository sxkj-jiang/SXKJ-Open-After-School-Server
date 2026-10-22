package com.sxkj.edu.domain.vo.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class SysParamVO {

    @ApiModelProperty("系统参数ID")
    private Long sysParamId;

    @ApiModelProperty("组名")
    private String groupName;

    @ApiModelProperty("名称")
    private String sysParamKey;

    @ApiModelProperty("内容")
    private String sysParamValue;

    @ApiModelProperty("排序")
    private String sysParamDesc;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("修改时间")
    private String updateTime;
}
