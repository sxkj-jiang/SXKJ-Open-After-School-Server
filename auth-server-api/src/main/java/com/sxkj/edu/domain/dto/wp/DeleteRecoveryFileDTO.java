package com.sxkj.edu.domain.dto.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DeleteRecoveryFileDTO {

    @ApiModelProperty(value = "删除文件id",required = true)
    @NotNull(message = "删除文件id集合不能为空")
    private List<Long> fileIdList;

}
