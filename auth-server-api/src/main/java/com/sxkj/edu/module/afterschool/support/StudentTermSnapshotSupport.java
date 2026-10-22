package com.sxkj.edu.module.afterschool.support;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.exception.BaseBusinessException;
import com.sxkj.edu.domain.sys.entity.mult.TEduclClass;
import com.sxkj.edu.module.afterschool.domain.entity.AsGuardianStudentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentIdentityEntity;
import com.sxkj.edu.module.afterschool.domain.entity.EduclGradeEntity;
import com.sxkj.edu.module.afterschool.domain.entity.EduclTermEntity;
import com.sxkj.edu.module.afterschool.enums.RegisterStatusEnum;
import com.sxkj.edu.module.afterschool.mapper.AsGuardianStudentMapper;
import com.sxkj.edu.module.afterschool.mapper.AsStudentMapper;
import com.sxkj.edu.module.afterschool.mapper.EduclGradeMapper;
import com.sxkj.edu.module.afterschool.mapper.EduclTermMapper;
import com.sxkj.edu.module.afterschool.service.EduclClassService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class StudentTermSnapshotSupport {

    private static final Long SYSTEM_USER_ID = 1L;
    private static final Integer SCHOOL_ROLL_ACTIVE = 1;

    @Resource
    private AsStudentMapper asStudentMapper;
    @Resource
    private AsGuardianStudentMapper asGuardianStudentMapper;
    @Resource
    private EduclTermMapper educlTermMapper;
    @Resource
    private EduclGradeMapper educlGradeMapper;
    @Resource
    private EduclClassService educlClassService;
    @Resource
    private StudentIdentitySupport studentIdentitySupport;

    /**
     * 幂等准备学校目标学期全部符合条件的学生快照。
     *
     * <p>目标快照一旦创建即独立维护，重复调用不会覆盖目标快照资料。</p>
     */
    @Transactional(rollbackFor = Exception.class)
    public List<AsStudentEntity> prepareSchoolTermSnapshots(Long schoolId, Long targetTermId) {
        EduclTermEntity targetTerm = requireTargetTerm(schoolId, targetTermId);
        Map<Long, AsStudentEntity> targets = listTargetSnapshots(schoolId, targetTermId);
        Map<Long, EduclTermEntity> sourceTerms = listPreviousTerms(schoolId, targetTerm);
        if (sourceTerms.isEmpty()) {
            return new ArrayList<>(targets.values());
        }

        List<String> failures = new ArrayList<>();
        Map<Long, AsStudentIdentityEntity> sourceIdentities = new LinkedHashMap<>();
        List<AsStudentEntity> sources = listLatestSourceSnapshots(
                schoolId, sourceTerms, failures, sourceIdentities);
        if (CollectionUtil.isEmpty(sources)) {
            if (!failures.isEmpty()) {
                throw businessError("目标学期学生快照准备失败：" + String.join("；", failures));
            }
            return new ArrayList<>(targets.values());
        }

        for (AsStudentEntity source : sources) {
            if (!SCHOOL_ROLL_ACTIVE.equals(source.getSchoolRollStatus())
                    || !isInheritableRegisterStatus(source.getRegisterStatus())) {
                continue;
            }
            try {
                if (targets.containsKey(source.getStudentIdentityId())) {
                    continue;
                }
                AsStudentEntity target = createSnapshot(
                        source,
                        targetTerm,
                        sourceIdentities.get(source.getStudentIdentityId()));
                if (target != null) {
                    targets.put(target.getStudentIdentityId(), target);
                }
            } catch (BaseBusinessException exception) {
                failures.add(source.getStudentName() + "：" + exception.getErrorMsg());
            }
        }
        if (!failures.isEmpty()) {
            throw businessError("目标学期学生快照准备失败：" + String.join("；", failures));
        }
        log.info("学校[{}]目标学期[{}]学生快照准备完成，共{}条",
                schoolId, targetTermId, targets.size());
        return new ArrayList<>(targets.values());
    }

    /**
     * 查询已经准备完成的目标学期学生快照，不执行新增或升班。
     */
    public AsStudentEntity findPreparedSnapshot(AsStudentEntity sourceStudent,
                                                Long schoolId,
                                                Long targetTermId) {
        AsStudentEntity target = findPreparedSnapshotCandidate(
                sourceStudent, schoolId, targetTermId);
        if (target != null) {
            assertSnapshotEligible(target);
        }
        return target;
    }

    /**
     * 查询可用于候选列表展示的目标学期学生快照。
     *
     * <p>学生不符合学籍或注册条件时返回空，避免一个无效学生影响其他学生展示。</p>
     */
    public AsStudentEntity findEligiblePreparedSnapshot(AsStudentEntity sourceStudent,
                                                        Long schoolId,
                                                        Long targetTermId) {
        AsStudentEntity target = findPreparedSnapshotCandidate(
                sourceStudent, schoolId, targetTermId);
        return isSnapshotEligible(target) ? target : null;
    }

    /**
     * 查询已经准备完成的目标学期学生快照，不校验业务参与资格。
     */
    private AsStudentEntity findPreparedSnapshotCandidate(AsStudentEntity sourceStudent,
                                                          Long schoolId,
                                                          Long targetTermId) {
        if (sourceStudent == null || sourceStudent.getId() == null
                || !Objects.equals(sourceStudent.getSchoolId(), schoolId)
                || !Integer.valueOf(1).equals(sourceStudent.getIsValid())) {
            return null;
        }
        requireTargetTerm(schoolId, targetTermId);
        if (Objects.equals(sourceStudent.getTermId(), targetTermId)) {
            return sourceStudent;
        }
        if (sourceStudent.getStudentIdentityId() == null) {
            return null;
        }
        return studentIdentitySupport.findSnapshot(
                schoolId, targetTermId, sourceStudent.getStudentIdentityId());
    }

    /**
     * 把来源学生解析为目标学期快照，目标不存在时幂等创建。
     */
    @Transactional(rollbackFor = Exception.class)
    public AsStudentEntity resolveOrPrepare(AsStudentEntity sourceStudent,
                                            Long schoolId,
                                            Long targetTermId) {
        if (sourceStudent == null || sourceStudent.getId() == null
                || !Objects.equals(sourceStudent.getSchoolId(), schoolId)
                || !Integer.valueOf(1).equals(sourceStudent.getIsValid())) {
            throw businessError("来源学生不存在或不属于当前学校");
        }
        studentIdentitySupport.resolveOrCreateIdentity(sourceStudent, SYSTEM_USER_ID);
        EduclTermEntity targetTerm = requireTargetTerm(schoolId, targetTermId);
        if (Objects.equals(sourceStudent.getTermId(), targetTermId)) {
            assertSnapshotEligible(sourceStudent);
            return sourceStudent;
        }
        AsStudentEntity target = studentIdentitySupport.findSnapshot(
                schoolId, targetTermId, sourceStudent.getStudentIdentityId());
        if (target != null) {
            assertSnapshotEligible(target);
            return target;
        }
        AsStudentEntity latestSource = resolveLatestSourceStudentByIdentity(
                sourceStudent, targetTerm);
        if (latestSource == null) {
            throw businessError("未找到可用于创建目标学期快照的学生");
        }
        assertSnapshotEligible(latestSource);
        EduclTermEntity sourceTerm = educlTermMapper.selectById(latestSource.getTermId());
        if (sourceTerm == null || !Integer.valueOf(1).equals(sourceTerm.getIsValid())) {
            throw businessError("来源学生学期不存在");
        }
        AsStudentEntity created = createSnapshot(latestSource, targetTerm);
        if (created == null) {
            throw businessError("学生在目标学期已毕业，不能参与课后服务");
        }
        return created;
    }

    /**
     * 根据学校、目标学期和学号执行发布后新增学生的单学生兜底准备。
     */
    @Transactional(rollbackFor = Exception.class)
    public AsStudentEntity resolveOrPrepare(Long schoolId, Long targetTermId, String studentNo) {
        if (StringUtils.isBlank(studentNo)) {
            throw businessError("学生学号不能为空");
        }
        EduclTermEntity targetTerm = requireTargetTerm(schoolId, targetTermId);
        AsStudentEntity source = resolveLatestSourceStudent(schoolId, targetTerm, studentNo);
        if (source == null) {
            throw businessError("未找到可用于创建目标学期快照的学生");
        }
        return resolveOrPrepare(source, schoolId, targetTermId);
    }

    /**
     * 稳定身份关系不按学期复制，本方法仅兼容旧调用并判断是否已有有效家长关系。
     */
    public boolean copyPreviousApprovedGuardianRelations(AsStudentEntity targetStudent) {
        if (targetStudent == null || targetStudent.getStudentIdentityId() == null) {
            return false;
        }
        LambdaQueryWrapper<AsGuardianStudentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsGuardianStudentEntity::getSchoolId, targetStudent.getSchoolId())
                .eq(AsGuardianStudentEntity::getStudentIdentityId,
                        targetStudent.getStudentIdentityId())
                .eq(AsGuardianStudentEntity::getBindStatus, 1)
                .eq(AsGuardianStudentEntity::getIsValid, 1);
        return asGuardianStudentMapper.selectCount(wrapper) > 0;
    }

    /**
     * 校验目标学期有效且属于指定学校。
     */
    private EduclTermEntity requireTargetTerm(Long schoolId, Long targetTermId) {
        if (schoolId == null || targetTermId == null) {
            throw businessError("学校和目标学期不能为空");
        }
        EduclTermEntity targetTerm = educlTermMapper.selectById(targetTermId);
        if (targetTerm == null || !Integer.valueOf(1).equals(targetTerm.getIsValid())
                || !Objects.equals(targetTerm.getSchoolId(), schoolId)) {
            throw businessError("目标学期不存在或不属于当前学校");
        }
        if (targetTerm.getStartDate() == null) {
            throw businessError("目标学期开始日期不能为空");
        }
        return targetTerm;
    }

    /**
     * 查询目标学期之前的全部有效学期。
     */
    private Map<Long, EduclTermEntity> listPreviousTerms(Long schoolId,
                                                        EduclTermEntity targetTerm) {
        LambdaQueryWrapper<EduclTermEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduclTermEntity::getSchoolId, schoolId)
                .eq(EduclTermEntity::getIsValid, 1)
                .ne(EduclTermEntity::getId, targetTerm.getId())
                .lt(EduclTermEntity::getStartDate, targetTerm.getStartDate())
                .orderByDesc(EduclTermEntity::getStartDate)
                .orderByDesc(EduclTermEntity::getId);
        List<EduclTermEntity> terms = educlTermMapper.selectList(wrapper);
        if (CollectionUtil.isEmpty(terms)) {
            return Collections.emptyMap();
        }
        return terms.stream()
                .filter(Objects::nonNull)
                .collect(java.util.stream.Collectors.toMap(
                        EduclTermEntity::getId,
                        item -> item,
                        (first, second) -> first,
                        LinkedHashMap::new));
    }

    /**
     * 按稳定身份选择目标学期之前最新的学生快照。
     *
     * <p>先选择最新快照再判断学籍和注册状态，避免学生已经休学、转学或被驳回后，
     * 又错误回退到更早的在籍档案。</p>
     */
    private List<AsStudentEntity> listLatestSourceSnapshots(
            Long schoolId,
            Map<Long, EduclTermEntity> sourceTerms,
            List<String> failures,
            Map<Long, AsStudentIdentityEntity> sourceIdentities) {
        LambdaQueryWrapper<AsStudentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsStudentEntity::getSchoolId, schoolId)
                .in(AsStudentEntity::getTermId, sourceTerms.keySet())
                .eq(AsStudentEntity::getIsValid, 1)
                .orderByAsc(AsStudentEntity::getId);
        List<AsStudentEntity> candidates = asStudentMapper.selectList(wrapper);
        if (CollectionUtil.isEmpty(candidates)) {
            return Collections.emptyList();
        }

        Map<Long, AsStudentEntity> latestByIdentity = new LinkedHashMap<>();
        for (AsStudentEntity candidate : candidates) {
            try {
                AsStudentIdentityEntity identity =
                        studentIdentitySupport.resolveOrCreateIdentity(
                                candidate, SYSTEM_USER_ID);
                if (candidate.getStudentIdentityId() == null) {
                    failures.add(candidate.getStudentName() + "：学生稳定身份不存在");
                    continue;
                }
                if (identity != null && identity.getId() != null) {
                    sourceIdentities.put(identity.getId(), identity);
                }
                latestByIdentity.merge(
                        candidate.getStudentIdentityId(),
                        candidate,
                        (first, second) -> newerStudentByTerm(
                                first, second, sourceTerms));
            } catch (BaseBusinessException exception) {
                failures.add(candidate.getStudentName() + "：" + exception.getErrorMsg());
            }
        }
        return new ArrayList<>(latestByIdentity.values());
    }

    /**
     * 比较两条同一稳定身份快照，返回所属学期较新的一条。
     */
    private AsStudentEntity newerStudentByTerm(
            AsStudentEntity first,
            AsStudentEntity second,
            Map<Long, EduclTermEntity> termMap) {
        EduclTermEntity firstTerm = termMap.get(first.getTermId());
        EduclTermEntity secondTerm = termMap.get(second.getTermId());
        if (firstTerm == null || firstTerm.getStartDate() == null) {
            return second;
        }
        if (secondTerm == null || secondTerm.getStartDate() == null) {
            return first;
        }
        int comparison = firstTerm.getStartDate().compareTo(secondTerm.getStartDate());
        return comparison == 0
                ? newerStudent(first, second)
                : (comparison > 0 ? first : second);
    }

    /**
     * 按学期开始时间查询最近历史学生快照。
     */
    private AsStudentEntity resolveLatestSourceStudent(Long schoolId,
                                                       EduclTermEntity targetTerm,
                                                       String studentNo) {
        LambdaQueryWrapper<AsStudentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsStudentEntity::getSchoolId, schoolId)
                .eq(AsStudentEntity::getStudentNo, StringUtils.trim(studentNo))
                .eq(AsStudentEntity::getIsValid, 1)
                .ne(AsStudentEntity::getTermId, targetTerm.getId())
                .orderByDesc(AsStudentEntity::getId);
        List<AsStudentEntity> candidates = asStudentMapper.selectList(wrapper);
        if (CollectionUtil.isEmpty(candidates)) {
            return null;
        }
        Map<Long, EduclTermEntity> termMap = mapTerms(candidates);
        return candidates.stream()
                .filter(candidate -> {
                    EduclTermEntity term = termMap.get(candidate.getTermId());
                    return term != null && term.getStartDate() != null
                            && targetTerm.getStartDate() != null
                            && term.getStartDate().before(targetTerm.getStartDate());
                })
                .max(Comparator.comparing(candidate -> termMap.get(candidate.getTermId()).getStartDate()))
                .orElse(null);
    }

    /**
     * 按稳定身份查询目标学期之前最新的学生快照。
     *
     * <p>客户端可能携带较早学期的 studentId，本方法以最新学籍状态为准，
     * 避免旧在籍快照绕过后续学期的休学、转学或注册状态变化。</p>
     */
    private AsStudentEntity resolveLatestSourceStudentByIdentity(
            AsStudentEntity requestedSource,
            EduclTermEntity targetTerm) {
        LambdaQueryWrapper<AsStudentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsStudentEntity::getSchoolId, requestedSource.getSchoolId())
                .eq(AsStudentEntity::getStudentIdentityId,
                        requestedSource.getStudentIdentityId())
                .eq(AsStudentEntity::getIsValid, 1)
                .ne(AsStudentEntity::getTermId, targetTerm.getId())
                .orderByDesc(AsStudentEntity::getId);
        List<AsStudentEntity> candidates = asStudentMapper.selectList(wrapper);
        if (CollectionUtil.isEmpty(candidates)) {
            EduclTermEntity requestedTerm =
                    educlTermMapper.selectById(requestedSource.getTermId());
            return isPreviousValidTerm(
                    requestedTerm, targetTerm, requestedSource.getSchoolId())
                    ? requestedSource : null;
        }
        candidates = new ArrayList<>(candidates);
        boolean containsRequested = candidates.stream()
                .anyMatch(item -> Objects.equals(item.getId(), requestedSource.getId()));
        if (!containsRequested) {
            candidates.add(requestedSource);
        }
        Map<Long, EduclTermEntity> termMap = mapTerms(candidates);
        return candidates.stream()
                .filter(Objects::nonNull)
                .filter(item -> Objects.equals(item.getStudentIdentityId(),
                        requestedSource.getStudentIdentityId()))
                .filter(item -> {
                    EduclTermEntity term = termMap.get(item.getTermId());
                    return isPreviousValidTerm(
                            term, targetTerm, requestedSource.getSchoolId());
                })
                .max(Comparator
                        .comparing((AsStudentEntity item) ->
                                termMap.get(item.getTermId()).getStartDate())
                        .thenComparing(AsStudentEntity::getId,
                                Comparator.nullsFirst(Long::compareTo)))
                .orElse(null);
    }

    /**
     * 判断来源学期是否为同校目标学期之前的有效学期。
     */
    private boolean isPreviousValidTerm(EduclTermEntity sourceTerm,
                                        EduclTermEntity targetTerm,
                                        Long schoolId) {
        return sourceTerm != null
                && Integer.valueOf(1).equals(sourceTerm.getIsValid())
                && Objects.equals(sourceTerm.getSchoolId(), schoolId)
                && sourceTerm.getStartDate() != null
                && targetTerm.getStartDate() != null
                && sourceTerm.getStartDate().before(targetTerm.getStartDate());
    }

    /**
     * 校验学生快照能否参与目标学期业务。
     */
    private void assertSnapshotEligible(AsStudentEntity student) {
        if (!SCHOOL_ROLL_ACTIVE.equals(student.getSchoolRollStatus())) {
            throw businessError("学生当前不在籍，不能参与目标学期业务");
        }
        if (!isInheritableRegisterStatus(student.getRegisterStatus())) {
            throw businessError("学生注册状态不允许生成目标学期档案");
        }
    }

    /**
     * 判断学生快照是否可以参与目标学期业务。
     */
    private boolean isSnapshotEligible(AsStudentEntity student) {
        return student != null
                && SCHOOL_ROLL_ACTIVE.equals(student.getSchoolRollStatus())
                && isInheritableRegisterStatus(student.getRegisterStatus());
    }

    /**
     * 批量查询候选学生所属学期。
     */
    private Map<Long, EduclTermEntity> mapTerms(List<AsStudentEntity> students) {
        if (CollectionUtil.isEmpty(students)) {
            return Collections.emptyMap();
        }
        List<Long> termIds = students.stream()
                .map(AsStudentEntity::getTermId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(java.util.stream.Collectors.toList());
        if (termIds.isEmpty()) {
            return Collections.emptyMap();
        }
        List<EduclTermEntity> terms = educlTermMapper.selectBatchIds(termIds);
        if (CollectionUtil.isEmpty(terms)) {
            return Collections.emptyMap();
        }
        return terms.stream().filter(Objects::nonNull)
                .collect(java.util.stream.Collectors.toMap(
                        EduclTermEntity::getId, item -> item, (first, second) -> first));
    }

    /**
     * 查询目标学期内有效学生快照，并按稳定身份建立索引。
     */
    private Map<Long, AsStudentEntity> listTargetSnapshots(Long schoolId, Long targetTermId) {
        LambdaQueryWrapper<AsStudentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsStudentEntity::getSchoolId, schoolId)
                .eq(AsStudentEntity::getTermId, targetTermId)
                .eq(AsStudentEntity::getIsValid, 1)
                .orderByAsc(AsStudentEntity::getId);
        List<AsStudentEntity> targets = asStudentMapper.selectList(wrapper);
        if (CollectionUtil.isEmpty(targets)) {
            return new LinkedHashMap<>();
        }
        return targets.stream()
                .filter(item -> item.getStudentIdentityId() != null)
                .collect(java.util.stream.Collectors.toMap(
                        AsStudentEntity::getStudentIdentityId,
                        item -> item,
                        this::newerStudent,
                        LinkedHashMap::new));
    }

    /**
     * 创建目标学期快照，并按入学年份计算目标年级和映射同名班级。
     */
    private AsStudentEntity createSnapshot(AsStudentEntity source,
                                           EduclTermEntity targetTerm) {
        return createSnapshot(source, targetTerm, null);
    }

    /**
     * 使用已解析的稳定身份创建目标学期快照，避免批量准备时重复查询身份。
     */
    private AsStudentEntity createSnapshot(AsStudentEntity source,
                                           EduclTermEntity targetTerm,
                                           AsStudentIdentityEntity sourceIdentity) {
        AsStudentEntity target = new AsStudentEntity();
        BeanUtils.copyProperties(source, target);
        target.setId(null);
        target.setTermId(targetTerm.getId());
        target.setUpdateTime(null);
        target.setUpdateUser(null);
        target.setCreateTime(new Date());
        target.setCreateUser(SYSTEM_USER_ID);
        target.setIsValid(1);

        Integer enrollmentYear = resolveEnrollmentYear(source, sourceIdentity);
        int gradeLevel = calculateGradeLevel(enrollmentYear, targetTerm);
        if (gradeLevel > 9) {
            return null;
        }
        EduclGradeEntity targetGrade = resolveTargetGrade(
                source.getSchoolId(), gradeLevel);
        target.setGradeId(targetGrade.getId());
        if (!Objects.equals(source.getGradeId(), targetGrade.getId())) {
            target.setClassId(resolveTargetClassId(source, targetGrade.getId()));
        }

        try {
            if (asStudentMapper.insert(target) != 1) {
                throw businessError("目标学期学生快照创建失败");
            }
            return target;
        } catch (DuplicateKeyException exception) {
            AsStudentEntity concurrent = studentIdentitySupport.findSnapshot(
                    source.getSchoolId(), targetTerm.getId(), source.getStudentIdentityId());
            if (concurrent != null) {
                return concurrent;
            }
            throw exception;
        }
    }

    /**
     * 解析学生入学年份；旧数据缺失时只允许从来源班级初始化。
     */
    private Integer resolveEnrollmentYear(
            AsStudentEntity source,
            AsStudentIdentityEntity sourceIdentity) {
        AsStudentIdentityEntity identity = sourceIdentity == null
                ? studentIdentitySupport.requireIdentity(
                        source.getStudentIdentityId(), source.getSchoolId())
                : sourceIdentity;
        if (identity == null) {
            throw businessError("学生稳定身份不存在，不能自动升班");
        }
        if (identity.getEnrollmentYear() != null) {
            return identity.getEnrollmentYear();
        }
        if (source.getClassId() == null) {
            throw businessError("学生入学年份为空，不能自动升班");
        }
        TEduclClass sourceClass = educlClassService.getById(source.getClassId());
        if (sourceClass == null || sourceClass.getEnrollmentYear() == null) {
            throw businessError("学生入学年份为空，且来源班级未配置入学年份");
        }
        studentIdentitySupport.initializeEnrollmentYear(
                identity, sourceClass.getEnrollmentYear(), SYSTEM_USER_ID);
        return sourceClass.getEnrollmentYear();
    }

    /**
     * 根据一年级基准入学年份和目标学期计算学生目标年级序号。
     */
    private int calculateGradeLevel(Integer enrollmentYear, EduclTermEntity targetTerm) {
        if (enrollmentYear == null || targetTerm == null
                || targetTerm.getStartDate() == null) {
            throw businessError("学生入学年份或目标学期开始日期为空");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(targetTerm.getStartDate());
        int academicYear = calendar.get(Calendar.YEAR);
        if (calendar.get(Calendar.MONTH) < Calendar.SEPTEMBER) {
            academicYear--;
        }
        int gradeLevel = academicYear - enrollmentYear + 1;
        if (gradeLevel < 1) {
            throw businessError("学生入学年份晚于目标学年");
        }
        return gradeLevel;
    }

    /**
     * 按学校和年级序号查询目标年级。
     */
    private EduclGradeEntity resolveTargetGrade(Long schoolId, int gradeLevel) {
        String[] names = {"", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        if (gradeLevel < 1 || gradeLevel >= names.length) {
            throw businessError("学生目标年级序号不正确");
        }
        String gradeName = names[gradeLevel] + "年级";
        LambdaQueryWrapper<EduclGradeEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduclGradeEntity::getSchoolId, schoolId)
                .eq(EduclGradeEntity::getGradeName, gradeName)
                .eq(EduclGradeEntity::getIsValid, 1)
                .last("LIMIT 1");
        EduclGradeEntity grade = educlGradeMapper.selectOne(wrapper);
        if (grade == null) {
            throw businessError("未找到学生目标年级[" + gradeName + "]");
        }
        return grade;
    }

    /**
     * 按来源班级名称和目标年级映射目标班级。
     */
    private Long resolveTargetClassId(AsStudentEntity source, Long targetGradeId) {
        if (source.getClassId() == null) {
            throw businessError("来源班级为空，不能映射目标年级同名班级");
        }
        TEduclClass sourceClass = educlClassService.getById(source.getClassId());
        if (sourceClass == null || StringUtils.isBlank(sourceClass.getClassName())) {
            throw businessError("来源班级不存在，不能生成目标学期学生");
        }
        LambdaQueryWrapper<TEduclClass> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TEduclClass::getSchoolId, source.getSchoolId())
                .eq(TEduclClass::getGradeId, targetGradeId)
                .eq(TEduclClass::getClassName, sourceClass.getClassName())
                .eq(TEduclClass::getIsValid, 1);
        List<TEduclClass> classes = educlClassService.list(wrapper);
        if (CollectionUtil.isEmpty(classes)) {
            throw businessError("目标年级不存在同名班级[" + sourceClass.getClassName() + "]");
        }
        if (classes.size() > 1) {
            throw businessError("目标年级存在多个同名班级["
                    + sourceClass.getClassName() + "]，不能自动升班");
        }
        return classes.get(0).getId();
    }

    /**
     * 稳定身份重复时保留编号较大的学生快照。
     */
    private AsStudentEntity newerStudent(AsStudentEntity first, AsStudentEntity second) {
        if (first.getId() == null) {
            return second;
        }
        if (second.getId() == null) {
            return first;
        }
        return first.getId() >= second.getId() ? first : second;
    }

    /**
     * 判断注册状态是否允许继承到目标学期。
     */
    private boolean isInheritableRegisterStatus(Integer registerStatus) {
        return RegisterStatusEnum.PENDING.getCode().equals(registerStatus)
                || RegisterStatusEnum.APPROVED.getCode().equals(registerStatus)
                || RegisterStatusEnum.APR_ING.getCode().equals(registerStatus);
    }

    /**
     * 构造统一业务异常。
     */
    private BaseBusinessException businessError(String message) {
        return new BaseBusinessException(ResultCode.PARA_ERROR.code(), message);
    }
}
