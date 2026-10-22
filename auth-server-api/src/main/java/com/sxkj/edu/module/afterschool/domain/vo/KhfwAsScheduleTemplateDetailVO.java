package com.sxkj.edu.module.afterschool.domain.vo;

import com.sxkj.edu.module.afterschool.domain.dto.AsSchedulePeriodAO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 课后时间管理方案详情视图对象
 *
 * @author YH
 * @date 2026-05-22
 */
@Data
@ApiModel("课后时间管理方案详情视图对象")
public class KhfwAsScheduleTemplateDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("方案ID")
    private Long id;

    @ApiModelProperty("所属学校ID")
    private Long schoolId;

    @ApiModelProperty("学校名称")
    private String schoolName;

    @ApiModelProperty("方案名称")
    private String templateName;

    @ApiModelProperty("方案类型：2课后时间管理")
    private Integer templateType;

    @ApiModelProperty("启用状态：1启用 0停用")
    private Integer enabledStatus;

    @ApiModelProperty("课后时间频率集合")
    private List<String> dayScopeList;

    @ApiModelProperty("适用年级id集合")
    private List<Long> gradeScopeList;

    @ApiModelProperty("节次列表")
    private List<AsSchedulePeriodAO> periodList;
}
