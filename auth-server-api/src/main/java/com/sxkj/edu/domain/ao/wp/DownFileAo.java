package com.sxkj.edu.domain.ao.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DownFileAo {


    @ApiModelProperty(value = "下载文件路径集合",required = true)
    @NotNull(message = "下载文件路径集合不能为空")
    private List<String> filePathList;


}
