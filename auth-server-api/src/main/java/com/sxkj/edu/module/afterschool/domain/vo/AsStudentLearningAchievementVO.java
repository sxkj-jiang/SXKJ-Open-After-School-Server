package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生学习成果 VO
 *
 * @author system
 * @date 2026-06-19
 */
@Data
@ApiModel("学生学习成果")
public class AsStudentLearningAchievementVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("课后服务名称")
    private String serviceName;

    @ApiModelProperty("课程班名称")
    private String courseClassName;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("学生姓名")
    private String studentName;

    @ApiModelProperty("日期（课次日期，格式 yyyy-MM-dd）")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date sessionDate;

    @ApiModelProperty("上课开始时间")
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    private String startTime;

    @ApiModelProperty("上课结束时间")
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    private String endTime;

    @ApiModelProperty("完整课次日期，格式 yyyy-MM-dd hh:mm")
    private String classResTime;

    @ApiModelProperty("班级ID")
    private Long classId;

    @ApiModelProperty("年级ID")
    private Long gradeId;

    @ApiModelProperty("课次ID")
    private Long sessionId;

    @ApiModelProperty("学习打卡id")
    private Long feedbackId;

    @ApiModelProperty("年级名称")
    private String gradeName;

    @ApiModelProperty("班级名称")
    private String className;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("任课老师ID")
    private Long teacherId;

    @ApiModelProperty("任课老师姓名")
    private String teacherName;

    @ApiModelProperty("学生是否打卡：0否 1是")
    private Integer isCheckIn;

    @ApiModelProperty("学生打卡提交日期，格式 yyyy-MM-dd HH:mm：ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date checkInDate;
}
