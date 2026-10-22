package com.sxkj.edu.domain.base.webShow;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author zhengqn
 * @since 2022-12-16
 */
@Data
public class HotSearchPageReq extends PageQuery {

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "类型 0-平台公告；1-平台资讯", required = true)
    private Integer type;

}
