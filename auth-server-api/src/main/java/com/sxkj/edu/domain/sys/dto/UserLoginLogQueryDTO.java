package com.sxkj.edu.domain.sys.dto;

import com.sxkj.edu.common.base.domain.PageParamDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class UserLoginLogQueryDTO extends PageParamDTO {


    @ApiModelProperty("开始日期，格式为：yyyy-mm-dd")
    private String startDate;

    @ApiModelProperty("结束日期, 格式为：yyyy-mm-dd")
    private String endDate;


    @ApiModelProperty("用户名")
    private String userName;

}
