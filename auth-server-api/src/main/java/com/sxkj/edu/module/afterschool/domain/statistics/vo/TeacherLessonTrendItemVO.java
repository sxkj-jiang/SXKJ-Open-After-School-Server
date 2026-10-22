package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 课时趋势子项VO
 *
 * @author system
 * @date 2026-06-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("课时趋势子项")
public class TeacherLessonTrendItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("月份（如：2026年1月）")
    private String month;

    @ApiModelProperty("课时数")
    private Integer lessonCount;
}
