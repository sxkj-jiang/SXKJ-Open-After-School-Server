package com.sxkj.edu.domain.vo.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FileShareVO {

    @ApiModelProperty("分享主键id")
    private Long id;

    @ApiModelProperty(value = "提取码",required = true)
    private String filePath;

    @ApiModelProperty(value = "分享地址",required = true)
    private String fileUrl;

    @ApiModelProperty(value="批次号",required = true)
    private String shareBatchNum;

}
