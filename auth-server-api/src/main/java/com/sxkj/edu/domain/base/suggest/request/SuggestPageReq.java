package com.sxkj.edu.domain.base.suggest.request;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author zhengqn
 * @since 2022-12-16
 */
@Data
public class SuggestPageReq extends PageQuery {

    @ApiModelProperty(value = "反馈人/账号/手机号")
    private String keyword;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "反馈人id", hidden = true)
    private Long creator;

}
