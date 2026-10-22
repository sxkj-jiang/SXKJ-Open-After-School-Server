package com.sxkj.edu.module.afterschool.support;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.exception.BaseBusinessException;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentTermChangeLogEntity;
import com.sxkj.edu.module.afterschool.domain.entity.EduclTermEntity;
import com.sxkj.edu.module.afterschool.mapper.AsStudentMapper;
import com.sxkj.edu.module.afterschool.mapper.AsStudentTermChangeLogMapper;
import com.sxkj.edu.module.afterschool.mapper.EduclTermMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 学生学期内资料变更生效组件。
 *
 * <p>只处理已登记且到达生效时间的学生资料，不修改报名、排课、调课等业务状态。</p>
 */
@Component
public class StudentTermChangeApplySupport {

    private static final Long SYSTEM_USER_ID = 1L;
    private static final int BATCH_SIZE = 200;

    @Resource
    private AsStudentTermChangeLogMapper asStudentTermChangeLogMapper;
    @Resource
    private AsStudentMapper asStudentMapper;
    @Resource
    private EduclTermMapper educlTermMapper;
    @Resource
    private StudentBusinessGuard studentBusinessGuard;

    /**
     * 查询已经到达生效时间的待处理记录。
     */
    public List<Long> listDueChangeIds(Date now) {
        LambdaQueryWrapper<AsStudentTermChangeLogEntity> wrapper =
                new LambdaQueryWrapper<>();
        wrapper.select(AsStudentTermChangeLogEntity::getId)
                .eq(AsStudentTermChangeLogEntity::getApplyStatus,
                        CoreConstant.AS_STUDENT_TERM_CHANGE_PENDING)
                .le(AsStudentTermChangeLogEntity::getEffectiveTime, now)
                .eq(AsStudentTermChangeLogEntity::getIsValid, 1)
                .orderByAsc(AsStudentTermChangeLogEntity::getEffectiveTime)
                .last("LIMIT " + BATCH_SIZE);
        List<AsStudentTermChangeLogEntity> records =
                asStudentTermChangeLogMapper.selectList(wrapper);
        if (records == null || records.isEmpty()) {
            return new ArrayList<>();
        }
        return records.stream()
                .map(AsStudentTermChangeLogEntity::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 原子领取并应用一条已到期的资料变更。
     *
     * @return 是否由当前执行器完成处理
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean applyPendingChange(Long changeId, Date now) {
        if (changeId == null || now == null) {
            return false;
        }
        LambdaUpdateWrapper<AsStudentTermChangeLogEntity> claimWrapper =
                new LambdaUpdateWrapper<>();
        claimWrapper.eq(AsStudentTermChangeLogEntity::getId, changeId)
                .eq(AsStudentTermChangeLogEntity::getApplyStatus,
                        CoreConstant.AS_STUDENT_TERM_CHANGE_PENDING)
                .le(AsStudentTermChangeLogEntity::getEffectiveTime, now)
                .eq(AsStudentTermChangeLogEntity::getIsValid, 1)
                .set(AsStudentTermChangeLogEntity::getApplyStatus,
                        CoreConstant.AS_STUDENT_TERM_CHANGE_PROCESSING)
                .set(AsStudentTermChangeLogEntity::getUpdateUser, SYSTEM_USER_ID)
                .set(AsStudentTermChangeLogEntity::getUpdateTime, now);
        if (asStudentTermChangeLogMapper.update(null, claimWrapper) != 1) {
            return false;
        }

        AsStudentTermChangeLogEntity change =
                asStudentTermChangeLogMapper.selectById(changeId);
        AsStudentEntity student = validateCurrentProfile(change);
        if (isLeavingSchool(student.getSchoolRollStatus(),
                change.getNewSchoolRollStatus())) {
            disableFutureSnapshots(student, change.getNewSchoolRollStatus(), now);
        }

        if (!updateCurrentProfile(change, now)) {
            throw businessError("学生资料到期生效失败");
        }

        LambdaUpdateWrapper<AsStudentTermChangeLogEntity> finishWrapper =
                new LambdaUpdateWrapper<>();
        finishWrapper.eq(AsStudentTermChangeLogEntity::getId, changeId)
                .eq(AsStudentTermChangeLogEntity::getApplyStatus,
                        CoreConstant.AS_STUDENT_TERM_CHANGE_PROCESSING)
                .set(AsStudentTermChangeLogEntity::getApplyStatus,
                        CoreConstant.AS_STUDENT_TERM_CHANGE_APPLIED)
                .set(AsStudentTermChangeLogEntity::getApplyTime, now)
                .set(AsStudentTermChangeLogEntity::getFailureReason, null)
                .set(AsStudentTermChangeLogEntity::getUpdateUser, SYSTEM_USER_ID)
                .set(AsStudentTermChangeLogEntity::getUpdateTime, now);
        if (asStudentTermChangeLogMapper.update(null, finishWrapper) != 1) {
            throw businessError("学生资料变更状态更新失败");
        }
        return true;
    }

    /**
     * 将无法生效的资料变更标记为失败，避免定时任务反复执行同一错误记录。
     */
    @Transactional(rollbackFor = Exception.class)
    public void markApplyFailed(Long changeId, String failureReason, Date now) {
        if (changeId == null || now == null) {
            return;
        }
        String message = failureReason == null ? "学生资料变更生效失败"
                : failureReason.trim();
        if (message.length() > 500) {
            message = message.substring(0, 500);
        }
        LambdaUpdateWrapper<AsStudentTermChangeLogEntity> wrapper =
                new LambdaUpdateWrapper<>();
        wrapper.eq(AsStudentTermChangeLogEntity::getId, changeId)
                .eq(AsStudentTermChangeLogEntity::getApplyStatus,
                        CoreConstant.AS_STUDENT_TERM_CHANGE_PENDING)
                .eq(AsStudentTermChangeLogEntity::getIsValid, 1)
                .set(AsStudentTermChangeLogEntity::getApplyStatus,
                        CoreConstant.AS_STUDENT_TERM_CHANGE_FAILED)
                .set(AsStudentTermChangeLogEntity::getFailureReason, message)
                .set(AsStudentTermChangeLogEntity::getUpdateUser, SYSTEM_USER_ID)
                .set(AsStudentTermChangeLogEntity::getUpdateTime, now);
        asStudentTermChangeLogMapper.update(null, wrapper);
    }

    /**
     * 校验待生效记录仍与当前学生资料一致，防止覆盖后续人工调整。
     */
    private AsStudentEntity validateCurrentProfile(
            AsStudentTermChangeLogEntity change) {
        if (change == null || change.getStudentId() == null) {
            throw businessError("学生资料变更记录不存在");
        }
        AsStudentEntity student = asStudentMapper.selectById(change.getStudentId());
        if (student == null || !Integer.valueOf(1).equals(student.getIsValid())) {
            throw businessError("待变更学生档案不存在");
        }
        if (!Objects.equals(student.getSchoolId(), change.getSchoolId())
                || !Objects.equals(student.getTermId(), change.getTermId())
                || !Objects.equals(student.getStudentIdentityId(),
                change.getStudentIdentityId())) {
            throw businessError("待变更学生档案与登记范围不一致");
        }
        if (!Objects.equals(student.getGradeId(), change.getOldGradeId())
                || !Objects.equals(student.getClassId(), change.getOldClassId())
                || !Objects.equals(student.getSchoolRollStatus(),
                change.getOldSchoolRollStatus())) {
            throw businessError("学生资料已发生其他调整，本次待生效变更已停止");
        }
        return student;
    }

    /**
     * 按登记时的原资料条件更新当前学生，避免覆盖并发生效期间的人工调整。
     */
    private boolean updateCurrentProfile(AsStudentTermChangeLogEntity change,
                                         Date now) {
        LambdaUpdateWrapper<AsStudentEntity> wrapper =
                new LambdaUpdateWrapper<>();
        wrapper.eq(AsStudentEntity::getId, change.getStudentId())
                .eq(AsStudentEntity::getSchoolId, change.getSchoolId())
                .eq(AsStudentEntity::getTermId, change.getTermId())
                .eq(AsStudentEntity::getStudentIdentityId,
                        change.getStudentIdentityId())
                .eq(AsStudentEntity::getIsValid, 1);
        if (change.getOldGradeId() == null) {
            wrapper.isNull(AsStudentEntity::getGradeId);
        } else {
            wrapper.eq(AsStudentEntity::getGradeId, change.getOldGradeId());
        }
        if (change.getOldClassId() == null) {
            wrapper.isNull(AsStudentEntity::getClassId);
        } else {
            wrapper.eq(AsStudentEntity::getClassId, change.getOldClassId());
        }
        if (change.getOldSchoolRollStatus() == null) {
            wrapper.isNull(AsStudentEntity::getSchoolRollStatus);
        } else {
            wrapper.eq(AsStudentEntity::getSchoolRollStatus,
                    change.getOldSchoolRollStatus());
        }
        wrapper.set(AsStudentEntity::getGradeId, change.getNewGradeId())
                .set(AsStudentEntity::getClassId, change.getNewClassId())
                .set(AsStudentEntity::getSchoolRollStatus,
                        change.getNewSchoolRollStatus())
                .set(AsStudentEntity::getUpdateUser, SYSTEM_USER_ID)
                .set(AsStudentEntity::getUpdateTime, now);
        return asStudentMapper.update(null, wrapper) == 1;
    }

    /**
     * 学生离校类变更生效时，停用尚未产生业务的未来学期快照。
     */
    private void disableFutureSnapshots(AsStudentEntity current,
                                        Integer newSchoolRollStatus,
                                        Date now) {
        EduclTermEntity currentTerm =
                educlTermMapper.selectById(current.getTermId());
        if (currentTerm == null || currentTerm.getStartDate() == null) {
            throw businessError("学生所属学期不存在，不能变更学籍状态");
        }
        LambdaQueryWrapper<AsStudentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsStudentEntity::getSchoolId, current.getSchoolId())
                .eq(AsStudentEntity::getStudentIdentityId,
                        current.getStudentIdentityId())
                .ne(AsStudentEntity::getId, current.getId())
                .eq(AsStudentEntity::getIsValid, 1);
        List<AsStudentEntity> snapshots = asStudentMapper.selectList(wrapper);
        if (snapshots == null || snapshots.isEmpty()) {
            return;
        }
        for (AsStudentEntity snapshot : snapshots) {
            EduclTermEntity term = educlTermMapper.selectById(snapshot.getTermId());
            if (term == null || term.getStartDate() == null
                    || !term.getStartDate().after(currentTerm.getStartDate())) {
                continue;
            }
            if (studentBusinessGuard.hasStudentBusiness(snapshot.getId())) {
                throw businessError("未来学期学生档案已产生业务，请先处理未来学期报名或排课");
            }
            snapshot.setSchoolRollStatus(newSchoolRollStatus);
            snapshot.setIsValid(0);
            snapshot.setUpdateUser(SYSTEM_USER_ID);
            snapshot.setUpdateTime(now);
            if (asStudentMapper.updateById(snapshot) != 1) {
                throw businessError("未来学期学生档案停用失败");
            }
        }
    }

    /**
     * 判断是否由在籍状态变更为离校类状态。
     */
    private boolean isLeavingSchool(Integer oldStatus, Integer newStatus) {
        return Integer.valueOf(1).equals(oldStatus)
                && newStatus != null
                && !Integer.valueOf(1).equals(newStatus);
    }

    /**
     * 构造统一业务异常。
     */
    private BaseBusinessException businessError(String message) {
        return new BaseBusinessException(ResultCode.STU_MANAGER_ERROR.code(), message);
    }
}
