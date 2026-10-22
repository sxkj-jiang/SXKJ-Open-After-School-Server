package com.sxkj.edu.domain.dto.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author jiangjj
 * @version 1.0
 * @description:
 * @date 2023/12/8 19:23
 */
@Data
public class UpdateFileDTO {

    @ApiModelProperty(value = "修改文件id",required = true)
    @NotBlank
    private String userFileId;

    @ApiModelProperty("文件内容")
    private String fileContent;
}
