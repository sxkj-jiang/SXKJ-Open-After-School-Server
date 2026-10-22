package com.sxkj.edu.module.afterschool.domain.statistics.ao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 教师统计-点到率详情分页查询参数
 *
 * @author system
 * @date 2026-06-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("教师统计-点到率详情分页查询参数")
public class TeacherDdlDetailAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学校ID",required = true)
    private Long schoolId;

    @ApiModelProperty(value = "教师姓名")
    private String teacherName;

    @ApiModelProperty(value = "课程类型ID")
    private Long categoryId;

    @ApiModelProperty(value = "年级ID")
    private Long gradeId;

    @ApiModelProperty("上课日期-开始")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date sessionDateStart;

    @ApiModelProperty("上课日期-结束")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date sessionDateEnd;
}
