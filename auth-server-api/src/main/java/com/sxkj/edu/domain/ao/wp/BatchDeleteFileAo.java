package com.sxkj.edu.domain.ao.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class BatchDeleteFileAo {

    @ApiModelProperty(value = "文件id集合",required = true)
    @NotEmpty(message = "文件id集合不能为空")
    private List<Long> idList;

    public BatchDeleteFileAo() {}

    public BatchDeleteFileAo(List<Long> idList) {
        this.idList = idList;
    }
}
