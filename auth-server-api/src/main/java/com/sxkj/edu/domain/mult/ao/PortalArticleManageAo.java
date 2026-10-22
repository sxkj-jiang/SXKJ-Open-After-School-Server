package com.sxkj.edu.domain.mult.ao;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PortalArticleManageAo extends PageQuery {

    @ApiModelProperty(value = "文章类型编号，1-校园新闻，2-通知公告，3-工作动态，4-首页轮播，5-广告",required = true)
    @NotNull(message = "文章类型编号不能为空")
    private Long id;

    @ApiModelProperty(value = "学校id")
    private Long schoolId;

}
