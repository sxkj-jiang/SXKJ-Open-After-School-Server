package com.sxkj.edu.module.afterschool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxkj.edu.module.afterschool.domain.entity.AsGuardianStudentEntity;
import com.sxkj.edu.module.afterschool.domain.vo.AsStudentVO;
import com.sxkj.edu.module.system.login.domain.LoginStuParentDataVO;
import com.sxkj.edu.module.system.login.domain.LoginStuParentDetailVO;
import com.sxkj.edu.module.system.login.domain.LoginStuParentTaskVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface AsGuardianStudentMapper extends BaseMapper<AsGuardianStudentEntity> {

    // 返回学生
    LoginStuParentDetailVO getStuParentDetail(@Param("parentId") Long parentId,
                                              @Param("schoolId") Long schoolId,
                                              @Param("termId") Long termId);

    // 根据家长id、学期获取学生信息
    List<LoginStuParentDetailVO> getStuTermListParentDetail(@Param("parentId") Long parentId,
                                              @Param("schoolId") Long schoolId,
                                              @Param("termId") Long termId);

    // 根据学生ID获取监护人详情,兼容多个学生家长数据
    List<LoginStuParentDataVO> getStuParentDetailByStudentId(@Param("studentId") Long studentId, @Param("schoolId") Long schoolId);

    // 根据监护人ID和学校ID查询通过的学生ID（t_as_student子表id）,兼容多个小孩数据
    List<Long> queryPassStudentIdByParentUserId(@Param("parentId") Long parentId,
                                          @Param("schoolId") Long schoolId,
                                          @Param("termId") Long termId, @Param("studentIdentityId") Long studentIdentityId);


    // 根据学生ID列表和学校ID查询绑定的监护人学生信息
    List<AsStudentVO> queryBindParentStuByStuIds(@Param("studentIds")List<Long> studentIds, @Param("schoolId") Long schoolId);

    // 根据家长账号id查询学生任务信息
    List<LoginStuParentTaskVO> queryBindParentTaskByStuParentIds(
            @Param("stuParentIds") List<Long> stuParentIds,
            @Param("schoolId") Long schoolId,
            @Param("termId") Long termId);

    // 返回家长的多个学生
    List<LoginStuParentDetailVO> getStuListParentData(@Param("parentId") Long parentId,
                                              @Param("schoolId") Long schoolId,
                                              @Param("termId") Long termId);
}
