package com.sxkj.edu.domain.base.mult;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description: TPortalIndustryPageAo
 * @Author yanghui
 * @Date 2024年10月25日
 */
@Data
public class TPortalIndustryPageAo extends PageQuery {

    @ApiModelProperty(value = "行业名称", required = true)
    private String industryName;

    @ApiModelProperty(value = "创建时间开始")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeStart;

    @ApiModelProperty(value = "创建时间结束")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;
}
