package com.sxkj.edu.module.afterschool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentIdentityEntity;

/**
 * 学生身份主表，关联t_as_student
 *
 * @author jiangjj
 * @date 2026-05-19
 */
public interface AsStudentIdentityService extends IService<AsStudentIdentityEntity> {

    // 同步创建学生身份信息
    Long createStudentIdentityStudent(AsStudentEntity asStudentEntity,Integer enrollmentYear);

    // 更新
    Boolean updateStudentIdentityStudent(AsStudentEntity asStudentEntity,Integer enrollmentYear);
}
