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
 * 教师课时详情-详细课时记录分页查询参数
 *
 * @author system
 * @date 2026-06-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("教师课时详情-详细课时记录分页查询参数")
public class TeacherLessonRecordAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学校ID", required = true)
    private Long schoolId;

    @ApiModelProperty(value = "教师ID", required = true)
    private Long teacherId;

    @ApiModelProperty(value = "学习周期ID（学期ID）", required = true)
    private Long termId;

    @ApiModelProperty("上课日期-开始")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date sessionDateStart;

    @ApiModelProperty("上课日期-结束")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date sessionDateEnd;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("年级ID")
    private Long gradeId;
}
