package com.sxkj.edu.domain.base.mult.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ResearchUnitVO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("调查id")
    private Long researchId;

    @ApiModelProperty("关联单位/角色id")
    private Long relatedId;

    @ApiModelProperty("关联单位/角色名称")
    private String relatedName;

    @ApiModelProperty("调查对象关联类型 1-单位 2-角色")
    private Integer type;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("状态 0-未填写 1-已填写")
    private Integer status;
}
