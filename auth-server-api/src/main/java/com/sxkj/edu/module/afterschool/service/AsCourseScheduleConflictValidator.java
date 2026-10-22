package com.sxkj.edu.module.afterschool.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.module.afterschool.domain.dto.AsCourseScheduleRuleAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsServiceCourseItemAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseClassEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseScheduleRuleEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsServiceEntity;
import com.sxkj.edu.module.afterschool.domain.vo.CourseScheduleConflictDetail;
import com.sxkj.edu.module.afterschool.domain.vo.CourseScheduleConflictResult;
import com.sxkj.edu.module.afterschool.enums.CourseScheduleConflictTypeEnum;
import com.sxkj.edu.module.afterschool.mapper.AsCourseClassMapper;
import com.sxkj.edu.module.afterschool.mapper.AsCourseScheduleRuleMapper;
import com.sxkj.edu.module.afterschool.mapper.AsServiceMapper;
import com.sxkj.edu.module.afterschool.support.CourseScheduleSessionExpander;
import com.sxkj.edu.module.afterschool.support.CourseScheduleSlot;
import com.sxkj.edu.module.afterschool.support.TermSuspensionDateSupport;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 课后服务课程排课冲突校验器。
 */
@Service
public class AsCourseScheduleConflictValidator {

    @Resource
    private AsCourseClassMapper asCourseClassMapper;

    @Resource
    private AsCourseScheduleRuleMapper asCourseScheduleRuleMapper;

    @Resource
    private AsServiceMapper asServiceMapper;

    @Resource
    private TermSuspensionDateSupport termSuspensionDateSupport;

    public CourseScheduleConflictResult validateForSave(AsServiceEntity service, List<AsServiceCourseItemAO> requestCourses) {
        if (service == null || CollectionUtils.isEmpty(requestCourses)) {
            return CourseScheduleConflictResult.ok();
        }
        Set<Long> requestCourseIds = requestCourses.stream()
                .map(AsServiceCourseItemAO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<LocalDate> suspensionDates = termSuspensionDateSupport.listSuspensionDates(service);
        List<CourseScheduleSlot> requestSlots = expandRequestCourses(service, requestCourses, suspensionDates);
        List<CourseScheduleSlot> existingSlots = loadExistingSlots(service, requestCourseIds);
        return detectConflicts(requestSlots, existingSlots);
    }

    /**
     * 校验指定服务下全部已保存课程是否存在排课冲突，供发布前二次校验使用。
     */
    public CourseScheduleConflictResult validateSavedCourses(AsServiceEntity service, List<AsCourseClassEntity> courses) {
        if (service == null || CollectionUtils.isEmpty(courses)) {
            return CourseScheduleConflictResult.ok();
        }
        Set<Long> courseIds = courses.stream().map(AsCourseClassEntity::getId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<LocalDate> suspensionDates = termSuspensionDateSupport.listSuspensionDates(service);
        List<CourseScheduleSlot> requestSlots = expandSavedCourses(service, courses, suspensionDates);
        List<CourseScheduleSlot> existingSlots = loadExistingSlots(service, courseIds);
        return detectConflicts(requestSlots, existingSlots);
    }

    private List<CourseScheduleSlot> expandRequestCourses(AsServiceEntity service,
                                                          List<AsServiceCourseItemAO> requestCourses,
                                                          Set<LocalDate> suspensionDates) {
        List<CourseScheduleSlot> slots = new ArrayList<>();
        for (AsServiceCourseItemAO item : requestCourses) {
            AsCourseClassEntity course = toTransientCourse(service, item);
            slots.addAll(CourseScheduleSessionExpander.expand(
                    service, course, item.getScheduleRules(), true, suspensionDates));
        }
        return slots;
    }

    private List<CourseScheduleSlot> expandSavedCourses(AsServiceEntity service,
                                                        List<AsCourseClassEntity> courses,
                                                        Set<LocalDate> suspensionDates) {
        Map<Long, List<AsCourseScheduleRuleEntity>> ruleMap = mapRulesByCourse(
                courses.stream().map(AsCourseClassEntity::getId).collect(Collectors.toList()));
        List<CourseScheduleSlot> slots = new ArrayList<>();
        for (AsCourseClassEntity course : courses) {
            List<AsCourseScheduleRuleEntity> rules = ruleMap.getOrDefault(course.getId(), Collections.emptyList());
            slots.addAll(CourseScheduleSessionExpander.expandFromEntities(
                    service, course, rules, true, suspensionDates));
        }
        return slots;
    }

    private List<CourseScheduleSlot> loadExistingSlots(AsServiceEntity currentService, Set<Long> excludeCourseIds) {
        if (currentService == null || currentService.getSchoolId() == null) {
            return Collections.emptyList();
        }
        QueryWrapper<AsCourseClassEntity> courseWrapper = new QueryWrapper<>();
        courseWrapper.eq("is_valid", 1).eq("school_id", currentService.getSchoolId());
        if (!CollectionUtils.isEmpty(excludeCourseIds)) {
            courseWrapper.notIn("id", excludeCourseIds);
        }
        List<AsCourseClassEntity> courses = asCourseClassMapper.selectList(courseWrapper);
        if (CollectionUtils.isEmpty(courses)) {
            return Collections.emptyList();
        }

        Map<Long, AsServiceEntity> serviceMap = mapServices(courses.stream().map(AsCourseClassEntity::getServiceId).collect(Collectors.toSet()));
        Map<Long, List<AsCourseScheduleRuleEntity>> ruleMap = mapRulesByCourse(
                courses.stream().map(AsCourseClassEntity::getId).collect(Collectors.toList()));

        Map<String, Set<LocalDate>> suspensionDatesByScope = loadSuspensionDatesByCalendarScope(
                serviceMap.values(), currentService);
        List<CourseScheduleSlot> slots = new ArrayList<>();
        for (AsCourseClassEntity course : courses) {
            AsServiceEntity courseService = serviceMap.get(course.getServiceId());
            if (!isComparableService(courseService, currentService)) {
                continue;
            }
            Set<LocalDate> suspensionDates = suspensionDatesByScope.getOrDefault(
                    calendarScopeKey(courseService), Collections.emptySet());
            List<AsCourseScheduleRuleEntity> rules = ruleMap.getOrDefault(course.getId(), Collections.emptyList());
            slots.addAll(CourseScheduleSessionExpander.expandFromEntities(
                    courseService, course, rules, false, suspensionDates));
        }
        return slots;
    }

    /**
     * 同一次冲突校验中每个学期只查询一次停课日期，避免按服务逐条查询校历。
     */
    private Map<String, Set<LocalDate>> loadSuspensionDatesByCalendarScope(
            Collection<AsServiceEntity> services, AsServiceEntity currentService) {
        Map<String, List<AsServiceEntity>> servicesByTerm = services.stream()
                .filter(service -> isComparableService(service, currentService))
                .filter(service -> service.getSchoolId() != null && service.getTermId() != null)
                .collect(Collectors.groupingBy(this::calendarScopeKey));
        Map<String, Set<LocalDate>> result = new HashMap<>();
        for (Map.Entry<String, List<AsServiceEntity>> entry : servicesByTerm.entrySet()) {
            Date rangeStart = null;
            Date rangeEnd = null;
            for (AsServiceEntity service : entry.getValue()) {
                if (service.getCourseStartDate() != null
                        && (rangeStart == null || service.getCourseStartDate().before(rangeStart))) {
                    rangeStart = service.getCourseStartDate();
                }
                if (service.getCourseEndDate() != null
                        && (rangeEnd == null || service.getCourseEndDate().after(rangeEnd))) {
                    rangeEnd = service.getCourseEndDate();
                }
            }
            AsServiceEntity representative = entry.getValue().get(0);
            result.put(entry.getKey(), termSuspensionDateSupport.listSuspensionDates(
                    representative.getSchoolId(), representative.getTermId(), rangeStart, rangeEnd));
        }
        return result;
    }

    private String calendarScopeKey(AsServiceEntity service) {
        if (service == null || service.getSchoolId() == null || service.getTermId() == null) {
            return "";
        }
        return service.getSchoolId() + ":" + service.getTermId();
    }

    private CourseScheduleConflictResult detectConflicts(List<CourseScheduleSlot> requestSlots,
                                                         List<CourseScheduleSlot> existingSlots) {
        if (CollectionUtils.isEmpty(requestSlots)) {
            return CourseScheduleConflictResult.ok();
        }
        LinkedHashSet<String> dedupeKeys = new LinkedHashSet<>();
        List<CourseScheduleConflictDetail> conflicts = new ArrayList<>();

        detectInternalConflicts(requestSlots, dedupeKeys, conflicts);
        detectCrossConflicts(requestSlots, existingSlots, dedupeKeys, conflicts);

        if (conflicts.isEmpty()) {
            return CourseScheduleConflictResult.ok();
        }
        CourseScheduleConflictResult result = new CourseScheduleConflictResult();
        result.setHasConflict(true);
        result.setConflicts(conflicts);
        return result;
    }

    private void detectInternalConflicts(List<CourseScheduleSlot> requestSlots,
                                         Set<String> dedupeKeys,
                                         List<CourseScheduleConflictDetail> conflicts) {
        for (int i = 0; i < requestSlots.size(); i++) {
            CourseScheduleSlot current = requestSlots.get(i);
            for (int j = i + 1; j < requestSlots.size(); j++) {
                CourseScheduleSlot target = requestSlots.get(j);
                appendConflictIfNeeded(current, target, dedupeKeys, conflicts);
            }
        }
    }

    private void detectCrossConflicts(List<CourseScheduleSlot> requestSlots,
                                      List<CourseScheduleSlot> existingSlots,
                                      Set<String> dedupeKeys,
                                      List<CourseScheduleConflictDetail> conflicts) {
        for (CourseScheduleSlot current : requestSlots) {
            for (CourseScheduleSlot existing : existingSlots) {
                appendConflictIfNeeded(current, existing, dedupeKeys, conflicts);
            }
        }
    }

    private void appendConflictIfNeeded(CourseScheduleSlot left,
                                        CourseScheduleSlot right,
                                        Set<String> dedupeKeys,
                                        List<CourseScheduleConflictDetail> conflicts) {
        if (!CourseScheduleSessionExpander.sameDate(left.getSessionDate(), right.getSessionDate())
                || !CourseScheduleSessionExpander.timeOverlap(left.getStartTime(), left.getEndTime(),
                right.getStartTime(), right.getEndTime())) {
            return;
        }
        if (left.getCourseClassId() != null
                && Objects.equals(left.getCourseClassId(), right.getCourseClassId())) {
            addConflict(conflicts, dedupeKeys, left, right, CourseScheduleConflictTypeEnum.SAME_COURSE_DUPLICATE);
            return;
        }
        if (left.getTeacherId() != null && Objects.equals(left.getTeacherId(), right.getTeacherId())) {
            addConflict(conflicts, dedupeKeys, left, right, resolveTeacherConflictType(left, right));
        }
        if (hasPlaceConflict(left, right)) {
            addConflict(conflicts, dedupeKeys, left, right, resolvePlaceConflictType(left, right));
        }
        if (hasClassAssignScopeConflict(left, right)) {
            addConflict(conflicts, dedupeKeys, left, right, resolveClassScopeConflictType(left, right));
        }
    }

    /**
     * 地点冲突仅在双方（或单方设置了地点的一方）有明确地点，且地点相同时才触发。
     */
    private boolean hasPlaceConflict(CourseScheduleSlot left, CourseScheduleSlot right) {
        if (!StringUtils.hasText(left.getPlaceName()) || !StringUtils.hasText(right.getPlaceName())) {
            return false;
        }
        return Objects.equals(left.getPlaceName(), right.getPlaceName());
    }

    private CourseScheduleConflictTypeEnum resolveTeacherConflictType(CourseScheduleSlot left, CourseScheduleSlot right) {
        if (left.isFromRequest() && right.isFromRequest()) {
            return CourseScheduleConflictTypeEnum.TEACHER_TIME_INTERNAL;
        }
        return CourseScheduleConflictTypeEnum.TEACHER_TIME_WITH_EXISTING;
    }

    private CourseScheduleConflictTypeEnum resolvePlaceConflictType(CourseScheduleSlot left, CourseScheduleSlot right) {
        if (left.isFromRequest() && right.isFromRequest()) {
            return CourseScheduleConflictTypeEnum.PLACE_TIME_INTERNAL;
        }
        return CourseScheduleConflictTypeEnum.PLACE_TIME_WITH_EXISTING;
    }

    private CourseScheduleConflictTypeEnum resolveClassScopeConflictType(CourseScheduleSlot left, CourseScheduleSlot right) {
        if (left.isFromRequest() && right.isFromRequest()) {
            return CourseScheduleConflictTypeEnum.CLASS_SCOPE_TIME_INTERNAL;
        }
        return CourseScheduleConflictTypeEnum.CLASS_SCOPE_TIME_WITH_EXISTING;
    }

    private boolean hasClassAssignScopeConflict(CourseScheduleSlot left, CourseScheduleSlot right) {
        if (!isClassAssignCourse(left) || !isClassAssignCourse(right)) {
            return false;
        }
        Set<Long> leftClassIds = parseIdSet(left.getApplicableClassIds());
        Set<Long> rightClassIds = parseIdSet(right.getApplicableClassIds());
        return !leftClassIds.isEmpty() && !rightClassIds.isEmpty() && hasIntersection(leftClassIds, rightClassIds);
    }

    private boolean isClassAssignCourse(CourseScheduleSlot slot) {
        return slot != null && Objects.equals(slot.getEnrollType(), CoreConstant.ENROLL_TYPE_CLASS_ASSIGN);
    }

    private boolean hasIntersection(Set<Long> left, Set<Long> right) {
        for (Long item : left) {
            if (right.contains(item)) {
                return true;
            }
        }
        return false;
    }

    private Set<Long> parseIdSet(String json) {
        if (!StringUtils.hasText(json)) {
            return Collections.emptySet();
        }
        try {
            return JSON.parseArray(json, Long.class).stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
        } catch (Exception ex) {
            return Collections.emptySet();
        }
    }

    private void addConflict(List<CourseScheduleConflictDetail> conflicts,
                             Set<String> dedupeKeys,
                             CourseScheduleSlot left,
                             CourseScheduleSlot right,
                             CourseScheduleConflictTypeEnum type) {
        CourseScheduleSlot requestSlot = left.isFromRequest() ? left : right;
        CourseScheduleSlot otherSlot = left.isFromRequest() ? right : left;
        if (!requestSlot.isFromRequest()) {
            return;
        }
        String dedupeKey = type.getCode() + "|"
                + slotKey(requestSlot) + "|"
                + slotKey(otherSlot);
        if (!dedupeKeys.add(dedupeKey)) {
            return;
        }
        String date = CourseScheduleSessionExpander.formatDate(requestSlot.getSessionDate());
        String timeRange = CourseScheduleSessionExpander.formatTimeRange(requestSlot.getStartTime(), requestSlot.getEndTime());
        String message;
        if (type == CourseScheduleConflictTypeEnum.SAME_COURSE_DUPLICATE) {
            message = type.formatMessage(requestSlot.getCourseClassName(), null, null, date, timeRange);
        } else if (type == CourseScheduleConflictTypeEnum.PLACE_TIME_INTERNAL
                || type == CourseScheduleConflictTypeEnum.PLACE_TIME_WITH_EXISTING) {
            message = type.formatMessage(requestSlot.getCourseClassName(), otherSlot.getCourseClassName(),
                    requestSlot.getPlaceName(), date, timeRange);
        } else {
            message = type.formatMessage(requestSlot.getCourseClassName(), otherSlot.getCourseClassName(),
                    null, date, timeRange);
        }
        CourseScheduleConflictDetail detail = new CourseScheduleConflictDetail();
        detail.setConflictType(type);
        detail.setMessage(message);
        detail.setCourseClassId(requestSlot.getCourseClassId());
        detail.setCourseClassName(requestSlot.getCourseClassName());
        detail.setConflictCourseClassId(otherSlot.getCourseClassId());
        detail.setConflictCourseClassName(otherSlot.getCourseClassName());
        conflicts.add(detail);
    }

    private String slotKey(CourseScheduleSlot slot) {
        return String.valueOf(slot.getCourseClassId()) + "|"
                + CourseScheduleSessionExpander.formatDate(slot.getSessionDate()) + "|"
                + slot.getStartTime() + "|" + slot.getEndTime();
    }

    private AsCourseClassEntity toTransientCourse(AsServiceEntity service, AsServiceCourseItemAO item) {
        AsCourseClassEntity course = new AsCourseClassEntity();
        course.setId(item.getId());
        course.setSchoolId(service.getSchoolId());
        course.setServiceId(service.getId());
        course.setCourseClassName(item.getCourseClassName());
        course.setTeacherId(item.getTeacherId());
        course.setPlaceId(item.getPlaceId());
        course.setEnrollType(item.getEnrollType());
        course.setApplicableClassIds(item.getApplicableClassIds());
        course.setApplicableGradeIds(item.getApplicableGradeIds());
        return course;
    }

    private Map<Long, List<AsCourseScheduleRuleEntity>> mapRulesByCourse(Collection<Long> courseIds) {
        List<Long> ids = courseIds.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        QueryWrapper<AsCourseScheduleRuleEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("is_valid", 1).in("course_class_id", ids).orderByAsc("id");
        return asCourseScheduleRuleMapper.selectList(wrapper).stream()
                .collect(Collectors.groupingBy(AsCourseScheduleRuleEntity::getCourseClassId, LinkedHashMap::new, Collectors.toList()));
    }

    private Map<Long, AsServiceEntity> mapServices(Collection<Long> serviceIds) {
        List<Long> ids = serviceIds.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        return asServiceMapper.selectBatchIds(ids).stream()
                .filter(this::isValidService)
                .collect(Collectors.toMap(AsServiceEntity::getId, item -> item, (a, b) -> a));
    }

    private boolean isValidService(AsServiceEntity entity) {
        return entity != null && Objects.equals(entity.getIsValid(), 1);
    }

    private boolean isComparableService(AsServiceEntity existingService, AsServiceEntity currentService) {
        if (!isValidService(existingService)) {
            return false;
        }
        if (Objects.equals(existingService.getServiceStatus(), CoreConstant.SERVICE_STATUS_FINISHED)) {
            return false;
        }
        return isSameService(existingService, currentService) || isCoursePeriodOverlap(existingService, currentService);
    }

    private boolean isSameService(AsServiceEntity existingService, AsServiceEntity currentService) {
        return existingService != null
                && currentService != null
                && existingService.getId() != null
                && Objects.equals(existingService.getId(), currentService.getId());
    }

    private boolean isCoursePeriodOverlap(AsServiceEntity left, AsServiceEntity right) {
        if (left == null || right == null
                || left.getCourseStartDate() == null || left.getCourseEndDate() == null
                || right.getCourseStartDate() == null || right.getCourseEndDate() == null) {
            return true;
        }
        return !left.getCourseStartDate().after(right.getCourseEndDate())
                && !left.getCourseEndDate().before(right.getCourseStartDate());
    }
}
