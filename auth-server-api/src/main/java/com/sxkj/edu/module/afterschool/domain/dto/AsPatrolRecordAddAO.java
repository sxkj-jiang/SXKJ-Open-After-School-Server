package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.module.afterschool.domain.vo.TeacherCheckItemVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("巡课记录新增/更新对象")
public class AsPatrolRecordAddAO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("记录ID（编辑时必填）")
    private Long id;

    @NotNull(message = "学校ID不能为空")
    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @NotNull(message = "巡课计划ID不能为空")
    @ApiModelProperty(value = "巡课计划ID", required = true)
    private Long planId;

    @NotNull(message = "课次ID不能为空")
    @ApiModelProperty(value = "课次ID", required = true)
    private Long sessionId;

    @NotNull(message = "课程班ID不能为空")
    @ApiModelProperty(value = "课程班ID", required = true)
    private Long courseClassId;

    @NotNull(message = "巡课人不能为空")
    @ApiModelProperty(value = "巡课人账号ID", required = true)
    private Long patrolUserId;

    @ApiModelProperty("教师提交巡课评分项集合")
    private List<TeacherCheckItemVO> teacherCheckItemVOList;

    @ApiModelProperty("综合评价")
    private String summary;

    @ApiModelProperty("问题整改单列表（有问题时传入）")
    private List<RectificationOrderItemAO> rectificationOrders;
}
