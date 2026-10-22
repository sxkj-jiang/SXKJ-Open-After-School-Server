package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 班主任课后报名分页查询对象。
 * <p>支持按学校、学期、课后服务、Tab类型、关键词、年级、班级等多维度筛选。</p>
 *
 * @author wangyouhuai
 * @date 2026-06-12
 */
@Data
@ApiModel("班主任课后报名分页查询对象")
public class AsHeadteacherEnrollmentPageQueryAO extends PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学校ID，不传时取当前登录学校或教师档案学校")
    private Long schoolId;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("课后服务ID")
    private Long serviceId;

    @ApiModelProperty("Tab类型：all全部 registered已报名 assigned已分配 unassigned未分配")
    private String tabType;

    @ApiModelProperty("学号/姓名关键词")
    private String keyword;

    @ApiModelProperty("年级ID")
    private Long gradeId;

    @ApiModelProperty("班级ID")
    private Long classId;
}
