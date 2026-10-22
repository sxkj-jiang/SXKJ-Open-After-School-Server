package com.sxkj.edu.domain.base.mult;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: TPortalCareerRecommendVo
 * @Author yanghui
 * @Date 2024年10月29日
 */
@Data
public class TPortalCareerRecommendVo {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("职业名称")
    private String name;

    @ApiModelProperty("职业代码")
    private Integer careerCode;

    @ApiModelProperty("关联专业ids")
    private String majorIds;

    @ApiModelProperty("关联专业名称")
    private String majorNames;

    @ApiModelProperty("首页是否推荐展示 0-否 1-是")
    private Integer isHomeRecommend;

    @ApiModelProperty("首页推荐展示排序")
    private Integer homeRecommendSort;

}
