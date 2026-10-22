package com.sxkj.edu.domain.sys.dto;

import com.sxkj.edu.common.base.domain.PageParamDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class EmailQueryDTO extends PageParamDTO {


    @ApiModelProperty("开始日期")
    private String startDate;

    @ApiModelProperty("结束日期")
    private String endDate;


    @ApiModelProperty("标题")
    private String title;


    @ApiModelProperty("发送状态 0未发送 1已发送")
    private Integer sendStatus;

}
