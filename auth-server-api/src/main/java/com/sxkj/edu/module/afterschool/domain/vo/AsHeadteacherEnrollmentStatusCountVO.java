package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 班主任课后报名 Tab 数量汇总对象。
 * <p>统计当前班主任管理的班级下，全部/已报名/已分配/未分配四个维度的学生-学期数量。</p>
 *
 * @author wangyouhuai
 * @date 2026-06-12
 */
@Data
@ApiModel("班主任课后报名Tab数量汇总")
public class AsHeadteacherEnrollmentStatusCountVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("全部数量")
    private Long allCount = 0L;

    @ApiModelProperty("已报名数量")
    private Long registeredCount = 0L;

    @ApiModelProperty("已分配数量")
    private Long assignedCount = 0L;

    @ApiModelProperty("未分配数量")
    private Long unassignedCount = 0L;
}
