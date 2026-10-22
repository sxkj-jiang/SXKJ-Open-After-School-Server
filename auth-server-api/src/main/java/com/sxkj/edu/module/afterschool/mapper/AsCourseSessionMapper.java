package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.module.afterschool.domain.ao.AsStudentScheduleQueryAO;
import com.sxkj.edu.module.afterschool.domain.ao.AsTeacherScheduleQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsTeacherTodayCoursePageQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsStudentTodayCoursePageQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsStudentSessionPageQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseSessionEntity;
import com.sxkj.edu.module.afterschool.domain.vo.*;
import com.sxkj.edu.module.afterschool.domain.dto.AsSessionPeriodDTO;
import com.sxkj.edu.module.afterschool.domain.dto.AsSessionStudentDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface AsCourseSessionMapper extends BaseMapper<AsCourseSessionEntity> {

    /**
     * 查询学生的课表数据
     *
     * @param query 查询参数
     * @param weekStartDate 周开始日期
     * @param weekEndDate 周结束日期
     * @param monthStartDate 月开始日期
     * @param monthEndDate 月结束日期
     * @return 课表数据列表
     */
    List<AsStudentScheduleItemVO> queryStudentSchedule(
            @Param("query") AsStudentScheduleQueryAO query,
            @Param("weekStartDate") String weekStartDate,
            @Param("weekEndDate") String weekEndDate,
            @Param("monthStartDate") String monthStartDate,
            @Param("monthEndDate") String monthEndDate,
            @Param("dayDate") String dayDate
    );

    /**
     * 查询教师的课表数据
     *
     * @param query 查询参数
     * @param weekStartDate 周开始日期
     * @param weekEndDate 周结束日期
     * @param monthStartDate 月开始日期
     * @param monthEndDate 月结束日期
     * @return 课表数据列表
     */
    List<AsStudentScheduleItemVO> queryTeacherSchedule(
            @Param("query") AsTeacherScheduleQueryAO query,
            @Param("weekStartDate") String weekStartDate,
            @Param("weekEndDate") String weekEndDate,
            @Param("monthStartDate") String monthStartDate,
            @Param("monthEndDate") String monthEndDate,
            @Param("dayDate") String dayDate
    );

    /**
     * 分页查询教师今日课程
     *
     * @param page 分页参数
     * @param query 查询参数
     * @param teacherId 教师ID
     * @param today 今日日期
     * @return 分页结果
     */
    Page<AsTeacherTodayCourseVO> pageTeacherTodayCourse(
            Page<Object> page,
            @Param("query") AsTeacherTodayCoursePageQueryAO query,
            @Param("teacherId") Long teacherId,
            @Param("today") String today
    );

    /**
     * 查询教师今日课程详情
     *
     * @param sessionId 课次ID
     * @param teacherId 教师ID
     * @return 课程详情
     */
    AsTeacherTodayCourseDetailVO getTeacherTodayCourseDetail(
            @Param("sessionId") Long sessionId,
            @Param("teacherId") Long teacherId
    );

    /**
     * 分页查询学生今日课程
     *
     * @param page 分页参数
     * @param query 查询参数
     * @param studentId 学生ID
     * @param today 今日日期
     * @return 分页结果
     */
    Page<AsStudentTodayCourseVO> pageStudentTodayCourse(
            Page<Object> page,
            @Param("query") AsStudentTodayCoursePageQueryAO query,
            @Param("studentId") Long studentId,
            @Param("today") String today
    );

    AsStudentTodayCourseVO pageStudentTodayCourseDetail(
            @Param("schoolId") Long schoolId,
            @Param("sessionId") Long sessionId,
            @Param("studentId") Long studentId,
            @Param("today") String today
    );

    /**
     * 分页查询学生课次（根据请假时间）
     *
     * @param page 分页参数
     * @param query 查询参数
     * @param studentId 学生ID
     * @return 分页结果
     */
    Page<AsStudentSessionVO> pageStudentSessions(
            Page<Object> page,
            @Param("query") AsStudentSessionPageQueryAO query,
            @Param("studentId") Long studentId
    );

    /**
     * 教师巡课任务-登记详情：查询指定课程班当天的课次列表
     *
     * @param courseClassId 课程班ID
     * @param today 今日日期，格式 yyyy-MM-dd
     * @return 课次列表（sessionId、classTime）
     */
    List<AsPatrolRegistrationDetailVO.SessionItem> listPatrolTodaySessionItems(
            @Param("courseClassId") Long courseClassId,
            @Param("today") String today
    );

    /**
     * 查询当天课次关联的作息时间段列表（含课次ID）
     *
     * @param today 今日日期，格式 yyyy-MM-dd
     * @return 课次作息时间段列表（sessionId、startTime、endTime）
     */
    List<AsSessionPeriodDTO> listTodaySchedulePeriods(@Param("today") String today);

    /**
     * 根据学生ID和课次ID查询学生名称、课程班名称、作息时间段
     *
     * @param studentId 学生ID
     * @param sessionId 课次ID
     * @return 课次学生信息（studentName、courseClassName、startTime、endTime）
     */
    AsSessionStudentDTO getSessionStudentInfo(@Param("studentId") Long studentId, @Param("sessionId") Long sessionId);

    /**
     * 查询指定课次需要参加的学生列表（去向为课程班），含班主任用户ID
     *
     * @param sessionId 课次ID
     * @return 学生列表（studentId、studentName、studentNo、headTeacherUserId）
     */
    List<AsAttendanceStudentVO> getSessionStudents(@Param("sessionId") Long sessionId);

    /**
     * 教师课程反馈-根据课次ID查询学生列表
     */
    List<AsTeacherFeedbackStudentVO> listStudentsBySessionId(@Param("sessionId") Long sessionId);

    /**
     * 根据课次ID和学校ID查询课程班名称
     */
    String getCourseClassNameBySession(@Param("sessionId") Long sessionId, @Param("schoolId") Long schoolId);

    /**
     * 根据课次ID列表查询课程详情
     *
     * @param sessionIds 课次ID列表
     * @return 课程详情列表
     */
    List<AsLeaveRequestManagerQueryCourseDetVO> queryCourseDetBySessionIds(@Param("sessionIds") List<Long> sessionIds);
}
