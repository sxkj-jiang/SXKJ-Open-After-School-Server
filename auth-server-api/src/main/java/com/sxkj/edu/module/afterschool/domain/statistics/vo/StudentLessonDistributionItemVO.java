package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 学生课时分布子项VO
 *
 * @author system
 * @date 2026-06-10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("学生课时分布子项")
public class StudentLessonDistributionItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("课时数")
    private Integer lessonCount;
}
