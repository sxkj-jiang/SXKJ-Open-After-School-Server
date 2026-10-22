package com.sxkj.edu.module.afterschool.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.common.base.domain.BaseEntity;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.utils.ExcelUtils;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.sys.entity.mult.TEduclClass;
import com.sxkj.edu.domain.vo.FileVO;
import com.sxkj.edu.module.afterschool.domain.entity.*;
import com.sxkj.edu.module.afterschool.domain.excel.*;
import com.sxkj.edu.module.afterschool.domain.vo.AsLeaveRequestQueryStatusApplyVO;
import com.sxkj.edu.module.afterschool.domain.vo.EduclTermVO;
import com.sxkj.edu.module.afterschool.mapper.*;
import com.sxkj.edu.module.afterschool.service.AsStudentIdentityService;
import com.sxkj.edu.module.afterschool.service.AsStudentService;
import com.sxkj.edu.module.afterschool.service.EduclTermService;
import com.sxkj.edu.module.system.employee.domain.bo.RequestTokenBO;
import com.sxkj.edu.module.afterschool.domain.statistics.ao.*;
import com.sxkj.edu.module.afterschool.domain.statistics.vo.*;
import com.sxkj.edu.module.afterschool.service.StatisticsService;
import com.sxkj.edu.module.afterschool.support.StudentTermScope;
import com.sxkj.edu.module.afterschool.support.StudentTermScopeSupport;
import com.sxkj.edu.service.BaseFileService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Autowired
    private AsTeacherMapper asTeacherMapper;

    @Autowired
    private EduclTermMapper educlTermMapper;

    @Autowired
    private EduclGradeMapper educlGradeMapper;

    @Autowired
    private BaseFileService baseFileService;

    @Autowired
    private com.sxkj.edu.module.afterschool.mapper.AsCourseScheduleRuleMapper asCourseScheduleRuleMapper;

    @Autowired
    private StudentTermScopeSupport studentTermScopeSupport;
    @Autowired
    private AsStudentService asStudentService;

    @Autowired
    private EduclClassMapper educlClassMapper;
    @Autowired
    private EduclTermService educlTermService;

    @Override
    public PageResultDTO<LessonStatisticsTeacherPageVO> pageTeacherLessonStatistics(LessonStatisticsTeacherAO ao) {
        // 获取当前用户所属学校ID
        if(ObjectUtil.isNull(ao.getSchoolId()))ao.setSchoolId(SmartRequestTokenUtil.getRequestUserStuSchoolId());
        // 执行分页查询
        Page<LessonStatisticsTeacherPageVO> page = statisticsMapper.pageTeacherLessonStatistics(new Page<>(ao.getPageIndex(), ao.getPageSize()), ao);
        // 封装返回结果
        PageResultDTO<LessonStatisticsTeacherPageVO> result = new PageResultDTO<>();
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setPageIndex(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setList(page.getRecords());
        return result;
    }

    @Override
    public void exportTeacherLessonStatistics(LessonStatisticsTeacherAO ao, HttpServletResponse response) {
        // 获取当前用户所属学校ID
        if (ObjectUtil.isNull(ao.getSchoolId())) ao.setSchoolId(SmartRequestTokenUtil.getRequestUserStuSchoolId());
        PageResultDTO<LessonStatisticsTeacherPageVO> page = this.pageTeacherLessonStatistics(ao);
        List<LessonStatisticsTeacherPageVO> records = page.getList();
        if (CollectionUtil.isEmpty(records)) {
            return;
        }
        // 转换为导出模型
        List<LessonStatisticsTeacherExportExcel> excelList = new ArrayList<>();
        int index = 1;
        for (LessonStatisticsTeacherPageVO vo : records) {
            LessonStatisticsTeacherExportExcel excel = new LessonStatisticsTeacherExportExcel();
            excel.setSequence(String.valueOf(index++));
            excel.setTeacherNo(vo.getTeacherNo());
            excel.setTeacherName(vo.getTeacherName());
            excel.setTermName(vo.getTermName());
            excel.setTotalLessonHours(vo.getTotalLessonHours());
            excel.setCompletedLessonHours(vo.getCompletedLessonHours());
            excel.setAdjustedSessions(vo.getAdjustedSessions());
            excel.setSubstituteSessions(vo.getSubstituteSessions());
            excelList.add(excel);
        }
        try {
            ExcelUtils.exportExcel(response, excelList, LessonStatisticsTeacherExportExcel.class, "教师课时统计导出", "教师课时统计");
        } catch (Exception e) {
            log.error("导出教师课时统计异常", e);
        }
    }

    @Override
    public PageResultDTO<LessonStatisticsStudentPageVO> pageStudentLessonStatistics(LessonStatisticsStudentAO ao) {
        // 获取当前用户所属学校ID
        if(ObjectUtil.isNull(ao.getSchoolId()))ao.setSchoolId(SmartRequestTokenUtil.getRequestUserStuSchoolId());
        EduclTermEntity term = resolveStatisticsTerm(ao.getSchoolId(), ao.getTermId());
        ao.setTermId(term.getId());

        Page<LessonStatisticsStudentPageVO> page = new Page<>(ao.getPageIndex(), ao.getPageSize());
        statisticsMapper.pageStudentLessonNewStatistics(page, ao);

        List<LessonStatisticsStudentPageVO> records = page.getRecords();
        // 批量查当页学生详情
        if (CollectionUtil.isNotEmpty(records)) {
            List<Long> studentIds = records.stream()
                    .map(LessonStatisticsStudentPageVO::getStudentId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(studentIds)) {
                List<Map<String, Object>> details = statisticsMapper.batchStudentLessonStatisticsDetail(studentIds);
                Map<Long, Map<String, Object>> detailMap = new HashMap<>(details.size());
                for (Map<String, Object> row : details) {
                    detailMap.put(((Number) row.get("studentId")).longValue(), row);
                }
                // 组装数据
                for (LessonStatisticsStudentPageVO vo : records) {
                    Map<String, Object> detail = detailMap.get(vo.getStudentId());
                    if (detail != null) {
                        vo.setCourseClassNames(detail.get("courseClassNames") != null ? detail.get("courseClassNames").toString() : "");
                        vo.setCourseCategoryNames(detail.get("categoryNames") != null ? detail.get("categoryNames").toString() : "");
                        vo.setTotalLessonHours(detail.get("totalLessonHours") != null ? ((Number) detail.get("totalLessonHours")).intValue() : 0);
                        vo.setAttendanceHours(detail.get("attendanceHours") != null ? ((Number) detail.get("attendanceHours")).intValue() : 0);
                        vo.setAbsenceHours(detail.get("absenceHours") != null ? ((Number) detail.get("absenceHours")).intValue() : 0);
                    }
                    vo.setAttendanceRate(calcAttendanceRate(vo.getAttendanceHours(), vo.getAbsenceHours()) + "%");
                }
                if (ao.getMinAbsenceHours() != null) {
                    records = records.stream()
                            .filter(vo -> vo.getAbsenceHours() != null && vo.getAbsenceHours() >= ao.getMinAbsenceHours())
                            .collect(Collectors.toList());
                }
            }
        }
        // 封装返回结果
        PageResultDTO<LessonStatisticsStudentPageVO> result = new PageResultDTO<>();
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setPageIndex(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setList(records);
        return result;
    }


    @Override
    public LessonStatisticsTeacherDetailVO getTeacherLessonDetail(LessonStatisticsTeacherDetailAO ao) {
        LessonStatisticsTeacherDetailVO vo = new LessonStatisticsTeacherDetailVO();

        // 1. 查询教师信息
        AsTeacherEntity teacher = asTeacherMapper.selectById(ao.getTeacherId());
        if (teacher == null) {
            log.warn("教师不存在, teacherId={}", ao.getTeacherId());
            return vo;
        }
        vo.setTeacherName(teacher.getTeacherName());
        vo.setTeacherNo(teacher.getTeacherNo());
        vo.setGenderName(teacher.getGender() != null && teacher.getGender() == 1 ? "女" : "男");
        vo.setRegistYear(teacher.getRegistYear());
        vo.setEmployeeStatus(teacher.getEmployeeStatus());

        // 2. 查询教师头像
        if (teacher.getAvatarFileId() != null && teacher.getAvatarFileId() != 0L) {
            FileVO avatar = baseFileService.getFileById(teacher.getAvatarFileId());
            vo.setAvatar(avatar);
        }

        // 3. 查询学期信息
        EduclTermEntity term = educlTermMapper.selectById(ao.getTermId());
        if (term == null) {
            log.warn("学期不存在, termId={}", ao.getTermId());
            return vo;
        }
        vo.setTermName(term.getTermName());

        // 4. 查询总课时数
        Map<String, Object> teacherHourMap = statisticsMapper.countTeacherLessonHours(ao.getTeacherId(), ao.getTermId(),ao.getSchoolId());
        if(null != teacherHourMap){
            int totalLessonHours = teacherHourMap.get("totalLessonHours") != null ? ((Number) teacherHourMap.get("totalLessonHours")).intValue() : 0;
            int completedLessonHours = teacherHourMap.get("completedLessonHours") != null ? ((Number) teacherHourMap.get("completedLessonHours")).intValue() : 0;
            int adjustedSessions = teacherHourMap.get("adjustedSessions") != null ? ((Number) teacherHourMap.get("adjustedSessions")).intValue() : 0;
            int substituteSessions = teacherHourMap.get("substituteSessions") != null ? ((Number) teacherHourMap.get("substituteSessions")).intValue() : 0;
            vo.setTotalLessonHours(totalLessonHours);
            vo.setCompletedLessonHours(completedLessonHours);
            vo.setAdjustedSessions(adjustedSessions);
            vo.setSubstituteSessions(substituteSessions);
        }

        // 7. 课时趋势（根据学期起止月份填充补零）
        List<TeacherLessonTrendItemVO> trendFromDb = statisticsMapper.listTeacherMonthlyLessonTrend(ao.getTeacherId(), ao.getTermId());
        vo.setLessonTrend(buildFullMonthTrend(trendFromDb, term.getStartDate(), term.getEndDate()));

        // 8. 课时分布
        List<StatisticsCommonCountVO> countVOS = statisticsMapper.listTeacherLessonKlxData(ao.getSchoolId(), ao.getTermId(), ao.getTeacherId());
        vo.setLessonDistribution(countVOS);

        return vo;
    }

    @Override
    public PageResultDTO<TeacherLessonRecordPageVO> pageTeacherLessonRecord(TeacherLessonRecordAO ao) {
        Page<TeacherLessonRecordPageVO> page = statisticsMapper.pageTeacherLessonRecord(new Page<>(ao.getPageIndex(), ao.getPageSize()), ao);
        List<TeacherLessonRecordPageVO> records = page.getRecords();
        if(CollectionUtil.isNotEmpty(records)){
            LambdaQueryWrapper<EduclGradeEntity> gradeQuery = new LambdaQueryWrapper<>();
            gradeQuery.eq(BaseEntity::getIsValid, 1).eq(EduclGradeEntity::getSchoolId, ao.getSchoolId());
            List<EduclGradeEntity> gradeList = educlGradeMapper.selectList(gradeQuery);

            // 批量查询所有课次的学生出勤统计
            List<Long> sessionIds = records.stream().map(TeacherLessonRecordPageVO::getSessionId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            Map<Long, String> rateCache = new HashMap<>();
            if (CollectionUtil.isNotEmpty(sessionIds)) {
                List<Map<String, Object>> statsList = statisticsMapper.batchSessionAttendanceStats(sessionIds);
                for (Map<String, Object> row : statsList) {
                    Long sid = ((Number) row.get("sessionId")).longValue();
                    int total = row.get("totalCount") != null ? ((Number) row.get("totalCount")).intValue() : 0;
                    int attended = row.get("attendedCount") != null ? ((Number) row.get("attendedCount")).intValue() : 0;
                    rateCache.put(sid, calcLessonAttendanceRate(attended, total)+"%");
                }
            }

            for (TeacherLessonRecordPageVO vo : records) {
                // 解析applicableGradeIds -> 年级名称分号连接
                if (StringUtils.isNotBlank(vo.getApplicableGradeIds())) {
                    List<Long> ids = JSON.parseArray(vo.getApplicableGradeIds(), Long.class);
                    if (CollectionUtil.isNotEmpty(ids) && CollectionUtil.isNotEmpty(gradeList)) {
                        vo.setGradeNames(gradeList.stream().filter(g -> ids.contains(g.getId()))
                                .map(EduclGradeEntity::getGradeName)
                                .collect(Collectors.joining(";")));
                    }
                }else {
                    //根据行政班id反推年级
                    if (StringUtils.isNotBlank(vo.getApplicableClassIds())) {
                        List<Long> ids = JSON.parseArray(vo.getApplicableClassIds(), Long.class);
                        LambdaQueryWrapper<TEduclClass> classQuery = new LambdaQueryWrapper<>();
                        classQuery.eq(TEduclClass::getIsValid, 1).eq(TEduclClass::getSchoolId, ao.getSchoolId())
                                .in(TEduclClass::getId, ids);
                        List<TEduclClass> classList = educlClassMapper.selectList(classQuery);
                        if(CollectionUtil.isNotEmpty(classList)){
                            List<Long> gradeIds = classList.stream().map(TEduclClass::getGradeId).filter(Objects::nonNull)
                                    .distinct().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
                            vo.setGradeNames(gradeList.stream().filter(g -> gradeIds.contains(g.getId()))
                                    .map(EduclGradeEntity::getGradeName)
                                    .collect(Collectors.joining(";")));
                        }
                    }
                }
                // 设置学生出勤率
                vo.setAttendanceRate(vo.getSessionId() != null ? rateCache.getOrDefault(vo.getSessionId(), "0%") : "0%");
            }
        }

        PageResultDTO<TeacherLessonRecordPageVO> result = new PageResultDTO<>();
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setPageIndex(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setList(records);
        return result;
    }

    @Override
    public LessonStatisticsStudentDetailVO getStudentLessonDetail(LessonStatisticsStudentDetailAO ao) {
        LessonStatisticsStudentDetailVO vo = new LessonStatisticsStudentDetailVO();
        if(ObjectUtil.isNull(ao.getSchoolId())) ao.setSchoolId(SmartRequestTokenUtil.getRequestUserStuSchoolId());
        // 获取学生id
        Long studentId = asStudentService.getStuIdByTermAndIdentifyId(ao.getTermId(), ao.getSchoolId(), ao.getStudentIdentityId());
        vo.setTermList(this.getStuTermData(ao.getStudentIdentityId(), ao.getSchoolId()));

        EduclTermEntity term = resolveStatisticsTerm(ao.getSchoolId(), ao.getTermId());
        Long termId = term.getId();
        ao.setTermId(termId);
        // 查询学生基本信息
        StudentDetailSummaryVO detail = statisticsMapper.getStudentDetailSummary(studentId, termId);
        if (detail == null || detail.getStudentNo() == null) {
            log.warn("学生不存在, studentId={}", studentId);
            return vo;
        }
        SmartBeanUtil.copyProperties(detail, vo);
        vo.setGenderName(detail.getGender() != null && detail.getGender() == 1 ? "女" : "男");
        // 学生头像
        if (ObjectUtil.isNotNull(detail.getAvatarFileId())) {
            vo.setAvatar(baseFileService.getFileById(detail.getAvatarFileId()));
        }
        Date termStartDate = detail.getTermStartDate();
        Date termEndDate = detail.getTermEndDate();
        // 课时出勤率计算
        vo.setAttendanceRate(calcAttendanceRate(detail.getAttendanceHours(), detail.getAbsenceHours()) + "%");

        // 学生月度出勤趋势
        List<StudentMonthlyAttendanceVO> monthlyData = statisticsMapper.listStudentMonthlyAttendanceTrend(ao);
        // 构建月份 -> 出勤率 Map（按时间顺序）
        Map<String, BigDecimal> monthRateMap = new HashMap<>();
        if (monthlyData != null) {
            for (StudentMonthlyAttendanceVO row : monthlyData) {
                BigDecimal rate = calcAttendanceRate(row.getAttendanceHours(), row.getAbsenceHours());
                monthRateMap.put(row.getMonth(), rate );
            }
        }

        //计算当前月与上月出勤率对比
        Calendar now = Calendar.getInstance();
        String currentMonthLabel = now.get(Calendar.YEAR) + "年" + String.format("%02d", now.get(Calendar.MONTH) + 1) + "月";
        Calendar prevCal = (Calendar) now.clone();
        prevCal.add(Calendar.MONTH, -1);
        String prevMonthLabel = prevCal.get(Calendar.YEAR) + "年" + String.format("%02d", prevCal.get(Calendar.MONTH) + 1) + "月";

        BigDecimal currentRate = monthRateMap.getOrDefault(currentMonthLabel, BigDecimal.ZERO);
        BigDecimal prevRate = monthRateMap.getOrDefault(prevMonthLabel, BigDecimal.ZERO);
        BigDecimal diff = currentRate.subtract(prevRate);

        if (diff.compareTo(BigDecimal.ZERO) > 0) {
            vo.setTrend("up");
        } else if (diff.compareTo(BigDecimal.ZERO) < 0) {
            vo.setTrend("down");
        } else {
            vo.setTrend("flat");
        }
        vo.setTrendRate(diff.abs().setScale(1, RoundingMode.HALF_UP) + "%");

        // 出勤趋势
        List<StudentAttendanceTrendItemVO> trendList = buildFullMonthAttendanceTrend(monthRateMap, termStartDate, termEndDate);
        vo.setAttendanceTrend(trendList);

        //课时分布
        List<StudentLessonDistributionItemVO> distributionList = statisticsMapper.listStudentLessonDistribution(ao);
        vo.setLessonDistribution(distributionList != null ? distributionList : Collections.emptyList());
        return vo;
    }

    private List<EduclTermVO> getStuTermData(Long studentIdentityId,Long schoolId) {
        List<EduclTermVO> termList = new ArrayList<>();
        //根据身份唯一标识查询学生的学期数据
        LambdaQueryWrapper<AsStudentEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AsStudentEntity::getIsValid, 1)
                .eq(AsStudentEntity::getStudentIdentityId, studentIdentityId)
                .eq(AsStudentEntity::getSchoolId, schoolId);
        List<AsStudentEntity> list = asStudentService.list(queryWrapper);
        if(CollectionUtil.isNotEmpty(list)){
            List<Long> termIds = list.stream().map(AsStudentEntity::getTermId).distinct().collect(Collectors.toList());
            LambdaQueryWrapper<EduclTermEntity> qw = new LambdaQueryWrapper<>();
            qw.eq(EduclTermEntity::getIsValid, 1).in(BaseEntity::getId,termIds)
                    .eq(EduclTermEntity::getSchoolId, schoolId);
            List<EduclTermEntity> termEntities = educlTermMapper.selectList(qw);
            if(CollectionUtil.isNotEmpty(termEntities)){
                termList = termEntities.stream().map(term -> {
                    EduclTermVO vo = new EduclTermVO();
                    SmartBeanUtil.copyProperties(term, vo);
                    return vo;
                }).collect(Collectors.toList());
            }
        }
        return termList;
    }

    @Override
    public PageResultDTO<StudentLessonRecordPageVO> pageStudentLessonRecord(StudentLessonRecordAO ao) {
        if (ObjectUtil.isNull(ao.getSchoolId())) {
            ao.setSchoolId(SmartRequestTokenUtil.getRequestUserStuSchoolId());
        }
        // 获取学生id
        Long studentId = asStudentService.getStuIdByTermAndIdentifyId(ao.getTermId(), ao.getSchoolId(), ao.getStudentIdentityId());
        ao.setStudentId(studentId);
        EduclTermEntity term = resolveStatisticsTerm(ao.getSchoolId(), ao.getTermId());
        ao.setTermId(term.getId());
        Page<StudentLessonRecordPageVO> page = statisticsMapper.pageStudentLessonRecord(new Page<>(ao.getPageIndex(), ao.getPageSize()), ao);
        if(CollectionUtil.isNotEmpty(page.getRecords())){
            page.getRecords().forEach(record -> {
            });
        }
        PageResultDTO<StudentLessonRecordPageVO> result = new PageResultDTO<>();
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setPageIndex(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setList(page.getRecords());
        return result;
    }


    /**
     * 根据学期起止时间生成完整月份出勤趋势列表，未匹配的月份出勤率为0%
     */
    private List<StudentAttendanceTrendItemVO> buildFullMonthAttendanceTrend(Map<String, BigDecimal> trendData, Date startDate, Date endDate) {
        List<StudentAttendanceTrendItemVO> fullTrend = new ArrayList<>();
        if (startDate == null || endDate == null) {
            return fullTrend;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        endCal.set(Calendar.DAY_OF_MONTH, 1);
        while (!cal.after(endCal)) {
            String monthLabel = cal.get(Calendar.YEAR) + "年" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "月";
            BigDecimal rate = trendData != null ? trendData.get(monthLabel) : BigDecimal.ZERO;
            if (rate != null && rate.compareTo(BigDecimal.ZERO) > 0) {
                fullTrend.add(new StudentAttendanceTrendItemVO(monthLabel, rate + "%"));
            } else {
                fullTrend.add(new StudentAttendanceTrendItemVO(monthLabel, "0%"));
            }
            cal.add(Calendar.MONTH, 1);
        }
        return fullTrend;
    }

    /**
     * 根据学期起止时间生成完整月份列表，对SQL查询结果补零
     */
    private List<TeacherLessonTrendItemVO> buildFullMonthTrend(List<TeacherLessonTrendItemVO> trendFromDb, Date startDate, Date endDate) {
        // SQL查询结果转为Map：月份 -> 课时数
        Map<String, Integer> monthCountMap = new LinkedHashMap<>();
        if (trendFromDb != null) {
            for (TeacherLessonTrendItemVO item : trendFromDb) {
                monthCountMap.put(item.getMonth(), item.getLessonCount());
            }
        }
        // 如果学期起止时间为空，直接返回查询结果
        if (startDate == null || endDate == null) {
            return trendFromDb != null ? trendFromDb : Collections.emptyList();
        }
        // 遍历起止时间之间的所有月份，填充结果
        List<TeacherLessonTrendItemVO> fullTrend = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        endCal.set(Calendar.DAY_OF_MONTH, 1);
        while (!cal.after(endCal)) {
            String monthLabel = cal.get(Calendar.YEAR) + "年" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "月";
            int count = monthCountMap.getOrDefault(monthLabel, 0);
            fullTrend.add(new TeacherLessonTrendItemVO(monthLabel, count));
            cal.add(Calendar.MONTH, 1);
        }
        return fullTrend;
    }

    /**
     * 计算课时出勤率
     * 出勤率 = 出勤课时数 / 课时数 * 100%
     */
    private BigDecimal calcLessonAttendanceRate(Integer aHours, Integer bHours) {
        if (aHours == null) aHours = 0;
        if (bHours == null || bHours == 0) return BigDecimal.ZERO;
        BigDecimal rate = new BigDecimal(aHours)
                .divide(new BigDecimal(bHours), 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100))
                .setScale(1, RoundingMode.HALF_UP);
        return rate ;
    }

    /**
     * 计算出勤率
     * 出勤率 = 出勤课时数 / (出勤课时数 + 缺勤/请假课时数) * 100%
     */
    private BigDecimal calcAttendanceRate(Integer attendanceHours, Integer absenceHours) {
        if (attendanceHours == null) attendanceHours = 0;
        if (absenceHours == null) absenceHours = 0;
        int total = attendanceHours + absenceHours;
        if (total == 0) return BigDecimal.ZERO;
        BigDecimal rate = new BigDecimal(attendanceHours)
                .divide(new BigDecimal(total), 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100))
                .setScale(1, RoundingMode.HALF_UP);
        return rate;
    }


    @Override
    public TeacherStatisticsVO teacherStatistics(TeacherStatisticsAO ao) {
        TeacherStatisticsVO vo = new TeacherStatisticsVO();

        if(0 == ao.getType() || 1 == ao.getType()) {
            // 学期ID为空则查询当前学期
            if (ao.getCqlTermId() == null) {
                ao.setCqlTermId(getCurrentTermId(ao.getSchoolId()));
            }
            // 出勤率分布
            vo.setCqlData(this.getTeacherCqlData(ao));
        }

        if(0 == ao.getType() || 2 == ao.getType()) {
            // 学期ID为空则查询当前学期
            if (ao.getYdqsTermId() == null) {
                ao.setYdqsTermId(getCurrentTermId(ao.getSchoolId()));
            }
            // 月度趋势
            vo.setYdqsData(this.getTeacherYdqsData(ao));
        }

        if(0 == ao.getType() || 3 == ao.getType()) {
            // 学期ID为空则查询当前学期
            if (ao.getKclxTermId() == null) {
                ao.setKclxTermId(getCurrentTermId(ao.getSchoolId()));
            }
            // 课程类型分布
            vo.setKclxData(this.getTeacherKclxData(ao));
        }

        if(0 == ao.getType() || 4 == ao.getType()) {
            // 学期ID为空则查询当前学期
            if (ao.getYdksTermId() == null) {
                ao.setYdksTermId(getCurrentTermId(ao.getSchoolId()));
            }
            // 月度课时趋势
            vo.setYdksData(this.getTeacherYdksData(ao));
        }

        if(0 == ao.getType() || 5 == ao.getType()) {
            // 学期ID为空则查询当前学期
            if (ao.getDdlTermId() == null) {
                ao.setDdlTermId(getCurrentTermId(ao.getSchoolId()));
            }
        }

        if(0 == ao.getType() || 6 == ao.getType()) {
            // 学期ID为空则查询当前学期
            if (ao.getPjfsTermId() == null) {
                ao.setPjfsTermId(getCurrentTermId(ao.getSchoolId()));
            }
            // 评价分数分布
            vo.setPjfsData(this.getTeacherPjfsData(ao));
        }

        return vo;
    }

    /**
     * 根据当前日期查询所处学期ID
     */
    private Long getCurrentTermId(Long schoolId) {
        LambdaQueryWrapper<EduclTermEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduclTermEntity::getIsValid, 1)
                .eq(EduclTermEntity::getSchoolId, schoolId)
                .le(EduclTermEntity::getStartDate, new Date())
                .ge(EduclTermEntity::getEndDate, new Date())
                .last("LIMIT 1");
        EduclTermEntity term = educlTermMapper.selectOne(wrapper);
        return term != null ? term.getId() : null;
    }


    /**
     * 教师统计-评价分数分布数据
     */
    private List<StatisticsCommonCountVO> getTeacherPjfsData(TeacherStatisticsAO ao) {
        // 数据库一次查询返回5个分数段数量
        Map<String, Object> stats = statisticsMapper.countTeacherPjfsData(ao.getSchoolId(), ao.getPjfsTermId());
        List<StatisticsCommonCountVO> result = new ArrayList<>();
        if (stats != null) {
            int below60 = stats.get("below60") != null ? ((Number) stats.get("below60")).intValue() : 0;
            int range60to69 = stats.get("range60to69") != null ? ((Number) stats.get("range60to69")).intValue() : 0;
            int range70to79 = stats.get("range70to79") != null ? ((Number) stats.get("range70to79")).intValue() : 0;
            int range80to89 = stats.get("range80to89") != null ? ((Number) stats.get("range80to89")).intValue() : 0;
            int range90to100 = stats.get("range90to100") != null ? ((Number) stats.get("range90to100")).intValue() : 0;


            result.add(buildCountVO("<60分", below60, null, null));
            result.add(buildCountVO("60分~69分", range60to69, null, null));
            result.add(buildCountVO("70分~79分", range70to79, null, null));
            result.add(buildCountVO("80分~89分", range80to89, null, null));
            result.add(buildCountVO("90分~100分", range90to100, null, null));
        }else {
            result.add(buildCountVO("<60分", 0, null, null));
            result.add(buildCountVO("60分~69分", 0, null, null));
            result.add(buildCountVO("70分~79分", 0, null, null));
            result.add(buildCountVO("80分~89分", 0, null, null));
            result.add(buildCountVO("90分~100分", 0, null, null));
        }
        return result;
    }

    private StatisticsCommonCountVO buildCountVO(String name, Integer value,BigDecimal sValue, String ratio) {
        StatisticsCommonCountVO vo = new StatisticsCommonCountVO();
        vo.setName(name);
        if(null != ratio)vo.setRatio(ratio);
        if(null != value)vo.setValue(value);
        if(null != sValue)vo.setSValue(sValue);
        return vo;
    }

    /**
     * 教师统计-月度课时趋势数据
     */
    private List<StatisticsCommonCountVO> getTeacherYdksData(TeacherStatisticsAO ao) {
        EduclTermEntity term = null;
        if (ao.getYdksTermId() != null) {
            // 查询学期信息
            term = educlTermMapper.selectById(ao.getYdksTermId());
        }
        // 查询每月课时数据
        List<StatisticsCommonCountVO> result = new ArrayList<>();
        if (term == null){
            List<Map<String, Object>> monthlyData = statisticsMapper.listMonthlyLessonCount(ao.getSchoolId(),ao.getYdksTermId(), null, null);
            if (CollectionUtil.isNotEmpty(monthlyData)) {
                for (Map<String, Object> row : monthlyData) {
                    int count = row.get("lessonCount") != null ? ((Number) row.get("lessonCount")).intValue() : 0;
                    result.add(buildCountVO((String) row.get("month"), count, null, null));
                }
            }
        }else {
            List<Map<String, Object>> monthlyData = statisticsMapper.listMonthlyLessonCount(ao.getSchoolId(),ao.getYdksTermId(), term.getStartDate(), term.getEndDate());
            Map<String, Integer> monthCountMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(monthlyData)) {
                for (Map<String, Object> row : monthlyData) {
                    String month = (String) row.get("month");
                    int count = row.get("lessonCount") != null ? ((Number) row.get("lessonCount")).intValue() : 0;
                    monthCountMap.put(month, count);
                }
            }
            // 学期起止时间填充所有月份
            Calendar cal = Calendar.getInstance();
            cal.setTime(term.getStartDate());
            cal.set(Calendar.DAY_OF_MONTH, 1);
            Calendar endCal = Calendar.getInstance();
            endCal.setTime(term.getEndDate());
            endCal.set(Calendar.DAY_OF_MONTH, 1);
            while (!cal.after(endCal)) {
                String monthLabel = cal.get(Calendar.YEAR) + "年" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "月";
                result.add(buildCountVO(monthLabel, monthCountMap.getOrDefault(monthLabel, 0), null, null));
                cal.add(Calendar.MONTH, 1);
            }
        }
        return result;
    }

    /**
     * 教师统计-课程类型分布数据
     */
    private List<StatisticsCommonCountVO> getTeacherKclxData(TeacherStatisticsAO ao) {
        return statisticsMapper.listTeacherKclxData(ao.getSchoolId(),ao.getKclxTermId());
    }

    /**
     * 教师统计-月度趋势数据
     */
    private List<StatisticsCommonCountVO> getTeacherYdqsData(TeacherStatisticsAO ao) {
        Long termId = ao.getYdqsTermId();

        //查询学期信息
        EduclTermEntity term = educlTermMapper.selectById(termId);
        if (term == null || term.getStartDate() == null || term.getEndDate() == null) {
            return Collections.emptyList();
        }

        // 查询每月出勤数据
        List<Map<String, Object>> monthlyData = statisticsMapper.listTeacherMonthlyAttendanceRate(termId);
        Map<String, String> monthRateMap = new HashMap<>();
        if (monthlyData != null) {
            for (Map<String, Object> row : monthlyData) {
                String month = (String) row.get("month");
                int total = row.get("totalSessions") != null ? ((Number) row.get("totalSessions")).intValue() : 0;
                int attended = row.get("attendedSessions") != null ? ((Number) row.get("attendedSessions")).intValue() : 0;
                String rate = "0.00%";
                if (total > 0) {
                    BigDecimal rateBd = new BigDecimal(attended).divide(new BigDecimal(total), 4, RoundingMode.HALF_UP)
                            .multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
                    rate = rateBd + "%";
                }
                monthRateMap.put(month, rate);
            }
        }

        // 学期起止时间填充所有月份
        List<StatisticsCommonCountVO> result = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(term.getStartDate());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(term.getEndDate());
        endCal.set(Calendar.DAY_OF_MONTH, 1);
        while (!cal.after(endCal)) {
            String monthLabel = cal.get(Calendar.YEAR) + "年" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "月";
            StatisticsCommonCountVO vo = new StatisticsCommonCountVO();
            vo.setName(monthLabel);
            vo.setRatio(monthRateMap.getOrDefault(monthLabel, "0.00%"));
            result.add(buildCountVO(monthLabel, null, null, monthRateMap.getOrDefault(monthLabel, "0.00%")));
            cal.add(Calendar.MONTH, 1);
        }
        return result;
    }

    /**
     * 教师统计-出勤率分布数据
     */
    private List<StatisticsCommonCountVO> getTeacherCqlData(TeacherStatisticsAO ao) {
        Long termId = ao.getCqlTermId();
        Map<String, Object> stats = statisticsMapper.countTeacherCqlStatistics(termId);
        if (stats == null) {
            return Collections.emptyList();
        }
        int attendanceHours = stats.get("attendanceHours") != null ? ((Number) stats.get("attendanceHours")).intValue() : 0;
        int adjustHours = stats.get("adjustHours") != null ? ((Number) stats.get("adjustHours")).intValue() : 0;

        StatisticsCommonCountVO attendanceVO = new StatisticsCommonCountVO();
        attendanceVO.setName("出勤课时");
        attendanceVO.setValue(attendanceHours);

        StatisticsCommonCountVO adjustVO = new StatisticsCommonCountVO();
        adjustVO.setName("调课课时");
        adjustVO.setValue(adjustHours);

        List<StatisticsCommonCountVO> result = new ArrayList<>();
        result.add(attendanceVO);
        result.add(adjustVO);
        return result;
    }

    /**
     * 出勤状态转中文描述
     */
    private String convertAttendanceStatus(Integer status) {
        if (status == null) return "未记录";
        switch (status) {
            case 1: return "出勤";
            case 2: return "迟到";
            case 3: return "早退";
            case 4: return "缺勤";
            case 5: return "请假";
            default: return "未知";
        }
    }

    @Override
    public SchoolSummaryStatisticsVO getSchoolSummaryStatistics(SchoolSummaryStatisticsAO ao) {
        SchoolSummaryStatisticsVO vo = new SchoolSummaryStatisticsVO();

        // 1. 学校ID：入参为空时取当前账号所属学校ID
        Long schoolId = ao.getSchoolId();
        if (ObjectUtil.isNull(schoolId) || schoolId == 0L) {
            schoolId = SmartRequestTokenUtil.getRequestUserStuSchoolId();
        }

        // 2. 判断当前账号是否为超级管理员（isSuperman = true 表示超级管理员）
        RequestTokenBO requestUser = SmartRequestTokenUtil.getRequestUser();
        boolean isSuperAdmin = requestUser != null
                && requestUser.getEmployeeBO() != null
                && Boolean.TRUE.equals(requestUser.getEmployeeBO().getIsSuperman());

        // 3. 超级管理员才返回学校数
        if (isSuperAdmin) {
            Integer schoolCount = statisticsMapper.countAllSchools();
            vo.setSchoolCount(schoolCount == null ? 0 : schoolCount);
        }

        // 4. 查询核心统计指标
        //获取当前学期
        Long termId =null;
        EduclTermEntity term = educlTermService.getCurrentTermBySchoolId(schoolId);
        if(null != term) termId = term.getId();
        Map<String, Object> stats = statisticsMapper.getSchoolSummaryStatistics(schoolId,termId);
        if (stats != null) {
            vo.setTotalStudentCount(toInt(stats.get("totalStudentCount")));
            vo.setRegisteredStudentCount(toInt(stats.get("registeredStudentCount")));
            vo.setPendingStudentCount(toInt(stats.get("pendingStudentCount")));
            vo.setClassCount(toInt(stats.get("classCount")));
            vo.setTeacherCount(toInt(stats.get("teacherCount")));
            vo.setAdminCourseCount(toInt(stats.get("adminCourseCount")));
            vo.setAfterSchoolCourseCount(toInt(stats.get("afterSchoolCourseCount")));

            // 5. 计算学生出勤率：已到课记录数 / 总出勤记录数 * 100
            int presentCount = toInt(stats.get("attendancePresentCount"));
            int totalCount = toInt(stats.get("attendanceTotalCount"));
            if (totalCount > 0) {
                BigDecimal rate = new BigDecimal(presentCount)
                        .divide(new BigDecimal(totalCount), 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(100))
                        .setScale(1, RoundingMode.HALF_UP);
                vo.setStudentAttendanceRate(rate + "%");
            } else {
                vo.setStudentAttendanceRate("0%");
            }
        } else {
            // 兜底初始化
            vo.setTotalStudentCount(0);
            vo.setRegisteredStudentCount(0);
            vo.setPendingStudentCount(0);
            vo.setClassCount(0);
            vo.setTeacherCount(0);
            vo.setAdminCourseCount(0);
            vo.setAfterSchoolCourseCount(0);
            vo.setStudentAttendanceRate("0%");
        }

        // 6. 查询通知公告前5条（article_type_id 固定为 14298248723427，按日期倒序）
        Long articleTypeId = 14298248723427L;
        List<NoticeArticleItemVO> noticeList = statisticsMapper.listTopNotices(schoolId, articleTypeId, 5);
        vo.setNoticeList(noticeList != null ? noticeList : Collections.emptyList());

        return vo;
    }

    /**
     * Map值安全转换为int
     */
    private int toInt(Object value) {
        if (value == null) return 0;
        if (value instanceof Number) return ((Number) value).intValue();
        return 0;
    }



    @Override
    public StudentStatisticsVO studentStatistics(StudentStatisticsAO ao) {
        EduclTermEntity term = resolveStudentStatisticsTerm(ao);
        StudentStatisticsVO vo = new StudentStatisticsVO();
        vo.setTermId(term.getId());
        vo.setTermName(term.getTermName());
        if(0 == ao.getType() || 1 == ao.getType()) {
            // 学生考勤分布
            vo.setKqfbData(this.getKqfbData(ao));
        }

        if(0 == ao.getType() || 2 == ao.getType()) {
            // 月度趋势
            vo.setYdqsData(this.getYdqsData(ao));
        }

        if(0 == ao.getType() || 3 == ao.getType()) {
            // 特色课程分布
            vo.setTskcData(this.getTskcData(ao));
        }

        if(0 == ao.getType() || 4 == ao.getType()) {
            // 行政课程分布
            vo.setXzkcData(this.getXzkcData(ao));
        }

        if(0 == ao.getType() || 5 == ao.getType()) {
            // 兴趣发展
            vo.setXqfzData(this.getXqfzData(ao));
        }

        if(0 == ao.getType() || 6 == ao.getType()) {
            // 学生选课分布
            vo.setXsxkData(this.getXsxkData(ao));
        }

        if(0 == ao.getType() || 7 == ao.getType()) {
            // 学生性别分布
            vo.setXbfbData(this.getXbfbData(ao));
        }
        return vo;
    }

    /**
     * 学生画像统计-学生选课分布：按报名来源分组统计已审核通过的学生数量
     */
    private List<StatisticsCommonCountVO> getXsxkData(StudentStatisticsAO ao) {
        List<StatisticsCommonCountVO> result = statisticsMapper.countStudentXsxkData(ao.getSchoolId(), ao.getXsxkfTermId());
        return result;
    }



    /**
     * 构建课程类别统计VO
     */
    private CourseCategoryStatisticsVO buildCategoryStatisticsVO(String categoryTypeName, List<Map<String, Object>> dataList) {
        CourseCategoryStatisticsVO vo = new CourseCategoryStatisticsVO();
        vo.setCourseCategoryName(categoryTypeName);
        vo.setCourseCategoryCount(dataList.stream()
                .mapToInt(row -> row.get("courseCount") != null ? ((Number) row.get("courseCount")).intValue() : 0)
                .sum());
        vo.setChildrenList(dataList.stream().map(row -> {
            CourseCategoryChildStatisticsVO childVO = new CourseCategoryChildStatisticsVO();
            childVO.setCategoryId(row.get("categoryId") != null ? ((Number) row.get("categoryId")).longValue() : null);
            childVO.setCourseCategoryName(row.get("categoryName") != null ? (String) row.get("categoryName") : "未分类");
            childVO.setCourseCategoryCount(row.get("courseCount") != null ? ((Number) row.get("courseCount")).intValue() : 0);
            return childVO;
        }).collect(Collectors.toList()));
        return vo;
    }




    /**
     * 构建上课时间列表
     */
    private List<String> buildSessionTimes(Long courseClassId) {
        List<String> sessionTimes = new ArrayList<>();
        // 查询该课程班的所有排课规则
        LambdaQueryWrapper<AsCourseScheduleRuleEntity> ruleQuery = new LambdaQueryWrapper<>();
        ruleQuery.eq(AsCourseScheduleRuleEntity::getCourseClassId, courseClassId).eq(BaseEntity::getIsValid, 1);
        List<AsCourseScheduleRuleEntity> rules = asCourseScheduleRuleMapper.selectList(ruleQuery);
        if (CollectionUtil.isEmpty(rules)) {
            return sessionTimes;
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // 星期映射
        Map<Integer, String> weekDayMap = new HashMap<>();
        weekDayMap.put(1, "周一");
        weekDayMap.put(2, "周二");
        weekDayMap.put(3, "周三");
        weekDayMap.put(4, "周四");
        weekDayMap.put(5, "周五");
        weekDayMap.put(6, "周六");
        weekDayMap.put(7, "周日");

        for (AsCourseScheduleRuleEntity rule : rules) {
            String timeStr = "";
            String startTime = rule.getStartTime() != null ? timeFormat.format(rule.getStartTime()) : "";
            String endTime = rule.getEndTime() != null ? timeFormat.format(rule.getEndTime()) : "";
            String timeRange = (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) ? startTime + "-" + endTime : "";

            if (rule.getRuleType() != null && rule.getRuleType() == 1) {
                // 周期规则：每周一、周三 08:00-09:00
                if (StringUtils.isNotBlank(rule.getWeekDaysJson())) {
                    List<Integer> weekDays = JSON.parseArray(rule.getWeekDaysJson(), Integer.class);
                    if (CollectionUtil.isNotEmpty(weekDays)) {
                        String weekDaysStr = weekDays.stream().map(weekDayMap::get).filter(Objects::nonNull).collect(Collectors.joining("、"));
                        timeStr = "每" + weekDaysStr;
                        if (StringUtils.isNotBlank(timeRange)) timeStr += " " + timeRange;
                    }
                }
            } else if (rule.getRuleType() != null && rule.getRuleType() == 2) {
                // 自定义日期：2024-09-01 08:00-09:00
                if (rule.getSessionDate() != null) {
                    timeStr = dateFormat.format(rule.getSessionDate());
                    if (StringUtils.isNotBlank(timeRange)) timeStr += " " + timeRange;
                }
            }
            if (StringUtils.isNotBlank(timeStr)) {
                sessionTimes.add(timeStr);
            }
        }
        return sessionTimes;
    }

    /**
     * 课程出勤统计-出勤趋势数据
     */
    private List<AttendanceTrendStatisticsVO> getAttendanceTrendData(CoursAttendanceStatisticsAO ao) {
        Long schoolId = ao.getSchoolId();
        Long termId = ao.getTermId();
    
        // 查询学期信息
        EduclTermEntity term = educlTermMapper.selectById(termId);
        if (term == null || term.getStartDate() == null || term.getEndDate() == null) {
            return Collections.emptyList();
        }
    
        // 查询每周各年级的出勤数据
        List<WeekGradeAttendanceVO> weeklyGradeData = statisticsMapper.countCourseAttendanceByWeekAndGrade(
                schoolId, termId, term.getStartDate(), term.getEndDate());
        // 按周分组
        Map<Integer, List<WeekGradeAttendanceVO>> weekGroupMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(weeklyGradeData)) {
            for (WeekGradeAttendanceVO row : weeklyGradeData) {
                Integer weekNum = row.getWeekNum() != null ? row.getWeekNum() : 0;
                weekGroupMap.computeIfAbsent(weekNum, k -> new ArrayList<>()).add(row);
            }
        }
        // 构建返回结果
        List<AttendanceTrendStatisticsVO> result = new ArrayList<>();
        for (Map.Entry<Integer, List<WeekGradeAttendanceVO>> entry : weekGroupMap.entrySet()) {
            Integer weekNum = entry.getKey();
            List<WeekGradeAttendanceVO> gradeDataList = entry.getValue();
            String weekLabel = "第" + weekNum + "周";
            // 构建该周各年级的出勤率数据
            List<StatisticsCommonCountVO> gradeData = new ArrayList<>();
            for (WeekGradeAttendanceVO row : gradeDataList) {
                if(row.getGradeName() != null){
                    String gradeName = row.getGradeName();
                    int attendanceCount = row.getAttendanceCount() != null ? row.getAttendanceCount() : 0;
                    int totalCount = row.getTotalCount() != null ? row.getTotalCount() : 0;

                    String rate = "0.00%";
                    if (totalCount > 0) {
                        BigDecimal rateBd = new BigDecimal(attendanceCount)
                                .divide(new BigDecimal(totalCount), 4, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(100))
                                .setScale(2, RoundingMode.HALF_UP);
                        rate = rateBd + "%";
                    }
                    gradeData.add(buildCountVO(gradeName, null, null, rate));
                }
            }
            AttendanceTrendStatisticsVO trendVO = new AttendanceTrendStatisticsVO();
            trendVO.setWeek(weekLabel);
            trendVO.setGradeData(gradeData);
            result.add(trendVO);
        }
        return result;
    }




    /**
     * 学生画像统计-兴趣发展数据
     */
    private StatisticsXqfzVO getXqfzData(StudentStatisticsAO ao) {
        StatisticsXqfzVO vo = new StatisticsXqfzVO();
        Long schoolId = ao.getSchoolId();
        Long termId = ao.getXqfzTermId();

        // 1. 雷达图数据：按课程类别分组统计报名人数
        List<Map<String, Object>> ldtDbData = statisticsMapper.countStudentXqfzLdtData(schoolId, termId);
        List<StatisticsCommonCountVO> ldtData = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(ldtDbData)) {
            for (Map<String, Object> row : ldtDbData) {
                String categoryName = row.get("categoryName") != null ? (String) row.get("categoryName") : "未分类";
                int cnt = row.get("cnt") != null ? ((Number) row.get("cnt")).intValue() : 0;
                ldtData.add(buildCountVO(categoryName, cnt, null, null));
            }
        }
        vo.setLdtData(ldtData);

        // 2. 最热门课程：报名人数前2的课程班名称拼接
        List<Map<String, Object>> hotCourseData = statisticsMapper.countStudentXqfzHotCourse(schoolId, termId);
        if (CollectionUtil.isNotEmpty(hotCourseData)) {
            String hotCourseNames = hotCourseData.stream()
                    .map(row -> row.get("courseClassName") != null ? (String) row.get("courseClassName") : "")
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining("、"));
            vo.setHotCourseNames(hotCourseNames);
        }

        // 3. 参与度最高年级：报名人数最多的年级
        Map<String, Object> hotGradeData = statisticsMapper.countStudentXqfzHotGrade(schoolId, termId);
        if (hotGradeData != null && hotGradeData.get("gradeName") != null) {
            vo.setHotGradeName((String) hotGradeData.get("gradeName"));
        }

        // 4. 课后服务总参与度：报名学生数 / 在籍学生总数
        Map<String, Object> participationData = statisticsMapper.countStudentXqfzParticipation(schoolId, termId);
        if (participationData != null) {
            int enrolledCount = participationData.get("enrolledCount") != null ? ((Number) participationData.get("enrolledCount")).intValue() : 0;
            int totalStudentCount = participationData.get("totalStudentCount") != null ? ((Number) participationData.get("totalStudentCount")).intValue() : 0;
            if (totalStudentCount > 0) {
                BigDecimal rate = new BigDecimal(enrolledCount)
                        .divide(new BigDecimal(totalStudentCount), 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(100))
                        .setScale(2, RoundingMode.HALF_UP);
                vo.setTotalParticipationRate(rate + "%");
            } else {
                vo.setTotalParticipationRate("0.00%");
            }
        }
        return vo;
    }

    /**
     * 学生画像统计-学生性别分布数据
     */
    private List<StatisticsCommonCountVO> getXbfbData(StudentStatisticsAO ao) {
        List<Map<String, Object>> dbData = statisticsMapper.countStudentXbfbData(
                ao.getSchoolId(), ao.getTermId(), ao.getXbfbGradeId());
        // 将查询结果转为 gender -> count 的Map
        Map<Integer, Integer> genderCountMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(dbData)) {
            for (Map<String, Object> row : dbData) {
                Integer gender = row.get("gender") != null ? ((Number) row.get("gender")).intValue() : null;
                int cnt = row.get("cnt") != null ? ((Number) row.get("cnt")).intValue() : 0;
                genderCountMap.put(gender, cnt);
            }
        }
        List<StatisticsCommonCountVO> result = new ArrayList<>();
        result.add(buildCountVO("男", genderCountMap.getOrDefault(0, 0), null, null));
        result.add(buildCountVO("女", genderCountMap.getOrDefault(1, 0), null, null));
        return result;
    }

    /**
     * 学生画像统计-月度趋势数据
     */
    private List<StatisticsCommonCountVO> getYdqsData(StudentStatisticsAO ao) {
        Long termId = ao.getYdqsTermId();

        // 查询学期信息
        EduclTermEntity term = educlTermMapper.selectById(termId);
        if (term == null || term.getStartDate() == null || term.getEndDate() == null) {
            return Collections.emptyList();
        }

        // 查询每月出勤数据
        List<Map<String, Object>> monthlyData = statisticsMapper.countStudentYdqsData(ao.getSchoolId(), termId,null);
        Map<String, String> monthRateMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(monthlyData)) {
            for (Map<String, Object> row : monthlyData) {
                String month = (String) row.get("month");
                int attendanceCount = row.get("attendanceCount") != null ? ((Number) row.get("attendanceCount")).intValue() : 0;
                int totalCount = row.get("totalCount") != null ? ((Number) row.get("totalCount")).intValue() : 0;
                String rate = "0.00%";
                if (totalCount > 0) {
                    BigDecimal rateBd = new BigDecimal(attendanceCount)
                            .divide(new BigDecimal(totalCount), 4, RoundingMode.HALF_UP)
                            .multiply(new BigDecimal(100))
                            .setScale(2, RoundingMode.HALF_UP);
                    rate = rateBd + "%";
                }
                monthRateMap.put(month, rate);
            }
        }

        // 学期起止时间填充所有月份
        List<StatisticsCommonCountVO> result = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(term.getStartDate());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(term.getEndDate());
        endCal.set(Calendar.DAY_OF_MONTH, 1);
        while (!cal.after(endCal)) {
            String monthLabel = cal.get(Calendar.YEAR) + "年" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "月";
            result.add(buildCountVO(monthLabel, null, null, monthRateMap.getOrDefault(monthLabel, "0.00%")));
            cal.add(Calendar.MONTH, 1);
        }
        return result;
    }

    private List<StatisticsCommonCountVO> getXzkcData(StudentStatisticsAO ao) {
        List<Map<String, Object>> dbData = statisticsMapper.countStudentXzkcData(
                ao.getSchoolId(), ao.getXzkcfbTermId());
        List<StatisticsCommonCountVO> result = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(dbData)) {
            for (Map<String, Object> row : dbData) {
                String gradeName = row.get("gradeName") != null ? (String) row.get("gradeName") : "未分年级";
                int cnt = row.get("cnt") != null ? ((Number) row.get("cnt")).intValue() : 0;
                result.add(buildCountVO(gradeName, cnt, null, null));
            }
        }
        return result;
    }

    /**
     * 学生画像统计-特色课程分布数据
     */
    private List<StatisticsCommonCountVO> getTskcData(StudentStatisticsAO ao) {
        List<Map<String, Object>> dbData = statisticsMapper.countStudentTskcData(
                ao.getSchoolId(), ao.getTskcfbTermId());
        List<StatisticsCommonCountVO> result = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(dbData)) {
            for (Map<String, Object> row : dbData) {
                String gradeName = row.get("gradeName") != null ? (String) row.get("gradeName") : "未分年级";
                int cnt = row.get("cnt") != null ? ((Number) row.get("cnt")).intValue() : 0;
                result.add(buildCountVO(gradeName, cnt, null, null));
            }
        }
        return result;
    }

    /**
     * 学生画像统计-学生考勤分布数据
     */
    private List<StatisticsCommonCountVO> getKqfbData(StudentStatisticsAO ao) {
        List<Map<String, Object>> dbData = statisticsMapper.countStudentKqfbData(
                ao.getSchoolId(), ao.getTermId(), ao.getKqfbSessionStartDate(), ao.getKqfbSessionEndDate());
        // 将查询结果转为 attendanceStatus -> count 的Map
        Map<Integer, Integer> statusCountMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(dbData)) {
            for (Map<String, Object> row : dbData) {
                Integer status = row.get("attendanceStatus") != null ? ((Number) row.get("attendanceStatus")).intValue() : null;
                int cnt = row.get("cnt") != null ? ((Number) row.get("cnt")).intValue() : 0;
                statusCountMap.put(status, cnt);
            }
        }
        // 按固定顺序构建5种考勤状态的结果
        List<StatisticsCommonCountVO> result = new ArrayList<>();
        result.add(buildCountVO("出勤", statusCountMap.getOrDefault(1, 0), null, null));
        result.add(buildCountVO("迟到", statusCountMap.getOrDefault(2, 0), null, null));
        result.add(buildCountVO("早退", statusCountMap.getOrDefault(3, 0), null, null));
        result.add(buildCountVO("缺勤", statusCountMap.getOrDefault(4, 0), null, null));
        result.add(buildCountVO("请假", statusCountMap.getOrDefault(5, 0), null, null));
        return result;
    }

    /**
     * 解析统计查询所属学期，日期空档时允许只读回看最近学期。
     */
    private EduclTermEntity resolveStatisticsTerm(Long schoolId, Long termId) {
        return termId == null
                ? studentTermScopeSupport.resolveReadableTerm(schoolId)
                : studentTermScopeSupport.requireTermInSchool(schoolId, termId);
    }

    /**
     * 兼容学生画像旧版分区学期字段，并将本次查询统一收敛到一个学期。
     */
    private EduclTermEntity resolveStudentStatisticsTerm(StudentStatisticsAO ao) {
        List<Long> requestedTermIds = Arrays.asList(
                ao.getTermId(), ao.getYdqsTermId(), ao.getTskcfbTermId(),
                ao.getXzkcfbTermId(), ao.getXqfzTermId());
        List<Long> distinctTermIds = requestedTermIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (distinctTermIds.size() > 1) {
            throw new com.sxkj.edu.common.exception.BaseBusinessException(
                    com.sxkj.edu.common.base.constants.ResultCode.PARA_ERROR.code(),
                    "学生画像统计只允许查询同一学期");
        }
        Long termId = distinctTermIds.isEmpty() ? null : distinctTermIds.get(0);
        EduclTermEntity term = resolveStatisticsTerm(ao.getSchoolId(), termId);
        ao.setTermId(term.getId());
        ao.setYdqsTermId(term.getId());
        ao.setTskcfbTermId(term.getId());
        ao.setXzkcfbTermId(term.getId());
        ao.setXqfzTermId(term.getId());
        return term;
    }



    @Override
    public PageResultDTO<StudentProfilePageVO> pageStudentProfile(StudentProfilePageAO ao) {
        Page<StudentProfilePageVO> page = statisticsMapper.pageStudentProfile(
                new Page<>(ao.getPageIndex(), ao.getPageSize()), ao);
        PageResultDTO<StudentProfilePageVO> result = new PageResultDTO<>();
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setPageIndex(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setList(page.getRecords());
        return result;
    }

    @Override
    public StudentProfileDetailVO getStudentProfileDetail(StudentProfileDetailAO ao) {
        LambdaQueryWrapper<AsStudentEntity> stuQw = new LambdaQueryWrapper<>();
        stuQw.eq(AsStudentEntity::getStudentNo, ao.getStudentNo())
                .eq(AsStudentEntity::getRegisterStatus, 1).eq(AsStudentEntity::getIsValid, 1)
                .eq(AsStudentEntity::getSchoolId, ao.getSchoolId()).orderByDesc(AsStudentEntity::getCreateTime);

        List<AsStudentEntity> studentList = asStudentService.list(stuQw);
        if(CollectionUtil.isEmpty(studentList)) return null;

        List<Long> stuIds = studentList.stream().map(BaseEntity::getId).collect(Collectors.toList());
        // 查询学生出勤率、综合评分
        Map<String, Object> attendanceAndScore = statisticsMapper.getStudentAttendanceAndScore(stuIds);

        // 取创建时间最新的学生记录作为基本信息（已按createTime倒序）
        AsStudentEntity latestStudent = studentList.get(0);
        StudentProfileDetailVO detailVO = new StudentProfileDetailVO();
        detailVO.setStudentNo(latestStudent.getStudentNo());
        detailVO.setStudentName(latestStudent.getStudentName());
        detailVO.setGender(latestStudent.getGender());
        // 年级名称
        if (latestStudent.getGradeId() != null) {
            EduclGradeEntity grade = educlGradeMapper.selectById(latestStudent.getGradeId());
            detailVO.setGradeName(grade != null ? grade.getGradeName() : "");
        }
        // 班级名称
        if (latestStudent.getClassId() != null) {
            com.sxkj.edu.domain.sys.entity.mult.TEduclClass clazz = educlClassMapper.selectById(latestStudent.getClassId());
            detailVO.setClassName(clazz != null ? clazz.getClassName() : "");
        }
        // 头像文件ID转URL
        if (latestStudent.getAvatarFileId() != null) {
            String avatarUrl = baseFileService.getFileUrlById(latestStudent.getAvatarFileId());
            detailVO.setStudentAvatar(avatarUrl);
        }
        // 出勤率格式化
        if (attendanceAndScore != null && attendanceAndScore.get("attendanceRate") != null) {
            BigDecimal rate = new BigDecimal(attendanceAndScore.get("attendanceRate").toString());
            detailVO.setAttendanceRate(rate.setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
        } else {
            detailVO.setAttendanceRate("0.00%");
        }
        // 综合评分
        if (attendanceAndScore != null && attendanceAndScore.get("comprehensiveScore") != null) {
            detailVO.setComprehensiveScore(new BigDecimal(attendanceAndScore.get("comprehensiveScore").toString()));
        }
        //参与课程数据
        List<StudentProfileDetailCourseVO> courseList = statisticsMapper.listStudentProfileCourses(stuIds);
        detailVO.setCourseList(courseList);
        //兴趣发展统计
        List<StatisticsCommonCountVO> courseCategoryData = statisticsMapper.listStudentProfileCourseCategoryData(stuIds);
        detailVO.setCourseCategoryData(courseCategoryData);

        if(0 == ao.getType() || 1 == ao.getType()) {
            // 出勤情况
            StudentProfileDetailAttendanceVO attendanceData = new StudentProfileDetailAttendanceVO();
            Map<String, Object> attStats = statisticsMapper.getStudentAttendanceStats(stuIds);
            if (attStats != null) {
                attendanceData.setLeaveCount(attStats.get("leaveCount") != null ? ((Number) attStats.get("leaveCount")).intValue() : 0);
                attendanceData.setLateCount(attStats.get("lateCount") != null ? ((Number) attStats.get("lateCount")).intValue() : 0);
                attendanceData.setEarlyLeaveCount(attStats.get("earlyLeaveCount") != null ? ((Number) attStats.get("earlyLeaveCount")).intValue() : 0);
                attendanceData.setAbsentCount(attStats.get("absentCount") != null ? ((Number) attStats.get("absentCount")).intValue() : 0);
                if (attStats.get("attendanceRate") != null) {
                    BigDecimal rate = new BigDecimal(attStats.get("attendanceRate").toString());
                    attendanceData.setAttendanceRate(rate.setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
                } else {
                    attendanceData.setAttendanceRate("0.00%");
                }
            }
            detailVO.setAttendanceData(attendanceData);

            StudentStatisticsAO stuAo = new StudentStatisticsAO();
            stuAo.setStuIds(stuIds);
            stuAo.setSchoolId(ao.getSchoolId());
            stuAo.setTermId(ao.getTermId());
            List<StatisticsCommonCountVO> ydqsData = this.getYdqsData(stuAo);
            detailVO.setYdqsData(ydqsData);
        }
        //教师评价列表
        List<StudentProfileTeacherEvaluateVO> teacherEvaluateList = statisticsMapper.listStudentProfileTeacherEvaluates(stuIds);
        detailVO.setTeacherEvaluateList(teacherEvaluateList);
        return detailVO;
    }
}
