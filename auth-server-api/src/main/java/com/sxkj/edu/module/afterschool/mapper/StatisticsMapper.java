package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.module.afterschool.domain.statistics.ao.*;
import com.sxkj.edu.module.afterschool.domain.statistics.vo.CourseAttendanceWarningPageVO;
import com.sxkj.edu.module.afterschool.domain.statistics.vo.LessonStatisticsStudentPageVO;
import com.sxkj.edu.module.afterschool.domain.statistics.vo.LessonStatisticsTeacherPageVO;
import com.sxkj.edu.module.afterschool.domain.statistics.vo.NoticeArticleItemVO;
import com.sxkj.edu.module.afterschool.domain.statistics.vo.StudentAttendanceTrendItemVO;
import com.sxkj.edu.module.afterschool.domain.statistics.vo.StudentDetailSummaryVO;
import com.sxkj.edu.module.afterschool.domain.statistics.vo.StudentLessonDistributionItemVO;
import com.sxkj.edu.module.afterschool.domain.statistics.vo.StudentLessonRecordPageVO;
import com.sxkj.edu.module.afterschool.domain.statistics.vo.StudentMonthlyAttendanceVO;
import com.sxkj.edu.module.afterschool.domain.statistics.vo.TeacherDdlDetailPageVO;
import com.sxkj.edu.module.afterschool.domain.statistics.vo.TeacherLessonRecordPageVO;
import com.sxkj.edu.module.afterschool.domain.statistics.vo.TeacherLessonTrendItemVO;
import com.sxkj.edu.module.afterschool.domain.statistics.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 统计报表Mapper接口
 *
 * @author system
 * @date 2026-06-09
 */
@Mapper
@Component
public interface StatisticsMapper {

    /**
     * 教师课时统计-分页查询
     */
    Page<LessonStatisticsTeacherPageVO> pageTeacherLessonStatistics(Page<LessonStatisticsTeacherPageVO> page, @Param("query") LessonStatisticsTeacherAO query
    );

    /**
     * 学生课时统计-分页查询（原）
     */
    Page<LessonStatisticsStudentPageVO> pageStudentLessonStatistics(Page<LessonStatisticsStudentPageVO> page, @Param("query") LessonStatisticsStudentAO query);

    /**
     * 学生课时统计-分页查询
     */
    Page<LessonStatisticsStudentPageVO> pageStudentLessonNewStatistics(Page<LessonStatisticsStudentPageVO> page, @Param("query") LessonStatisticsStudentAO query);

    /**
     * 学生课时统计-批量查当页学生详情
     */
    List<Map<String, Object>> batchStudentLessonStatisticsDetail(@Param("studentIds") List<Long> studentIds);

    /**
     * 教师课时详情-查询月度课时趋势
     */
    List<TeacherLessonTrendItemVO> listTeacherMonthlyLessonTrend(@Param("teacherId") Long teacherId, @Param("termId") Long termId);

    /**
     * 教师课时详情-详细课时记录分页查询
     */
    Page<TeacherLessonRecordPageVO> pageTeacherLessonRecord(Page<TeacherLessonRecordPageVO> page, @Param("query") TeacherLessonRecordAO query);

    /**
     * 根据课次ID查询学生总人数
     */
    Integer countStudentsBySessionId(@Param("sessionId") Long sessionId);

    /**
     * 根据课次ID查询出勤学生数
     */
    Integer countAttendedStudentsBySessionId(@Param("sessionId") Long sessionId);

    /**
     * 学生课时详情-详细课时记录分页查询
     */
    Page<StudentLessonRecordPageVO> pageStudentLessonRecord(Page<StudentLessonRecordPageVO> page, @Param("query") StudentLessonRecordAO query);

    /**
     * 批量查询课次的学生出勤统计（一次查询返回总人数和出勤人数）
     */
    List<Map<String, Object>> batchSessionAttendanceStats(@Param("sessionIds") List<Long> sessionIds);

    /**
     * 学生课时详情-关联查询学生基本信息+班级+学期+课时汇总（一次查询）
     */
    StudentDetailSummaryVO getStudentDetailSummary(@Param("studentId") Long studentId, @Param("termId") Long termId);

    /**
     * 学生课时详情-月度出勤趋势（按月分组，返回每月出勤课时和总课时）
     */
    List<StudentMonthlyAttendanceVO> listStudentMonthlyAttendanceTrend(@Param("query") LessonStatisticsStudentDetailAO ao);

    /**
     * 学生课时详情-课时分布（按课程班分组，返回每个课程班的课时数）
     */
    List<StudentLessonDistributionItemVO> listStudentLessonDistribution(@Param("query") LessonStatisticsStudentDetailAO ao);

    /**
     * 教师统计-出勤率分布：统计学期内今日及之前的课次总数、已出勤课时、调课课时
     */
    Map<String, Object> countTeacherCqlStatistics(@Param("termId") Long termId);

    /**
     * 教师统计-月度出勤率趋势：按月分组，返回每月出勤次数和课次总数
     */
    List<Map<String, Object>> listTeacherMonthlyAttendanceRate(@Param("termId") Long termId);

    /**
     * 学校汇总统计-核心指标（学生数、教师数、班级数、课程数、出勤率）
     */
    Map<String, Object> getSchoolSummaryStatistics(@Param("schoolId") Long schoolId, @Param("termId") Long termId);

    /**
     * 学校汇总统计-统计全部学校数量（超级管理员用）
     */
    Integer countAllSchools();

    /**
     * 学校汇总统计-查询通知公告前N条（按创建时间倒序）
     */
    List<NoticeArticleItemVO> listTopNotices(@Param("schoolId") Long schoolId, @Param("articleTypeId") Long articleTypeId, @Param("limit") int limit);

    /**
     * 教师统计-课程类型分布
     */
    List<StatisticsCommonCountVO>  listTeacherKclxData(@Param("schoolId") Long schoolId,@Param("termId") Long termId);

    /**
     * 教师统计-月度课时趋势：按年月分组统计课次数量
     */
    List<Map<String, Object>> listMonthlyLessonCount(@Param("schoolId") Long schoolId,@Param("termId") Long termId, @Param("startDate") java.util.Date startDate, @Param("endDate") java.util.Date endDate);

    /**
     * 教师统计-评价分数段分布：按5个分数段分组统计数量
     */
    Map<String, Object> countTeacherPjfsData(@Param("schoolId") Long schoolId, @Param("termId") Long termId);


    /**
     * 学生统计-考勤分布：按attendance_status分组统计数量
     */
    List<Map<String, Object>> countStudentKqfbData(@Param("schoolId") Long schoolId,
                                                   @Param("termId") Long termId,
                                                   @Param("kqfbSessionStartDate") Date kqfbSessionStartDate,
                                                   @Param("kqfbSessionEndDate") Date kqfbSessionEndDate);

    /**
     * 学生统计-特色课程分布：按年级分组统计报名人数
     */
    List<Map<String, Object>> countStudentTskcData(@Param("schoolId") Long schoolId,
                                                   @Param("tskcfbTermId") Long tskcfbTermId);

    /**
     * 学生统计-行政课程分布：按年级分组统计报名人数（enroll_source=3）
     */
    List<Map<String, Object>> countStudentXzkcData(@Param("schoolId") Long schoolId,
                                                   @Param("xzkcfbTermId") Long xzkcfbTermId);

    /**
     * 学生统计-学生选课分布：按报名来源分组统计已审核通过的学生数量
     */
    List<StatisticsCommonCountVO> countStudentXsxkData(@Param("schoolId") Long schoolId,
                                                   @Param("xsxkTermId") Long xsxkTermId);

    /**
     * 学生统计-月度趋势
     */
    List<Map<String, Object>> countStudentYdqsData(@Param("schoolId") Long schoolId,@Param("ydqsTermId") Long ydqsTermId,@Param("stuIds") List<Long> stuIds);

    /**
     * 学生统计-性别分布：按性别分组统计学生数量，可选按年级过滤
     */
    List<Map<String, Object>> countStudentXbfbData(@Param("schoolId") Long schoolId,
                                                   @Param("termId") Long termId,
                                                   @Param("xbfbGradeId") Long xbfbGradeId);

    /**
     * 学生统计-兴趣发展雷达图：按课程类别分组统计报名人数
     */
    List<Map<String, Object>> countStudentXqfzLdtData(@Param("schoolId") Long schoolId,
                                                      @Param("xqfzTermId") Long xqfzTermId);

    /**
     * 学生统计-兴趣发展最热门课程：报名人数前2的课程班名称
     */
    List<Map<String, Object>> countStudentXqfzHotCourse(@Param("schoolId") Long schoolId,
                                                        @Param("xqfzTermId") Long xqfzTermId);

    /**
     * 学生统计-兴趣发展参与度最高年级：报名人数最多的年级
     */
    Map<String, Object> countStudentXqfzHotGrade(@Param("schoolId") Long schoolId,
                                                  @Param("xqfzTermId") Long xqfzTermId);

    /**
     * 学生统计-兴趣发展课后服务参与度：统计学期下报名学生数和在籍学生总数
     */
    Map<String, Object> countStudentXqfzParticipation(@Param("schoolId") Long schoolId,
                                                      @Param("xqfzTermId") Long xqfzTermId);


    /**
     * 课程出勤统计-按周和年级统计出勤率
     */
    List<WeekGradeAttendanceVO> countCourseAttendanceByWeekAndGrade(@Param("schoolId") Long schoolId, 
                                                                    @Param("termId") Long termId,
                                                                    @Param("startDate") java.util.Date startDate,
                                                                    @Param("endDate") java.util.Date endDate);


    /**
     *  教师课时详情-课时分布
     */
    List<StatisticsCommonCountVO> listTeacherLessonKlxData(@Param("schoolId")Long schoolId,@Param("termId") Long termId, @Param("teacherId") Long teacherId);

    /**
     *  教师课时详情-查询教师课时数据
     */
    Map<String, Object> countTeacherLessonHours(@Param("teacherId") Long teacherId, @Param("termId") Long termId, @Param("schoolId")Long schoolId);

    /**
     * 学生画像-分页查询
     */
    Page<StudentProfilePageVO> pageStudentProfile(Page<StudentProfilePageVO> page, @Param("query") StudentProfilePageAO query);

    /**
     * 学生画像-根据学生ID集合查询出勤率与综合评分
     */
    Map<String, Object> getStudentAttendanceAndScore(@Param("stuIds") List<Long> stuIds);

    /**
     * 学生画像-根据学生ID集合查询报名课程数据
     */
    List<StudentProfileDetailCourseVO> listStudentProfileCourses(@Param("stuIds") List<Long> stuIds);

    /**
     * 学生画像-根据学生ID集合查询报名课程按类别分组统计
     */
    List<StatisticsCommonCountVO> listStudentProfileCourseCategoryData(@Param("stuIds") List<Long> stuIds);

    /**
     * 学生画像-根据学生ID集合查询出勤情况统计
     */
    Map<String, Object> getStudentAttendanceStats(@Param("stuIds") List<Long> stuIds);

    /**
     * 学生画像-根据学生ID集合查询教师评价列表
     */
    List<StudentProfileTeacherEvaluateVO> listStudentProfileTeacherEvaluates(@Param("stuIds") List<Long> stuIds);

}
