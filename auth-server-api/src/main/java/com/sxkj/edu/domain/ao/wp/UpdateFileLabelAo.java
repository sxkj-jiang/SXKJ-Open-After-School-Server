package com.sxkj.edu.domain.ao.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UpdateFileLabelAo {

    @ApiModelProperty(value = "文件id", required = true)
    @NotNull(message = "文件id不能为空")
    private Long id;

    @ApiModelProperty(value = "文件标签id列表", required = true)
    @NotNull(message = "文件标签id列表不能为空")
    private List<Long> labelIds;

}
