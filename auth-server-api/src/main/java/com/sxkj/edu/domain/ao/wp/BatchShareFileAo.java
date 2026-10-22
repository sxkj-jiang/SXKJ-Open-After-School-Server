package com.sxkj.edu.domain.ao.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BatchShareFileAo {

    @ApiModelProperty(value = "文件id",required = true)
    @NotNull
    private List<Long> idList;

}
