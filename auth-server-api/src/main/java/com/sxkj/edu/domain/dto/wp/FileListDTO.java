package com.sxkj.edu.domain.dto.wp;

import com.sxkj.edu.common.base.domain.PageParamDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FileListDTO extends PageParamDTO {

    @ApiModelProperty(value = "文件路径",required = true)
    private String filePath;

}
