package com.sxkj.edu.domain.base.mult;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author YZY
 * @Date 2024年07月08日
 */
@Data
public class TPortalIndustryAndCareerVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Long industryId;

    @ApiModelProperty("行业名称")
    private String industryName;

    @ApiModelProperty("职业id")
    private Long careerId;

    @ApiModelProperty("职业名称")
    private String careerName;

    @ApiModelProperty(value = "行业排序")
    private Integer industrySort;

    @ApiModelProperty(value = "职业排序")
    private Integer careerSort;

    @ApiModelProperty("专业ids")
    private String majorIds;
}
