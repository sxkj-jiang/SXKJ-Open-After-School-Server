package com.sxkj.edu.domain.base.down;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *@Description : 编辑/查看 论文管理页面VO
 *@Author : JIANGJJ
 *@Date : 2024-4-30 12:20
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Data
public class TDownloadReq extends PageQuery {

    @ApiModelProperty(value = "下载名称")
    private String name;

    @ApiModelProperty(value = "发布开始时间")
    private Date startTime;

    @ApiModelProperty(value = "发布结束时间")
    private Date endTime;

}
