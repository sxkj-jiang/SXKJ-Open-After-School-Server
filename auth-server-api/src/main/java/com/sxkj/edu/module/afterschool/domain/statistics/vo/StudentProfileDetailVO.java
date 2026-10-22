package com.sxkj.edu.module.afterschool.domain.statistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class StudentProfileDetailVO {

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("姓名")
    private String studentName;

    @ApiModelProperty("性别：0-男，1-女")
    private Integer gender;

    @ApiModelProperty("年级名称")
    private String gradeName;

    @ApiModelProperty("班级名称")
    private String className;

    @ApiModelProperty("学生头像")
    private String studentAvatar;

    @ApiModelProperty("出勤率")
    private String attendanceRate;

    @ApiModelProperty("综合评分")
    private BigDecimal comprehensiveScore;

    @ApiModelProperty("参与课程列表")
    private List<StudentProfileDetailCourseVO> courseList;

    @ApiModelProperty("兴趣发展统计")
    private List<StatisticsCommonCountVO> courseCategoryData;

    @ApiModelProperty("出勤情况")
    private StudentProfileDetailAttendanceVO attendanceData;

    @ApiModelProperty("月度趋势集合数据")
    private List<StatisticsCommonCountVO> ydqsData;

    @ApiModelProperty("教师评价列表")
    private List<StudentProfileTeacherEvaluateVO> teacherEvaluateList;

}
