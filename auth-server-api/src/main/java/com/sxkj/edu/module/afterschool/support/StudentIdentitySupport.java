package com.sxkj.edu.module.afterschool.support;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.exception.BaseBusinessException;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentIdentityEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentNoChangeLogEntity;
import com.sxkj.edu.module.afterschool.mapper.AsStudentIdentityMapper;
import com.sxkj.edu.module.afterschool.mapper.AsStudentMapper;
import com.sxkj.edu.module.afterschool.mapper.AsStudentNoChangeLogMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 学生稳定身份统一处理组件。
 *
 * <p>该组件负责稳定身份创建、学期快照解析和学号受控纠错，不承载报名等业务状态。</p>
 */
@Component
public class StudentIdentitySupport {

    @Resource
    private AsStudentIdentityMapper asStudentIdentityMapper;
    @Resource
    private AsStudentNoChangeLogMapper asStudentNoChangeLogMapper;
    @Resource
    private AsStudentMapper asStudentMapper;

    /**
     * 根据学生资料查找或创建学校内稳定身份。
     */
    @Transactional(rollbackFor = Exception.class)
    public AsStudentIdentityEntity resolveOrCreateIdentity(AsStudentEntity student, Long operatorId) {
        return resolveOrCreateIdentity(student, operatorId, null);
    }

    /**
     * 根据学生资料和入学年份查找或创建学校内稳定身份。
     */
    @Transactional(rollbackFor = Exception.class)
    public AsStudentIdentityEntity resolveOrCreateIdentity(AsStudentEntity student,
                                                           Long operatorId,
                                                           Integer enrollmentYear) {
        if (student == null || student.getSchoolId() == null
                || StringUtils.isBlank(student.getStudentNo())
                || StringUtils.isBlank(student.getStudentName())) {
            throw businessError("学生稳定身份资料不完整");
        }
        if (student.getStudentIdentityId() != null) {
            AsStudentIdentityEntity identity = requireIdentity(
                    student.getStudentIdentityId(), student.getSchoolId());
            return initializeEnrollmentYear(identity, enrollmentYear, operatorId);
        }

        AsStudentIdentityEntity identity = findByCurrentStudentNo(
                student.getSchoolId(), student.getStudentNo());
        if (identity != null) {
            if (!StringUtils.equals(StringUtils.trim(identity.getStudentName()),
                    StringUtils.trim(student.getStudentName()))) {
                throw businessError("该学号已属于其他学生，请核对学生身份");
            }
            student.setStudentIdentityId(identity.getId());
            return initializeEnrollmentYear(identity, enrollmentYear, operatorId);
        }
        AsStudentNoChangeLogEntity historical = findHistoricalStudentNo(
                student.getSchoolId(), student.getStudentNo());
        if (historical != null) {
            throw businessError("该学号曾被其他学生使用，不能重新分配");
        }

        identity = new AsStudentIdentityEntity();
        identity.setSchoolId(student.getSchoolId());
        identity.setEnrollmentYear(enrollmentYear);
        identity.setStudentNo(StringUtils.trim(student.getStudentNo()));
        identity.setStudentName(StringUtils.trim(student.getStudentName()));
        identity.setGender(student.getGender());
        identity.setIdCard(student.getIdCard());
        identity.setAvatarFileId(student.getAvatarFileId());
        identity.setCreateUser(operatorId);
        identity.setCreateTime(new Date());
        identity.setIsValid(1);
        try {
            if (asStudentIdentityMapper.insert(identity) != 1
                    || identity.getId() == null) {
                throw businessError("学生稳定身份创建失败");
            }
        } catch (DuplicateKeyException exception) {
            AsStudentIdentityEntity concurrent = findByCurrentStudentNo(
                    student.getSchoolId(), student.getStudentNo());
            if (concurrent == null) {
                throw exception;
            }
            if (!StringUtils.equals(StringUtils.trim(concurrent.getStudentName()),
                    StringUtils.trim(student.getStudentName()))) {
                throw businessError("该学号已属于其他学生，请核对学生身份");
            }
            identity = concurrent;
        }
        student.setStudentIdentityId(identity.getId());
        return initializeEnrollmentYear(identity, enrollmentYear, operatorId);
    }

    /**
     * 幂等初始化学生入学年份；已有值不允许被不同年份静默覆盖。
     */
    @Transactional(rollbackFor = Exception.class)
    public AsStudentIdentityEntity initializeEnrollmentYear(
            AsStudentIdentityEntity identity,
            Integer enrollmentYear,
            Long operatorId) {
        if (identity == null || enrollmentYear == null) {
            return identity;
        }
        if (enrollmentYear < 1900 || enrollmentYear > 2200) {
            throw businessError("学生入学年份不正确");
        }
        if (identity.getEnrollmentYear() != null) {
            if (!Objects.equals(identity.getEnrollmentYear(), enrollmentYear)) {
                throw businessError("学生入学年份与已有稳定身份不一致");
            }
            return identity;
        }
        Date now = new Date();
        LambdaUpdateWrapper<AsStudentIdentityEntity> wrapper =
                new LambdaUpdateWrapper<>();
        wrapper.eq(AsStudentIdentityEntity::getId, identity.getId())
                .eq(AsStudentIdentityEntity::getSchoolId, identity.getSchoolId())
                .eq(AsStudentIdentityEntity::getIsValid, 1)
                .isNull(AsStudentIdentityEntity::getEnrollmentYear)
                .set(AsStudentIdentityEntity::getEnrollmentYear, enrollmentYear)
                .set(AsStudentIdentityEntity::getUpdateUser, operatorId)
                .set(AsStudentIdentityEntity::getUpdateTime, now);
        if (asStudentIdentityMapper.update(null, wrapper) == 1) {
            identity.setEnrollmentYear(enrollmentYear);
            identity.setUpdateUser(operatorId);
            identity.setUpdateTime(now);
            return identity;
        }
        AsStudentIdentityEntity concurrent =
                requireIdentity(identity.getId(), identity.getSchoolId());
        if (Objects.equals(concurrent.getEnrollmentYear(), enrollmentYear)) {
            return concurrent;
        }
        throw businessError("学生入学年份已被其他操作设置为不同年份");
    }

    /**
     * 查询学校内有效稳定身份。
     */
    public AsStudentIdentityEntity requireIdentity(Long studentIdentityId, Long schoolId) {
        if (studentIdentityId == null || schoolId == null) {
            throw businessError("学生稳定身份和学校不能为空");
        }
        AsStudentIdentityEntity identity = asStudentIdentityMapper.selectById(studentIdentityId);
        if (identity == null || !Integer.valueOf(1).equals(identity.getIsValid())
                || !Objects.equals(identity.getSchoolId(), schoolId)) {
            throw businessError("学生稳定身份不存在或不属于当前学校");
        }
        return identity;
    }

    /**
     * 按稳定身份和学期查询有效学生快照。
     */
    public AsStudentEntity findSnapshot(Long schoolId, Long termId, Long studentIdentityId) {
        if (schoolId == null || termId == null || studentIdentityId == null) {
            return null;
        }
        LambdaQueryWrapper<AsStudentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsStudentEntity::getSchoolId, schoolId)
                .eq(AsStudentEntity::getTermId, termId)
                .eq(AsStudentEntity::getStudentIdentityId, studentIdentityId)
                .eq(AsStudentEntity::getIsValid, 1)
                .last("LIMIT 1");
        List<AsStudentEntity> students = asStudentMapper.selectList(wrapper);
        return students == null || students.isEmpty() ? null : students.get(0);
    }


    /**
     * 校验学号可由指定稳定身份使用。
     */
    public void assertStudentNoAvailable(Long schoolId, String studentNo, Long studentIdentityId) {
        if (schoolId == null || StringUtils.isBlank(studentNo)) {
            throw businessError("学校和学号不能为空");
        }
        AsStudentIdentityEntity current = findByCurrentStudentNo(schoolId, studentNo);
        if (current != null && !Objects.equals(current.getId(), studentIdentityId)) {
            throw businessError("该学号已被其他学生使用");
        }
        AsStudentNoChangeLogEntity historical = findHistoricalStudentNo(schoolId, studentNo);
        if (historical != null
                && !Objects.equals(historical.getStudentIdentityId(), studentIdentityId)) {
            throw businessError("该学号曾被其他学生使用，不能重新分配");
        }
    }

    /**
     * 受控纠正学生学号，并同步所有有效学期快照。
     */
    @Transactional(rollbackFor = Exception.class)
    public void correctStudentNo(AsStudentEntity selectedSnapshot,
                                 String newStudentNo,
                                 String changeReason,
                                 Long operatorId) {
        if (selectedSnapshot == null || selectedSnapshot.getStudentIdentityId() == null) {
            throw businessError("学生稳定身份不存在，不能纠正学号");
        }
        String normalizedNo = StringUtils.trim(newStudentNo);
        if (StringUtils.isBlank(normalizedNo)) {
            throw businessError("新学号不能为空");
        }
        if (StringUtils.isBlank(changeReason)) {
            throw businessError("学号纠错原因不能为空");
        }
        AsStudentIdentityEntity identity = requireIdentity(
                selectedSnapshot.getStudentIdentityId(), selectedSnapshot.getSchoolId());
        if (StringUtils.equals(identity.getStudentNo(), normalizedNo)) {
            return;
        }
        assertStudentNoAvailable(identity.getSchoolId(), normalizedNo, identity.getId());

        LambdaQueryWrapper<AsStudentEntity> conflictWrapper = new LambdaQueryWrapper<>();
        conflictWrapper.eq(AsStudentEntity::getSchoolId, identity.getSchoolId())
                .eq(AsStudentEntity::getStudentNo, normalizedNo)
                .ne(AsStudentEntity::getStudentIdentityId, identity.getId())
                .eq(AsStudentEntity::getIsValid, 1)
                .last("LIMIT 1");
        if (asStudentMapper.selectCount(conflictWrapper) > 0) {
            throw businessError("新学号与其他学生的学期档案冲突");
        }

        String oldStudentNo = identity.getStudentNo();
        identity.setStudentNo(normalizedNo);
        identity.setUpdateUser(operatorId);
        identity.setUpdateTime(new Date());
        if (asStudentIdentityMapper.updateById(identity) != 1) {
            throw businessError("学生稳定身份学号更新失败");
        }

        AsStudentEntity snapshotUpdate = new AsStudentEntity();
        snapshotUpdate.setStudentNo(normalizedNo);
        snapshotUpdate.setUpdateUser(operatorId);
        snapshotUpdate.setUpdateTime(new Date());
        LambdaQueryWrapper<AsStudentEntity> snapshotWrapper = new LambdaQueryWrapper<>();
        snapshotWrapper.eq(AsStudentEntity::getSchoolId, identity.getSchoolId())
                .eq(AsStudentEntity::getStudentIdentityId, identity.getId())
                .eq(AsStudentEntity::getIsValid, 1);
        if (asStudentMapper.update(snapshotUpdate, snapshotWrapper) <= 0) {
            throw businessError("学生学期快照学号更新失败");
        }

        AsStudentNoChangeLogEntity changeLog = new AsStudentNoChangeLogEntity();
        changeLog.setSchoolId(identity.getSchoolId());
        changeLog.setStudentIdentityId(identity.getId());
        changeLog.setOldStudentNo(oldStudentNo);
        changeLog.setNewStudentNo(normalizedNo);
        changeLog.setChangeReason(StringUtils.trim(changeReason));
        changeLog.setCreateUser(operatorId);
        changeLog.setCreateTime(new Date());
        changeLog.setIsValid(1);
        if (asStudentNoChangeLogMapper.insert(changeLog) != 1) {
            throw businessError("学生学号纠错记录保存失败");
        }
    }

    /**
     * 同步稳定身份中的基础资料，不修改其他学期的年级、班级和学籍状态。
     */
    public void updateIdentityProfile(AsStudentEntity student, Long operatorId) {
        if (student == null || student.getStudentIdentityId() == null) {
            return;
        }
        AsStudentIdentityEntity identity = requireIdentity(
                student.getStudentIdentityId(), student.getSchoolId());
        identity.setStudentName(student.getStudentName());
        identity.setGender(student.getGender());
        identity.setIdCard(student.getIdCard());
        identity.setAvatarFileId(student.getAvatarFileId());
        identity.setUpdateUser(operatorId);
        identity.setUpdateTime(new Date());
        if (asStudentIdentityMapper.updateById(identity) != 1) {
            throw businessError("学生稳定身份资料更新失败");
        }
    }

    /**
     * 按当前规范学号查询稳定身份。
     */
    private AsStudentIdentityEntity findByCurrentStudentNo(Long schoolId, String studentNo) {
        LambdaQueryWrapper<AsStudentIdentityEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsStudentIdentityEntity::getSchoolId, schoolId)
                .eq(AsStudentIdentityEntity::getStudentNo, StringUtils.trim(studentNo))
                .eq(AsStudentIdentityEntity::getIsValid, 1)
                .last("LIMIT 1");
        List<AsStudentIdentityEntity> identities = asStudentIdentityMapper.selectList(wrapper);
        return identities == null || identities.isEmpty() ? null : identities.get(0);
    }

    /**
     * 查询旧学号占用记录。
     */
    private AsStudentNoChangeLogEntity findHistoricalStudentNo(Long schoolId, String studentNo) {
        LambdaQueryWrapper<AsStudentNoChangeLogEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsStudentNoChangeLogEntity::getSchoolId, schoolId)
                .eq(AsStudentNoChangeLogEntity::getOldStudentNo, StringUtils.trim(studentNo))
                .eq(AsStudentNoChangeLogEntity::getIsValid, 1)
                .last("LIMIT 1");
        List<AsStudentNoChangeLogEntity> logs = asStudentNoChangeLogMapper.selectList(wrapper);
        return logs == null || logs.isEmpty() ? null : logs.get(0);
    }

    /**
     * 构造统一业务异常。
     */
    private BaseBusinessException businessError(String message) {
        return new BaseBusinessException(ResultCode.PARA_ERROR.code(), message);
    }
}
