package com.sxkj.edu.module.afterschool.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.config.AssembleUtil;
import com.sxkj.edu.config.handle.business.EnrollmentTimeSlot;
import com.sxkj.edu.config.handle.business.ScheduleConflict;
import com.sxkj.edu.config.handle.business.GuardianAvailableCourseReadContext;
import com.sxkj.edu.module.afterschool.domain.vo.AsEnrollmentOccupiedSlotData;
import com.sxkj.edu.module.afterschool.domain.entity.*;
import com.sxkj.edu.module.afterschool.mapper.*;
import com.sxkj.edu.module.afterschool.support.CourseClassScopeSupport;
import com.sxkj.edu.module.afterschool.support.SessionEnrollmentSupport;
import com.sxkj.edu.module.afterschool.support.StudentTermScopeSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 课后服务时段计算服务：时段映射、占用判断、课次筛选、冲突检测。
 *
 * @author system
 */
@Service
@Slf4j
public class AsEnrollmentTimeSlotService {

    @Resource
    private AsEnrollmentMapper asEnrollmentMapper;
    @Resource
    private AsCourseSessionMapper asCourseSessionMapper;
    @Resource
    private AsScheduleTemplateMapper asScheduleTemplateMapper;
    @Resource
    private AsSchedulePeriodMapper asSchedulePeriodMapper;
    @Resource
    private AsStudentDestinationMapper asStudentDestinationMapper;
    @Resource
    private AsServiceMapper asServiceMapper;
    @Resource
    private AsCourseClassMapper asCourseClassMapper;
    @Resource
    private AsStudentMapper asStudentMapper;
    @Resource
    private SessionEnrollmentSupport sessionEnrollmentSupport;
    @Resource
    private CourseClassScopeSupport courseClassScopeSupport;

    @Resource
    private AsCourseScheduleRuleMapper asCourseScheduleRuleMapper;

    // ======================== 时段键构建 ========================

    /** 构造课次时段键。 */
    public String buildEnrollmentTimeSlotKey(AsCourseSessionEntity session) {
        Integer weekDay = session.getWeekDay();
        Integer startMinute = minutesOfDay(session.getStartTime());
        Integer endMinute = minutesOfDay(session.getEndTime());
        if (weekDay != null && startMinute != null && endMinute != null) {
            return buildWeeklyTimeSlotKey(weekDay, startMinute, endMinute);
        }
        String date = session.getSessionDate() == null ? String.valueOf(session.getId())
                : new SimpleDateFormat("yyyy-MM-dd").format(session.getSessionDate());
        return "date:" + date + ":" + weekDay + ":" + startMinute + ":" + endMinute;
    }

    public String buildWeeklyTimeSlotKey(Integer weekDay, Integer startMinute, Integer endMinute) {
        return "time:" + weekDay + ":" + startMinute + ":" + endMinute;
    }

    public String buildConcreteSessionTimeKey(AsCourseSessionEntity session) {
        return buildConcreteSessionTimeKey(toEnrollmentTimeSlot(session));
    }

    public String buildConcreteSessionTimeKey(EnrollmentTimeSlot slot) {
        if (slot == null || !StringUtils.hasText(slot.getSlotKey())) {
            return null;
        }
        if (slot.getSessionDate() == null) {
            return slot.getSlotKey();
        }
        return "session:" + new SimpleDateFormat("yyyy-MM-dd").format(slot.getSessionDate())
                + ":" + slot.getStartMinute() + ":" + slot.getEndMinute();
    }

    public String serviceStudentKey(AsEnrollmentEntity enrollment) {
        return enrollment == null ? null : serviceStudentKey(enrollment.getSchoolId(), enrollment.getServiceId(), enrollment.getStudentId());
    }

    public String serviceStudentKey(Long schoolId, Long serviceId, Long studentId) {
        return schoolId + "_" + serviceId + "_" + studentId;
    }

    public String serviceStudentDestinationKey(Long schoolId, Long serviceId, Long studentId, Integer destinationType) {
        return schoolId + "_" + serviceId + "_" + studentId + "_" + destinationType;
    }

    public String studentTermKey(Long schoolId, AsServiceEntity service, Long studentId) {
        if (schoolId == null || service == null || service.getTermId() == null || studentId == null) {
            return null;
        }
        return schoolId + "_" + service.getTermId() + "_" + studentId;
    }

    public String studentCourseKey(Long studentId, Long courseClassId) {
        return String.valueOf(studentId) + ":" + String.valueOf(courseClassId);
    }

    // ======================== 服务时段映射 ========================

    /** 加载服务时段，优先使用模板，回退到课次。 */
    public Map<Long, List<EnrollmentTimeSlot>> mapServiceTimeSlots(Collection<Long> serviceIds,
                                                                    Map<Long, AsServiceEntity> preloadedServiceMap) {
        List<Long> ids = serviceIds == null ? Collections.emptyList() : serviceIds.stream()
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<Long, List<EnrollmentTimeSlot>> templateSlots = mapAfterSchoolTemplateTimeSlots(ids, preloadedServiceMap);
        List<Long> fallbackIds = ids.stream()
                .filter(id -> CollectionUtils.isEmpty(templateSlots.get(id))).collect(Collectors.toList());
        if (fallbackIds.isEmpty()) {
            return templateSlots;
        }
        Map<Long, List<EnrollmentTimeSlot>> result = new HashMap<>(templateSlots);
        result.putAll(mapCourseSessionServiceTimeSlots(fallbackIds));
        return result;
    }

    /** 未选课列表只使用模板范围内真实存在且尚未结束的课次。 */
    public Map<Long, List<EnrollmentTimeSlot>> mapAssignableConcreteServiceTimeSlots(
            Collection<Long> serviceIds, Map<Long, AsServiceEntity> preloadedServiceMap) {
        List<Long> ids = serviceIds == null ? Collections.emptyList() : serviceIds.stream()
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<Long, List<EnrollmentTimeSlot>> templateSlotMap = mapAfterSchoolTemplateTimeSlots(ids, preloadedServiceMap);
        Map<Long, List<EnrollmentTimeSlot>> concreteSlotMap = mapCourseSessionServiceTimeSlots(ids);
        if (templateSlotMap.isEmpty() || concreteSlotMap.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<Long, List<EnrollmentTimeSlot>> result = new HashMap<>();
        for (Long serviceId : ids) {
            List<EnrollmentTimeSlot> templateSlots = templateSlotMap.get(serviceId);
            List<EnrollmentTimeSlot> concreteSlots = concreteSlotMap.get(serviceId);
            if (CollectionUtils.isEmpty(templateSlots) || CollectionUtils.isEmpty(concreteSlots)) {
                continue;
            }
            Map<String, List<EnrollmentTimeSlot>> templateBySlotKey = templateSlots.stream()
                    .filter(Objects::nonNull).filter(s -> StringUtils.hasText(s.getSlotKey()))
                    .collect(Collectors.groupingBy(EnrollmentTimeSlot::getSlotKey));
            List<EnrollmentTimeSlot> assignableSlots = concreteSlots.stream()
                    .filter(this::isAdminAssignableSessionTime)
                    .map(slot -> copyConcreteSlotWithTemplateScope(slot, templateBySlotKey.get(slot.getSlotKey())))
                    .filter(Objects::nonNull).collect(Collectors.toList());
            if (!assignableSlots.isEmpty()) {
                result.put(serviceId, sortSlots(assignableSlots));
            }
        }
        return result;
    }

    private EnrollmentTimeSlot copyConcreteSlotWithTemplateScope(EnrollmentTimeSlot concreteSlot,
                                                                  List<EnrollmentTimeSlot> matchedTemplateSlots) {
        if (concreteSlot == null || CollectionUtils.isEmpty(matchedTemplateSlots)) {
            return null;
        }
        boolean unrestricted = matchedTemplateSlots.stream()
                .anyMatch(s -> CollectionUtils.isEmpty(s.getGradeIds()));
        List<Long> gradeIds = unrestricted ? Collections.emptyList() : matchedTemplateSlots.stream()
                .filter(Objects::nonNull).filter(s -> !CollectionUtils.isEmpty(s.getGradeIds()))
                .flatMap(s -> s.getGradeIds().stream()).filter(Objects::nonNull).distinct()
                .collect(Collectors.toList());
        return new EnrollmentTimeSlot(concreteSlot.getSessionId(), concreteSlot.getSlotKey(),
                concreteSlot.getSessionDate(), concreteSlot.getWeekDay(), concreteSlot.getStartMinute(),
                concreteSlot.getEndMinute(), concreteSlot.getDisplayText(), gradeIds);
    }

    /** 加载课后时间模板时段。 */
    public Map<Long, List<EnrollmentTimeSlot>> mapAfterSchoolTemplateTimeSlots(
            Collection<Long> serviceIds, Map<Long, AsServiceEntity> preloadedServiceMap) {
        List<Long> ids = serviceIds == null ? Collections.emptyList() : serviceIds.stream()
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<Long, AsServiceEntity> serviceMap = new HashMap<>();
        if (preloadedServiceMap != null) {
            for (Long id : ids) {
                AsServiceEntity s = preloadedServiceMap.get(id);
                if (s != null) { serviceMap.put(id, s); }
            }
        }
        List<Long> missingIds = ids.stream().filter(id -> !serviceMap.containsKey(id)).collect(Collectors.toList());
        if (!missingIds.isEmpty()) {
            List<AsServiceEntity> loaded = asServiceMapper.selectBatchIds(missingIds);
            if (loaded != null) { loaded.forEach(s -> serviceMap.put(s.getId(), s)); }
        }
        if (serviceMap.isEmpty()) {
            return Collections.emptyMap();
        }
        Set<Long> explicitTemplateIds = serviceMap.values().stream()
                .map(AsServiceEntity::getAfterSchoolTimeTemplateId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> fallbackSchoolIds = serviceMap.values().stream()
                .filter(s -> s.getAfterSchoolTimeTemplateId() == null)
                .map(AsServiceEntity::getSchoolId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, List<Long>> enabledBySchool = mapEnabledAfterSchoolTemplateIds(fallbackSchoolIds);
        Set<Long> allTemplateIds = new HashSet<>(explicitTemplateIds);
        enabledBySchool.values().forEach(allTemplateIds::addAll);
        if (allTemplateIds.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<Long, List<AsSchedulePeriodEntity>> periodMap = mapAfterSchoolPeriods(allTemplateIds);
        Map<Long, List<EnrollmentTimeSlot>> result = new HashMap<>();
        for (Long serviceId : ids) {
            AsServiceEntity service = serviceMap.get(serviceId);
            if (service == null) { continue; }
            List<Long> templateIds = service.getAfterSchoolTimeTemplateId() == null
                    ? enabledBySchool.getOrDefault(service.getSchoolId(), Collections.emptyList())
                    : Collections.singletonList(service.getAfterSchoolTimeTemplateId());
            List<EnrollmentTimeSlot> slots = new ArrayList<>();
            for (Long templateId : templateIds) {
                for (AsSchedulePeriodEntity period : periodMap.getOrDefault(templateId, Collections.emptyList())) {
                    slots.addAll(toAfterSchoolTemplateSlots(period));
                }
            }
            if (!slots.isEmpty()) {
                result.put(serviceId, sortSlots(slots));
            }
        }
        return result;
    }

    private Map<Long, List<Long>> mapEnabledAfterSchoolTemplateIds(Collection<Long> schoolIds) {
        List<Long> ids = schoolIds == null ? Collections.emptyList() : schoolIds.stream()
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (ids.isEmpty()) { return Collections.emptyMap(); }
        LambdaQueryWrapper<AsScheduleTemplateEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsScheduleTemplateEntity::getIsValid, 1)
                .eq(AsScheduleTemplateEntity::getTemplateType, CoreConstant.SCHEDULE_TEMPLATE_TYPE_AFTER_SCHOOL)
                .eq(AsScheduleTemplateEntity::getEnabledStatus, CoreConstant.ENABLED_STATUS)
                .in(AsScheduleTemplateEntity::getSchoolId, ids)
                .orderByDesc(AsScheduleTemplateEntity::getId);
        List<AsScheduleTemplateEntity> templates = asScheduleTemplateMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(templates)) { return Collections.emptyMap(); }
        return templates.stream().filter(t -> t.getSchoolId() != null && t.getId() != null)
                .collect(Collectors.groupingBy(AsScheduleTemplateEntity::getSchoolId,
                        Collectors.mapping(AsScheduleTemplateEntity::getId, Collectors.toList())));
    }

    private Map<Long, List<AsSchedulePeriodEntity>> mapAfterSchoolPeriods(Collection<Long> templateIds) {
        List<Long> ids = templateIds == null ? Collections.emptyList() : templateIds.stream()
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (ids.isEmpty()) { return Collections.emptyMap(); }
        LambdaQueryWrapper<AsSchedulePeriodEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsSchedulePeriodEntity::getIsValid, 1)
                .eq(AsSchedulePeriodEntity::getPeriodType, CoreConstant.PERIOD_TYPE_AFTER_SCHOOL)
                .in(AsSchedulePeriodEntity::getTemplateId, ids)
                .orderByAsc(AsSchedulePeriodEntity::getSectionNo)
                .orderByAsc(AsSchedulePeriodEntity::getStartTime);
        List<AsSchedulePeriodEntity> periods = asSchedulePeriodMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(periods)) { return Collections.emptyMap(); }
        return periods.stream().filter(p -> p.getTemplateId() != null)
                .collect(Collectors.groupingBy(AsSchedulePeriodEntity::getTemplateId));
    }

    private List<EnrollmentTimeSlot> toAfterSchoolTemplateSlots(AsSchedulePeriodEntity period) {
        if (period == null) { return Collections.emptyList(); }
        List<Integer> weekDays = parseAfterSchoolWeekDays(period);
        if (weekDays.isEmpty()) { return Collections.emptyList(); }
        Integer startMinute = minutesOfDay(period.getStartTime());
        Integer endMinute = minutesOfDay(period.getEndTime());
        if (startMinute == null || endMinute == null) { return Collections.emptyList(); }
        List<Long> gradeIds = courseClassScopeSupport.parseLongList(period.getGradeScopeJson());
        List<EnrollmentTimeSlot> slots = new ArrayList<>();
        for (Integer weekDay : weekDays) {
            String displayText = AssembleUtil.getWeekDayName(weekDay) + " "
                    + formatTime(period.getStartTime()) + "-" + formatTime(period.getEndTime());
            slots.add(new EnrollmentTimeSlot(null, buildWeeklyTimeSlotKey(weekDay, startMinute, endMinute),
                    null, weekDay, startMinute, endMinute, displayText, gradeIds));
        }
        return slots;
    }

    private List<Integer> parseAfterSchoolWeekDays(AsSchedulePeriodEntity period) {
        if (period == null) { return Collections.emptyList(); }
        if (period.getDayOfWeek() != null) { return Collections.singletonList(period.getDayOfWeek()); }
        if (!StringUtils.hasText(period.getDayScopeJson())) { return Collections.emptyList(); }
        List<String> dayTexts;
        try {
            dayTexts = JSON.parseArray(period.getDayScopeJson(), String.class);
        } catch (Exception ex) {
            dayTexts = Arrays.asList(period.getDayScopeJson().replace("[", "").replace("]", "").replace("\"", "").split(","));
        }
        if (CollectionUtils.isEmpty(dayTexts)) { return Collections.emptyList(); }
        return dayTexts.stream().map(this::parseWeekDayText).filter(Objects::nonNull).distinct().sorted().collect(Collectors.toList());
    }

    private Integer parseWeekDayText(String text) {
        if (!StringUtils.hasText(text)) { return null; }
        String value = text.trim();
        if (value.matches("\\d+")) {
            int day = Integer.parseInt(value);
            return day >= 1 && day <= 7 ? day : null;
        }
        if (value.contains("一")) return 1;
        if (value.contains("二")) return 2;
        if (value.contains("三")) return 3;
        if (value.contains("四")) return 4;
        if (value.contains("五")) return 5;
        if (value.contains("六")) return 6;
        if (value.contains("日") || value.contains("天")) return 7;
        return null;
    }

    /** 兼容兜底：按服务加载所有有效课程课次时段。 */
    private Map<Long, List<EnrollmentTimeSlot>> mapCourseSessionServiceTimeSlots(Collection<Long> serviceIds) {
        List<Long> ids = serviceIds == null ? Collections.emptyList() : serviceIds.stream()
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (ids.isEmpty()) { return Collections.emptyMap(); }
        LambdaQueryWrapper<AsCourseClassEntity> cw = new LambdaQueryWrapper<>();
        cw.eq(AsCourseClassEntity::getIsValid, 1).in(AsCourseClassEntity::getServiceId, ids)
                .in(AsCourseClassEntity::getClassStatus, Arrays.asList(1, 2));
        List<AsCourseClassEntity> courses = asCourseClassMapper.selectList(cw);
        if (CollectionUtils.isEmpty(courses)) { return Collections.emptyMap(); }
        Map<Long, Long> courseServiceMap = courses.stream()
                .filter(c -> c.getId() != null && c.getServiceId() != null)
                .collect(Collectors.toMap(AsCourseClassEntity::getId, AsCourseClassEntity::getServiceId, (a, b) -> a));
        if (courseServiceMap.isEmpty()) { return Collections.emptyMap(); }
        LambdaQueryWrapper<AsCourseSessionEntity> sw = new LambdaQueryWrapper<>();
        sw.eq(AsCourseSessionEntity::getIsValid, 1).in(AsCourseSessionEntity::getCourseClassId, courseServiceMap.keySet())
                .ne(AsCourseSessionEntity::getSessionStatus, CoreConstant.SESSION_STATUS_CANCELLED);
        List<AsCourseSessionEntity> sessions = asCourseSessionMapper.selectList(sw);
        if (CollectionUtils.isEmpty(sessions)) { return Collections.emptyMap(); }
        Map<Long, List<EnrollmentTimeSlot>> result = new HashMap<>();
        for (AsCourseSessionEntity session : sessions) {
            Long serviceId = courseServiceMap.get(session.getCourseClassId());
            EnrollmentTimeSlot slot = toEnrollmentTimeSlot(session);
            if (serviceId == null || slot == null) { continue; }
            result.computeIfAbsent(serviceId, k -> new ArrayList<>()).add(slot);
        }
        result.replaceAll((k, v) -> sortSlots(v));
        return result;
    }

    // ======================== 学生占用计算 ========================

    /** 加载指定学生和服务组合下已占用的课程课次时段。 */
    public Map<String, Set<String>> mapStudentCourseSlotKeys(List<AsEnrollmentEntity> enrollments) {
        if (CollectionUtils.isEmpty(enrollments)) { return Collections.emptyMap(); }
        List<Long> schoolIds = enrollments.stream().map(AsEnrollmentEntity::getSchoolId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<Long> serviceIds = enrollments.stream().map(AsEnrollmentEntity::getServiceId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<Long> studentIds = enrollments.stream().map(AsEnrollmentEntity::getStudentId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (schoolIds.isEmpty() || serviceIds.isEmpty() || studentIds.isEmpty()) { return Collections.emptyMap(); }
        LambdaQueryWrapper<AsEnrollmentEntity> ew = new LambdaQueryWrapper<>();
        ew.eq(AsEnrollmentEntity::getIsValid, 1).in(AsEnrollmentEntity::getSchoolId, schoolIds)
                .in(AsEnrollmentEntity::getServiceId, serviceIds).in(AsEnrollmentEntity::getStudentId, studentIds)
                .in(AsEnrollmentEntity::getEnrollStatus, CoreConstant.ACTIVE_COURSE_ENROLL_STATUSES)
                .isNotNull(AsEnrollmentEntity::getCourseClassId).isNotNull(AsEnrollmentEntity::getSessionId);
        List<AsEnrollmentEntity> active = asEnrollmentMapper.selectList(ew);
        if (CollectionUtils.isEmpty(active)) { return Collections.emptyMap(); }
        List<Long> sessionIds = active.stream().map(AsEnrollmentEntity::getSessionId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (sessionIds.isEmpty()) { return Collections.emptyMap(); }
        Map<Long, EnrollmentTimeSlot> slotMap = mapSessionSlots(sessionIds);
        if (slotMap.isEmpty()) { return Collections.emptyMap(); }
        Map<String, Set<String>> result = new HashMap<>();
        for (AsEnrollmentEntity e : active) {
            EnrollmentTimeSlot slot = slotMap.get(e.getSessionId());
            if (slot == null) { continue; }
            result.computeIfAbsent(serviceStudentKey(e), k -> new HashSet<>()).add(slot.getSlotKey());
        }
        return result;
    }

    /** 按具体课次日期统计学期占用。 */
    public Map<String, Set<String>> mapStudentTermOccupiedSlotKeys(
            List<AsEnrollmentEntity> enrollments, Map<Long, AsServiceEntity> candidateServiceMap,
            boolean compareConcreteSessionDate) {
        if (CollectionUtils.isEmpty(enrollments) || CollectionUtils.isEmpty(candidateServiceMap)) {
            return mapStudentCourseSlotKeys(enrollments);
        }
        List<Long> schoolIds = enrollments.stream().map(AsEnrollmentEntity::getSchoolId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<Long> studentIds = enrollments.stream().map(AsEnrollmentEntity::getStudentId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<Long> termIds = candidateServiceMap.values().stream().map(AsServiceEntity::getTermId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (schoolIds.isEmpty() || studentIds.isEmpty() || termIds.isEmpty()) {
            return mapStudentCourseSlotKeys(enrollments);
        }
        LambdaQueryWrapper<AsServiceEntity> sw = new LambdaQueryWrapper<>();
        sw.eq(AsServiceEntity::getIsValid, 1).in(AsServiceEntity::getSchoolId, schoolIds).in(AsServiceEntity::getTermId, termIds);
        List<AsServiceEntity> termServices = asServiceMapper.selectList(sw);
        if (CollectionUtils.isEmpty(termServices)) { return mapStudentCourseSlotKeys(enrollments); }
        Map<Long, AsServiceEntity> termServiceMap = termServices.stream()
                .filter(s -> s.getId() != null).collect(Collectors.toMap(AsServiceEntity::getId, Function.identity(), (a, b) -> a));
        if (termServiceMap.isEmpty()) { return mapStudentCourseSlotKeys(enrollments); }
        Map<String, Set<String>> occupiedByStudentTerm = new HashMap<>();
        fillStudentTermCourseSlotKeys(schoolIds, studentIds, termServiceMap, occupiedByStudentTerm, compareConcreteSessionDate);
        fillStudentTermNonCourseSlotKeys(schoolIds, studentIds, termServiceMap, occupiedByStudentTerm, compareConcreteSessionDate);
        Map<String, Set<String>> result = new HashMap<>();
        for (AsEnrollmentEntity e : enrollments) {
            AsServiceEntity service = candidateServiceMap.get(e.getServiceId());
            String termKey = studentTermKey(e.getSchoolId(), service, e.getStudentId());
            Set<String> occupiedKeys = StringUtils.hasText(termKey)
                    ? occupiedByStudentTerm.getOrDefault(termKey, Collections.emptySet()) : Collections.emptySet();
            result.put(serviceStudentKey(e), occupiedKeys);
        }
        return result;
    }


    private void fillStudentTermCourseSlotKeys(List<Long> schoolIds, List<Long> studentIds,
                                                Map<Long, AsServiceEntity> termServiceMap,
                                                Map<String, Set<String>> occupiedByStudentTerm,
                                                boolean compareConcreteSessionDate) {
        if (CollectionUtils.isEmpty(termServiceMap)) { return; }
        LambdaQueryWrapper<AsEnrollmentEntity> ew = new LambdaQueryWrapper<>();
        ew.eq(AsEnrollmentEntity::getIsValid, 1).in(AsEnrollmentEntity::getSchoolId, schoolIds)
                .in(AsEnrollmentEntity::getServiceId, termServiceMap.keySet())
                .in(AsEnrollmentEntity::getStudentId, studentIds)
                .in(AsEnrollmentEntity::getEnrollStatus, CoreConstant.ACTIVE_COURSE_ENROLL_STATUSES)
                .isNotNull(AsEnrollmentEntity::getCourseClassId).isNotNull(AsEnrollmentEntity::getSessionId);
        List<AsEnrollmentEntity> active = asEnrollmentMapper.selectList(ew);
        if (CollectionUtils.isEmpty(active)) { return; }
        Map<Long, EnrollmentTimeSlot> slotMap = mapSessionSlots(active.stream().map(AsEnrollmentEntity::getSessionId).collect(Collectors.toList()));
        if (slotMap.isEmpty()) { return; }
        for (AsEnrollmentEntity e : active) {
            EnrollmentTimeSlot slot = slotMap.get(e.getSessionId());
            String termKey = studentTermKey(e.getSchoolId(), termServiceMap.get(e.getServiceId()), e.getStudentId());
            if (slot == null || !StringUtils.hasText(termKey)) { continue; }
            String occupiedKey = compareConcreteSessionDate ? buildConcreteSessionTimeKey(slot) : slot.getSlotKey();
            if (StringUtils.hasText(occupiedKey)) {
                occupiedByStudentTerm.computeIfAbsent(termKey, k -> new HashSet<>()).add(occupiedKey);
            }
        }
    }

    private void fillStudentTermNonCourseSlotKeys(List<Long> schoolIds, List<Long> studentIds,
                                                   Map<Long, AsServiceEntity> termServiceMap,
                                                   Map<String, Set<String>> occupiedByStudentTerm,
                                                   boolean compareConcreteSessionDate) {
        if (CollectionUtils.isEmpty(termServiceMap)) { return; }
        LambdaQueryWrapper<AsStudentDestinationEntity> dw = new LambdaQueryWrapper<>();
        dw.eq(AsStudentDestinationEntity::getIsValid, 1).in(AsStudentDestinationEntity::getSchoolId, schoolIds)
                .in(AsStudentDestinationEntity::getServiceId, termServiceMap.keySet())
                .in(AsStudentDestinationEntity::getStudentId, studentIds)
                .isNull(AsStudentDestinationEntity::getCourseClassId).isNotNull(AsStudentDestinationEntity::getSessionId)
                .in(AsStudentDestinationEntity::getDestinationType, Arrays.asList(
                        CoreConstant.DESTINATION_TYPE_GO_HOME, CoreConstant.DESTINATION_TYPE_TRAINING_INSTITUTION,
                        CoreConstant.DESTINATION_TYPE_OTHER));
        List<AsStudentDestinationEntity> destinations = asStudentDestinationMapper.selectList(dw);
        if (CollectionUtils.isEmpty(destinations)) { return; }
        Map<Long, EnrollmentTimeSlot> slotMap = mapSessionSlots(destinations.stream().map(AsStudentDestinationEntity::getSessionId).collect(Collectors.toList()));
        if (slotMap.isEmpty()) { return; }
        for (AsStudentDestinationEntity d : destinations) {
            EnrollmentTimeSlot slot = slotMap.get(d.getSessionId());
            String termKey = studentTermKey(d.getSchoolId(), termServiceMap.get(d.getServiceId()), d.getStudentId());
            if (slot == null || !StringUtils.hasText(termKey)) { continue; }
            String occupiedKey = compareConcreteSessionDate ? buildConcreteSessionTimeKey(slot) : slot.getSlotKey();
            if (StringUtils.hasText(occupiedKey)) {
                occupiedByStudentTerm.computeIfAbsent(termKey, k -> new HashSet<>()).add(occupiedKey);
            }
        }
    }

    // ======================== 未注册时段计算 ========================

    /** 计算尚未被有效课程报名覆盖的服务时段。 */
    public List<EnrollmentTimeSlot> findUnregisteredTimeSlots(Long schoolId, Long serviceId, Long studentId,
                                                               AsStudentEntity student,
                                                               Map<Long, List<EnrollmentTimeSlot>> serviceTimeSlotMap,
                                                               Map<String, Set<String>> occupiedSlotKeyMap) {
        return findUnregisteredTimeSlots(schoolId, serviceId, studentId, student, serviceTimeSlotMap, occupiedSlotKeyMap, false);
    }

    public List<EnrollmentTimeSlot> findUnregisteredTimeSlots(Long schoolId, Long serviceId, Long studentId,
                                                               AsStudentEntity student,
                                                               Map<Long, List<EnrollmentTimeSlot>> serviceTimeSlotMap,
                                                               Map<String, Set<String>> occupiedSlotKeyMap,
                                                               boolean compareConcreteSessionDate) {
        List<EnrollmentTimeSlot> serviceSlots = serviceTimeSlotMap == null ? null : serviceTimeSlotMap.get(serviceId);
        if (CollectionUtils.isEmpty(serviceSlots)) { return Collections.emptyList(); }
        Set<String> occupiedKeys = occupiedSlotKeyMap == null ? Collections.emptySet()
                : occupiedSlotKeyMap.getOrDefault(serviceStudentKey(schoolId, serviceId, studentId), Collections.emptySet());
        LinkedHashMap<String, EnrollmentTimeSlot> remaining = new LinkedHashMap<>();
        for (EnrollmentTimeSlot slot : serviceSlots) {
            String comparisonKey = compareConcreteSessionDate
                    ? buildConcreteSessionTimeKey(slot) : slot == null ? null : slot.getSlotKey();
            if (slot == null || !StringUtils.hasText(comparisonKey) || occupiedKeys.contains(comparisonKey)
                    || !isSlotApplicableToStudent(slot, student)) {
                continue;
            }
            remaining.putIfAbsent(comparisonKey, slot);
        }
        return sortSlots(remaining.values());
    }

    public boolean isSlotApplicableToStudent(EnrollmentTimeSlot slot, AsStudentEntity student) {
        if (slot == null || CollectionUtils.isEmpty(slot.getGradeIds())) { return true; }
        return student != null && student.getGradeId() != null && slot.getGradeIds().contains(student.getGradeId());
    }

    public boolean hasUnregisteredTime(AsEnrollmentEntity enrollment, AsServiceEntity service) {
        return !findUnregisteredTimeSlots(enrollment, service).isEmpty();
    }

    /** 简化版：按单个服务计算未注册时段。 */
    public List<EnrollmentTimeSlot> findUnregisteredTimeSlots(AsEnrollmentEntity enrollment, AsServiceEntity service) {
        if (enrollment == null || service == null) { return Collections.emptyList(); }
        Map<Long, List<EnrollmentTimeSlot>> serviceTimeSlotMap = mapServiceTimeSlots(
                Collections.singletonList(service.getId()), Collections.singletonMap(service.getId(), service));
        Map<String, Set<String>> occupiedSlotKeyMap = Collections.singletonMap(
                serviceStudentKey(enrollment), listOccupiedCourseSlotKeys(service.getSchoolId(), service.getId(), enrollment.getStudentId()));
        AsStudentEntity student = enrollment.getStudentId() == null ? null : asStudentMapper.selectById(enrollment.getStudentId());
        return findUnregisteredTimeSlots(enrollment.getSchoolId(), enrollment.getServiceId(), enrollment.getStudentId(),
                student, serviceTimeSlotMap, occupiedSlotKeyMap);
    }

    // ======================== 时段转换与排序 ========================

    public EnrollmentTimeSlot toEnrollmentTimeSlot(AsCourseSessionEntity session) {
        if (session == null || session.getId() == null) { return null; }
        String slotKey = buildEnrollmentTimeSlotKey(session);
        return new EnrollmentTimeSlot(session.getId(), slotKey, session.getSessionDate(), session.getWeekDay(),
                minutesOfDay(session.getStartTime()), minutesOfDay(session.getEndTime()),
                buildEnrollmentTimeSlotText(session), Collections.emptyList());
    }


    public Map<Long, EnrollmentTimeSlot> mapSessionSlots(Collection<Long> sessionIds) {
        List<Long> ids = sessionIds == null ? Collections.emptyList() : sessionIds.stream()
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (ids.isEmpty()) { return Collections.emptyMap(); }
        LambdaQueryWrapper<AsCourseSessionEntity> sw = new LambdaQueryWrapper<>();
        sw.eq(AsCourseSessionEntity::getIsValid, 1).in(AsCourseSessionEntity::getId, ids)
                .ne(AsCourseSessionEntity::getSessionStatus, CoreConstant.SESSION_STATUS_CANCELLED);
        List<AsCourseSessionEntity> sessions = asCourseSessionMapper.selectList(sw);
        if (CollectionUtils.isEmpty(sessions)) { return Collections.emptyMap(); }
        return sessions.stream().map(this::toEnrollmentTimeSlot).filter(Objects::nonNull)
                .collect(Collectors.toMap(EnrollmentTimeSlot::getSessionId, Function.identity(), (a, b) -> a));
    }


    public List<String> toSlotTexts(Collection<EnrollmentTimeSlot> slots) {
        if (CollectionUtils.isEmpty(slots)) { return Collections.emptyList(); }
        return sortSlots(slots).stream().map(EnrollmentTimeSlot::getDisplayText)
                .filter(StringUtils::hasText).distinct().collect(Collectors.toList());
    }

    private String buildEnrollmentTimeSlotText(AsCourseSessionEntity session) {
        if (session == null || session.getWeekDay() == null) { return buildSlotText(session); }
        String weekDayName = AssembleUtil.getWeekDayName(session.getWeekDay());
        String time = formatTime(session.getStartTime()) + "-" + formatTime(session.getEndTime());
        return StringUtils.hasText(weekDayName) ? weekDayName + " " + time : buildSlotText(session);
    }

    private String buildSlotText(AsCourseSessionEntity session) {
        if (session == null) { return null; }
        String weekDayName = AssembleUtil.getWeekDayName(session.getWeekDay());
        String day = session.getSessionDate() == null ? weekDayName
                : new SimpleDateFormat("yyyy年M月d日").format(session.getSessionDate())
                + (StringUtils.hasText(weekDayName) ? "（" + weekDayName.replace("每周", "周") + "）" : "");
        String start = formatTime(session.getStartTime());
        String end = formatTime(session.getEndTime());
        if (!StringUtils.hasText(start) || !StringUtils.hasText(end)) { return day; }
        return day + " " + start + "-" + end;
    }

    public Integer minutesOfDay(Date time) {
        if (time == null) { return null; }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        return calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
    }

    private String formatTime(Date date) {
        if (date == null) { return null; }
        return new SimpleDateFormat("HH:mm").format(date);
    }

    // ======================== 时段比较 ========================

    public boolean isWeeklyTimeOverlap(EnrollmentTimeSlot first, EnrollmentTimeSlot second) {
        if (first == null || second == null || first.getWeekDay() == null
                || first.getStartMinute() == null || first.getEndMinute() == null
                || second.getStartMinute() == null || second.getEndMinute() == null) {
            return false;
        }
        return Objects.equals(first.getWeekDay(), second.getWeekDay())
                && first.getStartMinute() < second.getEndMinute()
                && second.getStartMinute() < first.getEndMinute();
    }

    public boolean isConcreteSessionTimeOverlap(EnrollmentTimeSlot first, EnrollmentTimeSlot second) {
        if (first == null || second == null || first.getStartMinute() == null || first.getEndMinute() == null
                || second.getStartMinute() == null || second.getEndMinute() == null) {
            return false;
        }
        if (first.getSessionDate() == null || second.getSessionDate() == null) {
            return isWeeklyTimeOverlap(first, second);
        }
        boolean sameDate = new SimpleDateFormat("yyyy-MM-dd").format(first.getSessionDate())
                .equals(new SimpleDateFormat("yyyy-MM-dd").format(second.getSessionDate()));
        return sameDate && first.getStartMinute() < second.getEndMinute()
                && second.getStartMinute() < first.getEndMinute();
    }

    public boolean isSameSessionDay(AsCourseSessionEntity first, AsCourseSessionEntity second) {
        if (first == null || second == null) { return false; }
        if (first.getSessionDate() != null && second.getSessionDate() != null) {
            return new SimpleDateFormat("yyyy-MM-dd").format(first.getSessionDate())
                    .equals(new SimpleDateFormat("yyyy-MM-dd").format(second.getSessionDate()));
        }
        return first.getWeekDay() != null && Objects.equals(first.getWeekDay(), second.getWeekDay());
    }

    public boolean isSessionTimeOverlap(AsCourseSessionEntity first, AsCourseSessionEntity second) {
        if (first == null || second == null || first.getStartTime() == null || first.getEndTime() == null
                || second.getStartTime() == null || second.getEndTime() == null) {
            return false;
        }
        return first.getStartTime().before(second.getEndTime()) && first.getEndTime().after(second.getStartTime());
    }

    // ======================== 课次筛选 ========================

    public List<AsCourseSessionEntity> filterUnregisteredServiceSessions(AsServiceEntity service, Long studentId,
                                                                          List<AsCourseSessionEntity> serviceSessions) {
        return filterUnregisteredServiceSessions(service, studentId, serviceSessions, Collections.emptySet());
    }

    public List<AsCourseSessionEntity> filterUnregisteredServiceSessions(AsServiceEntity service, Long studentId,
                                                                          List<AsCourseSessionEntity> serviceSessions,
                                                                          Set<Long> excludedCourseClassIds) {
        return filterUnregisteredServiceSessions(service, studentId, serviceSessions, excludedCourseClassIds, false);
    }

    public List<AsCourseSessionEntity> filterUnregisteredConcreteServiceSessions(
            AsServiceEntity service, Long studentId, List<AsCourseSessionEntity> serviceSessions) {
        return filterUnregisteredServiceSessions(service, studentId, serviceSessions, Collections.emptySet(), true);
    }

    public List<AsCourseSessionEntity> filterUnregisteredServiceSessions(AsServiceEntity service, Long studentId,
                                                                          List<AsCourseSessionEntity> serviceSessions,
                                                                          Set<Long> excludedCourseClassIds,
                                                                          boolean compareConcreteSessionDate) {
        if (service == null || studentId == null || CollectionUtils.isEmpty(serviceSessions)) {
            return Collections.emptyList();
        }
        List<Long> occupiedSessionIds = listActiveCourseEnrollmentsInSameTerm(service, studentId).stream()
                .filter(e -> excludedCourseClassIds == null || !excludedCourseClassIds.contains(e.getCourseClassId()))
                .map(AsEnrollmentEntity::getSessionId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<EnrollmentTimeSlot> occupiedSlots = new ArrayList<>(mapSessionSlots(occupiedSessionIds).values());
        return serviceSessions.stream().filter(session -> {
            EnrollmentTimeSlot slot = toEnrollmentTimeSlot(session);
            return slot != null && occupiedSlots.stream().noneMatch(occupied -> compareConcreteSessionDate
                    ? isConcreteSessionTimeOverlap(slot, occupied) : isWeeklyTimeOverlap(slot, occupied));
        }).collect(Collectors.toList());
    }

    public List<AsCourseSessionEntity> listValidSessionsByService(Long serviceId) {
        LambdaQueryWrapper<AsCourseClassEntity> cw = new LambdaQueryWrapper<>();
        cw.eq(AsCourseClassEntity::getIsValid, 1).eq(AsCourseClassEntity::getServiceId, serviceId)
                .in(AsCourseClassEntity::getClassStatus, Arrays.asList(1, 2));
        List<AsCourseClassEntity> courses = asCourseClassMapper.selectList(cw);
        if (CollectionUtils.isEmpty(courses)) { return Collections.emptyList(); }
        List<Long> courseClassIds = courses.stream().map(AsCourseClassEntity::getId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (courseClassIds.isEmpty()) { return Collections.emptyList(); }
        LambdaQueryWrapper<AsCourseSessionEntity> sw = new LambdaQueryWrapper<>();
        sw.eq(AsCourseSessionEntity::getIsValid, 1).in(AsCourseSessionEntity::getCourseClassId, courseClassIds)
                .ne(AsCourseSessionEntity::getSessionStatus, 3);
        List<AsCourseSessionEntity> sessions = asCourseSessionMapper.selectList(sw);
        return sessions == null ? Collections.emptyList() : sessions;
    }

    // ======================== 占用失效 ========================

    public void invalidateNonCourseDestinationsBySlots(AsServiceEntity service, Long studentId,
                                                        List<AsCourseSessionEntity> targetSessions, Long operatorUserId) {
        if (service == null || studentId == null || CollectionUtils.isEmpty(targetSessions)) { return; }
        Set<String> targetSlotKeys = targetSessions.stream().map(this::toEnrollmentTimeSlot).filter(Objects::nonNull)
                .map(EnrollmentTimeSlot::getSlotKey).collect(Collectors.toSet());
        if (targetSlotKeys.isEmpty()) { return; }
        List<Long> sessionIds = listValidSessionsByService(service.getId()).stream()
                .filter(s -> {
                    EnrollmentTimeSlot slot = toEnrollmentTimeSlot(s);
                    return slot != null && targetSlotKeys.contains(slot.getSlotKey());
                }).map(AsCourseSessionEntity::getId).filter(Objects::nonNull).collect(Collectors.toList());
        if (sessionIds.isEmpty()) { return; }
        LambdaUpdateWrapper<AsStudentDestinationEntity> uw = new LambdaUpdateWrapper<>();
        uw.eq(AsStudentDestinationEntity::getIsValid, 1).eq(AsStudentDestinationEntity::getSchoolId, service.getSchoolId())
                .eq(AsStudentDestinationEntity::getServiceId, service.getId()).eq(AsStudentDestinationEntity::getStudentId, studentId)
                .isNull(AsStudentDestinationEntity::getCourseClassId).in(AsStudentDestinationEntity::getSessionId, sessionIds);
        AsStudentDestinationEntity update = new AsStudentDestinationEntity();
        update.setIsValid(0);
        update.setUpdateUser(operatorUserId);
        update.setUpdateTime(new Date());
        asStudentDestinationMapper.update(update, uw);
    }

    public void invalidateStudentAdjustNonCourseConflicts(AsServiceEntity targetService, Long studentId,
                                                           List<AsCourseSessionEntity> targetSessions, Long operatorUserId) {
        if (targetService == null || !isValid(targetService) || studentId == null || CollectionUtils.isEmpty(targetSessions)) { return; }
        List<AsCourseSessionEntity> activeTargetSessions = targetSessions.stream()
                .filter(this::isCurrentStudentAdjustSession).collect(Collectors.toList());
        if (activeTargetSessions.isEmpty()) { return; }
        List<Long> sameTermServiceIds = listSameTermServiceIds(targetService);
        LambdaQueryWrapper<AsStudentDestinationEntity> dw = new LambdaQueryWrapper<>();
        dw.eq(AsStudentDestinationEntity::getIsValid, 1).eq(AsStudentDestinationEntity::getSchoolId, targetService.getSchoolId())
                .in(AsStudentDestinationEntity::getServiceId, sameTermServiceIds).eq(AsStudentDestinationEntity::getStudentId, studentId)
                .in(AsStudentDestinationEntity::getDestinationType, Arrays.asList(
                        CoreConstant.DESTINATION_TYPE_GO_HOME, CoreConstant.DESTINATION_TYPE_TRAINING_INSTITUTION))
                .isNotNull(AsStudentDestinationEntity::getSessionId);
        List<AsStudentDestinationEntity> destinations = asStudentDestinationMapper.selectList(dw);
        if (CollectionUtils.isEmpty(destinations)) { return; }
        List<Long> destSessionIds = destinations.stream().map(AsStudentDestinationEntity::getSessionId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<AsCourseSessionEntity> destSessions = destSessionIds.isEmpty() ? Collections.emptyList() : asCourseSessionMapper.selectBatchIds(destSessionIds);
        Map<Long, AsCourseSessionEntity> destSessionMap = (destSessions == null ? Collections.<AsCourseSessionEntity>emptyList() : destSessions).stream()
                .filter(this::isCurrentStudentAdjustSession)
                .collect(Collectors.toMap(AsCourseSessionEntity::getId, Function.identity(), (a, b) -> a));
        List<Long> conflictIds = destinations.stream()
                .filter(d -> isConflictWithAnyTargetSession(destSessionMap.get(d.getSessionId()), activeTargetSessions))
                .map(AsStudentDestinationEntity::getId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (conflictIds.isEmpty()) { return; }
        AsStudentDestinationEntity update = new AsStudentDestinationEntity();
        update.setIsValid(0);
        update.setUpdateUser(operatorUserId);
        update.setUpdateTime(new Date());
        LambdaUpdateWrapper<AsStudentDestinationEntity> uw = new LambdaUpdateWrapper<>();
        uw.eq(AsStudentDestinationEntity::getIsValid, 1).in(AsStudentDestinationEntity::getId, conflictIds);
        asStudentDestinationMapper.update(update, uw);
    }

    private boolean isCurrentStudentAdjustSession(AsCourseSessionEntity session) {
        if (!isValid(session) || Objects.equals(session.getSessionStatus(), CoreConstant.SESSION_STATUS_CANCELLED)) { return false; }
        if (session.getSessionDate() == null) { return true; }
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        return !session.getSessionDate().before(today.getTime());
    }

    private boolean isConflictWithAnyTargetSession(AsCourseSessionEntity destSession, List<AsCourseSessionEntity> targetSessions) {
        if (destSession == null || CollectionUtils.isEmpty(targetSessions)) { return false; }
        return targetSessions.stream().anyMatch(t -> isSameSessionDay(destSession, t) && isSessionTimeOverlap(destSession, t));
    }

    // ======================== 同学期服务查询 ========================

    public List<Long> listSameTermServiceIds(AsServiceEntity targetService) {
        if (targetService == null || !isValid(targetService) || targetService.getId() == null || targetService.getTermId() == null) {
            return targetService == null || targetService.getId() == null ? Collections.emptyList() : Collections.singletonList(targetService.getId());
        }
        LambdaQueryWrapper<AsServiceEntity> sw = new LambdaQueryWrapper<>();
        sw.eq(AsServiceEntity::getIsValid, 1).eq(AsServiceEntity::getSchoolId, targetService.getSchoolId())
                .eq(AsServiceEntity::getTermId, targetService.getTermId());
        List<AsServiceEntity> services = asServiceMapper.selectList(sw);
        List<Long> serviceIds = CollectionUtils.isEmpty(services) ? Collections.emptyList()
                : services.stream().map(AsServiceEntity::getId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        return serviceIds.isEmpty() ? Collections.singletonList(targetService.getId()) : serviceIds;
    }

    public List<AsEnrollmentEntity> listActiveCourseEnrollmentsInSameTerm(AsServiceEntity service, Long studentId) {
        if (service == null || service.getSchoolId() == null || service.getId() == null || studentId == null) {
            return Collections.emptyList();
        }
        if (service.getTermId() == null) {
            return listActiveCourseEnrollments(service.getSchoolId(), service.getId(), studentId);
        }
        LambdaQueryWrapper<AsServiceEntity> sw = new LambdaQueryWrapper<>();
        sw.eq(AsServiceEntity::getIsValid, 1).eq(AsServiceEntity::getSchoolId, service.getSchoolId())
                .eq(AsServiceEntity::getTermId, service.getTermId());
        List<AsServiceEntity> sameTermServices = asServiceMapper.selectList(sw);
        if (CollectionUtils.isEmpty(sameTermServices)) {
            return listActiveCourseEnrollments(service.getSchoolId(), service.getId(), studentId);
        }
        List<Long> serviceIds = sameTermServices.stream().map(AsServiceEntity::getId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (serviceIds.isEmpty()) {
            return listActiveCourseEnrollments(service.getSchoolId(), service.getId(), studentId);
        }
        LambdaQueryWrapper<AsEnrollmentEntity> ew = new LambdaQueryWrapper<>();
        ew.eq(AsEnrollmentEntity::getIsValid, 1).eq(AsEnrollmentEntity::getSchoolId, service.getSchoolId())
                .in(AsEnrollmentEntity::getServiceId, serviceIds).eq(AsEnrollmentEntity::getStudentId, studentId)
                .in(AsEnrollmentEntity::getEnrollStatus, CoreConstant.ACTIVE_COURSE_ENROLL_STATUSES)
                .isNotNull(AsEnrollmentEntity::getSessionId).isNotNull(AsEnrollmentEntity::getCourseClassId);
        List<AsEnrollmentEntity> enrollments = asEnrollmentMapper.selectList(ew);
        return enrollments == null ? Collections.emptyList() : enrollments;
    }

    public List<AsEnrollmentEntity> listActiveCourseEnrollments(Long schoolId, Long serviceId, Long studentId) {
        LambdaQueryWrapper<AsEnrollmentEntity> ew = new LambdaQueryWrapper<>();
        ew.eq(AsEnrollmentEntity::getIsValid, 1).eq(AsEnrollmentEntity::getSchoolId, schoolId)
                .eq(AsEnrollmentEntity::getServiceId, serviceId).eq(AsEnrollmentEntity::getStudentId, studentId)
                .in(AsEnrollmentEntity::getEnrollStatus, CoreConstant.ACTIVE_COURSE_ENROLL_STATUSES)
                .isNotNull(AsEnrollmentEntity::getSessionId).isNotNull(AsEnrollmentEntity::getCourseClassId);
        List<AsEnrollmentEntity> enrollments = asEnrollmentMapper.selectList(ew);
        return enrollments == null ? Collections.emptyList() : enrollments;
    }

    // ======================== 非课程去向时段 ========================

    public List<EnrollmentTimeSlot> listNonCourseDestinationTimeSlots(AsEnrollmentEntity enrollment,
                                                                       AsStudentDestinationEntity destination) {
        if (enrollment == null || destination == null) { return Collections.emptyList(); }
        LambdaQueryWrapper<AsStudentDestinationEntity> dw = new LambdaQueryWrapper<>();
        dw.eq(AsStudentDestinationEntity::getIsValid, 1).eq(AsStudentDestinationEntity::getSchoolId, enrollment.getSchoolId())
                .eq(AsStudentDestinationEntity::getServiceId, enrollment.getServiceId())
                .eq(AsStudentDestinationEntity::getStudentId, enrollment.getStudentId())
                .eq(AsStudentDestinationEntity::getDestinationType, destination.getDestinationType())
                .isNull(AsStudentDestinationEntity::getCourseClassId).isNotNull(AsStudentDestinationEntity::getSessionId);
        List<Long> sessionIds = asStudentDestinationMapper.selectList(dw).stream()
                .map(AsStudentDestinationEntity::getSessionId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (sessionIds.isEmpty()) { return Collections.emptyList(); }
        LambdaQueryWrapper<AsCourseSessionEntity> sw = new LambdaQueryWrapper<>();
        sw.eq(AsCourseSessionEntity::getIsValid, 1).in(AsCourseSessionEntity::getId, sessionIds)
                .ne(AsCourseSessionEntity::getSessionStatus, CoreConstant.SESSION_STATUS_CANCELLED);
        return sortSlots(asCourseSessionMapper.selectList(sw).stream().map(this::toEnrollmentTimeSlot).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    public List<EnrollmentTimeSlot> resolveNonCourseDestinationTimeSlots(
            AsEnrollmentEntity enrollment, AsStudentDestinationEntity destination,
            Map<String, List<EnrollmentTimeSlot>> nonCourseDestinationSlotMap) {
        if (enrollment == null || destination == null) {
            return Collections.emptyList();
        }
        if (nonCourseDestinationSlotMap != null) {
            return nonCourseDestinationSlotMap.getOrDefault(
                    serviceStudentDestinationKey(enrollment, destination.getDestinationType()),
                    Collections.emptyList());
        }
        return listNonCourseDestinationTimeSlots(enrollment, destination);
    }

    public Map<String, List<EnrollmentTimeSlot>> mapNonCourseDestinationTimeSlots(
            List<AsEnrollmentEntity> enrollments, Map<String, AsStudentDestinationEntity> latestDestinationMap) {
        if (CollectionUtils.isEmpty(enrollments) || CollectionUtils.isEmpty(latestDestinationMap)) {
            return Collections.emptyMap();
        }
        List<Long> schoolIds = enrollments.stream()
                .map(AsEnrollmentEntity::getSchoolId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        List<Long> serviceIds = enrollments.stream()
                .map(AsEnrollmentEntity::getServiceId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        List<Long> studentIds = enrollments.stream()
                .map(AsEnrollmentEntity::getStudentId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (schoolIds.isEmpty() || serviceIds.isEmpty() || studentIds.isEmpty()) {
            return Collections.emptyMap();
        }
        Set<String> targetKeys = latestDestinationMap.values().stream()
                .filter(Objects::nonNull)
                .map(destination -> serviceStudentDestinationKey(
                        destination.getSchoolId(), destination.getServiceId(),
                        destination.getStudentId(), destination.getDestinationType()))
                .collect(Collectors.toSet());
        if (targetKeys.isEmpty()) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<AsStudentDestinationEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsStudentDestinationEntity::getIsValid, 1)
                .in(AsStudentDestinationEntity::getSchoolId, schoolIds)
                .in(AsStudentDestinationEntity::getServiceId, serviceIds)
                .in(AsStudentDestinationEntity::getStudentId, studentIds)
                .isNull(AsStudentDestinationEntity::getCourseClassId)
                .isNotNull(AsStudentDestinationEntity::getSessionId)
                .in(AsStudentDestinationEntity::getDestinationType, Arrays.asList(
                        CoreConstant.DESTINATION_TYPE_GO_HOME, CoreConstant.DESTINATION_TYPE_TRAINING_INSTITUTION,
                        CoreConstant.DESTINATION_TYPE_OTHER));
        List<AsStudentDestinationEntity> destinations = asStudentDestinationMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(destinations)) {
            return Collections.emptyMap();
        }
        Map<String, List<Long>> sessionIdsByKey = new HashMap<>();
        for (AsStudentDestinationEntity destination : destinations) {
            String key = serviceStudentDestinationKey(
                    destination.getSchoolId(), destination.getServiceId(),
                    destination.getStudentId(), destination.getDestinationType());
            if (!targetKeys.contains(key) || destination.getSessionId() == null) {
                continue;
            }
            sessionIdsByKey.computeIfAbsent(key, ignored -> new ArrayList<>()).add(destination.getSessionId());
        }
        if (sessionIdsByKey.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<Long, EnrollmentTimeSlot> sessionSlotMap = mapSessionSlots(sessionIdsByKey.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));
        if (sessionSlotMap.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, List<EnrollmentTimeSlot>> result = new HashMap<>();
        for (Map.Entry<String, List<Long>> entry : sessionIdsByKey.entrySet()) {
            List<EnrollmentTimeSlot> slots = entry.getValue().stream()
                    .distinct()
                    .map(sessionSlotMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            result.put(entry.getKey(), sortSlots(slots));
        }
        return result;
    }


    /**
     * 批量解析课程班真实上课时间，优先使用排课规则，规则缺失时回退到有效未取消课次。
     */
    public Map<Long, String> mapCourseScheduleTimeDesc(Collection<Long> courseClassIds) {
        return mapCourseScheduleTimeDesc(courseClassIds, mapCourseSessions(courseClassIds));
    }

    public Map<Long, String> mapCourseScheduleTimeDesc(Collection<Long> courseClassIds,
                                                        Map<Long, List<AsCourseSessionEntity>> sessionMap) {
        List<Long> ids = courseClassIds == null ? Collections.emptyList() : courseClassIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<Long, List<AsCourseScheduleRuleEntity>> ruleMap = mapCourseScheduleRules(ids);
        Map<Long, String> result = new HashMap<>();
        for (Long courseClassId : ids) {
            String desc = buildScheduleTimeDescFromRules(ruleMap.get(courseClassId));
            if (!StringUtils.hasText(desc)) {
                desc = buildScheduleTimeDescFromSessions(sessionMap == null ? null : sessionMap.get(courseClassId));
            }
            if (StringUtils.hasText(desc)) {
                result.put(courseClassId, desc);
            }
        }
        return result;
    }

    /**
     * 查询课程班排课规则，并按课程班分组保持规则顺序。
     */
    public Map<Long, List<AsCourseScheduleRuleEntity>> mapCourseScheduleRules(Collection<Long> courseClassIds) {
        if (CollectionUtils.isEmpty(courseClassIds)) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<AsCourseScheduleRuleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsCourseScheduleRuleEntity::getIsValid, 1)
                .in(AsCourseScheduleRuleEntity::getCourseClassId, courseClassIds)
                .orderByAsc(AsCourseScheduleRuleEntity::getRuleType)
                .orderByAsc(AsCourseScheduleRuleEntity::getSessionDate)
                .orderByAsc(AsCourseScheduleRuleEntity::getStartTime);
        List<AsCourseScheduleRuleEntity> rules = asCourseScheduleRuleMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(rules)) {
            return Collections.emptyMap();
        }
        return rules.stream()
                .filter(rule -> rule.getCourseClassId() != null)
                .collect(Collectors.groupingBy(AsCourseScheduleRuleEntity::getCourseClassId,
                        LinkedHashMap::new, Collectors.toList()));
    }

    public Map<Long, Boolean> mapCourseStarted(Collection<Long> courseClassIds,
                                                Map<Long, List<AsCourseSessionEntity>> sessionMap) {
        List<Long> ids = courseClassIds == null ? Collections.emptyList() : courseClassIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<Long, Boolean> result = ids.stream().collect(Collectors.toMap(Function.identity(), id -> false, (a, b) -> a));
        if (CollectionUtils.isEmpty(sessionMap)) {
            return result;
        }
        Date now = new Date();
        for (Map.Entry<Long, List<AsCourseSessionEntity>> entry : sessionMap.entrySet()) {
            Long courseClassId = entry.getKey();
            if (courseClassId == null || Boolean.TRUE.equals(result.get(courseClassId))) {
                continue;
            }
            for (AsCourseSessionEntity session : entry.getValue()) {
                if (session != null && isCourseSessionStarted(session, now)) {
                    result.put(courseClassId, true);
                    break;
                }
            }
        }
        return result;
    }

    private String serviceStudentDestinationKey(AsEnrollmentEntity enrollment, Integer destinationType) {
        return enrollment == null ? null : serviceStudentDestinationKey(
                enrollment.getSchoolId(), enrollment.getServiceId(), enrollment.getStudentId(), destinationType);
    }

    /**
     * 查询课程班有效未取消课次，并按课程班分组保持时间顺序。
     */
    public Map<Long, List<AsCourseSessionEntity>> mapCourseSessions(Collection<Long> courseClassIds) {
        if (CollectionUtils.isEmpty(courseClassIds)) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<AsCourseSessionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsCourseSessionEntity::getIsValid, 1)
                .in(AsCourseSessionEntity::getCourseClassId, courseClassIds)
                .ne(AsCourseSessionEntity::getSessionStatus, CoreConstant.SESSION_STATUS_CANCELLED)
                .orderByAsc(AsCourseSessionEntity::getSessionDate)
                .orderByAsc(AsCourseSessionEntity::getStartTime);
        List<AsCourseSessionEntity> sessions = asCourseSessionMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(sessions)) {
            return Collections.emptyMap();
        }
        return sessions.stream()
                .filter(session -> session.getCourseClassId() != null)
                .collect(Collectors.groupingBy(AsCourseSessionEntity::getCourseClassId,
                        LinkedHashMap::new, Collectors.toList()));
    }

    public Set<String> mapSessionSlotKeys(List<Long> sessionIds) {
        if (CollectionUtils.isEmpty(sessionIds)) { return Collections.emptySet(); }
        LambdaQueryWrapper<AsCourseSessionEntity> sw = new LambdaQueryWrapper<>();
        sw.eq(AsCourseSessionEntity::getIsValid, 1).in(AsCourseSessionEntity::getId, sessionIds)
                .ne(AsCourseSessionEntity::getSessionStatus, CoreConstant.SESSION_STATUS_CANCELLED);
        List<AsCourseSessionEntity> sessions = asCourseSessionMapper.selectList(sw);
        if (CollectionUtils.isEmpty(sessions)) { return Collections.emptySet(); }
        return sessions.stream().map(this::toEnrollmentTimeSlot).filter(Objects::nonNull)
                .map(EnrollmentTimeSlot::getSlotKey).collect(Collectors.toSet());
    }

    public Set<String> listOccupiedCourseSlotKeys(Long schoolId, Long serviceId, Long studentId) {
        List<Long> sessionIds = listActiveCourseEnrollments(schoolId, serviceId, studentId).stream()
                .map(AsEnrollmentEntity::getSessionId).filter(Objects::nonNull).collect(Collectors.toList());
        return mapSessionSlotKeys(sessionIds);
    }

    // ======================== 冲突检测 ========================

    public ScheduleConflict findScheduleConflict(Long schoolId, Long studentId, Long targetCourseClassId,
                                                  List<AsCourseSessionEntity> targetSessions) {
        return findScheduleConflict(schoolId, studentId, targetCourseClassId, targetSessions, Collections.singleton(targetCourseClassId));
    }

    public ScheduleConflict findScheduleConflict(Long schoolId, Long studentId, Long targetCourseClassId,
                                                  List<AsCourseSessionEntity> targetSessions, Set<Long> excludedCourseClassIds) {
        return findScheduleConflict(schoolId, studentId, targetCourseClassId, targetSessions, excludedCourseClassIds, null);
    }

    public ScheduleConflict findScheduleConflict(Long schoolId, Long studentId, Long targetCourseClassId,
                                                  List<AsCourseSessionEntity> targetSessions,
                                                  Set<Long> excludedCourseClassIds,
                                                  GuardianAvailableCourseReadContext readContext) {
        if (schoolId == null || studentId == null || targetCourseClassId == null || CollectionUtils.isEmpty(targetSessions)) {
            return null;
        }
        Set<Long> excludes = excludedCourseClassIds == null ? Collections.singleton(targetCourseClassId) : excludedCourseClassIds;
        List<AsEnrollmentEntity> studentEnrollments = readContext == null
                ? sessionEnrollmentSupport.listActiveSessionEnrollments(schoolId, studentId)
                : readContext.activeEnrollments(schoolId, () -> sessionEnrollmentSupport.listActiveSessionEnrollments(schoolId, studentId));
        List<AsEnrollmentEntity> activeEnrollments = studentEnrollments.stream()
                .filter(e -> !excludes.contains(e.getCourseClassId())).collect(Collectors.toList());
        if (activeEnrollments.isEmpty()) { return null; }
        List<Long> sessionIds = activeEnrollments.stream().map(AsEnrollmentEntity::getSessionId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (sessionIds.isEmpty()) { return null; }
        Map<Long, AsCourseSessionEntity> existingSessionMap;
        if (readContext == null) {
            List<AsCourseSessionEntity> existingSessions = asCourseSessionMapper.selectBatchIds(sessionIds);
            existingSessionMap = (existingSessions == null ? Collections.<AsCourseSessionEntity>emptyList() : existingSessions).stream()
                    .collect(Collectors.toMap(AsCourseSessionEntity::getId, Function.identity(), (a, b) -> a));
        } else {
            existingSessionMap = readContext.existingSessions(sessionIds, asCourseSessionMapper::selectBatchIds);
        }
        List<Long> courseClassIds = activeEnrollments.stream().map(AsEnrollmentEntity::getCourseClassId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, AsCourseClassEntity> courseMap;
        if (readContext == null) {
            List<AsCourseClassEntity> conflictCourses = courseClassIds.isEmpty() ? Collections.emptyList() : asCourseClassMapper.selectBatchIds(courseClassIds);
            courseMap = (conflictCourses == null ? Collections.<AsCourseClassEntity>emptyList() : conflictCourses).stream()
                    .collect(Collectors.toMap(AsCourseClassEntity::getId, Function.identity(), (a, b) -> a));
        } else {
            courseMap = readContext.existingCourses(courseClassIds, asCourseClassMapper::selectBatchIds);
        }
        for (AsCourseSessionEntity target : targetSessions) {
            for (AsEnrollmentEntity enrollment : activeEnrollments) {
                AsCourseSessionEntity existing = existingSessionMap.get(enrollment.getSessionId());
                if (isSameSessionDay(target, existing) && isSessionTimeOverlap(target, existing)) {
                    AsCourseClassEntity conflictCourse = courseMap.get(enrollment.getCourseClassId());
                    return new ScheduleConflict(enrollment.getCourseClassId(),
                            conflictCourse == null ? null : conflictCourse.getCourseClassName());
                }
            }
        }
        return null;
    }

    public boolean isAdminAssignableSessionTime(EnrollmentTimeSlot slot) {
        if (slot == null) { return false; }
        if (slot.getSessionDate() == null) { return true; }
        Calendar now = Calendar.getInstance();
        Calendar sessionDate = Calendar.getInstance();
        sessionDate.setTime(slot.getSessionDate());
        int nowDay = now.get(Calendar.YEAR) * 1000 + now.get(Calendar.DAY_OF_YEAR);
        int sessionDay = sessionDate.get(Calendar.YEAR) * 1000 + sessionDate.get(Calendar.DAY_OF_YEAR);
        if (sessionDay != nowDay) { return sessionDay > nowDay; }
        if (slot.getEndMinute() == null) { return true; }
        int nowMinute = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE);
        return slot.getEndMinute() > nowMinute;
    }

    /** 课程覆盖至少一个剩余服务时段时，视为可分配。 */
    public boolean courseCoversUnregisteredTime(AsServiceEntity service, Long studentId, List<AsCourseSessionEntity> targetSessions) {
        if (service == null || studentId == null) { return false; }
        if (CollectionUtils.isEmpty(targetSessions)) { return true; }
        AsEnrollmentEntity enrollment = new AsEnrollmentEntity();
        enrollment.setSchoolId(service.getSchoolId());
        enrollment.setServiceId(service.getId());
        enrollment.setStudentId(studentId);
        Set<String> remainingSlotKeys = findUnregisteredTimeSlots(enrollment, service).stream()
                .map(EnrollmentTimeSlot::getSlotKey).collect(Collectors.toSet());
        if (remainingSlotKeys.isEmpty()) { return false; }
        return targetSessions.stream().map(this::toEnrollmentTimeSlot).filter(Objects::nonNull)
                .map(EnrollmentTimeSlot::getSlotKey).anyMatch(remainingSlotKeys::contains);
    }
    public List<EnrollmentTimeSlot> sortSlots(Collection<EnrollmentTimeSlot> slots) {
        if (CollectionUtils.isEmpty(slots)) {
            return Collections.emptyList();
        }
        return slots.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator
                        .comparing(EnrollmentTimeSlot::getSessionDate, Comparator.nullsLast(Date::compareTo))
                        .thenComparing(EnrollmentTimeSlot::getWeekDay, Comparator.nullsLast(Integer::compareTo))
                        .thenComparing(EnrollmentTimeSlot::getStartMinute, Comparator.nullsLast(Integer::compareTo))
                        .thenComparing(EnrollmentTimeSlot::getEndMinute, Comparator.nullsLast(Integer::compareTo)))
                .collect(Collectors.toList());
    }

    private boolean isValid(Object entity) {
        if (entity == null) { return false; }
        if (entity instanceof AsServiceEntity) { return Objects.equals(((AsServiceEntity) entity).getIsValid(), 1); }
        if (entity instanceof AsCourseSessionEntity) { return Objects.equals(((AsCourseSessionEntity) entity).getIsValid(), 1); }
        return true;
    }


    private String buildScheduleTimeDescFromRules(List<AsCourseScheduleRuleEntity> rules) {
        if (CollectionUtils.isEmpty(rules)) {
            return null;
        }
        List<String> items = rules.stream()
                .map(this::formatScheduleRule)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
        return items.isEmpty() ? null : String.join("，", items);
    }

    private String buildScheduleTimeDescFromSessions(List<AsCourseSessionEntity> sessions) {
        if (CollectionUtils.isEmpty(sessions)) {
            return null;
        }
        List<String> items = sessions.stream()
                .map(this::buildSlotText)
                .filter(StringUtils::hasText)
                .distinct()
                .collect(Collectors.toList());
        return items.isEmpty() ? null : String.join("，", items);
    }

    private String formatScheduleRule(AsCourseScheduleRuleEntity rule) {
        if (rule == null) {
            return null;
        }
        String startTime = StringUtils.hasText(formatTime(rule.getStartTime())) ? formatTime(rule.getStartTime()) : "--:--";
        String endTime = StringUtils.hasText(formatTime(rule.getEndTime())) ? formatTime(rule.getEndTime()) : "--:--";
        if (Objects.equals(rule.getRuleType(), 1)) {
            List<Integer> weekDays = parseWeekDays(rule.getWeekDaysJson());
            if (CollectionUtils.isEmpty(weekDays)) {
                return null;
            }
            String weekDesc = weekDays.stream()
                    .map(this::weeklyRuleDayName)
                    .filter(StringUtils::hasText)
                    .collect(Collectors.joining("、"));
            return StringUtils.hasText(weekDesc) ? "每周" + weekDesc + " " + startTime + "-" + endTime : null;
        }
        if (Objects.equals(rule.getRuleType(), 2) && rule.getSessionDate() != null) {
            return new SimpleDateFormat("yyyy年M月d日").format(rule.getSessionDate())
                    + "（" + AssembleUtil.dateWeekDayName(rule.getSessionDate()) + "）" + startTime + "-" + endTime;
        }
        return null;
    }

    private List<Integer> parseWeekDays(String weekDaysJson) {
        if (!StringUtils.hasText(weekDaysJson)) {
            return Collections.emptyList();
        }
        try {
            List<Integer> weekDays = JSON.parseArray(weekDaysJson, Integer.class);
            return weekDays == null ? Collections.emptyList() : weekDays.stream()
                    .filter(Objects::nonNull)
                    .sorted()
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    private String weeklyRuleDayName(Integer weekDay) {
        String name = AssembleUtil.getWeekDayName(weekDay);
        return StringUtils.hasText(name) ? name.replace("每周", "") : null;
    }

    private boolean isCourseSessionStarted(AsCourseSessionEntity session, Date now) {
        if (session == null || Objects.equals(session.getSessionStatus(), CoreConstant.SESSION_STATUS_CANCELLED)) {
            return false;
        }
        if (session.getSessionStatus() != null && !Objects.equals(session.getSessionStatus(), CoreConstant.SESSION_STATUS_NOT_STARTED)) {
            return true;
        }
        Date startTime = combineSessionDateTime(session.getSessionDate(), session.getStartTime());
        return startTime != null && !startTime.after(now);
    }

    private Date combineSessionDateTime(Date date, Date time) {
        if (date == null) {
            return null;
        }
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        if (time == null) {
            return dateCalendar.getTime();
        }
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTime(time);
        dateCalendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
        dateCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
        dateCalendar.set(Calendar.SECOND, timeCalendar.get(Calendar.SECOND));
        dateCalendar.set(Calendar.MILLISECOND, timeCalendar.get(Calendar.MILLISECOND));
        return dateCalendar.getTime();
    }
}
