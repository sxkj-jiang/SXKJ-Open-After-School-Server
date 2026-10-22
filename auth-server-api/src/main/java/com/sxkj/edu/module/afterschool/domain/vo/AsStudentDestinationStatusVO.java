package com.sxkj.edu.module.afterschool.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@ApiModel("教师查询考勤记录返回VO")
public class AsStudentDestinationStatusVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课次ID")
    private Long sessionId;

    /**
     * 所属学校
     */
    private Long schoolId;

    /**
     * 服务ID
     */
    private Long serviceId;

    /**
     * 学生ID
     */
    private Long studentId;


    /**
     * 学生学号，去向/选课流程展示冗余
     */
    private String studentNo;

    /**
     * 去向类型：1课程班 2回家 3培训机构 4留班 5行政课 6其他
     */
    private Integer destinationType;

    /**
     * 课程班ID
     */
    private Long courseClassId;

    @ApiModelProperty("考勤状态:1正常/到课 2迟到 3早退 4缺勤 5请假")
    private String attendanceStatus;


}
