package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 家长端选课首页初始化信息
 *
 * @author system
 * @date 2026-05-21
 */
@Data
@ApiModel("家长端选课首页初始化信息")
public class AsGuardianEnrollmentHomeOptionsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("当前学生ID")
    private Long studentId;

    @ApiModelProperty("当前学生跨学期稳定身份ID")
    private Long studentIdentityId;

    @ApiModelProperty("课后服务ID")
    private Long serviceId;

    @ApiModelProperty("课后服务名称")
    private String serviceName;

    @ApiModelProperty("业务所属学期ID")
    private Long termId;

    @ApiModelProperty("业务所属学期名称")
    private String termName;

    @ApiModelProperty("报名开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date enrollStartTime;

    @ApiModelProperty("报名截止时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date enrollEndTime;

    @ApiModelProperty("报名截止展示文案")
    private String deadlineText;

    @ApiModelProperty("是否存在可展示公开选课")
    private Boolean publicAvailable;

    @ApiModelProperty("是否存在待处理邀请选课")
    private Boolean inviteAvailable;

    @ApiModelProperty("默认选中Tab：public/invite")
    private String selectedTab;

    @ApiModelProperty("是否需要先选择课后服务")
    private Boolean serviceSelectionRequired = false;

    @ApiModelProperty("当前学生的课后服务")
    private List<ServiceOption> serviceOptions = new ArrayList<>();

    @ApiModelProperty("课程类别筛选项")
    private List<CategoryOption> categories = new ArrayList<>();

    @ApiModelProperty("当前家长绑定学生")
    private List<AsGuardianStudentOptionVO> studentOptions = new ArrayList<>();

    @Data
    public static class CategoryOption implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long categoryId;
        private String categoryName;
        private Integer courseCount;
    }

    /**
     * 家长端可报名课后服务选项。
     */
    @Data
    public static class ServiceOption implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("课后服务ID")
        private Long serviceId;

        @ApiModelProperty("课后服务名称")
        private String serviceName;

        @ApiModelProperty("业务所属学期ID")
        private Long termId;

        @ApiModelProperty("业务所属学期名称")
        private String termName;

        @ApiModelProperty("该服务学期对应的学生ID")
        private Long studentId;

        @ApiModelProperty("学生跨学期稳定身份ID")
        private Long studentIdentityId;

        @ApiModelProperty("报名开始时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date enrollStartTime;

        @ApiModelProperty("报名截止时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date enrollEndTime;

        @ApiModelProperty("报名状态：not_started-未到报名时间, enrolling-报名中, ended-报名已结束")
        private String enrollStatus;
    }
}
