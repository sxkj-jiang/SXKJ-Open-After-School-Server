package com.sxkj.edu.domain.vo.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryFileLabelCountVO {

    @ApiModelProperty("标签id")
    private Long id;

    @ApiModelProperty("标签名称")
    private String labelName;

    @ApiModelProperty("市州名称")
    private String cityStateName;

    @ApiModelProperty("标签文件总数量")
    private Integer count;

}
