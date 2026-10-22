package com.sxkj.edu.domain.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description: BaseFilePageAo
 * @Author yanghui
 * @Date 2024年11月22日
 */
@Data
public class BaseFilePageAo extends PageQuery {

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("菜单名称")
    private String menuType;

    @ApiModelProperty("开始时间 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("结束时间 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
