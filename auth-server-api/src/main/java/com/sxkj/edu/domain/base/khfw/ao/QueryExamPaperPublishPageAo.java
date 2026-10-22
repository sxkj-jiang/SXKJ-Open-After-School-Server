package com.sxkj.edu.domain.base.khfw.ao;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QueryExamPaperPublishPageAo extends PageQuery {

    @ApiModelProperty("发布状态：0 默认未发布，1 已发布")
    @NotNull(message = "发布状态不能为空")
    private Integer status;
}
