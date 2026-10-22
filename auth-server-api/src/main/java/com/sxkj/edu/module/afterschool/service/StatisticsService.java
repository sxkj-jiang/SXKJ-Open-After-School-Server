package com.sxkj.edu.module.afterschool.service;

import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.module.afterschool.domain.statistics.ao.*;
import com.sxkj.edu.module.afterschool.domain.statistics.vo.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

public interface StatisticsService {

    /**
     * 教师课时统计-分页查询
     */
    PageResultDTO<LessonStatisticsTeacherPageVO> pageTeacherLessonStatistics(LessonStatisticsTeacherAO ao);

    /**
     * 教师课时统计-批量导出
     */
    void exportTeacherLessonStatistics(LessonStatisticsTeacherAO ao, HttpServletResponse response);

    /**
     * 学生课时统计-分页查询
     */
    PageResultDTO<LessonStatisticsStudentPageVO> pageStudentLessonStatistics(LessonStatisticsStudentAO ao);


    /**
     * 教师课时详情
     */
    LessonStatisticsTeacherDetailVO getTeacherLessonDetail(LessonStatisticsTeacherDetailAO ao);

    /**
     * 教师课时详情-详细课时记录分页查询
     */
    PageResultDTO<TeacherLessonRecordPageVO> pageTeacherLessonRecord(TeacherLessonRecordAO ao);

    /**
     * 学生课时详情
     */
    LessonStatisticsStudentDetailVO getStudentLessonDetail(LessonStatisticsStudentDetailAO ao);

    /**
     * 学生课时详情-详细课时记录分页查询
     */
    PageResultDTO<StudentLessonRecordPageVO> pageStudentLessonRecord(StudentLessonRecordAO ao);


    /**
     * 教师统计
     */
    TeacherStatisticsVO teacherStatistics(TeacherStatisticsAO ao);

    /**
     * 学校汇总统计
     */
    SchoolSummaryStatisticsVO getSchoolSummaryStatistics(SchoolSummaryStatisticsAO ao);



    /**
     * 学生统计
     */
    StudentStatisticsVO studentStatistics(StudentStatisticsAO ao);



    /**
     * 学生画像-分页查询
     */
    PageResultDTO<StudentProfilePageVO> pageStudentProfile(StudentProfilePageAO ao);


    /**
     * 学生画像-详情查询
     */
    StudentProfileDetailVO getStudentProfileDetail( StudentProfileDetailAO ao);
}
