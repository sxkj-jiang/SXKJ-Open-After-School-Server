package com.sxkj.edu.domain.base.mult.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ResearchTemplateVO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("表头名称")
    private String headerName;

    @ApiModelProperty("父级id")
    private Long parentId;

    @ApiModelProperty("计量单位")
    private String measureUnit;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("调查活动id")
    private Long researchId;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("是否必填 1-是 2-否")
    private Integer isNeed;

    @ApiModelProperty("填写值")
    private String writeContent;

    @ApiModelProperty("子集")
    private List<ResearchTemplateVO> child;

}
