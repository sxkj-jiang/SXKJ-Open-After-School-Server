package com.sxkj.edu.domain.ao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description: PlateOperatePvStatisticsAo
 * @Author yanghui
 * @Date 2024年12月27日
 */
@Data
public class PlateOperatePvStatisticsAo extends PageQuery {

    @ApiModelProperty("页面代码")
    private String pageCode;

    @ApiModelProperty("访问开始日期 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    @ApiModelProperty("访问结束日期 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
}
