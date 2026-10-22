package com.sxkj.edu.domain.vo.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShareFileVO {

    @ApiModelProperty("批次号")
    private String shareBatchNum;

    @ApiModelProperty("提取编码")
    private String extractionCode;

    @ApiModelProperty("文件地址")
    private String shareUrl;

    
}
