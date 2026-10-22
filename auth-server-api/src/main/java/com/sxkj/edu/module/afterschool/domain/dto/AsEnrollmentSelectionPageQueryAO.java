package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 学生选课组班分页查询对象
 *
 * @author system
 * @date 2026-05-20
 */
@Data
@ApiModel("学生选课组班分页查询对象")
public class AsEnrollmentSelectionPageQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学校ID，不传时取当前登录学校")
    private Long schoolId;

    @ApiModelProperty(value = "课后服务ID",required = true)
    private Long serviceId;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("报名方式：1公开选课 2邀请选课 3行政班安排")
    private Integer enrollType;

    @ApiModelProperty("学号/姓名关键词")
    private String keyword;

    @ApiModelProperty("年级ID")
    private Long gradeId;

    @ApiModelProperty("班级ID")
    private Long classId;

    @ApiModelProperty("家长端是否包含未分配记录")
    private Boolean includeUnassigned;

    @ApiModelProperty("学期ID")
    private Long termId;
}
