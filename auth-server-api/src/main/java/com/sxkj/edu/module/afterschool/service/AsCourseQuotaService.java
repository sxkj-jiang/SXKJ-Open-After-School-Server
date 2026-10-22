package com.sxkj.edu.module.afterschool.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.exception.BaseBusinessException;
import com.sxkj.edu.module.afterschool.domain.dto.AsCourseGradeQuotaAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsServiceCourseItemAO;
import com.sxkj.edu.module.afterschool.domain.entity.*;
import com.sxkj.edu.module.afterschool.domain.vo.AsCourseGradeQuotaVO;
import com.sxkj.edu.module.afterschool.domain.vo.AsCourseQuotaVO;
import com.sxkj.edu.module.afterschool.mapper.AsCourseClassMapper;
import com.sxkj.edu.module.afterschool.mapper.AsCourseGradeQuotaMapper;
import com.sxkj.edu.module.afterschool.mapper.AsServiceMapper;
import com.sxkj.edu.module.afterschool.support.CourseClassScopeSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 公开课名额唯一写入口。成员、总人数、年级人数必须与报名记录处于同一事务。
 * 年级配置缺失时拒绝新增名额，不把历史报名隐式转换为零占用。
 */
@Service
public class AsCourseQuotaService {
    @Resource private AsCourseGradeQuotaMapper quotaMapper;
    @Resource private AsCourseClassMapper courseMapper;
    @Resource private AsServiceMapper serviceMapper;
    @Resource private CourseClassScopeSupport scopeSupport;

    public boolean isPublic(AsCourseClassEntity course) {
        return course != null && Objects.equals(course.getEnrollType(), CoreConstant.ENROLL_TYPE_PUBLIC);
    }

    public String validateConfiguration(AsServiceCourseItemAO item) {
        if (!Objects.equals(item.getEnrollType(), CoreConstant.ENROLL_TYPE_PUBLIC)) {
            return null;
        }
        if (item.getCapacity() == null || item.getCapacity() <= 0) {
            return "公开课学生总人数上限必须为正整数";
        }
        List<Long> grades;
        try {
            grades = scopeSupport.parseLongList(item.getApplicableGradeIds());
        } catch (RuntimeException exception) {
            return "适用年级格式不合法";
        }
        List<AsCourseGradeQuotaAO> quotas = item.getGradeQuotas();
        if (grades.isEmpty() || quotas == null || quotas.isEmpty()) {
            return "公开课必须为每个适用年级配置人数上限";
        }
        Set<Long> configured = new HashSet<>();
        long sum = 0;
        for (AsCourseGradeQuotaAO quota : quotas) {
            if (quota == null || quota.getGradeId() == null || !configured.add(quota.getGradeId())) {
                return "年级限额的年级不能为空或重复";
            }
            if (quota.getCapacity() == null || quota.getCapacity() <= 0) {
                return "每个年级的人数上限必须为正整数";
            }
            sum += quota.getCapacity();
        }
        if (!configured.equals(new HashSet<>(grades))) {
            return "年级限额必须与适用年级一一对应";
        }
        return sum > item.getCapacity() ? "年级人数上限之和不能大于课程班学生总人数上限" : null;
    }

    /** 保存/异步发布之前的只读预校验，实际保存仍在行锁内再次校验。 */
    public String validateExistingConfiguration(AsCourseClassEntity course, AsServiceCourseItemAO item) {
        if (!isPublic(course)) {
            return null;
        }
        ensureLoaded(course);
        if (course.getGradeQuotaRows().isEmpty() && (quotaMapper.hasLegacyEnrollment(course.getId()) > 0
                || value(course.getEnrolledCount()) > 0)) {
            return "该公开课已有历史报名，不能以零人数初始化年级限额；请新建课程班";
        }
        Map<Long, AsCourseGradeQuotaAO> requested = item.getGradeQuotas().stream()
                .collect(Collectors.toMap(AsCourseGradeQuotaAO::getGradeId, q -> q));
        for (AsCourseGradeQuotaEntity row : course.getGradeQuotaRows()) {
            AsCourseGradeQuotaAO setting = requested.get(row.getGradeId());
            if (value(row.getEnrolledCount()) > 0
                    && (setting == null || (setting.getCapacity() < row.getEnrolledCount()
                    && !canKeepGradeCapacityAfterStudentAdjust(course, row, setting.getCapacity())))) {
                return "不能移除已占用名额的年级，年级上限也不能小于已占用人数";
            }
        }
        return null;
    }

    /** 只允许有有效调课审批凭证的超额课程保留原上限，不允许借机调低上限。 */
    public String validateCourseCapacityChange(AsCourseClassEntity course, Integer requestedCapacity) {
        if (course != null && requestedCapacity != null && requestedCapacity > 0
                && value(course.getEnrolledCount()) > requestedCapacity
                && !canKeepCapacityAfterStudentAdjust(course, requestedCapacity)) {
            return "课程容量不能小于已报名人数";
        }
        return null;
    }

    /** 总容量校验必须比较修改前的课程配置，不能使用已经写回的新容量。 */
    private boolean canKeepCapacityAfterStudentAdjust(AsCourseClassEntity course, Integer requestedCapacity) {
        return Objects.equals(course.getCapacity(), requestedCapacity)
                && (isPublic(course) || Objects.equals(course.getEnrollType(), CoreConstant.ENROLL_TYPE_INVITE))
                && quotaMapper.hasApprovedStudentAdjust(course.getSchoolId(), course.getId(), null) > 0;
    }

    /** 年级容量例外仅适用于该年级仍有有效调课占位、且本次上限没有变化的情况。 */
    private boolean canKeepGradeCapacityAfterStudentAdjust(AsCourseClassEntity course,
                                                          AsCourseGradeQuotaEntity grade,
                                                          Integer requestedCapacity) {
        return Objects.equals(grade.getCapacity(), requestedCapacity)
                && quotaMapper.hasApprovedStudentAdjust(course.getSchoolId(), course.getId(), grade.getGradeId()) > 0;
    }

    /** 调课同时触及多个班时固定加锁顺序；锁由数据库事务结束时释放。 */
    @Transactional(propagation = Propagation.MANDATORY)
    public void lockCourses(Collection<Long> courseIds) {
        courseIds.stream().filter(Objects::nonNull).distinct().sorted().forEach(quotaMapper::lockCourse);
    }

    /** 仅编辑配置调用当前读；报名热路径仍由条件UPDATE判断名额。 */
    @Transactional(propagation = Propagation.MANDATORY)
    public AsCourseClassEntity currentCourse(Long courseId) {
        return quotaMapper.currentCourse(courseId);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveConfiguration(AsCourseClassEntity course, AsServiceCourseItemAO item) {
        if (!isPublic(course)) {
            return;
        }
        String error = validateConfiguration(item);
        if (error != null) {
            throw failure(error);
        }
        AsCourseClassEntity current = quotaMapper.currentCourse(course.getId());
        List<AsCourseGradeQuotaEntity> existing = quotaMapper.listCurrent(course.getId());
        if (existing.isEmpty() && (quotaMapper.hasLegacyEnrollment(course.getId()) > 0
                || value(current.getEnrolledCount()) > 0)) {
            throw failure("该公开课已有历史报名，不能以零人数初始化年级限额；请新建课程班");
        }
        if (value(current.getEnrolledCount()) > item.getCapacity()
                && !canKeepCapacityAfterStudentAdjust(current, item.getCapacity())) {
            throw failure("课程班总上限不能小于已占用人数");
        }
        Map<Long, AsCourseGradeQuotaAO> requested = item.getGradeQuotas().stream()
                .collect(Collectors.toMap(AsCourseGradeQuotaAO::getGradeId, q -> q));
        for (AsCourseGradeQuotaEntity row : existing) {
            AsCourseGradeQuotaAO setting = requested.get(row.getGradeId());
            if (value(row.getEnrolledCount()) > 0
                    && (setting == null || (setting.getCapacity() < row.getEnrolledCount()
                    && !canKeepGradeCapacityAfterStudentAdjust(current, row, setting.getCapacity())))) {
                throw failure("不能移除已占用名额的年级，年级上限也不能小于已占用人数");
            }
        }
        if (!Objects.equals(current.getCapacity(), item.getCapacity())
                && quotaMapper.changeTotalCapacity(course.getId(), item.getCapacity()) != 1) {
            throw failure("课程容量更新失败，请刷新后重试");
        }
        Map<Long, AsCourseGradeQuotaEntity> rows = existing.stream()
                .collect(Collectors.toMap(AsCourseGradeQuotaEntity::getGradeId, q -> q));
        for (AsCourseGradeQuotaAO setting : item.getGradeQuotas()) {
            AsCourseGradeQuotaEntity row = rows.get(setting.getGradeId());
            if (row == null) {
                row = new AsCourseGradeQuotaEntity();
                row.setSchoolId(course.getSchoolId());
                row.setCourseClassId(course.getId());
                row.setGradeId(setting.getGradeId());
                row.setCapacity(setting.getCapacity());
                row.setEnrolledCount(0);
                row.setIsValid(1);
                quotaMapper.insert(row);
            } else {
                row.setCapacity(setting.getCapacity());
                row.setIsValid(1);
                quotaMapper.updateById(row);
            }
        }
        for (AsCourseGradeQuotaEntity row : existing) {
            if (!requested.containsKey(row.getGradeId())) {
                row.setIsValid(0);
                quotaMapper.updateById(row);
            }
        }
        course.setGradeQuotaRows(null);
    }

    /** 批量装载查询上下文，循环构建课程卡片时不逐个查库。 */
    public void load(Collection<AsCourseClassEntity> courses) {
        List<AsCourseClassEntity> publicCourses = courses.stream().filter(this::isPublic)
                .filter(c -> c.getId() != null).collect(Collectors.toList());
        if (publicCourses.isEmpty()) {
            return;
        }
        List<Long> ids = publicCourses.stream().map(AsCourseClassEntity::getId).distinct().collect(Collectors.toList());
        Map<Long, List<AsCourseGradeQuotaEntity>> rows = quotaMapper.selectList(
                new LambdaQueryWrapper<AsCourseGradeQuotaEntity>()
                        .in(AsCourseGradeQuotaEntity::getCourseClassId, ids)
                        .eq(AsCourseGradeQuotaEntity::getIsValid, 1)
                        .orderByAsc(AsCourseGradeQuotaEntity::getGradeId)).stream()
                .collect(Collectors.groupingBy(AsCourseGradeQuotaEntity::getCourseClassId));
        publicCourses.forEach(c -> c.setGradeQuotaRows(rows.getOrDefault(c.getId(), Collections.emptyList())));
    }

    public List<AsCourseGradeQuotaAO> configuration(AsCourseClassEntity course) {
        if (!isPublic(course)) {
            return Collections.emptyList();
        }
        ensureLoaded(course);
        return course.getGradeQuotaRows().stream().map(row -> {
            AsCourseGradeQuotaAO ao = new AsCourseGradeQuotaAO();
            ao.setGradeId(row.getGradeId());
            ao.setCapacity(row.getCapacity());
            return ao;
        }).collect(Collectors.toList());
    }

    public AsCourseQuotaVO view(AsCourseClassEntity course, AsStudentEntity student) {
        if (!isPublic(course)) {
            return null;
        }
        ensureLoaded(course);
        AsServiceCourseItemAO item = new AsServiceCourseItemAO();
        item.setEnrollType(course.getEnrollType());
        item.setApplicableGradeIds(course.getApplicableGradeIds());
        item.setCapacity(course.getCapacity());
        item.setGradeQuotas(configuration(course));
        AsCourseQuotaVO vo = new AsCourseQuotaVO();
        vo.setConfigured(validateConfiguration(item) == null);
        List<AsCourseGradeQuotaVO> grades = course.getGradeQuotaRows().stream().map(row -> {
            AsCourseGradeQuotaVO grade = new AsCourseGradeQuotaVO();
            grade.setGradeId(row.getGradeId());
            grade.setCapacity(row.getCapacity());
            grade.setEnrolledCount(value(row.getEnrolledCount()));
            grade.setRemainingCount(Math.max(0, value(row.getCapacity()) - value(row.getEnrolledCount())));
            return grade;
        }).collect(Collectors.toList());
        vo.setGradeQuotas(grades);
        vo.setOccupiedCount(grades.stream().mapToInt(AsCourseGradeQuotaVO::getEnrolledCount).sum());
        vo.setRemainingCount(Boolean.TRUE.equals(vo.getConfigured())
                ? Math.max(0, value(course.getCapacity()) - vo.getOccupiedCount()) : 0);
        if (student != null) {
            vo.setStudentGradeId(student.getGradeId());
            vo.setAvailableCount(0);
            for (AsCourseGradeQuotaVO grade : grades) {
                if (Objects.equals(grade.getGradeId(), student.getGradeId())) {
                    vo.setGradeCapacity(grade.getCapacity());
                    vo.setGradeEnrolledCount(grade.getEnrolledCount());
                    vo.setGradeRemainingCount(grade.getRemainingCount());
                    vo.setAvailableCount(Math.min(vo.getRemainingCount(), grade.getRemainingCount()));
                    break;
                }
            }
        }
        return vo;
    }

    /**
     *
     * @param course
     * @param student
     * @return
     */
    public String unavailableReason(AsCourseClassEntity course, AsStudentEntity student) {
        AsCourseQuotaVO vo = view(course, student);
        if (vo == null) {
            return null;
        }
        String scopeError = validateStudentQuotaScope(vo, student);
        if (scopeError != null) {
            return scopeError;
        }
        if (vo.getRemainingCount() <= 0) {
            return "课程班名额已满";
        }
        return vo.getGradeRemainingCount() <= 0 ? "该年级报名名额已满" : null;
    }


    /** 调课只豁免人数上限，不豁免公开课年级配置完整性和学生年级归属。 */
    public String unavailableReasonForStudentAdjust(AsCourseClassEntity course, AsStudentEntity student) {
        AsCourseQuotaVO vo = view(course, student);
        return vo == null ? null : validateStudentQuotaScope(vo, student);
    }

    /** 普通报名与调课共用配置校验，容量判断由各自入口独立执行。 */
    private String validateStudentQuotaScope(AsCourseQuotaVO vo, AsStudentEntity student) {
        if (!Boolean.TRUE.equals(vo.getConfigured())) {
            return "课程班年级名额未配置完整，暂不可报名";
        }
        if (student == null || vo.getGradeCapacity() == null) {
            return "学生年级不在课程名额配置范围内";
        }
        return null;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void reserve(AsCourseClassEntity course, AsStudentEntity student) {
        if (!isPublic(course)) {
            throw failure("非公开课不能使用年级占位入口");
        }
        AsServiceEntity service = serviceMapper.selectById(course.getServiceId());
        if (student == null || service == null || student.getGradeId() == null
                || !Objects.equals(student.getSchoolId(), course.getSchoolId())
                || !Objects.equals(student.getTermId(), service.getTermId())) {
            throw failure("学生学期或年级与课程不匹配");
        }
        // 同一课程的配置、进入与退出共用行锁；唯一成员转换负责防止多入口重复占位。
        lockCourses(Collections.singleton(course.getId()));
        quotaMapper.createMember(course.getId(), student.getId());
        if (quotaMapper.activateMember(course.getId(), student.getId(), student.getGradeId(), student.getTermId()) != 1) {
            throw failure("学生已占用该课程名额，请刷新后重试");
        }
        if (quotaMapper.reserveTotal(course.getId(), student.getGradeId()) != 1) {
            throw failure("课程班名额已满、年级名额未配置或课程已停用，请刷新后重试");
        }
        if (quotaMapper.reserveGrade(course.getId(), student.getGradeId()) != 1) {
            // 不能返回普通错误结果，否则上面的总占位会随外层事务一起提交。
            throw failure("该年级报名名额已满");
        }
    }

    /**
     * 仅供学生调课审核占位：允许超过总额和年级限额，仍按唯一学生成员记账。
     * 使用当前读重验配置；任何一步失败都抛异常，由外层事务回滚全部变更。
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void reserveForStudentAdjust(AsCourseClassEntity course, AsStudentEntity student) {
        if (!isPublic(course) || course.getId() == null) {
            throw failure("非公开课不能使用年级占位入口");
        }
        AsCourseClassEntity current = quotaMapper.currentCourse(course.getId());
        if (!isPublic(current) || !Objects.equals(current.getIsValid(), 1)) {
            throw failure("目标课程已变更，请刷新后重试");
        }
        AsServiceEntity service = serviceMapper.selectById(current.getServiceId());
        if (student == null || service == null || student.getGradeId() == null
                || !Objects.equals(student.getSchoolId(), current.getSchoolId())
                || !Objects.equals(student.getTermId(), service.getTermId())) {
            throw failure("学生学期或年级与课程不匹配");
        }
        current.setGradeQuotaRows(quotaMapper.listCurrent(current.getId()).stream()
                .filter(row -> Objects.equals(row.getIsValid(), 1)).collect(Collectors.toList()));
        String scopeError = unavailableReasonForStudentAdjust(current, student);
        if (scopeError != null) {
            throw failure(scopeError);
        }
        quotaMapper.createMember(current.getId(), student.getId());
        if (quotaMapper.activateMember(current.getId(), student.getId(), student.getGradeId(), student.getTermId()) != 1) {
            throw failure("学生已占用该课程名额，请刷新后重试");
        }
        if (quotaMapper.reserveTotalForStudentAdjust(current.getId(), student.getGradeId()) != 1
                || quotaMapper.reserveGradeForStudentAdjust(current.getId(), student.getGradeId()) != 1) {
            throw failure("调课名额占位失败，课程或年级配置已变更，请刷新后重试");
        }
    }

    /** 返回false只代表不适用新账本的旧课/非公开课，允许原流程正常退出。 */
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean release(Long courseId, Long studentId) {
        AsCourseClassEntity course = courseMapper.selectById(courseId);
        if (!isPublic(course)) {
            return false;
        }
        lockCourses(Collections.singleton(courseId));
        if (quotaMapper.beginRelease(courseId, studentId) == 0) {
            return quotaMapper.hasMember(courseId, studentId) > 0 || quotaMapper.hasQuota(courseId) > 0;
        }
        if (quotaMapper.releaseGrade(courseId, studentId) != 1 || quotaMapper.releaseTotal(courseId) != 1) {
            throw failure("课程名额计数不一致，本次退出未生效，请联系管理员核查");
        }
        quotaMapper.finishRelease(courseId, studentId);
        return true;
    }


    private void ensureLoaded(AsCourseClassEntity course) {
        if (course.getGradeQuotaRows() == null) {
            load(Collections.singleton(course));
        }
    }

    private int value(Integer value) {
        return value == null ? 0 : value;
    }

    private BaseBusinessException failure(String message) {
        return new BaseBusinessException(ResultCode.PARA_ERROR.code(), message);
    }
}
