package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 学生调课分页查询参数。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("学生调课分页查询参数")
public class AsStudentAdjustPageQueryAO extends PageQuery {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("关键字：学号/姓名/申请编号")
    private String keyword;

    @ApiModelProperty("课后服务ID")
    private Long serviceId;

    @ApiModelProperty("课程名称，模糊查询")
    private String courseName;

    @ApiModelProperty("课程类别ID")
    private Long categoryId;

    @ApiModelProperty("任课教师ID")
    private Long teacherId;

    @ApiModelProperty("年级ID")
    private Long gradeId;

    @ApiModelProperty("行政班ID")
    private Long classId;

    @ApiModelProperty("班主任ID")
    private Long headTeacherId;

    @ApiModelProperty("原课程班ID")
    private Long fromCourseClassId;

    @ApiModelProperty("目标课程班ID")
    private Long toCourseClassId;

    @ApiModelProperty("申请状态：0待审核 1已通过 2已驳回 3已撤回")
    private Integer requestStatus;

    @ApiModelProperty("申请开始时间")
    private Date applyStartTime;

    @ApiModelProperty("申请结束时间")
    private Date applyEndTime;

    @ApiModelProperty("学期ID")
    private Long termId;
}
