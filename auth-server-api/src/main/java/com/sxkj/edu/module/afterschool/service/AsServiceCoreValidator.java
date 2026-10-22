package com.sxkj.edu.module.afterschool.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.domain.BaseEntity;
import com.sxkj.edu.domain.sys.entity.BaseFileEntity;
import com.sxkj.edu.module.afterschool.domain.dto.AsCourseScheduleRuleAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsServiceBasicSaveAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsServiceCourseItemAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseCategoryEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseClassEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsSchedulePeriodEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsScheduleTemplateEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsServiceEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsTeacherEntity;
import com.sxkj.edu.module.afterschool.domain.entity.EduclTermEntity;
import com.sxkj.edu.module.afterschool.domain.entity.ExamRoomEntity;
import com.sxkj.edu.module.afterschool.mapper.AsCourseCategoryMapper;
import com.sxkj.edu.module.afterschool.mapper.AsCourseClassMapper;
import com.sxkj.edu.module.afterschool.mapper.AsScheduleTemplateMapper;
import com.sxkj.edu.module.afterschool.mapper.AsServiceMapper;
import com.sxkj.edu.module.afterschool.mapper.AsTeacherMapper;
import com.sxkj.edu.module.afterschool.mapper.EduclTermMapper;
import com.sxkj.edu.module.afterschool.support.CourseClassScopeSupport;
import com.sxkj.edu.service.BaseFileService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 课后服务新增、复制共用的核心校验器。
 */
@Service
public class AsServiceCoreValidator {

    @Resource
    private AsCourseQuotaService asCourseQuotaService;

    @Resource
    private EduclTermMapper educlTermMapper;

    @Resource
    private AsScheduleTemplateMapper asScheduleTemplateMapper;

    @Resource
    private AsServiceMapper asServiceMapper;

    @Resource
    private AsTeacherMapper asTeacherMapper;

    @Resource
    private AsCourseCategoryMapper asCourseCategoryMapper;

    @Resource
    private AsCourseClassMapper asCourseClassMapper;

    @Resource
    private AsSchedulePeriodService asSchedulePeriodService;

    @Resource
    private CourseClassScopeSupport courseClassScopeSupport;

    @Resource
    private ExamRoomService examRoomService;

    @Resource
    private BaseFileService baseFileService;

    /**
     * 校验课后服务基本信息，新增和复制必须使用同一口径。
     */
    public String validateBasic(AsServiceBasicSaveAO ao, Long schoolId) {
        if (ao == null) {
            return "请求参数不能为空";
        }
        if (!StringUtils.hasText(ao.getServiceName())) {
            return "课后服务名称不能为空";
        }
        if (ao.getTermId() == null) {
            return "学期不能为空";
        }
        if (schoolId == null || schoolId <= 0) {
            return "学校不能为空";
        }
        if (ao.getEnrollStartTime() == null) {
            return "报名开始时间不能为空";
        }
        if (ao.getEnrollEndTime() == null) {
            return "报名结束时间不能为空";
        }
        if (!ao.getEnrollStartTime().before(ao.getEnrollEndTime())) {
            return "报名开始时间必须早于报名结束时间";
        }
        if (ao.getCourseStartDate() == null || ao.getCourseEndDate() == null) {
            return "开课周期不能为空";
        }
        Date courseStartDate = normalizeDate(ao.getCourseStartDate());
        Date courseEndDate = normalizeDate(ao.getCourseEndDate());
        if (courseStartDate.after(courseEndDate)) {
            return "开课日期不能晚于结课日期";
        }
        if (!ao.getEnrollEndTime().before(courseStartDate)) {
            return "报名结束时间必须早于开课日期";
        }
        EduclTermEntity term = educlTermMapper.selectById(ao.getTermId());
        if (!isValid(term)) {
            return "学期不存在";
        }
        if (term.getStartDate() != null && courseStartDate.before(normalizeDate(term.getStartDate()))) {
            return "开课日期不能早于学期开始日期";
        }
        if (term.getEndDate() != null && courseEndDate.after(normalizeDate(term.getEndDate()))) {
            return "结课日期不能晚于学期结束日期";
        }
        if (ao.getAfterSchoolTimeTemplateId() != null) {
            AsScheduleTemplateEntity template = asScheduleTemplateMapper.selectById(ao.getAfterSchoolTimeTemplateId());
            if (!isValid(template)) {
                return "课后时间方案不存在";
            }
        }
        QueryWrapper<AsServiceEntity> duplicateWrapper = new QueryWrapper<>();
        duplicateWrapper.eq("is_valid", 1)
                .eq("school_id", schoolId)
                .eq("term_id", ao.getTermId())
                .eq("service_name", ao.getServiceName().trim());
        if (ao.getId() != null) {
            duplicateWrapper.ne("id", ao.getId());
        }
        Integer duplicateCount = asServiceMapper.selectCount(duplicateWrapper);
        if (duplicateCount != null && duplicateCount > 0) {
            return "同一学校同一学期下课后服务名称不能重复";
        }
        return null;
    }

    /**
     * 校验课程、适用范围和排课规则。
     */
    public String validateCourseItem(AsServiceEntity service, AsServiceCourseItemAO item) {
        if (item == null) {
            return "课程信息不能为空";
        }
        if (!StringUtils.hasText(item.getCourseClassName())) {
            return "课程班名称不能为空";
        }
        if (!Arrays.asList(CoreConstant.ENROLL_TYPE_PUBLIC, CoreConstant.ENROLL_TYPE_INVITE,
                CoreConstant.ENROLL_TYPE_CLASS_ASSIGN).contains(item.getEnrollType())) {
            return "报名方式不合法";
        }
        if (item.getTeacherId() == null) {
            return "授课教师不能为空";
        }
        if (!isValid(asTeacherMapper.selectById(item.getTeacherId()))) {
            return "授课教师不存在";
        }
        if (item.getCategoryId() != null && !isValid(asCourseCategoryMapper.selectById(item.getCategoryId()))) {
            return "课后类别不存在";
        }
        if (Objects.equals(item.getEnrollType(), CoreConstant.ENROLL_TYPE_PUBLIC)) {
            String quotaError = asCourseQuotaService.validateConfiguration(item);
            if (quotaError != null) {
                return quotaError;
            }
            if (item.getGenderRequirement() != null && !Arrays.asList(0, 1, 2).contains(item.getGenderRequirement())) {
                return "性别要求不合法";
            }
        }
        if (Objects.equals(item.getEnrollType(), CoreConstant.ENROLL_TYPE_INVITE)
                && item.getParentConfirmRequired() != null
                && !Arrays.asList(0, 1).contains(item.getParentConfirmRequired())) {
            return "家长确认配置不合法";
        }
        String scopeValidate = courseClassScopeSupport.validateScope(service.getSchoolId(),
                item.getApplicableClassIds(), item.getApplicableGradeIds());
        if (scopeValidate != null) {
            return scopeValidate;
        }
        if (CollectionUtils.isEmpty(item.getScheduleRules())) {
            return "请至少配置一条排课规则";
        }
        if (item.getId() != null) {
            AsCourseClassEntity existing = asCourseClassMapper.selectById(item.getId());
            if (!isValid(existing) || !Objects.equals(existing.getServiceId(), service.getId())) {
                return "课程班不存在或不属于当前服务";
            }
            if (!Objects.equals(existing.getEnrollType(), item.getEnrollType())
                    && (asCourseQuotaService.isPublic(existing)
                    || Objects.equals(item.getEnrollType(), CoreConstant.ENROLL_TYPE_PUBLIC))) {
                return "已有课程班不能切换公开选课类型，请新建课程班";
            }
            String capacityChangeError = asCourseQuotaService.validateCourseCapacityChange(existing, item.getCapacity());
            if (capacityChangeError != null) {
                return capacityChangeError;
            }
            String quotaChangeError = asCourseQuotaService.validateExistingConfiguration(existing, item);
            if (quotaChangeError != null) {
                return quotaChangeError;
            }
        }
        for (AsCourseScheduleRuleAO rule : emptyList(item.getScheduleRules())) {
            String validate = validateRule(service, rule);
            if (validate != null) {
                return validate;
            }
        }
        return null;
    }

    /**
     * 校验单条排课规则，并按当前节次配置补全起止时间。
     */
    public String validateRule(AsServiceEntity service, AsCourseScheduleRuleAO rule) {
        if (rule == null) {
            return "排课规则不能为空";
        }
        if (rule.getSchedulePeriodId() != null) {
            AsSchedulePeriodEntity period = asSchedulePeriodService.getById(rule.getSchedulePeriodId());
            if (!isValid(period)) {
                return "节次不存在";
            }
            if (service.getAfterSchoolTimeTemplateId() != null
                    && !Objects.equals(period.getTemplateId(), service.getAfterSchoolTimeTemplateId())) {
                return "节次不属于当前课后时间方案";
            }
            String periodStart = formatTime(period.getStartTime());
            String periodEnd = formatTime(period.getEndTime());
            if (periodStart != null && periodEnd != null) {
                rule.setStartTime(periodStart);
                rule.setEndTime(periodEnd);
            }
        }
        Date startTime = parseTime(rule.getStartTime());
        Date endTime = parseTime(rule.getEndTime());
        if (startTime == null || endTime == null) {
            return rule.getSchedulePeriodId() == null ? "请选择节次或填写上课时间" : "节次时间配置不完整";
        }
        if (!startTime.before(endTime)) {
            return "上课开始时间必须早于结束时间";
        }
        if (isWeeklyRule(rule)) {
            if (CollectionUtils.isEmpty(rule.getWeekDays())) {
                return "周期排课必须选择星期";
            }
            for (Integer weekDay : rule.getWeekDays()) {
                if (weekDay == null || weekDay < 1 || weekDay > 7) {
                    return "星期取值范围为1到7";
                }
            }
        } else if (isDateRule(rule)) {
            if (rule.getSessionDate() == null) {
                return "自定义日期排课必须填写上课日期";
            }
            Date sessionDate = normalizeDate(rule.getSessionDate());
            if (service.getCourseStartDate() != null
                    && sessionDate.before(normalizeDate(service.getCourseStartDate()))) {
                return "自定义上课日期不能早于开课日期";
            }
            if (service.getCourseEndDate() != null
                    && sessionDate.after(normalizeDate(service.getCourseEndDate()))) {
                return "自定义上课日期不能晚于结课日期";
            }
        } else {
            return "排课规则类型不合法";
        }
        return null;
    }

    /**
     * 严格校验课程引用数据，供复制和发布前复核使用。
     */
    public String validateCourseReferences(AsServiceEntity service, AsServiceCourseItemAO item,
                                           Long courseFileId) {
        if (service == null || service.getSchoolId() == null) {
            return "课后服务学校信息不完整";
        }
        if (item == null) {
            return "课程信息不能为空";
        }
        Long schoolId = service.getSchoolId();
        String teacherValidate = validateTeacherReference(item.getTeacherId(), schoolId);
        if (teacherValidate != null) {
            return teacherValidate;
        }
        String categoryValidate = validateCategoryReference(item.getCategoryId(), schoolId, "课后类别");
        if (categoryValidate != null) {
            return categoryValidate;
        }
        String moduleValidate = validateCategoryReference(item.getModuleId(), schoolId, "学习模块");
        if (moduleValidate != null) {
            return moduleValidate;
        }
        String placeValidate = validatePlaceReferences(item.getPlaceId(), schoolId);
        if (placeValidate != null) {
            return placeValidate;
        }
        if (courseFileId != null) {
            BaseFileEntity file = baseFileService.getById(courseFileId);
            if (file == null || !Objects.equals(file.getIsValid(), 1)) {
                return "课程封面文件不存在或已失效";
            }
        }
        for (AsCourseScheduleRuleAO rule : emptyList(item.getScheduleRules())) {
            String ruleValidate = validateRuleReferences(service, rule, schoolId);
            if (ruleValidate != null) {
                return ruleValidate;
            }
        }
        return null;
    }

    private String validateRuleReferences(AsServiceEntity service, AsCourseScheduleRuleAO rule, Long schoolId) {
        if (rule == null) {
            return "排课规则不能为空";
        }
        if (rule.getTeacherId() != null) {
            String teacherValidate = validateTeacherReference(rule.getTeacherId(), schoolId);
            if (teacherValidate != null) {
                return "排课规则中的" + teacherValidate;
            }
        }
        if (rule.getSchedulePeriodId() != null) {
            AsSchedulePeriodEntity period = asSchedulePeriodService.getById(rule.getSchedulePeriodId());
            if (!isValid(period)) {
                return "排课节次不存在或已失效";
            }
            if (!Objects.equals(period.getSchoolId(), schoolId)) {
                return "排课节次不属于当前学校";
            }
            if (service.getAfterSchoolTimeTemplateId() != null
                    && !Objects.equals(period.getTemplateId(), service.getAfterSchoolTimeTemplateId())) {
                return "排课节次不属于当前课后时间方案";
            }
        }
        String placeValidate = validatePlaceReferences(rule.getPlaceId(), schoolId);
        return placeValidate == null ? null : "排课规则中的" + placeValidate;
    }

    private String validateTeacherReference(Long teacherId, Long schoolId) {
        if (teacherId == null) {
            return "授课教师不能为空";
        }
        AsTeacherEntity teacher = asTeacherMapper.selectById(teacherId);
        if (!isValid(teacher)) {
            return "授课教师不存在或已失效";
        }
        if (!Objects.equals(teacher.getSchoolId(), schoolId)) {
            return "授课教师不属于当前学校";
        }
        if (!Objects.equals(teacher.getRegisterStatus(), CoreConstant.TEACHER_REGISTER_STATUS_APPROVED)) {
            return "授课教师尚未审核通过";
        }
        if (!Objects.equals(teacher.getEmployeeStatus(), CoreConstant.TEACHER_EMPLOYEE_STATUS_ACTIVE)) {
            return "授课教师不是在职状态";
        }
        return null;
    }

    private String validateCategoryReference(Long categoryId, Long schoolId, String fieldName) {
        if (categoryId == null) {
            return null;
        }
        AsCourseCategoryEntity category = asCourseCategoryMapper.selectById(categoryId);
        if (!isValid(category)) {
            return fieldName + "不存在或已失效";
        }
        if (!Objects.equals(category.getSchoolId(), schoolId)) {
            return fieldName + "不属于当前学校";
        }
        if (!Objects.equals(category.getEnabledStatus(), CoreConstant.ENABLED_STATUS)) {
            return fieldName + "未启用";
        }
        return null;
    }

    private String validatePlaceReferences(String placeIds, Long schoolId) {
        if (!StringUtils.hasText(placeIds)) {
            return null;
        }
        for (String value : placeIds.split(",")) {
            Long placeId;
            try {
                placeId = Long.valueOf(value.trim());
            } catch (NumberFormatException exception) {
                return "上课地点配置无效";
            }
            ExamRoomEntity room = examRoomService.getById(placeId);
            if (!isValid(room)) {
                return "上课地点不存在或已失效";
            }
            if (!Objects.equals(room.getSchoolId(), schoolId)) {
                return "上课地点不属于当前学校";
            }
        }
        return null;
    }

    private boolean isWeeklyRule(AsCourseScheduleRuleAO rule) {
        return rule != null && ("weekly".equalsIgnoreCase(rule.getRuleType()) || "1".equals(rule.getRuleType()));
    }

    private boolean isDateRule(AsCourseScheduleRuleAO rule) {
        return rule != null && ("date".equalsIgnoreCase(rule.getRuleType()) || "2".equals(rule.getRuleType()));
    }

    private Date parseTime(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        try {
            return new SimpleDateFormat("HH:mm").parse(value.trim());
        } catch (ParseException e) {
            return null;
        }
    }

    private String formatTime(Date time) {
        return time == null ? null : new SimpleDateFormat("HH:mm").format(time);
    }

    private Date normalizeDate(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private boolean isValid(BaseEntity entity) {
        return entity != null && Objects.equals(entity.getIsValid(), 1);
    }

    private <E> List<E> emptyList(List<E> list) {
        return list == null ? Collections.emptyList() : list;
    }
}
