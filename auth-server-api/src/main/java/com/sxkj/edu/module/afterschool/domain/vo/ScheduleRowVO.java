package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 课表数据行（按年级+班级）
 */
@Data
public class ScheduleRowVO {

    @ApiModelProperty(value = "年级id")
    private Long gradeId;

    @ApiModelProperty(value = "年级名称")
    private String gradeName;

    @ApiModelProperty(value = "班级id")
    private Long classId;

    @ApiModelProperty(value = "班级名称")
    private String className;

    /**
     * key格式：weekDay_sectionNo，如 "2_8" 表示周二第八节
     * value：该单元格的课程列表（一个时间段可能有多门课）
     */
    @ApiModelProperty(value = "单元格数据，key=weekDay_sectionNo")
    private Map<String, List<ScheduleCellVO>> cellMap;
}
