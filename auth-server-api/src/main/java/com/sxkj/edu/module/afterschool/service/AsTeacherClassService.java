package com.sxkj.edu.module.afterschool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.domain.ao.AsHeadTeacherClassStudentInfoAO;
import com.sxkj.edu.domain.sys.entity.mult.TEduclClass;
import com.sxkj.edu.domain.vo.AsHeadTeacherClassStudentInfoVO;
import com.sxkj.edu.module.afterschool.domain.dto.AsTeacherClassQueryDto;
import com.sxkj.edu.module.afterschool.domain.entity.AsTeacherClassEntity;
import com.sxkj.edu.module.afterschool.domain.vo.AsHeadTeacherClassStudentListVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsTeacherBindClassVO;

import java.util.List;

public interface AsTeacherClassService extends IService<AsTeacherClassEntity> {


    Boolean createTeacherBindClass(Long schoolId, List<Long> classId, Long teacherId, Integer bindType);

    // 根据学校、班级id查询班主任
    AsTeacherClassQueryDto getTeacherBySchoolAndClass(Long schoolId, Long classId);

    /**
     * 根据班主任用户ID查询名下自然班级学生列表
     *
     * @param teacherId 当前登录用户ID
     * @return 以自然班级为维度的学生列表
     */
    List<AsHeadTeacherClassStudentListVO> queryHeadTeacherClassStudentList(
            Long teacherId, Long schoolId, Long termId);

    /**
     * 同步新增/编辑班主任班级关联数据
     */
    void syncHeadTeacherClass(Long id, Long headTeacherId, Long schoolId);

    /**
     * 根据任课教师ID与学校ID查询任课绑定班级列表
     *
     * @param teacherId 任课教师ID
     * @param schoolId  学校ID
     * @return 任课绑定班级列表
     */
    List<AsTeacherBindClassVO> queryTeacherBindClassList(Long teacherId, Long schoolId);

    List<AsHeadTeacherClassStudentInfoVO> queryClassStudentInfo(AsHeadTeacherClassStudentInfoAO ao);
}
