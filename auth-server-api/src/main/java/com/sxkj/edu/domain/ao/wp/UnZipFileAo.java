package com.sxkj.edu.domain.ao.wp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(value = "默认解压路径为当前路径")
public class UnZipFileAo {

    @ApiModelProperty(value = "文件id集合",required = true)
    @NotNull(message = "文件id集合不能为空")
    private List<Long> idList;


}
