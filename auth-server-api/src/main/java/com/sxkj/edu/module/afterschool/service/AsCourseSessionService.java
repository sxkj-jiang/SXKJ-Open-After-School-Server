package com.sxkj.edu.module.afterschool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.module.afterschool.domain.ao.AsCourseClassTransferAO;
import com.sxkj.edu.module.afterschool.domain.ao.AsStudentScheduleQueryAO;
import com.sxkj.edu.module.afterschool.domain.ao.AsTeacherScheduleQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsStudentSessionPageQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsStudentTodayCoursePageQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsStudentTodayCoursePageQueryDetailAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsTeacherTodayCoursePageQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseClassEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseSessionEntity;
import com.sxkj.edu.module.afterschool.domain.vo.*;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface AsCourseSessionService extends IService<AsCourseSessionEntity> {



    /**
     * 查询学生课表（按周/月/天）
     *
     * @param ao 查询参数
     * @return 按周分组的课表数据
     */
    List<AsStudentScheduleWeekVO> queryStudentSchedule(AsStudentScheduleQueryAO ao);

    /**
     * 查询教师课表（按周/月/天）
     *
     * @param ao 查询参数
     * @return 按周分组的课表数据
     */
    List<AsStudentScheduleWeekVO> queryTeacherSchedule(AsTeacherScheduleQueryAO ao);

    /**
     * 分页查询教师今日课程
     *
     * @param ao 查询参数
     * @param userTeacherId 教师ID
     * @return 分页结果
     */
    PageResultDTO<AsTeacherTodayCourseVO> pageTeacherTodayCourse(AsTeacherTodayCoursePageQueryAO ao, Long userTeacherId);


    /**
     * 查询教师今日课程详情
     *
     * @param sessionId 课次ID
     * @param userTeacherId 教师用户ID
     * @return 课程详情
     */
    AsTeacherTodayCourseDetailVO getTeacherTodayCourseDetail(Long sessionId, Long userTeacherId);

    /**
     * 分页查询学生今日课程
     *
     * @param ao 查询参数
     * @param studentId 学生ID
     * @return 分页结果
     */
    PageResultDTO<AsStudentTodayCourseVO> pageStudentTodayCourse(AsStudentTodayCoursePageQueryAO ao, Long studentId);


    /**
     * 分页查询学生课次（根据请假时间）
     *
     * @param ao 查询参数
     * @param studentId 学生ID
     * @return 分页结果
     */
     PageResultDTO<AsStudentSessionVO> pageStudentSessions(AsStudentSessionPageQueryAO ao, Long studentId);


    // 教师课程移交
    Boolean transferTeacherSubjectSession(AsCourseClassTransferAO ao);
}
