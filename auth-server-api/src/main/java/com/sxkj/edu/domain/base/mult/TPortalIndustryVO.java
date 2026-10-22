package com.sxkj.edu.domain.base.mult;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:
 * @Author YZY
 * @Date 2024年07月08日
 */
@Data
public class TPortalIndustryVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("行业名称")
    private String industryName;

    @ApiModelProperty("行业代码")
    private String code;

    @ApiModelProperty("描述")
    private String describeNotes;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "排序")
    private Integer sort;
}
