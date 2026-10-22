package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 课程分布详情-课程分页列表VO
 */
@Data
@ApiModel("课程分布详情-课程分页列表VO")
public class CourseClassDetailPageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("所属类别（例如：特色课程/艺术类）")
    private String categoryPath;

    @ApiModelProperty("适用年级ID集合JSON（内部使用）")
    private String applicableGradeIds;

    @ApiModelProperty("适用年级名称，多个用分号连接")
    private String gradeNames;

    @ApiModelProperty("授课教师名称")
    private String teacherName;

    @ApiModelProperty("上课时间集合")
    private List<String> sessionTimes;
}
