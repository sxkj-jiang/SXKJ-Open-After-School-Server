package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 学生选课组班列表行对象
 *
 * @author system
 * @date 2026-05-20
 */
@Data
@ApiModel("学生选课组班列表行对象")
public class AsEnrollmentSelectionRowVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("选课记录ID")
    private Long enrollmentId;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("服务ID")
    private Long serviceId;

    @ApiModelProperty("服务名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String serviceName;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("课程类别ID")
    private Long categoryId;

    @ApiModelProperty("课程类别名称")
    private String categoryName;

    @ApiModelProperty("报名方式")
    private Integer enrollType;

    @ApiModelProperty("报名方式名称")
    private String enrollTypeName;

    @ApiModelProperty("报名来源")
    private Integer enrollSource;

    @ApiModelProperty("报名来源名称")
    private String enrollSourceName;

    @ApiModelProperty("报名状态")
    private Integer enrollStatus;

    @ApiModelProperty("报名状态名称")
    private String enrollStatusName;

    @ApiModelProperty("课后去向类型：1课程班 2回家 3培训机构/校外机构 4其他")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer destinationType;

    @ApiModelProperty("课后去向名称")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String destinationName;

    @ApiModelProperty("未报名课程的课后时间摘要，用于未选课组班展示")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String unregisteredScheduleText;

    @ApiModelProperty("未报名课程的课后时间明细")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> unregisteredScheduleItems = new ArrayList<>();

    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("性别：0男 1女")
    private Integer gender;

    @ApiModelProperty("年级ID")
    private Long gradeId;

    @ApiModelProperty("年级名称")
    private String gradeName;

    @ApiModelProperty("班级ID")
    private Long classId;

    @ApiModelProperty("班级名称")
    private String className;

    @ApiModelProperty("操作人")
    private String operatorName;

    @ApiModelProperty("报名/分配时间")
    private Date enrollTime;

    @ApiModelProperty("已学学时（小时）：按学生实际出勤课次的时长累计，保留一位小数")
    private Integer learnedSessions;

    @ApiModelProperty("是否可查看详情")
    private Boolean canDetail;

    @ApiModelProperty("是否可分配")
    private Boolean canAssign;

    @ApiModelProperty("是否可编辑")
    private Boolean canEdit;
}
