package com.sxkj.edu.module.afterschool.support;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sxkj.edu.module.afterschool.domain.entity.AsEnrollmentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsEnrollmentLogEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsServiceEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentDestinationEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentEntity;
import com.sxkj.edu.module.afterschool.mapper.AsEnrollmentMapper;
import com.sxkj.edu.module.afterschool.mapper.AsEnrollmentLogMapper;
import com.sxkj.edu.module.afterschool.mapper.AsServiceMapper;
import com.sxkj.edu.module.afterschool.mapper.AsStudentDestinationMapper;
import com.sxkj.edu.module.afterschool.mapper.AsStudentMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 学生和学期业务引用检查组件。
 */
@Component
public class StudentBusinessGuard {

    @Resource
    private AsEnrollmentMapper asEnrollmentMapper;
    @Resource
    private AsEnrollmentLogMapper asEnrollmentLogMapper;
    @Resource
    private AsStudentDestinationMapper asStudentDestinationMapper;
    @Resource
    private AsStudentMapper asStudentMapper;
    @Resource
    private AsServiceMapper asServiceMapper;

    /**
     * 判断学生快照是否已经产生业务数据。
     */
    public boolean hasStudentBusiness(Long studentId) {
        if (studentId == null) {
            return false;
        }
        return asEnrollmentMapper.selectCount(new LambdaQueryWrapper<AsEnrollmentEntity>()
                .eq(AsEnrollmentEntity::getStudentId, studentId)) > 0
                || asEnrollmentLogMapper.selectCount(
                new LambdaQueryWrapper<AsEnrollmentLogEntity>()
                        .eq(AsEnrollmentLogEntity::getStudentId, studentId)) > 0
                || asStudentDestinationMapper.selectCount(new LambdaQueryWrapper<AsStudentDestinationEntity>()
                .eq(AsStudentDestinationEntity::getStudentId, studentId)) > 0;
    }

    /**
     * 判断学期是否已经产生学生或课后服务业务。
     */
    public boolean hasTermBusiness(Long schoolId, Long termId) {
        if (schoolId == null || termId == null) {
            return false;
        }
        long studentCount = asStudentMapper.selectCount(new LambdaQueryWrapper<AsStudentEntity>()
                .eq(AsStudentEntity::getSchoolId, schoolId)
                .eq(AsStudentEntity::getTermId, termId));
        if (studentCount > 0) {
            return true;
        }
        return asServiceMapper.selectCount(new LambdaQueryWrapper<AsServiceEntity>()
                .eq(AsServiceEntity::getSchoolId, schoolId)
                .eq(AsServiceEntity::getTermId, termId)) > 0;
    }
}
