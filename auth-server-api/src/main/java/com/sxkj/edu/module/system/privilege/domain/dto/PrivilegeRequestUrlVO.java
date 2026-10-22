package com.sxkj.edu.module.system.privilege.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class PrivilegeRequestUrlVO {

    @ApiModelProperty("注释说明")
    private String comment;

    @ApiModelProperty("controller.method")
    private String name;

    @ApiModelProperty("url")
    private String url;
}
