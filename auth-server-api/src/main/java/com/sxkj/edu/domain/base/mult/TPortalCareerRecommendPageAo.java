package com.sxkj.edu.domain.base.mult;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description: TPortalCareerRecommendPageAo
 * @Author yanghui
 * @Date 2024年10月29日
 */
@Data
public class TPortalCareerRecommendPageAo extends PageQuery {

    @ApiModelProperty("职业名称/代码")
    private String queryInfo;

    @ApiModelProperty("专业ids")
    private List<String> majorIds;
}
