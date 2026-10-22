package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 课程班分页查询VO
 *
 * @author system
 * @date 2026-05-28
 */
@Data
public class AsCourseClassPageVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课程班ID")
    private Long id;

    @ApiModelProperty("教师ID")
    private Long teacherId;

    @ApiModelProperty("课后服务名称")
    private String serviceName;

    @ApiModelProperty("课程班编码")
    private String courseClassCode;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("课程类别名称")
    private String categoryName;
    
    @ApiModelProperty("适用年级ID集合JSON")
    private String applicableGradeIds;

    @ApiModelProperty("适用年级名称，多个用逗号分隔")
    private String applicableGradeNames;

    @ApiModelProperty("报名方式：1公开报名 2邀请报名 3行政班安排")
    private Integer enrollType;

    @ApiModelProperty("报名方式描述")
    private String enrollTypeDesc;

    @ApiModelProperty("开课周期（开课开始日期-开课结束日期）")
    private String courseCycle;

    @ApiModelProperty("教师姓名")
    private String teacherName;

    @ApiModelProperty("上课地点ID")
    private String placeId;

    @ApiModelProperty("上课地点名称")
    private String placeName;

    @ApiModelProperty("学生人数")
    private Integer studentCount;

    @ApiModelProperty("公开课年级限额及占用情况，非公开课为空")
    private AsCourseQuotaVO quota;

    @ApiModelProperty("总出勤率（百分比）")
    private Double attendanceRate;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("学期ID")
    private Long termId;
}
