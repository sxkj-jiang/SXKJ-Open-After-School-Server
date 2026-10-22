package com.sxkj.edu.domain.vo.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class RestoreFileDTO {

    @ApiModelProperty(value = "文件id集合",required = true)
    @NotEmpty(message = "文件id集合不能为空")
    private List<Long> idList;
}
