package com.sxkj.edu.domain.dto.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CheckEndTimeDTO {

    @ApiModelProperty("批次号")
    private String shareBatchNum;

}