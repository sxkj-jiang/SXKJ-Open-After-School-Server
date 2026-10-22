package com.sxkj.edu.domain.base.mult.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ResearchVO {
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("问卷名称")
    private String surveyName;

    @ApiModelProperty("问卷描述")
    private String surveyDesc;

    @ApiModelProperty("调查对象类型 1-单位 2-角色 3-所有")
    private Integer surveyType;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("创建人id")
    private Long createUser;

    @ApiModelProperty("修改人id")
    private Long updateUser;

    @ApiModelProperty("创建人单位id")
    private Long unitId;

    @ApiModelProperty("创建人单位名称")
    private String unitName;

    @ApiModelProperty("关联单位数组")
    private List<ResearchUnitVO> researchUnitVOList;

    @ApiModelProperty("发起状态 0-未发起 1-已发起")
    private Integer publishStatus;

    @ApiModelProperty("填写状态 0-未填写 1-已填写")
    private Integer writeStatus;
}
