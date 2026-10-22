package com.sxkj.edu.module.system.privilege.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "EmpowerDTO", description = "批量授权册次")
public class EmpowerDTO {

    @ApiModelProperty("用户id")
    private List<Long> userIds;
    @ApiModelProperty("册次目录id")
    private List<Long> catalogueIds;

}
