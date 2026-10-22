package com.sxkj.edu.module.afterschool.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.domain.BaseEntity;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.afterschool.domain.ao.AsCourseClassTransferAO;
import com.sxkj.edu.module.afterschool.domain.ao.AsStudentScheduleQueryAO;
import com.sxkj.edu.module.afterschool.domain.ao.AsTeacherScheduleQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsStudentSessionPageQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsStudentTodayCoursePageQueryAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsStudentTodayCoursePageQueryDetailAO;
import com.sxkj.edu.module.afterschool.domain.dto.AsTeacherTodayCoursePageQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseClassEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseSessionEntity;
import com.sxkj.edu.module.afterschool.domain.entity.EduclTermEntity;
import com.sxkj.edu.module.afterschool.domain.vo.*;
import com.sxkj.edu.module.afterschool.enums.EnrollTypeEnum;
import com.sxkj.edu.module.afterschool.mapper.AsCourseClassMapper;
import com.sxkj.edu.module.afterschool.mapper.AsCourseSessionMapper;
import com.sxkj.edu.module.afterschool.support.StudentTermScope;
import com.sxkj.edu.module.afterschool.support.StudentTermScopeSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AsCourseSessionServiceImpl extends ServiceImpl<AsCourseSessionMapper, AsCourseSessionEntity> implements AsCourseSessionService{

    private static final ZoneId COURSE_ZONE_ID = ZoneId.of("Asia/Shanghai");
    private static final TimeZone COURSE_TIME_ZONE = TimeZone.getTimeZone(COURSE_ZONE_ID);

    @Resource
    private AsTeacherService asTeacherService;

    @Resource
    private AsCourseClassMapper asCourseClassMapper;

    @Resource
    private ExamRoomService examRoomService;

    @Resource
    private StudentTermScopeSupport studentTermScopeSupport;




    /**
     * Entity转VO
     */
    private AsCourseSessionVO convertToVO(AsCourseSessionEntity entity) {
        if (entity == null) {
            return null;
        }
        AsCourseSessionVO vo = new AsCourseSessionVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    /**
     * 查询学生课表（按周/月/天）
     *
     * @param ao 查询参数
     * @return 按周分组的课表数据
     */
    @Override
    public List<AsStudentScheduleWeekVO> queryStudentSchedule(AsStudentScheduleQueryAO ao) {
        EduclTermEntity term = ao.getTermId() == null
                ? studentTermScopeSupport.resolveReadableTerm(ao.getSchoolId())
                : studentTermScopeSupport.requireTermInSchool(ao.getSchoolId(), ao.getTermId());
        ao.setTermId(term.getId());
        studentTermScopeSupport.requireStudentInScope(ao.getStudentId(),
                new StudentTermScope(ao.getSchoolId(), term.getId(), null, null, null));
        // 计算日期范围
        String weekStartDate = null;
        String weekEndDate = null;
        String monthStartDate = null;
        String monthEndDate = null;
        String dayDate = null;

        try {
            LocalDate localDate = LocalDate.parse(ao.getQueryDate());

            if (ao.getQueryType() == 1) {
                // 周查询：获取该周的周一和周日
                LocalDate monday = localDate.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
                LocalDate sunday = localDate.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
                weekStartDate = monday.toString();
                weekEndDate = sunday.toString();
            } else if (ao.getQueryType() == 2) {
                // 月查询：获取该月的第一天和最后一天
                LocalDate firstDay = localDate.with(TemporalAdjusters.firstDayOfMonth());
                LocalDate lastDay = localDate.with(TemporalAdjusters.lastDayOfMonth());
                monthStartDate = firstDay.toString();
                monthEndDate = lastDay.toString();
            } else if (ao.getQueryType() == 3) {
                // 天查询：直接使用指定日期
                dayDate = ao.getQueryDate();
            }
        } catch (Exception e) {
            log.error("日期解析失败", e);
            return new ArrayList<>();
        }

        // 查询课表数据
        List<AsStudentScheduleItemVO> scheduleItems = baseMapper.queryStudentSchedule(
                ao, weekStartDate, weekEndDate, monthStartDate, monthEndDate, dayDate
        );

        if (scheduleItems == null || scheduleItems.isEmpty()) {
            return new ArrayList<>();
        }

        // 计算每个日期处于每月的第几周，并设置weekDayName
        Map<Integer, String> weekDayNameMap = CoreConstant.weekDayNameMap;

        for (AsStudentScheduleItemVO item : scheduleItems) {
            item.setTermId(term.getId());
            item.setTermName(term.getTermName());
            // 设置周几名称
            item.setWeekDayName(weekDayNameMap.getOrDefault(item.getWeekDay(), ""));
            item.setPlaceName(examRoomService.resolvePlaceName(item.getPlaceName()));

            // 计算处于每月的第几周
            if (item.getSessionDate() != null) {
                LocalDate sessionLocalDate = item.getSessionDate().toInstant().atZone(COURSE_ZONE_ID).toLocalDate();
                int dayOfMonth = sessionLocalDate.getDayOfMonth();
                // 计算是第几周（每7天为一周）
                int weekOfMonth = (dayOfMonth - 1) / 7 + 1;
                item.setWeekOfMonth(weekOfMonth);
            }
        }

        // 按周分组
        Map<Integer, List<AsStudentScheduleItemVO>> weekGroupMap = scheduleItems.stream()
                .collect(Collectors.groupingBy(AsStudentScheduleItemVO::getWeekOfMonth));

        // 构建返回结果
        List<AsStudentScheduleWeekVO> result = new ArrayList<>();
        for (Map.Entry<Integer, List<AsStudentScheduleItemVO>> entry : weekGroupMap.entrySet()) {
            AsStudentScheduleWeekVO weekVO = new AsStudentScheduleWeekVO();
            weekVO.setTermId(term.getId());
            weekVO.setTermName(term.getTermName());
            weekVO.setWeekOfMonth(entry.getKey());
            weekVO.setScheduleItems(entry.getValue());
            result.add(weekVO);
        }

        // 按周数升序排序
        result.sort(Comparator.comparing(AsStudentScheduleWeekVO::getWeekOfMonth));

        return result;
    }

    /**
     * 查询教师课表（按周/月/天）
     *
     * @param ao 查询参数
     * @return 按周分组的课表数据
     */
    @Override
    public List<AsStudentScheduleWeekVO> queryTeacherSchedule(AsTeacherScheduleQueryAO ao) {
        // 计算日期范围
        String weekStartDate = null;
        String weekEndDate = null;
        String monthStartDate = null;
        String monthEndDate = null;
        String dayDate = null;

        try {
            LocalDate localDate = LocalDate.parse(ao.getQueryDate());

            if (ao.getQueryType() == 1) {
                // 周查询：获取该周的周一和周日
                LocalDate monday = localDate.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
                LocalDate sunday = localDate.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
                weekStartDate = monday.toString();
                weekEndDate = sunday.toString();
            } else if (ao.getQueryType() == 2) {
                // 月查询：获取该月的第一天和最后一天
                LocalDate firstDay = localDate.with(TemporalAdjusters.firstDayOfMonth());
                LocalDate lastDay = localDate.with(TemporalAdjusters.lastDayOfMonth());
                monthStartDate = firstDay.toString();
                monthEndDate = lastDay.toString();
            } else if (ao.getQueryType() == 3) {
                // 天查询：直接使用指定日期
                dayDate = ao.getQueryDate();
            }
        } catch (Exception e) {
            log.error("日期解析失败", e);
            return new ArrayList<>();
        }

        // 查询课表数据
        List<AsStudentScheduleItemVO> scheduleItems = baseMapper.queryTeacherSchedule(
                ao, weekStartDate, weekEndDate, monthStartDate, monthEndDate, dayDate
        );

        if (scheduleItems == null || scheduleItems.isEmpty()) {
            return new ArrayList<>();
        }

        // 计算每个日期处于每月的第几周，并设置weekDayName
        Map<Integer, String> weekDayNameMap = new HashMap<>();
        weekDayNameMap.put(1, "周一");
        weekDayNameMap.put(2, "周二");
        weekDayNameMap.put(3, "周三");
        weekDayNameMap.put(4, "周四");
        weekDayNameMap.put(5, "周五");
        weekDayNameMap.put(6, "周六");
        weekDayNameMap.put(7, "周日");

        for (AsStudentScheduleItemVO item : scheduleItems) {
            // 设置周几名称
            item.setWeekDayName(weekDayNameMap.getOrDefault(item.getWeekDay(), ""));
            item.setPlaceName(examRoomService.resolvePlaceName(item.getPlaceName()));

            // 计算处于每月的第几周
            if (item.getSessionDate() != null) {
                LocalDate sessionLocalDate = item.getSessionDate().toInstant().atZone(COURSE_ZONE_ID).toLocalDate();
                int dayOfMonth = sessionLocalDate.getDayOfMonth();
                // 计算是第几周（每7天为一周）
                int weekOfMonth = (dayOfMonth - 1) / 7 + 1;
                item.setWeekOfMonth(weekOfMonth);
            }
        }

        // 按周分组
        Map<Integer, List<AsStudentScheduleItemVO>> weekGroupMap = scheduleItems.stream()
                .collect(Collectors.groupingBy(AsStudentScheduleItemVO::getWeekOfMonth));

        // 构建返回结果
        List<AsStudentScheduleWeekVO> result = new ArrayList<>();
        for (Map.Entry<Integer, List<AsStudentScheduleItemVO>> entry : weekGroupMap.entrySet()) {
            AsStudentScheduleWeekVO weekVO = new AsStudentScheduleWeekVO();
            weekVO.setWeekOfMonth(entry.getKey());
            weekVO.setScheduleItems(entry.getValue());
            result.add(weekVO);
        }

        // 按周数升序排序
        result.sort(Comparator.comparing(AsStudentScheduleWeekVO::getWeekOfMonth));

        return result;
    }

    /**
     * 分页查询教师今日课程
     *
     * @param ao 查询参数
     * @param userTeacherId 教师ID
     * @return 分页结果
     */
    @Override
    public PageResultDTO<AsTeacherTodayCourseVO> pageTeacherTodayCourse(AsTeacherTodayCoursePageQueryAO ao, Long userTeacherId) {
        EduclTermEntity term = ao.getTermId() == null
                ? studentTermScopeSupport.resolveReadableTerm(ao.getSchoolId())
                : studentTermScopeSupport.requireTermInSchool(ao.getSchoolId(), ao.getTermId());
        ao.setTermId(term.getId());
        // 根据教师用户id查教师id
        Long teacherId = asTeacherService.queryTeacherIdByPhone(null, ao.getSchoolId());
        // 获取今日日期
        String today = currentCourseDate();

        // 分页查询
        Page<Object> page = new Page<>(ao.getPageIndex(), ao.getPageSize());
        Page<AsTeacherTodayCourseVO> resultPage = baseMapper.pageTeacherTodayCourse(page, ao, teacherId, today);

        // 计算课堂状态
        List<AsTeacherTodayCourseVO> voList = resultPage.getRecords();
        if (voList != null && !voList.isEmpty()) {
            Date now = new Date();
            for (AsTeacherTodayCourseVO vo : voList) {
                vo.setTermId(term.getId());
                vo.setTermName(term.getTermName());
                // 根据上课开始结束时间自动计算课堂状态
                Integer classStatus = calculateClassStatus(now, vo.getSessionDate(), vo.getStartTime(), vo.getEndTime());
                vo.setClassStatus(classStatus);
                vo.setClassStatusDesc(getClassStatusDesc(classStatus));
                // 解析上课地点名称
                vo.setPlaceName(examRoomService.resolvePlaceName(vo.getPlaceName()));
            }
        }

        // 构建返回结果
        PageResultDTO<AsTeacherTodayCourseVO> result = new PageResultDTO<>();
        result.setTotal(resultPage.getTotal());
        result.setPages(resultPage.getPages());
        result.setPageIndex(resultPage.getCurrent());
        result.setPageSize(resultPage.getSize());
        result.setList(voList);

        return result;
    }

    /**
     * 根据当前时间和上课开始结束时间计算课堂状态
     *
     * @param now 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 0未开始 1进行中 2已结束
     */
    private Integer calculateClassStatus(Date now, Date sessionDate, Date startTime, Date endTime) {
        if (sessionDate == null || startTime == null || endTime == null) {
            return 0; // 默认未开始
        }

        Date classStartTime = combineDateAndTime(sessionDate, startTime);
        Date classEndTime = combineDateAndTime(sessionDate, endTime);
        if (classStartTime == null || classEndTime == null) {
            return 0;
        }
        if (classEndTime.before(classStartTime)) {
            Calendar calendar = Calendar.getInstance(COURSE_TIME_ZONE);
            calendar.setTime(classEndTime);
            calendar.add(Calendar.DATE, 1);
            classEndTime = calendar.getTime();
        }

        if (now.before(classStartTime)) {
            return 0; // 未开始
        } else if (now.after(classEndTime)) {
            return 2; // 已结束
        } else {
            return 1; // 进行中
        }
    }

    private Date combineDateAndTime(Date date, Date time) {
        if (date == null || time == null) {
            return null;
        }
        Calendar dateCalendar = Calendar.getInstance(COURSE_TIME_ZONE);
        dateCalendar.setTime(date);
        Calendar timeCalendar = Calendar.getInstance(COURSE_TIME_ZONE);
        timeCalendar.setTime(time);
        Calendar result = Calendar.getInstance(COURSE_TIME_ZONE);
        result.clear();
        result.set(Calendar.YEAR, dateCalendar.get(Calendar.YEAR));
        result.set(Calendar.MONTH, dateCalendar.get(Calendar.MONTH));
        result.set(Calendar.DAY_OF_MONTH, dateCalendar.get(Calendar.DAY_OF_MONTH));
        result.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
        result.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
        result.set(Calendar.SECOND, timeCalendar.get(Calendar.SECOND));
        result.set(Calendar.MILLISECOND, timeCalendar.get(Calendar.MILLISECOND));
        return result.getTime();
    }

    private String currentCourseDate() {
        return LocalDate.now(COURSE_ZONE_ID).toString();
    }

    /**
     * 获取课堂状态描述
     *
     * @param classStatus 课堂状态
     * @return 状态描述
     */
    private String getClassStatusDesc(Integer classStatus) {
        if (classStatus == null) {
            return "未知";
        }
        switch (classStatus) {
            case 0:
                return "未开始";
            case 1:
                return "进行中";
            case 2:
                return "已结束";
            default:
                return "未知";
        }
    }

    /**
     * 查询教师今日课程详情
     *
     * @param sessionId 课次ID
     * @param userTeacherId 教师用户ID
     * @return 课程详情
     */
    @Override
    public AsTeacherTodayCourseDetailVO getTeacherTodayCourseDetail(Long sessionId, Long userTeacherId) {
        StudentTermScope sessionScope = studentTermScopeSupport.requireSessionScope(sessionId);
        EduclTermEntity term = studentTermScopeSupport.requireTermInSchool(
                sessionScope.getSchoolId(), sessionScope.getTermId());
        // 根据教师用户id查教师id
        Long teacherId = asTeacherService.queryTeacherIdByPhone(null,null);
        // 查询课程详情
        AsTeacherTodayCourseDetailVO detail = baseMapper.getTeacherTodayCourseDetail(sessionId, teacherId);
        
        if (detail == null) {
            return null;
        }
        detail.setTermId(term.getId());
        detail.setTermName(term.getTermName());

        // 计算课堂状态
        Date now = new Date();
        Integer classStatus = calculateClassStatus(now, detail.getSessionDate(), detail.getStartTime(), detail.getEndTime());
        detail.setClassStatus(classStatus);
        detail.setClassStatusDesc(getClassStatusDesc(classStatus));
        // 解析上课地点名称
        detail.setPlaceName(examRoomService.resolvePlaceName(detail.getPlaceName()));
        // 设置报名方式名称
        EnrollTypeEnum enrollTypeEnum = EnrollTypeEnum.getByCode(detail.getEnrollType());
        detail.setEnrollTypeName(enrollTypeEnum != null ? enrollTypeEnum.getDesc() : null);

        return detail;
    }

    /**
     * 分页查询学生今日课程
     *
     * @param ao 查询参数
     * @param studentId 学生ID
     * @return 分页结果
     */
    @Override
    public PageResultDTO<AsStudentTodayCourseVO> pageStudentTodayCourse(AsStudentTodayCoursePageQueryAO ao, Long studentId) {
        EduclTermEntity term = ao.getTermId() == null
                ? studentTermScopeSupport.resolveReadableTerm(ao.getSchoolId())
                : studentTermScopeSupport.requireTermInSchool(ao.getSchoolId(), ao.getTermId());
        ao.setTermId(term.getId());
        studentTermScopeSupport.requireStudentInScope(studentId,
                new StudentTermScope(ao.getSchoolId(), term.getId(), null, null, null));
        // 获取今日日期
        String today = currentCourseDate();
        // 分页查询
        Page<Object> page = new Page<>(ao.getPageIndex(), ao.getPageSize());
        // 分页查询
        Page<AsStudentTodayCourseVO> resultPage = baseMapper.pageStudentTodayCourse(page, ao, studentId, today);

        // 计算课堂状态
        List<AsStudentTodayCourseVO> voList = resultPage.getRecords();
        if (voList != null && !voList.isEmpty()) {
            Date now = new Date();
            for (AsStudentTodayCourseVO vo : voList) {
                vo.setTermId(term.getId());
                vo.setTermName(term.getTermName());
                // 根据上课开始结束时间自动计算课堂状态
                Integer classStatus = calculateClassStatus(now, vo.getSessionDate(), vo.getStartTime(), vo.getEndTime());
                vo.setClassStatus(classStatus);
                vo.setClassStatusDesc(getClassStatusDesc(classStatus));
                // 解析上课地点名称（DB 存的是教室 ID，需通过 resolvePlaceName 转为可读名称）
                vo.setPlaceName(examRoomService.resolvePlaceName(vo.getPlaceName()));
            }
        }

        // 构建返回结果
        PageResultDTO<AsStudentTodayCourseVO> result = new PageResultDTO<>();
        result.setTotal(resultPage.getTotal());
        result.setPages(resultPage.getPages());
        result.setPageIndex(resultPage.getCurrent());
        result.setPageSize(resultPage.getSize());
        result.setList(voList);

        return result;
    }


    /**
     * 分页查询学生课次（根据请假时间）
     *
     * @param ao 查询参数
     * @param studentId 学生ID
     * @return 分页结果
     */
    @Override
    public PageResultDTO<AsStudentSessionVO> pageStudentSessions(AsStudentSessionPageQueryAO ao, Long studentId) {
        EduclTermEntity term = ao.getTermId() == null
                ? studentTermScopeSupport.resolveReadableTerm(ao.getSchoolId())
                : studentTermScopeSupport.requireTermInSchool(ao.getSchoolId(), ao.getTermId());
        ao.setTermId(term.getId());
        studentTermScopeSupport.requireStudentInScope(studentId,
                new StudentTermScope(ao.getSchoolId(), term.getId(), null, null, null));
        // 分页查询
        Page<Object> page = new Page<>(ao.getPageIndex(), ao.getPageSize());
        Page<AsStudentSessionVO> resultPage = baseMapper.pageStudentSessions(page, ao, studentId);

        // 构建返回结果
        PageResultDTO<AsStudentSessionVO> result = new PageResultDTO<>();
        result.setTotal(resultPage.getTotal());
        result.setPages(resultPage.getPages());
        result.setPageIndex(resultPage.getCurrent());
        result.setPageSize(resultPage.getSize());
        resultPage.getRecords().forEach(item -> {
            item.setTermId(term.getId());
            item.setTermName(term.getTermName());
        });
        result.setList(resultPage.getRecords());

        return result;
    }


    @Override
    public Boolean transferTeacherSubjectSession(AsCourseClassTransferAO ao) {
        LambdaUpdateWrapper<AsCourseSessionEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AsCourseSessionEntity::getSchoolId,ao.getSchoolId());
        updateWrapper.eq(AsCourseSessionEntity::getCourseClassId,ao.getCourseClassId());
        updateWrapper.eq(AsCourseSessionEntity::getIsValid,1);
        updateWrapper.set(AsCourseSessionEntity::getTeacherId,ao.getTeacherId());
        Boolean uptRow = this.update(updateWrapper);
        log.info("call transferTeacherSubjectSession in request:{},exec result:{} ",ao,uptRow);
        return uptRow;
    }
}
