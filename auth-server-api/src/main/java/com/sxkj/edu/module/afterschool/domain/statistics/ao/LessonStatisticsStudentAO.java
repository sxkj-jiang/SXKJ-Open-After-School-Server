package com.sxkj.edu.module.afterschool.domain.statistics.ao;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 课时统计-学生课时分页查询参数
 *
 * @author system
 * @date 2026-06-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("课时统计-学生课时分页查询参数")
public class LessonStatisticsStudentAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("学生姓名/学号（模糊）")
    private String queryInfo;

    @ApiModelProperty("课程类型ID")
    private Long categoryId;

    @ApiModelProperty("请假/缺勤超过课时数（筛选条件）")
    private Integer minAbsenceHours;

    @ApiModelProperty("学习周期ID（学期ID）")
    private Long termId;
}
