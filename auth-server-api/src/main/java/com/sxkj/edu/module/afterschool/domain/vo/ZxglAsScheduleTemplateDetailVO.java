package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ZxglAsScheduleTemplateDetailVO {

    @ApiModelProperty("方案ID")
    private Long id;

    @ApiModelProperty("所属学校ID")
    private Long schoolId;

    @ApiModelProperty("学校名称")
    private String schoolName;

    @ApiModelProperty("方案名称")
    private String templateName;

    @ApiModelProperty("启用状态：1启用 0停用")
    private Integer enabledStatus;

    @ApiModelProperty("节次列表")
    private List<AsSchedulePeriodVO> periodList;
}
