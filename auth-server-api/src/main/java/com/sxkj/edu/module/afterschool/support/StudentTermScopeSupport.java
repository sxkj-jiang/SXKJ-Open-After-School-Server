package com.sxkj.edu.module.afterschool.support;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.exception.BaseBusinessException;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseClassEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseSessionEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsGuardianStudentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsServiceEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.EduclTermEntity;
import com.sxkj.edu.module.afterschool.mapper.AsCourseClassMapper;
import com.sxkj.edu.module.afterschool.mapper.AsCourseSessionMapper;
import com.sxkj.edu.module.afterschool.mapper.AsGuardianStudentMapper;
import com.sxkj.edu.module.afterschool.mapper.AsServiceMapper;
import com.sxkj.edu.module.afterschool.mapper.AsStudentMapper;
import com.sxkj.edu.module.afterschool.mapper.EduclTermMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 学生学期范围统一解析与校验组件。
 *
 * <p>本组件只负责学校和学期边界，不承载报名、排课、调课等状态规则。</p>
 */
@Component
public class StudentTermScopeSupport {

    @Resource
    private AsServiceMapper asServiceMapper;
    @Resource
    private AsCourseClassMapper asCourseClassMapper;
    @Resource
    private AsCourseSessionMapper asCourseSessionMapper;
    @Resource
    private AsStudentMapper asStudentMapper;
    @Resource
    private AsGuardianStudentMapper asGuardianStudentMapper;
    @Resource
    private EduclTermMapper educlTermMapper;

    /**
     * 根据课后服务解析业务所属学期。
     */
    public StudentTermScope requireServiceScope(Long serviceId) {
        if (serviceId == null) {
            throw businessError("课后服务不能为空");
        }
        AsServiceEntity service = asServiceMapper.selectById(serviceId);
        if (!isValid(service)) {
            throw businessError("课后服务不存在或已失效");
        }
        requireTermInSchool(service.getSchoolId(), service.getTermId());
        return new StudentTermScope(service.getSchoolId(), service.getTermId(),
                service.getId(), null, null);
    }

    /**
     * 根据课程班回溯课后服务并解析业务所属学期。
     */
    public StudentTermScope requireCourseClassScope(Long courseClassId) {
        if (courseClassId == null) {
            throw businessError("课程班不能为空");
        }
        AsCourseClassEntity courseClass = asCourseClassMapper.selectById(courseClassId);
        if (!isValid(courseClass)) {
            throw businessError("课程班不存在或已失效");
        }
        StudentTermScope serviceScope = requireServiceScope(courseClass.getServiceId());
        if (!Objects.equals(courseClass.getSchoolId(), serviceScope.getSchoolId())) {
            throw businessError("课程班与课后服务不属于同一学校");
        }
        return new StudentTermScope(serviceScope.getSchoolId(), serviceScope.getTermId(),
                serviceScope.getServiceId(), courseClass.getId(), null);
    }

    /**
     * 根据课次回溯课程班和课后服务并解析业务所属学期。
     */
    public StudentTermScope requireSessionScope(Long sessionId) {
        if (sessionId == null) {
            throw businessError("课次不能为空");
        }
        AsCourseSessionEntity session = asCourseSessionMapper.selectById(sessionId);
        if (!isValid(session)) {
            throw businessError("课次不存在或已失效");
        }
        StudentTermScope courseScope = requireCourseClassScope(session.getCourseClassId());
        if (!Objects.equals(session.getSchoolId(), courseScope.getSchoolId())) {
            throw businessError("课次与课程班不属于同一学校");
        }
        return new StudentTermScope(courseScope.getSchoolId(), courseScope.getTermId(),
                courseScope.getServiceId(), courseScope.getCourseClassId(), session.getId());
    }

    /**
     * 校验学生快照属于指定学校和学期。
     */
    public AsStudentEntity requireStudentInScope(Long studentId, StudentTermScope scope) {
        if (studentId == null || scope == null) {
            throw businessError("学生或业务学期范围不能为空");
        }
        AsStudentEntity student = asStudentMapper.selectById(studentId);
        if (!isValid(student)) {
            throw businessError("学生不存在或已失效");
        }
        String error = validateStudentInScope(student, scope);
        if (error != null) {
            throw businessError(error);
        }
        return student;
    }


    /**
     * 返回学生学期范围错误，校验通过时返回空。
     */
    public String validateStudentInScope(AsStudentEntity student, StudentTermScope scope) {
        if (!isValid(student)) {
            return "学生不存在或已失效";
        }
        if (scope == null) {
            return "业务学期范围不能为空";
        }
        if (!Objects.equals(student.getSchoolId(), scope.getSchoolId())) {
            return "学生与业务不属于同一学校";
        }
        if (!Objects.equals(student.getTermId(), scope.getTermId())) {
            return CoreConstant.AS_STUDENT_TERM_MISMATCH_MESSAGE;
        }
        return null;
    }


    /**
     * 返回两个业务范围的学校和学期校验错误，校验通过时返回空。
     */
    public String validateSameSchoolAndTerm(StudentTermScope source, StudentTermScope target) {
        if (source == null || target == null
                || !Objects.equals(source.getSchoolId(), target.getSchoolId())
                || !Objects.equals(source.getTermId(), target.getTermId())) {
            return CoreConstant.AS_ADJUST_CROSS_TERM_MESSAGE;
        }
        return null;
    }

    /**
     * 校验学期有效且属于指定学校。
     */
    public EduclTermEntity requireTermInSchool(Long schoolId, Long termId) {
        if (schoolId == null || termId == null) {
            throw businessError("学校和学期不能为空");
        }
        EduclTermEntity term = educlTermMapper.selectById(termId);
        if (!isValid(term) || !Objects.equals(schoolId, term.getSchoolId())) {
            throw businessError("学期不存在或不属于当前学校");
        }
        return term;
    }

    /**
     * 查询学校日期当前学期。
     *
     * <p>保留原方法名是为了兼容现有调用方，实际不再以 term_status 提前切换结果作为业务当前学期。</p>
     */
    public EduclTermEntity requireDefaultTerm(Long schoolId) {
        return requireDateCurrentTerm(schoolId);
    }

    /**
     * 查询学校日期当前学期，日期空档时阻断当前业务。
     */
    public EduclTermEntity requireDateCurrentTerm(Long schoolId) {
        if (schoolId == null) {
            throw businessError("学校不能为空");
        }
        // 改为查询启用状态
        LambdaQueryWrapper<EduclTermEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduclTermEntity::getSchoolId, schoolId)
                .eq(EduclTermEntity::getIsValid, 1)
                .eq(EduclTermEntity::getTermStatus, 1)
                .orderByDesc(EduclTermEntity::getStartDate);
        List<EduclTermEntity> terms = educlTermMapper.selectList(wrapper);
        if (CollectionUtil.isEmpty(terms)) {
            throw businessError(CoreConstant.AS_ACTIVE_TERM_NOT_FOUND_MESSAGE);
        }
        return terms.get(0);
    }

    /**
     * 解析只读页面默认学期。
     *
     * <p>当天没有日期当前学期时返回最近已结束学期，只允许列表、详情和历史回看使用。</p>
     */
    public EduclTermEntity resolveReadableTerm(Long schoolId) {
        if (schoolId == null) {
            throw businessError("学校不能为空");
        }
        // 一个学校每学期只会开启一个学期，包含寒暑假
        LambdaQueryWrapper<EduclTermEntity> currentWrapper = new LambdaQueryWrapper<>();
        currentWrapper.eq(EduclTermEntity::getSchoolId, schoolId)
                .eq(EduclTermEntity::getIsValid, 1)
                .eq(EduclTermEntity::getTermStatus, 1)
                .orderByDesc(EduclTermEntity::getStartDate)
                .last("LIMIT 1");
        List<EduclTermEntity> currentTerms = educlTermMapper.selectList(currentWrapper);
        if (CollectionUtil.isNotEmpty(currentTerms)) {
            return currentTerms.get(0);
        }

        return null;
    }

    /**
     * 解析家长在指定学校和学期绑定的唯一学生快照。
     */
    public AsStudentEntity resolveGuardianStudent(Long parentId, Long schoolId, Long termId) {
        return resolveGuardianStudent(parentId, schoolId, termId, null);
    }

    /**
     * 解析家长指定稳定学生身份在目标学期的学生快照。
     */
    public AsStudentEntity resolveGuardianStudent(Long parentId,
                                                  Long schoolId,
                                                  Long termId,
                                                  Long studentIdentityId) {
        if (parentId == null) {
            throw businessError("家长账号不能为空");
        }
        LambdaQueryWrapper<AsGuardianStudentEntity> relationWrapper = new LambdaQueryWrapper<>();
        relationWrapper.eq(AsGuardianStudentEntity::getGuardianUserId, parentId)
                .eq(AsGuardianStudentEntity::getSchoolId, schoolId)
                .eq(AsGuardianStudentEntity::getBindStatus, 1)
                .eq(AsGuardianStudentEntity::getIsValid, 1);
        relationWrapper.eq(studentIdentityId != null,
                AsGuardianStudentEntity::getStudentIdentityId, studentIdentityId);
        List<AsGuardianStudentEntity> relations = asGuardianStudentMapper.selectList(relationWrapper);
        if (relations == null || relations.isEmpty()) {
            throw businessError("未找到有效的家长学生关系");
        }
        List<Long> identityIds = relations.stream()
                .map(AsGuardianStudentEntity::getStudentIdentityId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (identityIds.isEmpty()) {
            throw businessError("未找到家长绑定的学生");
        }
        LambdaQueryWrapper<AsStudentEntity> studentWrapper = new LambdaQueryWrapper<>();
        studentWrapper.eq(AsStudentEntity::getSchoolId, schoolId)
                .eq(AsStudentEntity::getTermId, termId)
                .in(AsStudentEntity::getStudentIdentityId, identityIds)
                .eq(AsStudentEntity::getIsValid, 1)
                .orderByDesc(AsStudentEntity::getCreateTime)
                .last("LIMIT 1");
        List<AsStudentEntity> students = asStudentMapper.selectList(studentWrapper);
        if (CollectionUtil.isEmpty(students)) {
            throw businessError("未找到家长在该学期唯一绑定的学生");
        }
        return students.get(0);
    }


    /**
     * 批量查询学期名称，供列表、详情和导出统一填充。
     */
    public Map<Long, String> listTermNames(Collection<Long> termIds) {
        if (termIds == null || termIds.isEmpty()) {
            return Collections.emptyMap();
        }
        List<Long> distinctIds = termIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (distinctIds.isEmpty()) {
            return Collections.emptyMap();
        }
        List<EduclTermEntity> terms = educlTermMapper.selectBatchIds(distinctIds);
        Map<Long, String> result = new LinkedHashMap<>();
        if (terms != null) {
            terms.stream().filter(this::isValid)
                    .forEach(term -> result.put(term.getId(), term.getTermName()));
        }
        return result;
    }

    /**
     * 判断基础实体是否有效。
     */
    private boolean isValid(com.sxkj.edu.common.base.domain.BaseEntity entity) {
        return entity != null && Integer.valueOf(1).equals(entity.getIsValid());
    }

    /**
     * 构造统一的业务参数异常。
     */
    private BaseBusinessException businessError(String message) {
        return new BaseBusinessException(ResultCode.PARA_ERROR.code(), message);
    }
}
