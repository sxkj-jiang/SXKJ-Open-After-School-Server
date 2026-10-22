package com.sxkj.edu.domain.sys.dto;

import com.sxkj.edu.common.base.domain.PageParamDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class UserOperateLogQueryDTO extends PageParamDTO {


    @ApiModelProperty("开始日期，格式为：yyyy-mm-dd")
    private String startDate;

    @ApiModelProperty("结束日期, 格式为：yyyy-mm-dd")
    private String endDate;


    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("请求结果 0失败 1成功")
    private Integer resultFlag;

}
