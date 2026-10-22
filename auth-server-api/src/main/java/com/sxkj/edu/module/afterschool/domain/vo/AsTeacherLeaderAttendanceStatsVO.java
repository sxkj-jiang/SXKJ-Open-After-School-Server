package com.sxkj.edu.module.afterschool.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 班主任查询名下自然班级学生考勤统计汇总VO
 *
 * @author system
 * @date 2026-06-01
 */
@Data
@ApiModel("班主任查询名下自然班级学生考勤统计汇总VO")
public class AsTeacherLeaderAttendanceStatsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("学期ID")
    private Long termId;

    @ApiModelProperty("学期名称")
    private String termName;

    @ApiModelProperty("班级ID")
    private Long classId;

    @ApiModelProperty("年级ID")
    private Long gradeId;

    @ApiModelProperty("课次的上课日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date sessionDate;

    @ApiModelProperty("第几节课")
    private Integer sectionNo;

    @ApiModelProperty("第几节课名字")
    private String sectionName;

    @ApiModelProperty("每节课开始时间")
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty("每节课结束时间")
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty("自然班级名称")
    private String courseClassName;

    @ApiModelProperty("课程名称")
    private String courseName;

    @ApiModelProperty("学生课后去向统计（根据destination_type汇总）")
    private Map<Integer, Integer> destinationStats;

    @ApiModelProperty("学生考勤统计（根据attendance_status汇总）")
    private Map<Integer, Integer> attendanceStats;

    @ApiModelProperty("学生数量")
    private Integer studentCount;

    @ApiModelProperty("去向类型:1课程班 2回家 3培训机构 4留班 5行政课 6其他")
    private Integer destinationType;

    @ApiModelProperty("考勤状态:1正常/到课 2迟到 3早退 4缺勤 5请假")
    private Integer attendanceStatus;
}
