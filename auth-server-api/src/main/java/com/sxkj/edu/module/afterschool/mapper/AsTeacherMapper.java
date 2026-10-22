package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.domain.ao.AsTeacherStudentQueryAO;
import com.sxkj.edu.domain.vo.AsTeacherCourseStudentVO;
import com.sxkj.edu.module.afterschool.domain.dto.AsTeacherQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsTeacherEntity;
import com.sxkj.edu.module.afterschool.domain.vo.AsTeacherBaseUserVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsTeacherByStudentIdVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsTeacherVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 教师档案Mapper接口
 *
 * @author system
 * @date 2026-05-17
 */
@Mapper
@Component
public interface AsTeacherMapper extends BaseMapper<AsTeacherEntity> {

    /**
     * 教师档案分页查询，关联t_base_user，状态以t_base_user为准
     *
     * @param page  分页参数
     * @param query 查询条件
     * @return 分页结果
     */
    Page<AsTeacherVO> pageTeacherWithUser(
            Page<Object> page,
            @Param("query") AsTeacherQueryAO query);

    /**
     * 按 register_status 分组统计教师审核状态数量
     *
     * @param schoolId 学校ID
     * @return 每行包含 register_status / cnt
     */
    List<Map<String, Object>> selectAuditSummary(@Param("schoolId") Long schoolId);

    AsTeacherByStudentIdVO selectTeacherByCourseId(@Param("schoolId") Long schoolId, @Param("courseId") Long courseId);

    // 查询用户的审核状态
    List<AsTeacherBaseUserVO> queryTeacherBaseUserAuditStatus(@Param("schoolId") Long schoolId, @Param("loginName") String loginName);

    /**
     * 批量查询存在待审核用户账号的教师手机号
     *
     * @param schoolId 学校ID
     * @param phones   待检查的手机号列表
     * @return 存在待审核账号的手机号集合
     */
    List<String> selectPendingAuditPhones(@Param("schoolId") Long schoolId, @Param("phones") List<String> phones);

    // 教师查询任课学生
    List<AsTeacherCourseStudentVO> listTeacherCourseStudents(@Param("query") AsTeacherStudentQueryAO query);
}
