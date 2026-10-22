package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseGradeQuotaEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseClassEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AsCourseGradeQuotaMapper extends BaseMapper<AsCourseGradeQuotaEntity> {

    int lockCourse(@Param("courseId") Long courseId);
    AsCourseClassEntity currentCourse(@Param("courseId") Long courseId);
    List<AsCourseGradeQuotaEntity> listCurrent(@Param("courseId") Long courseId);
    int hasLegacyEnrollment(@Param("courseId") Long courseId);
    int changeTotalCapacity(@Param("courseId") Long courseId, @Param("capacity") Integer capacity);
    int reserveTotal(@Param("courseId") Long courseId, @Param("gradeId") Long gradeId);
    int reserveGrade(@Param("courseId") Long courseId, @Param("gradeId") Long gradeId);
    /** 调课审核专用，总名额仍记账但不限制上限。 */
    int reserveTotalForStudentAdjust(@Param("courseId") Long courseId, @Param("gradeId") Long gradeId);
    /** 调课审核专用，年级名额仍记账但不限制上限。 */
    int reserveGradeForStudentAdjust(@Param("courseId") Long courseId, @Param("gradeId") Long gradeId);
    /** 查询仍有有效报名的调课审批凭证，仅用于保留超额课程的原有限额。 */
    int hasApprovedStudentAdjust(@Param("schoolId") Long schoolId, @Param("courseId") Long courseId,
                                 @Param("gradeId") Long gradeId);
    int createMember(@Param("courseId") Long courseId, @Param("studentId") Long studentId);
    int activateMember(@Param("courseId") Long courseId, @Param("studentId") Long studentId,
                       @Param("gradeId") Long gradeId, @Param("termId") Long termId);
    int beginRelease(@Param("courseId") Long courseId, @Param("studentId") Long studentId);
    int releaseGrade(@Param("courseId") Long courseId, @Param("studentId") Long studentId);
    int releaseTotal(@Param("courseId") Long courseId);
    int finishRelease(@Param("courseId") Long courseId, @Param("studentId") Long studentId);
    int hasQuota(@Param("courseId") Long courseId);
    int hasMember(@Param("courseId") Long courseId, @Param("studentId") Long studentId);
}
