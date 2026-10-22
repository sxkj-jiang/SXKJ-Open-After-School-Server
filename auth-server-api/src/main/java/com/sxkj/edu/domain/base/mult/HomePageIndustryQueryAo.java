package com.sxkj.edu.domain.base.mult;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: HomePageIndustryQueryAo
 * @Author yanghui
 * @Date 2024年10月28日
 */
@Data
public class HomePageIndustryQueryAo {

    @ApiModelProperty("行业id")
    private Long industryId;
}
