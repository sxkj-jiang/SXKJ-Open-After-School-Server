package com.sxkj.edu.module.afterschool.support;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.utils.CheckParamUtil;
import com.sxkj.edu.domain.sys.entity.mult.TEduclClass;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseClassEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsStudentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.EduclGradeEntity;
import com.sxkj.edu.module.afterschool.domain.vo.AsTeacherInviteStudentResDtO;
import com.sxkj.edu.module.afterschool.mapper.AsCourseClassMapper;
import com.sxkj.edu.module.afterschool.mapper.EduclClassMapper;
import com.sxkj.edu.module.afterschool.mapper.EduclGradeMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 课程班适用年级/班级范围解析与校验。
 */
@Component
public class CourseClassScopeSupport {

    @Resource
    private EduclClassMapper educlClassMapper;

    @Resource
    private EduclGradeMapper educlGradeMapper;

    /**
     * 校验课程班适用范围配置是否合法。
     */
    public String validateScope(Long schoolId, String applicableClassIds, String applicableGradeIds) {
        List<Long> classIds;
        List<Long> gradeIds;
        try {
            classIds = parseLongList(applicableClassIds);
            gradeIds = parseLongList(applicableGradeIds);
        } catch (RuntimeException exception) {
            return "适用范围格式不合法";
        }
        if (classIds.isEmpty() && gradeIds.isEmpty()) {
            return "适用年级和适用行政班至少填写一个";
        }
        if (schoolId == null || schoolId <= 0) {
            return "学校不能为空";
        }
        for (Long gradeId : gradeIds) {
            EduclGradeEntity grade = educlGradeMapper.selectById(gradeId);
            if (grade == null || !Objects.equals(grade.getIsValid(), 1)
                    || !Objects.equals(grade.getSchoolId(), schoolId)) {
                return "适用年级不存在或不属于当前学校";
            }
        }
        if (!classIds.isEmpty()) {
            QueryWrapper<TEduclClass> wrapper = new QueryWrapper<>();
            wrapper.in("id", classIds).eq("is_valid", 1).eq("school_id", schoolId);
            int count = educlClassMapper.selectCount(wrapper);
            if (count < classIds.size()) {
                return "适用行政班不存在或不属于当前学校";
            }
        }
        return null;
    }

    /**
     * 解析课程班最终适用的行政班 ID 集合（显式班级 + 年级下全部班级）。
     */
    public List<Long> resolveClassIds(Long schoolId, String applicableClassIds, String applicableGradeIds) {
        LinkedHashSet<Long> classIds = new LinkedHashSet<>(parseLongList(applicableClassIds));
        List<Long> gradeIds = parseLongList(applicableGradeIds);
        if (CheckParamUtil.checkLongIdParam(schoolId) && !gradeIds.isEmpty()) {
            QueryWrapper<TEduclClass> wrapper = new QueryWrapper<>();
            wrapper.in("grade_id", gradeIds).eq("is_valid", 1).eq("school_id", schoolId);
            List<TEduclClass> classes = educlClassMapper.selectList(wrapper);
            if (classes == null) {
                classes = Collections.emptyList();
            }
            for (TEduclClass clazz : classes) {
                if (clazz.getId() != null) {
                    classIds.add(clazz.getId());
                }
            }
        }
        return new ArrayList<>(classIds);
    }


    /**
     * 按课程类型解析最终适用班级。
     * 行政班安排是强制到具体行政班；已选择行政班时，不再用年级扩展到全年级。
     */
    public List<Long> resolveClassIds(Long schoolId, AsCourseClassEntity course) {
        if (course == null) {
            return Collections.emptyList();
        }
        List<Long> classIds = parseLongList(course.getApplicableClassIds());
        if (Objects.equals(course.getEnrollType(), CoreConstant.ENROLL_TYPE_CLASS_ASSIGN) && !classIds.isEmpty()) {
            return classIds;
        }
        return resolveClassIds(schoolId, course.getApplicableClassIds(), course.getApplicableGradeIds());
    }

    public boolean isStudentInCourseScope(AsStudentEntity student, AsCourseClassEntity course) {
        if (student == null || course == null || !Objects.equals(student.getIsValid(), 1)
                || !Objects.equals(course.getIsValid(), 1)) {
            return false;
        }
        List<Long> classIds = parseLongList(course.getApplicableClassIds());
        if (!classIds.isEmpty() && classIds.contains(student.getClassId())) {
            return true;
        }
        if (Objects.equals(course.getEnrollType(), CoreConstant.ENROLL_TYPE_CLASS_ASSIGN) && !classIds.isEmpty()) {
            return false;
        }
        List<Long> gradeIds = parseLongList(course.getApplicableGradeIds());
        return !gradeIds.isEmpty() && gradeIds.contains(student.getGradeId());
    }

    /**
     * 判断学生是否落在服务下任一课程班的适用范围内。
     */
    public boolean isStudentInServiceScope(Long schoolId, Long serviceId, AsStudentEntity student,
                                           AsCourseClassMapper asCourseClassMapper) {
        if (schoolId == null || serviceId == null || student == null
                || !Objects.equals(student.getSchoolId(), schoolId)) {
            return false;
        }
        List<AsCourseClassEntity> courses = listServiceCourses(serviceId, asCourseClassMapper);
        if (courses.isEmpty()) {
            return false;
        }
        for (AsCourseClassEntity course : courses) {
            if (isStudentInCourseScope(student, course)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 汇总服务下所有课程班解析后的适用行政班 ID。
     */
    public List<Long> listServiceScopeClassIds(Long schoolId, Long serviceId, AsCourseClassMapper asCourseClassMapper) {
        LinkedHashSet<Long> classIds = new LinkedHashSet<>();
        for (AsCourseClassEntity course : listServiceCourses(serviceId, asCourseClassMapper)) {
            classIds.addAll(resolveClassIds(schoolId, course));
        }
        return new ArrayList<>(classIds);
    }

    public List<AsCourseClassEntity> listServiceCourses(Long serviceId, AsCourseClassMapper asCourseClassMapper) {
        LambdaQueryWrapper<AsCourseClassEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsCourseClassEntity::getIsValid, 1)
                .eq(AsCourseClassEntity::getServiceId, serviceId);
        List<AsCourseClassEntity> courses = asCourseClassMapper.selectList(wrapper);
        return courses == null ? Collections.emptyList() : courses;
    }

    public List<Long> parseLongList(String json) {
        if (!StringUtils.hasText(json)) {
            return Collections.emptyList();
        }
        try {
            return JSON.parseArray(json, Long.class).stream()
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());
        } catch (Exception ignore) {
            String clean = json.replace("[", "").replace("]", "").replace("\"", "");
            if (!StringUtils.hasText(clean)) {
                return Collections.emptyList();
            }
            List<Long> result = new ArrayList<>();
            for (String item : clean.split(",")) {
                if (StringUtils.hasText(item)) {
                    result.add(Long.valueOf(item.trim()));
                }
            }
            return result.stream().distinct().collect(Collectors.toList());
        }
    }
}
