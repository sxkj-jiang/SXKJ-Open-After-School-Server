package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 课程班VO - 视图对象
 *
 * @author system
 * @date 2026-05-17
 */
@Data
public class AsCourseClassVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课程班ID")
    private Long id;

    @ApiModelProperty("所属学校ID")
    private Long schoolId;

    @ApiModelProperty("学校名称")
    private String schoolName;

    @ApiModelProperty("服务ID")
    private Long serviceId;

    @ApiModelProperty("服务名称")
    private String serviceName;

    @ApiModelProperty("课程班编码")
    private String courseClassCode;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("课后类别ID")
    private Long categoryId;

    @ApiModelProperty("类别名称")
    private String categoryName;

    @ApiModelProperty("学习模块ID")
    private Long moduleId;

    @ApiModelProperty("模块名称")
    private String moduleName;

    @ApiModelProperty("报名方式：1公开报名 2邀请报名 3行政班安排")
    private Integer enrollType;

    @ApiModelProperty("报名方式描述")
    private String enrollTypeDesc;

    @ApiModelProperty("邀请报名时是否需要家长确认：0否 1是")
    private Integer parentConfirmRequired;

    @ApiModelProperty("公开报名性别要求：0不限 1男 2女")
    private Integer genderRequirement;

    @ApiModelProperty("容量")
    private Integer capacity;

    @ApiModelProperty("公开课年级限额及占用情况，非公开课为空")
    private AsCourseQuotaVO quota;

    @ApiModelProperty("已报名人数")
    private Integer enrolledCount;

    @ApiModelProperty("剩余名额")
    private Integer remainingCount;

    @ApiModelProperty("教师ID")
    private Long teacherId;

    @ApiModelProperty("教师姓名")
    private String teacherName;

    @ApiModelProperty("地点id")
    private String placeId;

    @ApiModelProperty("地点名称")
    private String placeName;

    @ApiModelProperty("课程介绍")
    private String intro;

    @ApiModelProperty("课程班状态：1未开始 2进行中 3已结束 4停用")
    private Integer classStatus;

    @ApiModelProperty("课程班状态描述")
    private String classStatusDesc;

    @ApiModelProperty("总出勤率（百分比，如85.50表示85.5%）")
    private Double attendanceRate;

    @ApiModelProperty("上课时间描述，如：周一 09:00-10:30，周二 14:00-15:30")
    private String scheduleTimeDesc;

    @ApiModelProperty("总课时：根据所有有效未取消课次累计计算")
    private Integer totalHours;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "学期名称")
    private String termName;

    @ApiModelProperty(value = "学期ID")
    private Long termId;
}
