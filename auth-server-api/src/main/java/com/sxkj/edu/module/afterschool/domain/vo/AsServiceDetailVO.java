package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 课后服务聚合详情。
 *
 * @author system
 * @date 2026-05-23
 */
@Data
@ApiModel("课后服务聚合详情")
public class AsServiceDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("服务ID")
    private Long id;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("服务编码")
    private String serviceCode;

    @ApiModelProperty("服务名称")
    private String serviceName;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("报名开始时间")
    private Date enrollStartTime;

    @ApiModelProperty("报名结束时间")
    private Date enrollEndTime;

    @ApiModelProperty("开课日期")
    private Date courseStartDate;

    @ApiModelProperty("结课日期")
    private Date courseEndDate;

    @ApiModelProperty("课后时间方案ID")
    private Long afterSchoolTimeTemplateId;

    @ApiModelProperty("课后时间方案名称")
    private String afterSchoolTimeTemplateName;

    @ApiModelProperty("发布时间")
    private Date publishTime;

    @ApiModelProperty("服务状态")
    private Integer serviceStatus;

    @ApiModelProperty("服务状态文案")
    private String serviceStatusName;

    @ApiModelProperty("服务适用年级ID集合JSON")
    private String gradeScopeJson;

    @ApiModelProperty("服务适用行政班ID集合JSON")
    private String classScopeJson;

    @ApiModelProperty("选课初始化状态")
    private Integer selectionInitStatus;

    @ApiModelProperty("选课初始化时间")
    private Date selectionInitTime;

    @ApiModelProperty("课程班集合")
    private List<AsServiceCourseDetailVO> courses = new ArrayList<>();

    @ApiModelProperty("选课统计")
    private AsServiceEnrollmentStatVO enrollmentStat = new AsServiceEnrollmentStatVO();

    @ApiModelProperty("是否可编辑")
    private Boolean canEdit;

    @ApiModelProperty("是否可发布")
    private Boolean canPublish;

    @ApiModelProperty("是否可撤回")
    private Boolean canWithdraw;

    @ApiModelProperty("是否可结束")
    private Boolean canFinish;
}
