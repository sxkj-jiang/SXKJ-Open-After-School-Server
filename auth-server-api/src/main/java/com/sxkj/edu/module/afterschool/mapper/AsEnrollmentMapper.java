package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxkj.edu.module.afterschool.domain.ao.AsParentEnrollmentPageQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsEnrollmentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentEntity;
import com.sxkj.edu.module.afterschool.domain.vo.AsAdminUnselectedSlotQueryData;
import com.sxkj.edu.module.afterschool.domain.vo.AsEnrollmentOccupiedSlotData;
import com.sxkj.edu.module.afterschool.domain.vo.AsEnrollmentSelectedPageData;
import com.sxkj.edu.module.afterschool.domain.vo.AsEnrollmentSelectionCountData;
import com.sxkj.edu.module.afterschool.domain.vo.AsParentEnrollmentFlatVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsStudentEnrollmentRegistTermVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 学生选课/报名记录Mapper接口
 *
 * @author system
 * @date 2026-05-17
 */
@Mapper
@Component
public interface AsEnrollmentMapper extends BaseMapper<AsEnrollmentEntity> {

    /**
     * 公开课程列表批量读取每门课程最新的课次报名，条件与 findCourseEnrollment 一致。
     */
    List<AsEnrollmentEntity> listLatestGuardianCourseEnrollments(
            @Param("studentId") Long studentId, @Param("courseClassIds") Collection<Long> courseClassIds);

    /**
     * 查询管理端未选课组班的候选学生编号，仅返回轻量主键集合。
     *
     * @param schoolId 学校编号
     * @param termId 学期编号
     * @param serviceIds 已进入分配窗口的课后服务编号
     * @param gradeId 年级编号（可选）
     * @param classId 行政班编号（可选）
     * @param keyword 学号或姓名关键词（可选）
     * @param assignableSlots 可分配的实际课次时段
     * @return 按页面展示顺序排列的候选学生编号
     */
    List<Long> listAdminUnselectedStudentCandidateIds(
            @Param("schoolId") Long schoolId,
            @Param("termId") Long termId,
            @Param("serviceIds") Collection<Long> serviceIds,
            @Param("gradeId") Long gradeId,
            @Param("classId") Long classId,
            @Param("keyword") String keyword,
            @Param("assignableSlots") Collection<AsAdminUnselectedSlotQueryData> assignableSlots);

    /**
     * 分页查询管理端未选课候选学生编号，并由同连接的 FOUND_ROWS() 取得总数。
     *
     * @param schoolId 学校编号
     * @param termId 学期编号
     * @param serviceIds 已进入分配窗口的课后服务编号
     * @param gradeId 年级编号（可选）
     * @param classId 行政班编号（可选）
     * @param keyword 学号或姓名关键词（可选）
     * @param assignableSlots 可分配的实际课次时段
     * @param offset 分页偏移量
     * @param limit 每页条数
     * @return 当前页候选学生编号
     */
    List<Long> listAdminUnselectedStudentCandidatePageIds(
            @Param("schoolId") Long schoolId,
            @Param("termId") Long termId,
            @Param("serviceIds") Collection<Long> serviceIds,
            @Param("gradeId") Long gradeId,
            @Param("classId") Long classId,
            @Param("keyword") String keyword,
            @Param("assignableSlots") Collection<AsAdminUnselectedSlotQueryData> assignableSlots,
            @Param("offset") int offset,
            @Param("limit") int limit);

    /**
     * 读取上一条 SQL_CALC_FOUND_ROWS 查询计算出的总记录数。
     * 必须在同一数据库连接中紧跟分页查询调用。
     */
    long selectFoundRows();

    /**
     * 查询学生在指定课程班下最新的有效报名记录（优先有课次分配的）。
     *
     * @param courseClassId 课程班ID
     * @param studentId     学生ID
     * @return 最新的有效报名记录，不存在返回 null
     */
    AsEnrollmentEntity findEnrollmentByCourseAndStudent(@Param("courseClassId") Long courseClassId,
                                                         @Param("studentId") Long studentId);

    /**
     * 家长查询学生选课记录（平铺结构，按课次维度）
     *
     * @param studentIdList 学生ID
     * @param schoolId  学校ID
     * @param termId    学期ID（可选）
     * @return 选课记录平铺列表
     */
    List<AsParentEnrollmentFlatVO> listParentEnrollmentCourses(
            @Param("studentIdList") List<Long> studentIdList,
            @Param("schoolId") Long schoolId,
            @Param("termId") Long termId);

    // 管理端查询学生报名记录-并去重
    List<AsStudentEnrollmentRegistTermVO> queryStudentEnrollmentForList(@Param("studentId") Long studentId,
                                                                        @Param("schoolId") Long schoolId,
                                                                        @Param("termId") Long termId);

    /**
     * 班主任课后报名Tab数量统计（已报名 + 已分配），单次 SQL 条件聚合。
     *
     * @param schoolId   学校ID
     * @param studentIds 学生ID集合
     * @param serviceIds 服务ID集合
     * @param termId     学期ID（可选）
     * @return 单行聚合结果 [registeredCount, assignedCount]
     */
    Map<String, Long> countHeadteacherEnrollmentStatuses(@Param("schoolId") Long schoolId,
                                                          @Param("studentIds") Collection<Long> studentIds,
                                                          @Param("serviceIds") Collection<Long> serviceIds,
                                                          @Param("termId") Long termId);

    /**
     * 未选课统计：仅加载尚未分配到课程班/课次的报名记录及关联学生、课后服务信息。
     * 已选计数由 {@link #countSelectedPageData} 在 SQL 层完成，本查询只服务于未选计数路径。
     *
     * @param schoolId    学校ID
     * @param serviceIds  学期下服务ID集合
     * @param gradeId     年级ID（可选）
     * @param classId     班级ID（可选）
     * @param keyword     学号/姓名关键词（可选）
     * @return 未选报名记录平铺结果
     */
    List<AsEnrollmentSelectionCountData> listUnselectedCountData(@Param("schoolId") Long schoolId,
                                                                  @Param("serviceIds") Collection<Long> serviceIds,
                                                                  @Param("gradeId") Long gradeId,
                                                                  @Param("classId") Long classId,
                                                                  @Param("keyword") String keyword);

    /**
     * 查询服务范围内已经存在报名记录的学生编号。
     *
     * @param schoolId  学校编号
     * @param serviceId 课后服务编号
     * @param studentIds 候选学生编号
     * @return 已有报名记录的学生编号
     */
    List<Long> listExistingEnrollmentStudentIds(@Param("schoolId") Long schoolId,
                                                 @Param("serviceId") Long serviceId,
                                                 @Param("studentIds") Collection<Long> studentIds);

    /**
     * 查询服务适用行政班内尚无任何有效报名记录的已通过学生。
     *
     * @param schoolId 学校编号
     * @param termId 学期编号
     * @param serviceId 课后服务编号
     * @param classIds 服务适用行政班编号
     * @param gradeId 年级筛选（可选）
     * @param classId 行政班筛选（可选）
     * @param keyword 学号或姓名关键词（可选）
     * @param limit 单批最大返回数量
     * @return 本批需要补偿占位记录的学生
     */
    List<AsStudentEntity> listMissingPlaceholderStudents(
            @Param("schoolId") Long schoolId,
            @Param("termId") Long termId,
            @Param("serviceId") Long serviceId,
            @Param("classIds") Collection<Long> classIds,
            @Param("gradeId") Long gradeId,
            @Param("classId") Long classId,
            @Param("keyword") String keyword,
            @Param("limit") int limit);

    /**
     * 查询学生在指定学期内已被课程或非课程去向占用的时段。
     *
     * @param schoolIds 学校编号集合
     * @param termIds 学期编号集合
     * @param studentIds 学生编号集合
     * @return 去重后的占用时段
     */
    List<AsEnrollmentOccupiedSlotData> listStudentTermOccupiedSlots(
            @Param("schoolIds") Collection<Long> schoolIds,
            @Param("termIds") Collection<Long> termIds,
            @Param("studentIds") Collection<Long> studentIds);

    /**
     * 已选课组班分页查询：单次多表 JOIN 加载列表行所需全部字段，
     * 过滤条件（学期/年级/班级/关键词/报名方式）下推到 SQL，数据库层面完成分页。
     *
     * @param schoolId    学校ID
     * @param serviceIds  学期下服务ID集合
     * @param gradeId     年级ID（可选）
     * @param classId     班级ID（可选）
     * @param keyword     学号/姓名关键词（可选）
     * @param enrollType  报名方式（可选）
     * @param offset      分页偏移量
     * @param limit       每页条数
     * @return 扁平化分页结果
     */
    List<AsEnrollmentSelectedPageData> listSelectedPageData(@Param("schoolId") Long schoolId,
                                                            @Param("serviceIds") Collection<Long> serviceIds,
                                                            @Param("gradeId") Long gradeId,
                                                            @Param("classId") Long classId,
                                                            @Param("keyword") String keyword,
                                                            @Param("enrollType") Integer enrollType,
                                                            @Param("offset") int offset,
                                                            @Param("limit") int limit);

    /**
     * 已选课组班总记录数查询，参数与 listSelectedPageData 一致。
     *
     * @param schoolId    学校ID
     * @param serviceIds  学期下服务ID集合
     * @param gradeId     年级ID（可选）
     * @param classId     班级ID（可选）
     * @param keyword     学号/姓名关键词（可选）
     * @param enrollType  报名方式（可选）
     * @return 满足条件的去重后总记录数
     */
    long countSelectedPageData(@Param("schoolId") Long schoolId,
                               @Param("serviceIds") Collection<Long> serviceIds,
                               @Param("gradeId") Long gradeId,
                               @Param("classId") Long classId,
                               @Param("keyword") String keyword,
                               @Param("enrollType") Integer enrollType);

    /**
     * 查询邀请学生冲突判断所需的有效课程班占用数据。
     *
     * @param schoolId 学校编号
     * @param studentIds 学生编号集合
     * @return 按学生和课程班去重后的报名数据
     */
    List<AsEnrollmentEntity> listInviteActiveCourseEnrollments(
            @Param("schoolId") Long schoolId,
            @Param("studentIds") Collection<Long> studentIds);

    /**
     * 查询邀请学生判断所需的每个学生最新课次报名记录。
     *
     * @param schoolIds 学校编号集合
     * @param studentIds 学生编号集合
     * @return 每个学校和学生最新的一条课次报名记录
     */
    List<AsEnrollmentEntity> listInviteLatestSessionEnrollments(
            @Param("schoolIds") Collection<Long> schoolIds,
            @Param("studentIds") Collection<Long> studentIds);
}
