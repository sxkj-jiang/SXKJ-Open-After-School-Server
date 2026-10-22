package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 选课报名VO - 视图对象
 *
 * @author system
 * @date 2026-05-17
 */
@Data
public class AsEnrollmentVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("报名记录ID")
    private Long id;

    @ApiModelProperty("所属学校ID")
    private Long schoolId;

    @ApiModelProperty("学校名称")
    private String schoolName;

    @ApiModelProperty("服务ID")
    private Long serviceId;

    @ApiModelProperty("服务名称")
    private String serviceName;

    @ApiModelProperty("课程班ID")
    private Long courseClassId;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("学生ID")
    private Long studentId;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("学生学号")
    private String studentNo;

    @ApiModelProperty("报名来源：1家长公开报名 2教师邀请 3行政班安排")
    private Integer enrollSource;

    @ApiModelProperty("报名来源描述")
    private String enrollSourceDesc;

    @ApiModelProperty("报名状态：0待确认 1已报名 2已取消 3已分配 4未分配")
    private Integer enrollStatus;

    @ApiModelProperty("报名状态描述")
    private String enrollStatusDesc;

    @ApiModelProperty("来源邀请批次ID")
    private Long inviteId;

    @ApiModelProperty("是否需要家长确认：0否 1是")
    private Integer parentConfirmRequired;

    @ApiModelProperty("家长确认账号")
    private Long guardianConfirmUserId;

    @ApiModelProperty("家长确认时间")
    private Date guardianConfirmTime;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}
