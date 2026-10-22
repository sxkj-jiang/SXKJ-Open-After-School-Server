package com.sxkj.edu.domain.mult.ao;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description: TPortalArticleNewsAO
 * @Author yanghui
 * @Date 2024年08月09日
 */
@Data
public class TPortalArticleNewsHomeQueryAO extends PageQuery {


    @ApiModelProperty(value = "文章类型：1-新闻/2-公告/3-法律法规/4-其他",required = true)
    @NotNull(message = "文章类型不能为空")
    private Integer newsType;

    @ApiModelProperty(value = "新闻文章名称")
    private String newsName;

    @ApiModelProperty(value = "展示状态1-是，其他否")
    private Integer isShow;

}
