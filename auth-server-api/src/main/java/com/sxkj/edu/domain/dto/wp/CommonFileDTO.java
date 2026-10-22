package com.sxkj.edu.domain.dto.wp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CommonFileDTO {

    @ApiModelProperty("用户文件id")
    private String userFileId;

    @ApiModelProperty("共享用户id集合")
    private String commonUserList;
}
