package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.domain.base.khfw.ao.AsStudentBindParentAO;
import com.sxkj.edu.domain.base.khfw.vo.AsStudentBindParentVO;
import com.sxkj.edu.module.afterschool.domain.dto.AsStudentLearningAchievementQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsStudentQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentEntity;
import com.sxkj.edu.module.afterschool.domain.vo.AsStudentLearningAchievementVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsStudentVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsTeacherInviteScopeItemVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsTeacherInviteStudentPageItemVO;
import com.sxkj.edu.module.afterschool.domain.vo.TeacherNoticeStudentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 学生档案Mapper接口
 *
 * @author system
 * @date 2026-05-17
 */
public interface AsStudentMapper extends BaseMapper<AsStudentEntity> {

    /**
     * 按 register_status 分组统计学生家长审核状态数量
     *
     * @param schoolId 学校ID
     * @param termId   学期ID
     * @return 每行包含 register_status / cnt
     */
    List<Map<String, Object>> selectStudentAuditSummary(@Param("schoolId") Long schoolId,
                                                        @Param("termId") Long termId);

    /**
     * 学生学习成果分页查询
     *
     * @param page  分页参数
     * @param query 查询条件
     * @return 分页结果
     */
    Page<AsStudentLearningAchievementVO> pageLearningAchievement(
            Page<Object> page,
            @Param("query") AsStudentLearningAchievementQueryAO query);

    /**
     * 批量统计各学校校友数量（学籍状态为在籍）
     *
     * @param schoolIds 学校ID列表
     * @return 每行包含 schoolId / cnt
     */
    List<Map<String, Object>> countSchoolMateBySchoolIds(@Param("schoolIds") List<Long> schoolIds);

    /**
     * 根据学校ID和学生ID列表查询学生信息
     */
    List<TeacherNoticeStudentVO> queryStudentByIds(@Param("schoolId") Long schoolId, @Param("noticeObjIdList")List<Long> noticeObjIdList);

    /**
     * 条件查询学生绑定家长信息
     */
    List<AsStudentBindParentVO> querytBindParentByGradeOrClass(@Param("query") AsStudentBindParentAO parentAO);

    /**
     * 学生档案分页查询（取最新一条）
     */
    Page<AsStudentVO> pageStudent(Page<AsStudentVO> page, @Param("query") AsStudentQueryAO query);


}
