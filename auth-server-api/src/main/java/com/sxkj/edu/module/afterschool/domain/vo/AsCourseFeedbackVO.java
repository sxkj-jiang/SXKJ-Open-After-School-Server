package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 课程班反馈VO
 *
 * @author system
 * @date 2026-05-17
 */
@Data
@ApiModel("课程班反馈视图对象")
public class AsCourseFeedbackVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("反馈ID")
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

    @ApiModelProperty("教师ID")
    private Long teacherId;

    @ApiModelProperty("教师姓名")
    private String teacherName;

    @ApiModelProperty("反馈内容")
    private String feedbackContent;

    @ApiModelProperty("附件文件ID集合JSON")
    private String attachmentIds;

    @ApiModelProperty("反馈时间")
    private Date feedbackTime;

    @ApiModelProperty("反馈人用户ID")
    private Long creatorUserId;

    @ApiModelProperty("反馈人姓名")
    private String creatorName;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}
