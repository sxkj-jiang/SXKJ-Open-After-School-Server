package com.sxkj.edu.domain.base.webShow;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description: OperateInstructionPageAo
 * @Author yanghui
 * @Date 2025年01月17日
 */
@Data
public class OperateInstructionPageAo extends PageQuery {

    @ApiModelProperty(value = "菜单编码")
    private String menuCode;

    @ApiModelProperty(value = "角色类型")
    private Integer roleType;

    @ApiModelProperty(value = "更新开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "更新结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
