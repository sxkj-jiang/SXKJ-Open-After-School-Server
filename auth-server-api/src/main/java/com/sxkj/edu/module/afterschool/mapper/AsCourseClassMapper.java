package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.module.afterschool.domain.ao.AsCourseClassCountQueryAO;
import com.sxkj.edu.module.afterschool.domain.ao.AsCourseClassDropdownQueryAO;
import com.sxkj.edu.module.afterschool.domain.ao.AsCourseClassPageQueryAO;
import com.sxkj.edu.module.afterschool.domain.ao.AsCourseClassSessionDetailQueryAO;
import com.sxkj.edu.module.afterschool.domain.ao.AsSessionAttendancePageQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseClassEntity;
import com.sxkj.edu.module.afterschool.domain.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 课程班Mapper接口
 *
 * @author system
 * @date 2026-05-17
 */
@Mapper
@Component
public interface AsCourseClassMapper extends BaseMapper<AsCourseClassEntity> {

    /**
     * 分页查询课程班列表
     *
     * @param page 分页参数
     * @param query 查询条件
     * @return 分页结果
     */
    Page<AsCourseClassPageVO> pageQueryCourseClass(Page<AsCourseClassPageVO> page,
                                                   @Param("query") AsCourseClassPageQueryAO query,
                                                   @Param("presentStatusCodes") List<Integer> presentStatusCodes);

    /**
     * 查询课程班总出勤率。
     *
     * @param courseClassId       课程班ID
     * @param presentStatusCodes 计入出勤的状态编码
     * @return 总出勤率，百分比数值
     */
    Double queryCourseClassAttendanceRate(@Param("courseClassId") Long courseClassId,
                                          @Param("presentStatusCodes") List<Integer> presentStatusCodes);

    /**
     * 统计课程班各报名方式数量
     *
     * @param query 查询条件（schoolId）
     * @return 统计结果
     */
    AsCourseClassCountVO countCourseClass(@Param("query") AsCourseClassCountQueryAO query);

    /**
     * 课后服务课程班下拉筛选（当前日期在服务开课结课范围内）
     * @param ao 查询条件
     * @return 课程班下拉列表
     */
    List<AsCourseClassDropdownVO> listDropdownByServiceDateRange(@Param("ao") AsCourseClassDropdownQueryAO ao);

    /**
     * 教师巡课任务-登记详情（查询课程班地点和教师姓名）
     *
     * @param courseClassId 课程班ID
     * @return 登记详情VO（placeName、teacherName）
     */
    AsPatrolRegistrationDetailVO getPatrolRegistrationDetail(@Param("courseClassId") Long courseClassId);

    /**
     * 查询本次保存未提交的原课程班，作为待软删候选。
     *
     * @param serviceId 课后服务ID
     * @param retainIds 本次请求保留的课程班ID集合
     * @return 待软删课程班
     */
    List<AsCourseClassEntity> listRemovedCourseCandidates(@Param("serviceId") Long serviceId,
                                                          @Param("retainIds") List<Long> retainIds);

    /**
     * 统计课程班是否已有有效报名记录，用于判断编辑保存时是否允许删除课程班。
     *
     * @param courseClassId 课程班ID
     * @return 有效报名记录数
     */
    Integer countEnrollmentData(@Param("courseClassId") Long courseClassId);

    /**
     * 统计课程班是否已有有效学生去向记录，用于判断编辑保存时是否允许删除课程班。
     *
     * @param courseClassId 课程班ID
     * @return 有效学生去向记录数
     */
    Integer countStudentDestinationData(@Param("courseClassId") Long courseClassId);

    /**
     * 统计课程班是否已有有效点名记录，用于判断编辑保存时是否允许删除课程班。
     *
     * @param courseClassId 课程班ID
     * @return 有效点名记录数
     */
    Integer countAttendanceData(@Param("courseClassId") Long courseClassId);

    /**
     * 统计课程班是否已有非未开始课次，用于判断编辑保存时是否允许删除课程班。
     *
     * @param courseClassId 课程班ID
     * @return 已开始、已结束或已取消的有效课次数
     */
    Integer countStartedSession(@Param("courseClassId") Long courseClassId);

    /**
     * 软删除本次保存已移除且通过安全校验的课程班。
     *
     * @param courseIds 课程班ID集合
     * @param updateUser 更新人ID
     * @return 更新行数
     */
    int invalidateCourseClasses(@Param("courseIds") List<Long> courseIds,
                                @Param("updateUser") Long updateUser);

    /**
     * 软删除本次保存已移除课程班的排课规则。
     *
     * @param courseIds 课程班ID集合
     * @param updateUser 更新人ID
     * @return 更新行数
     */
    int invalidateCourseScheduleRules(@Param("courseIds") List<Long> courseIds,
                                      @Param("updateUser") Long updateUser);

    /**
     * 软删除本次保存已移除课程班下未开始的课次。
     *
     * @param courseIds 课程班ID集合
     * @param updateUser 更新人ID
     * @return 更新行数
     */
    int invalidateNotStartedCourseSessions(@Param("courseIds") List<Long> courseIds,
                                           @Param("updateUser") Long updateUser);

    /**
     * 课程班课次考勤分页查询
     *
     * @param page  分页参数
     * @param query 查询条件
     * @return 分页结果
     */
    Page<AsSessionAttendancePageVO> pageQuerySessionAttendance(Page<AsSessionAttendancePageVO> page,
                                                               @Param("query") AsSessionAttendancePageQueryAO query,
                                                               @Param("presentStatusCodes") List<Integer> presentStatusCodes,
                                                               @Param("earlyLeaveStatusCode") Integer earlyLeaveStatusCode);

    /**
     * 根据课程班ID查询课次详情集合
     *
     * @param query 查询条件（courseClassId + schoolId）
     * @return 课次详情列表
     */
    List<AsCourseClassSessionDetailVO> listSessionDetailByCourseClassId(@Param("query") AsCourseClassSessionDetailQueryAO query);

    /**
     * 根据课程班ID列表查询今天有课的课程班
     */
    List<AsCourseClassDropdownVO> listCourseClassWithTodaySession(@Param("courseClassIds") List<Long> courseClassIds);

    /**
     * 根据课程班ID列表查询学生信息
     */
    List<TeacherNoticeStudentVO> queryStudentByCourseClassIds(@Param("schoolId") Long schoolId, @Param("noticeObjIdList")List<Long> noticeObjIdList, @Param("studentIds")List<Long> studentIds);

    // 根据学校、课程查询有效学生人数
    Long countCourseClassBySchoolId(@Param("schoolId") Long schoolId, @Param("courseId") Long courseId);

    /**
     * 根据学校、课程班ID列表批量查询有效学生人数之和（按 cc 分组分别去重后累加）
     */
    Long sumCourseClassPersonByIds(@Param("schoolId") Long schoolId, @Param("courseClassIds") List<Long> courseClassIds);
}
