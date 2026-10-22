package com.sxkj.edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.module.afterschool.domain.entity.AsTeacherSubjectEntity;
import com.sxkj.edu.module.afterschool.domain.vo.AsSubjectVO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 学科教师关联 服务接口
 *
 * @author system
 * @date 2026-08-26
 */
public interface AsTeacherSubjectService extends IService<AsTeacherSubjectEntity> {

    /**
     * 批量绑定教师学科关联
     *
     * @param teacherId  教师ID
     * @param subjectIds 学科ID集合
     * @return 操作结果
     */
    JsonResult<String> saveTeacherSubjects(Long schoolId, Long teacherId, List<Long> subjectIds);

    /**
     * 根据教师ID查询教学科目
     *
     * @param teacherId 教师ID
     * @return 学科列表（含年级名称、学科名称）
     */
    List<AsSubjectVO> listSubjectByTeacherId(Long teacherId);

    /**
     * 批量查询教师教学科目，按教师ID分组返回
     *
     * @param teacherIds 教师ID集合
     * @return key=教师ID, value=学科列表
     */
    Map<Long, List<AsSubjectVO>> listSubjectsByTeacherIds(Collection<Long> teacherIds);
}
