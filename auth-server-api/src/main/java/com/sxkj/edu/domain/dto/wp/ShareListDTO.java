package com.sxkj.edu.domain.dto.wp;

import com.sxkj.edu.common.base.domain.PageParamDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShareListDTO extends PageParamDTO {

    @ApiModelProperty("分享文件路径")
    private String shareFilePath;

    @ApiModelProperty("批次号")
    private String shareBatchNum;
}
