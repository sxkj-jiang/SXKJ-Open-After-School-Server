package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 课后服务活动VO - 用于返回服务活动详情
 *
 * @author system
 * @date 2026-05-17
 */
@Data
public class AsServiceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("服务ID")
    private Long id;

    @ApiModelProperty("服务名称")
    private String serviceName;

    @ApiModelProperty("服务编码")
    private String serviceCode;

    @ApiModelProperty("服务类型：1-作业辅导，2-兴趣课程，3-体育活动，4-艺术活动")
    private Integer serviceType;

    @ApiModelProperty("服务类型名称")
    private String serviceTypeName;

    @ApiModelProperty("学校ID")
    private Long schoolId;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("报名开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date enrollStartTime;

    @ApiModelProperty("报名结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date enrollEndTime;

    @ApiModelProperty("开课日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date courseStartDate;

    @ApiModelProperty("结课日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date courseEndDate;

    @ApiModelProperty("发布日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishTime;

    @ApiModelProperty("课后时间方案ID")
    private Long afterSchoolTimeTemplateId;

    @ApiModelProperty("课后时间方案名称")
    private String afterSchoolTimeTemplateName;

    @ApiModelProperty("服务状态：0未发布 1报名中 2进行中 3已结束")
    private Integer serviceStatus;

    @ApiModelProperty("服务状态名称")
    private String serviceStatusName;

    @ApiModelProperty("课程班数量")
    private Integer courseClassCount;

    @ApiModelProperty("课次数量")
    private Integer sessionCount;

    @ApiModelProperty("选课初始化状态：0未初始化 1初始化中 2已初始化")
    private Integer selectionInitStatus;

    @ApiModelProperty("服务描述")
    private String description;

    @ApiModelProperty("适用年级ID列表")
    private String gradeIds;

    @ApiModelProperty("最大人数")
    private Integer maxStudents;

    @ApiModelProperty("已报名人数")
    private Integer enrolledCount;

    @ApiModelProperty("收费标准")
    private BigDecimal fee;

    @ApiModelProperty("授课教师ID")
    private Long teacherId;

    @ApiModelProperty("授课教师姓名")
    private String teacherName;

    @ApiModelProperty("上课地点")
    private String location;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("状态：0-未开始，1-进行中，2-已结束")
    private Integer status;

    @ApiModelProperty("状态名称")
    private String statusName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人ID")
    private Long creatorId;

    @ApiModelProperty("创建人姓名")
    private String creatorName;

    @ApiModelProperty("是否允许复制，仅已结束的有效课后服务为是")
    private Boolean canCopy;

}
