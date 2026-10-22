package com.sxkj.edu.module.afterschool.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.utils.ExcelUtils;
import com.sxkj.edu.module.afterschool.domain.ao.*;
import com.sxkj.edu.module.afterschool.domain.entity.*;
import com.sxkj.edu.module.afterschool.domain.excel.AsCourseClassExportExcel;
import com.sxkj.edu.module.afterschool.domain.vo.*;
import com.sxkj.edu.module.afterschool.enums.AttendanceStatusEnum;
import com.sxkj.edu.module.afterschool.enums.BaseExamRecordEnum;
import com.sxkj.edu.module.afterschool.enums.ClassStatusEnum;
import com.sxkj.edu.module.afterschool.enums.EnrollTypeEnum;
import com.sxkj.edu.module.afterschool.mapper.*;
import com.sxkj.edu.module.afterschool.service.*;
import com.sxkj.edu.module.afterschool.support.StudentTermScopeSupport;
import com.sxkj.edu.module.business.entity.SchoolManagePo;
import com.sxkj.edu.service.SchoolManageService;
import com.sxkj.edu.service.TBaseExamRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 课程班Service实现类
 *
 * @author jiangjj
 * @date 2026-05-17
 */
@Service
@Slf4j
public class AsCourseClassServiceImpl extends ServiceImpl<AsCourseClassMapper, AsCourseClassEntity>
        implements AsCourseClassService {

    private static final int ENROLL_STATUS_ENROLLED = 1;
    private static final int ENROLL_STATUS_ASSIGNED = 3;
    private static final int SESSION_STATUS_CANCELLED = 3;
    private static final int RULE_TYPE_WEEKLY = 1;
    private static final int RULE_TYPE_DATE = 2;
    private static final List<Integer> PRESENT_STATUS_CODES = AttendanceStatusEnum.getPresentCodes();
    private static final Integer EARLY_LEAVE_STATUS_CODE = AttendanceStatusEnum.EARLY_LEAVE.getCode();

    @Resource
    private ExamRoomService examRoomService;

    @Resource
    private AsServiceMapper asServiceMapper;

    @Resource
    private AsCourseCategoryMapper asCourseCategoryMapper;

    @Resource
    private AsTeacherMapper asTeacherMapper;

    @Resource
    private AsCourseSessionMapper asCourseSessionMapper;

    @Resource
    private AsCourseSessionService asCourseSessionService;

    @Resource
    private SchoolManageService schoolManageService;

    @Resource
    private EduclTermService educlTermService;

    @Resource
    private AsCourseScheduleRuleMapper asCourseScheduleRuleMapper;

    @Resource
    private AsEnrollmentMapper asEnrollmentMapper;

    @Resource
    private StudentTermScopeSupport studentTermScopeSupport;

    @Resource
    private AsCourseQuotaService asCourseQuotaService;

    @Resource
    private TBaseExamRecordService baseExamRecordService;

    @Override
    public AsCourseClassVO getCourseClassDetail(Long id) {
        AsCourseClassEntity entity = this.getById(id);
        if (entity == null) {
            return null;
        }
        return convertToVO(entity);
    }


    /**
     * Entity转VO
     */
    private AsCourseClassVO convertToVO(AsCourseClassEntity entity) {
        if (entity == null) {
            return null;
        }
        AsCourseClassVO vo = new AsCourseClassVO();
        BeanUtils.copyProperties(entity, vo);
        vo.setQuota(asCourseQuotaService.view(entity, null));
        Integer enrolledCount = countDistinctEnrolledStudents(entity.getId());
        vo.setEnrolledCount(enrolledCount);

        // 计算剩余名额：capacity=0 表示不限名额
        if (entity.getCapacity() != null && enrolledCount != null) {
            if (entity.getCapacity() > 0) {
                vo.setRemainingCount(Math.max(0, entity.getCapacity() - enrolledCount));
            } else {
                // 不限容量时，capacity 显示为已报名人数，剩余名额返回 null
                vo.setCapacity(enrolledCount);
                vo.setRemainingCount(null);
            }
        }

        // 解析上课地点：将教室ID转换为"综合楼 二楼 203"格式
        vo.setPlaceId(entity.getPlaceId());
        vo.setPlaceName(examRoomService.resolvePlaceName(entity.getPlaceId()));

        // 解析关联实体名称
        resolveSchoolName(vo, entity.getSchoolId());
        resolveServiceName(vo, entity.getServiceId());
        resolveCategoryName(vo, entity.getCategoryId());
        resolveTeacherName(vo, entity.getTeacherId());
        resolveEnrollTypeDesc(vo, entity.getEnrollType());
        resolveClassStatusDesc(vo, entity.getClassStatus());
        resolveAttendanceRate(vo, entity.getId());
        resolveScheduleTime(vo, entity.getId());

        return vo;
    }

    /**
     * 按已选课组班口径统计课程班实际报名人数，避免课次记录按行数重复计算。
     */
    private Integer countDistinctEnrolledStudents(Long courseClassId) {
        if (courseClassId == null) {
            return 0;
        }
        QueryWrapper<AsEnrollmentEntity> wrapper = new QueryWrapper<>();
        wrapper.select("COUNT(DISTINCT student_id)")
                .eq("is_valid", 1)
                .eq("course_class_id", courseClassId)
                .isNotNull("session_id")
                .in("enroll_status", Arrays.asList(ENROLL_STATUS_ENROLLED, ENROLL_STATUS_ASSIGNED))
                .apply("EXISTS (SELECT 1 FROM t_as_student s WHERE s.id = student_id AND s.is_valid = 1 AND s.register_status = 1)");
        List<Object> result = asEnrollmentMapper.selectObjs(wrapper);
        if (CollectionUtil.isEmpty(result) || result.get(0) == null) {
            return 0;
        }
        return ((Number) result.get(0)).intValue();
    }

    /**
     * 解析学校名称
     */
    private void resolveSchoolName(AsCourseClassVO vo, Long schoolId) {
        if (schoolId == null) {
            return;
        }
        SchoolManagePo school = schoolManageService.getById(schoolId);
        if (school != null) {
            vo.setSchoolName(school.getSchoolName());
        }
    }

    /**
     * 解析课后服务名称及学期名称
     */
    private void resolveServiceName(AsCourseClassVO vo, Long serviceId) {
        if (serviceId == null) {
            return;
        }
        AsServiceEntity service = asServiceMapper.selectById(serviceId);
        if (service != null) {
            vo.setServiceName(service.getServiceName());
            vo.setTermId(service.getTermId());
            // 根据服务关联的学期ID查询学期名称
            if (service.getTermId() != null) {
                EduclTermEntity term = educlTermService.getById(service.getTermId());
                if (term != null) {
                    vo.setTermName(term.getTermName());
                }
            }
        }
    }

    /**
     * 解析课程类别名称
     */
    private void resolveCategoryName(AsCourseClassVO vo, Long categoryId) {
        if (categoryId == null) {
            return;
        }
        AsCourseCategoryEntity category = asCourseCategoryMapper.selectById(categoryId);
        if (category != null) {
            vo.setCategoryName(category.getCategoryName());
        }
    }

    /**
     * 解析教师姓名
     */
    private void resolveTeacherName(AsCourseClassVO vo, Long teacherId) {
        if (teacherId == null) {
            return;
        }
        AsTeacherEntity teacher = asTeacherMapper.selectById(teacherId);
        if (teacher != null) {
            vo.setTeacherName(teacher.getTeacherName());
        }
    }

    /**
     * 解析报名方式描述
     */
    private void resolveEnrollTypeDesc(AsCourseClassVO vo, Integer enrollType) {
        if (enrollType == null) {
            return;
        }
        EnrollTypeEnum typeEnum = EnrollTypeEnum.getByCode(enrollType);
        if (typeEnum != null) {
            vo.setEnrollTypeDesc(typeEnum.getDesc());
        }
    }

    /**
     * 解析课程班状态描述
     */
    private void resolveClassStatusDesc(AsCourseClassVO vo, Integer classStatus) {
        if (classStatus == null) {
            return;
        }
        ClassStatusEnum statusEnum = ClassStatusEnum.getByCode(classStatus);
        if (statusEnum != null) {
            vo.setClassStatusDesc(statusEnum.getDesc());
        }
    }

    /**
     * 计算课程班总出勤率。
     * 出勤率 = 正常、迟到、早退记录数 / 总考勤记录数 * 100。
     */
    private void resolveAttendanceRate(AsCourseClassVO vo, Long courseClassId) {
        Double rate = baseMapper.queryCourseClassAttendanceRate(courseClassId, PRESENT_STATUS_CODES);
        vo.setAttendanceRate(rate == null ? 0.0D : rate);
    }

    /**
     * 解析课程班上课时间：周期规则显示为每周几，自定义日期显示为具体年月日。
     */
    private void resolveScheduleTime(AsCourseClassVO vo, Long courseClassId) {
        LambdaQueryWrapper<AsCourseSessionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsCourseSessionEntity::getCourseClassId, courseClassId)
                .eq(AsCourseSessionEntity::getIsValid, 1)
                .ne(AsCourseSessionEntity::getSessionStatus, SESSION_STATUS_CANCELLED)
                .orderByAsc(AsCourseSessionEntity::getSessionDate)
                .orderByAsc(AsCourseSessionEntity::getStartTime);
        List<AsCourseSessionEntity> sessions = asCourseSessionMapper.selectList(wrapper);

        if (sessions.isEmpty()) {
            return;
        }

        String scheduleTimeDesc = buildScheduleTimeDesc(courseClassId, sessions);
        if (StringUtils.isNotBlank(scheduleTimeDesc)) {
            vo.setScheduleTimeDesc(scheduleTimeDesc);
        }

        // 总课时按有效未取消课次计数
        vo.setTotalHours(sessions.size());
    }

    private String buildScheduleTimeDesc(Long courseClassId, List<AsCourseSessionEntity> sessions) {
        List<AsCourseScheduleRuleEntity> rules = listScheduleRules(courseClassId);
        if (CollectionUtil.isNotEmpty(rules)) {
            List<String> ruleItems = rules.stream()
                    .map(this::formatScheduleRule)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(ruleItems)) {
                return String.join("，", ruleItems);
            }
        }
        return formatSessionItems(sessions);
    }

    private List<AsCourseScheduleRuleEntity> listScheduleRules(Long courseClassId) {
        if (courseClassId == null) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<AsCourseScheduleRuleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AsCourseScheduleRuleEntity::getIsValid, 1)
                .eq(AsCourseScheduleRuleEntity::getCourseClassId, courseClassId)
                .orderByAsc(AsCourseScheduleRuleEntity::getRuleType)
                .orderByAsc(AsCourseScheduleRuleEntity::getSessionDate)
                .orderByAsc(AsCourseScheduleRuleEntity::getStartTime);
        return asCourseScheduleRuleMapper.selectList(wrapper);
    }

    private String formatScheduleRule(AsCourseScheduleRuleEntity rule) {
        if (rule == null) {
            return null;
        }
        SimpleDateFormat timeFmt = new SimpleDateFormat("HH:mm");
        String startTime = rule.getStartTime() != null ? timeFmt.format(rule.getStartTime()) : "--:--";
        String endTime = rule.getEndTime() != null ? timeFmt.format(rule.getEndTime()) : "--:--";
        if (Objects.equals(rule.getRuleType(), RULE_TYPE_WEEKLY)) {
            List<Integer> weekDays = parseWeekDays(rule.getWeekDaysJson());
            if (CollectionUtil.isEmpty(weekDays)) {
                return null;
            }
            String weekDesc = weekDays.stream()
                    .map(this::weeklyRuleDayName)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining("、"));
            return StringUtils.isBlank(weekDesc) ? null : "每周" + weekDesc + " " + startTime + "-" + endTime;
        }
        if (Objects.equals(rule.getRuleType(), RULE_TYPE_DATE) && rule.getSessionDate() != null) {
            SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy年M月d日");
            return dateFmt.format(rule.getSessionDate()) + "（" + dateWeekDayName(rule.getSessionDate()) + "）" + startTime + "-" + endTime;
        }
        return null;
    }

    private List<Integer> parseWeekDays(String weekDaysJson) {
        if (StringUtils.isBlank(weekDaysJson)) {
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

    private String formatSessionItems(List<AsCourseSessionEntity> sessions) {
        SimpleDateFormat timeFmt = new SimpleDateFormat("HH:mm");
        SimpleDateFormat shortDateFmt = new SimpleDateFormat("yyyy年M月d日");

        List<String> items = new ArrayList<>();
        for (AsCourseSessionEntity session : sessions) {
            if (session.getSessionDate() == null) {
                continue;
            }
            String startTime = session.getStartTime() != null ? timeFmt.format(session.getStartTime()) : "--:--";
            String endTime = session.getEndTime() != null ? timeFmt.format(session.getEndTime()) : "--:--";
            String shortDate = shortDateFmt.format(session.getSessionDate());
            items.add(shortDate + "（" + dateWeekDayName(session.getSessionDate()) + "）" + startTime + "-" + endTime);
        }
        return CollectionUtil.isEmpty(items) ? null : String.join("，", items);
    }

    private String dateWeekDayName(Date date) {
        if (date == null) {
            return "";
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String dayName = weeklyRuleDayName(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ? 7 : cal.get(Calendar.DAY_OF_WEEK) - 1);
        return StringUtils.isBlank(dayName) ? "" : "周" + dayName;
    }

    private String weeklyRuleDayName(Integer weekDay) {
        if (weekDay == null) {
            return "";
        }
        switch (weekDay) {
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
            case 7:
                return "日";
            default:
                return String.valueOf(weekDay);
        }
    }

    @Override
    public PageResultDTO<AsCourseClassPageVO> pageQueryCourseClass(AsCourseClassPageQueryAO query) {
        normalizeCourseClassPageScope(query);
        Page<AsCourseClassPageVO> page = new Page<>(query.getPageIndex(), query.getPageSize());
        Page<AsCourseClassPageVO> resultPage = baseMapper.pageQueryCourseClass(page, query, PRESENT_STATUS_CODES);

        // 解析教室名称
        if (CollectionUtil.isNotEmpty(resultPage.getRecords())) {
            List<Long> publicCourseIds = resultPage.getRecords().stream().filter(vo -> Objects.equals(vo.getEnrollType(), 1))
                    .map(AsCourseClassPageVO::getId).collect(Collectors.toList());
            Map<Long, AsCourseClassEntity> quotaCourses = publicCourseIds.isEmpty() ? Collections.emptyMap()
                    : baseMapper.selectBatchIds(publicCourseIds).stream()
                    .collect(Collectors.toMap(AsCourseClassEntity::getId, course -> course));
            asCourseQuotaService.load(quotaCourses.values());
            resultPage.getRecords().forEach(vo -> {
                vo.setQuota(asCourseQuotaService.view(quotaCourses.get(vo.getId()), null));
                vo.setPlaceName(examRoomService.resolvePlaceName(vo.getPlaceId()));
            });
        }

        PageResultDTO<AsCourseClassPageVO> result = new PageResultDTO<>();
        result.setTotal(resultPage.getTotal());
        result.setPages(resultPage.getPages());
        result.setPageIndex(resultPage.getCurrent());
        result.setPageSize(resultPage.getSize());
        result.setList(resultPage.getRecords());
        return result;
    }

    @Override
    public AsCourseClassCountVO countCourseClass(AsCourseClassCountQueryAO query) {
        AsCourseClassCountQueryAO safeQuery = query == null ? new AsCourseClassCountQueryAO() : query;
        normalizeCourseClassCountScope(safeQuery);
        AsCourseClassCountVO result = baseMapper.countCourseClass(safeQuery);
        return result == null ? new AsCourseClassCountVO() : result;
    }

    @Override
    public void exportCourseClass(AsCourseClassPageQueryAO query, HttpServletResponse response) {
        AsCourseClassPageQueryAO safeQuery = query == null ? new AsCourseClassPageQueryAO() : query;
        normalizeCourseClassPageScope(safeQuery);
        int batchSize = 500;
    
        // 先收集全部数据
        List<AsCourseClassExportExcel> excelList = new ArrayList<>();
        safeQuery.setPageIndex(1);
        safeQuery.setPageSize(batchSize);
        Page<AsCourseClassPageVO> firstPage = baseMapper.pageQueryCourseClass(
                new Page<>(1, batchSize), safeQuery, PRESENT_STATUS_CODES);
        firstPage.getRecords().forEach(vo -> vo.setPlaceName(examRoomService.resolvePlaceName(vo.getPlaceId())));
        firstPage.getRecords().forEach(vo -> excelList.add(toExportExcel(vo)));
        long total = firstPage.getTotal();
    
        long totalPages = (total + batchSize - 1) / batchSize;
        for (long i = 2; i <= totalPages; i++) {
            Page<AsCourseClassPageVO> nextPage = baseMapper.pageQueryCourseClass(
                    new Page<>(i, batchSize), safeQuery, PRESENT_STATUS_CODES);
            nextPage.getRecords().forEach(vo -> vo.setPlaceName(examRoomService.resolvePlaceName(vo.getPlaceId())));
            nextPage.getRecords().forEach(vo -> excelList.add(toExportExcel(vo)));
        }
    
        // 一次性导出
        try {
            ExcelUtils.exportExcel(response, excelList, AsCourseClassExportExcel.class, "课程班", "课程班");
        } catch (Exception e) {
            log.error("课程班导出异常", e);
        }
    }

    private AsCourseClassExportExcel toExportExcel(AsCourseClassPageVO vo) {
        AsCourseClassExportExcel ex = new AsCourseClassExportExcel();
        BeanUtils.copyProperties(vo, ex);
        return ex;
    }

    /**
     * 统一课程班列表和导出的学校、学期范围。
     */
    private void normalizeCourseClassPageScope(AsCourseClassPageQueryAO query) {
        EduclTermEntity term = query.getTermId() == null
                ? studentTermScopeSupport.resolveReadableTerm(query.getSchoolId())
                : studentTermScopeSupport.requireTermInSchool(
                        query.getSchoolId(), query.getTermId());
        query.setTermId(term.getId());
    }

    /**
     * 统一课程班状态数量的学校、学期范围。
     */
    private void normalizeCourseClassCountScope(AsCourseClassCountQueryAO query) {
        EduclTermEntity term = query.getTermId() == null
                ? studentTermScopeSupport.resolveReadableTerm(query.getSchoolId())
                : studentTermScopeSupport.requireTermInSchool(
                        query.getSchoolId(), query.getTermId());
        query.setTermId(term.getId());
    }

    @Override
    public List<AsCourseClassDropdownVO> listDropdownByServiceDateRange(AsCourseClassDropdownQueryAO ao) {
        return baseMapper.listDropdownByServiceDateRange(ao);
    }

    @Override
    public PageResultDTO<AsSessionAttendancePageVO> pageQuerySessionAttendance(AsSessionAttendancePageQueryAO query) {
        Page<AsSessionAttendancePageVO> page = new Page<>(query.getPageIndex(), query.getPageSize());
        Page<AsSessionAttendancePageVO> resultPage = baseMapper.pageQuerySessionAttendance(
                page, query, PRESENT_STATUS_CODES, EARLY_LEAVE_STATUS_CODE);

        PageResultDTO<AsSessionAttendancePageVO> result = new PageResultDTO<>();
        result.setTotal(resultPage.getTotal());
        result.setPages(resultPage.getPages());
        result.setPageIndex(resultPage.getCurrent());
        result.setPageSize(resultPage.getSize());
        result.setList(resultPage.getRecords());
        return result;
    }


    @Override
    public JsonResult<String> transferTeacherSubject(AsCourseClassTransferAO ao) {
        JsonResult<String> resResp = JsonResult.buildError("教师课程移交失败");
        AsCourseClassEntity origin = this.getById(ao.getCourseClassId());
        if(null == origin){
            return JsonResult.buildError("查无数据");
        }
        if(origin.getTeacherId().equals(ao.getTeacherId())){
            return JsonResult.buildError("您已经是当前课程的授课教师，无需移交");
        }
        origin.setTeacherId(ao.getTeacherId());
        origin.setUpdateTime(new Date());
        Boolean uptRow = this.updateById(origin);
        if(uptRow){
            asCourseSessionService.transferTeacherSubjectSession(ao);

            // 记录日志
            baseExamRecordService.addBaseExamineRecord(origin.getId(), BaseExamRecordEnum.KCB_TRANSFER.getCode(),1,
                    "课程班移交成功");
            resResp = JsonResult.buildSuccess("教师课程移交成功");
        }
        return resResp;
    }
}
