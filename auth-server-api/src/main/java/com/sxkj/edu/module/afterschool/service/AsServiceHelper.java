package com.sxkj.edu.module.afterschool.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.redis.RedisCache;
import com.sxkj.edu.config.AssembleUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.afterschool.domain.dto.*;
import com.sxkj.edu.module.afterschool.domain.entity.*;
import com.sxkj.edu.module.afterschool.domain.excel.AsAdminClassImportExcel;
import com.sxkj.edu.module.afterschool.domain.vo.AsCourseSessionPreviewVO;
import com.sxkj.edu.module.afterschool.domain.vo.SchoolBaseImportErrorVO;
import com.sxkj.edu.module.afterschool.enums.AsServiceDisplayTabEnum;
import com.sxkj.edu.module.afterschool.enums.ClassStatusEnum;
import com.sxkj.edu.module.afterschool.enums.EnrollTypeEnum;
import com.sxkj.edu.module.afterschool.mapper.AsCourseClassMapper;
import com.sxkj.edu.module.afterschool.mapper.AsCourseScheduleRuleMapper;
import com.sxkj.edu.module.afterschool.mapper.AsServiceMapper;
import com.sxkj.edu.module.afterschool.support.AsAsyncOperatorContext;
import com.sxkj.edu.module.afterschool.support.CourseClassScopeSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AsServiceHelper {

    @Resource
    private CourseClassScopeSupport courseClassScopeSupport;


    /**
     * 格式化日期为指定模式字符串，null 返回空串。
     */
    public String formatDateStr(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 格式化日期区间，自动处理单端为空的情况。
     */
    public String formatDateRange(Date start, Date end, String pattern, String separator) {
        String startText = formatDateStr(start, pattern);
        String endText = formatDateStr(end, pattern);
        if (!StringUtils.hasText(startText) && !StringUtils.hasText(endText)) {
            return "";
        }
        if (!StringUtils.hasText(startText)) {
            return endText;
        }
        if (!StringUtils.hasText(endText)) {
            return startText;
        }
        return startText + separator + endText;
    }


    /**
     * 判断排课规则是否为周规则。
     */
    public boolean isWeeklyRule(AsCourseScheduleRuleAO rule) {
        return rule != null && ("weekly".equalsIgnoreCase(rule.getRuleType()) || "1".equals(rule.getRuleType()));
    }

    /**
     * 判断排课规则是否为日期规则。
     */
    public boolean isDateRule(AsCourseScheduleRuleAO rule) {
        return rule != null && ("date".equalsIgnoreCase(rule.getRuleType()) || "2".equals(rule.getRuleType()));
    }

    /**
     * 根据服务实体推断其应归属的展示 Tab。
     */
    public AsServiceDisplayTabEnum resolveDisplayTab(AsServiceEntity service) {
        if (!isValid(service)) {
            return null;
        }
        Integer status = service.getServiceStatus();
        if (Objects.equals(status, CoreConstant.SERVICE_STATUS_DRAFT)) {
            return AsServiceDisplayTabEnum.UNPUBLISHED;
        }
        if (Objects.equals(status, CoreConstant.SERVICE_STATUS_FINISHED)) {
            return AsServiceDisplayTabEnum.FINISHED;
        }
        if (Objects.equals(status, CoreConstant.SERVICE_STATUS_RUNNING)) {
            return AsServiceDisplayTabEnum.RUNNING;
        }
        if (!Objects.equals(status, CoreConstant.SERVICE_STATUS_ENROLLING)) {
            return AsServiceDisplayTabEnum.UNPUBLISHED;
        }
        Date now = new Date();
        Date enrollStart = service.getEnrollStartTime();
        Date enrollEnd = service.getEnrollEndTime();
        Date courseStart = AssembleUtil.normalizeDate(service.getCourseStartDate());
        if (enrollStart != null && now.before(enrollStart)) {
            return AsServiceDisplayTabEnum.PENDING_ENROLL;
        }
        if (courseStart != null && !now.before(courseStart)) {
            return AsServiceDisplayTabEnum.RUNNING;
        }
        if (enrollEnd != null && now.after(enrollEnd) && courseStart != null && now.before(courseStart)) {
            return AsServiceDisplayTabEnum.PENDING_COURSE;
        }
        return AsServiceDisplayTabEnum.ENROLLING;
    }

    /**
     * 统计列表中属于指定展示 Tab 的服务数量。
     */
    public long countDisplayTab(List<AsServiceEntity> list, AsServiceDisplayTabEnum tab) {
        return list.stream().filter(entity -> Objects.equals(resolveDisplayTab(entity), tab)).count();
    }

    // ========================= 冲突检测 =========================

    /**
     * 判断两个时间段是否存在重叠。
     */
    public boolean timeOverlap(String start1, String end1, String start2, String end2) {
        Date s1 = AssembleUtil.parseTime(start1);
        Date e1 = AssembleUtil.parseTime(end1);
        Date s2 = AssembleUtil.parseTime(start2);
        Date e2 = AssembleUtil.parseTime(end2);
        if (s1 == null || e1 == null || s2 == null || e2 == null) {
            return false;
        }
        return s1.before(e2) && s2.before(e1);
    }

    /**
     * 标记预览课次列表中的时间冲突（课程班内部、教师、地点维度），并与已生成课次交叉比对。
     */
    public void markConflicts(List<AsCourseSessionPreviewVO> sessions, List<AsCourseSessionEntity> existingSessions) {
        for (int i = 0; i < sessions.size(); i++) {
            AsCourseSessionPreviewVO current = sessions.get(i);
            for (int j = i + 1; j < sessions.size(); j++) {
                AsCourseSessionPreviewVO target = sessions.get(j);
                if (!AssembleUtil.sameDate(current.getSessionDate(), target.getSessionDate())
                        || !timeOverlap(current.getStartTime(), current.getEndTime(),
                        target.getStartTime(), target.getEndTime())) {
                    continue;
                }
                if (Objects.equals(current.getCourseClassId(), target.getCourseClassId())) {
                    addConflict(current, "同一课程班存在重复上课时间");
                    addConflict(target, "同一课程班存在重复上课时间");
                }
                if (current.getTeacherId() != null
                        && Objects.equals(current.getTeacherId(), target.getTeacherId())) {
                    addConflict(current, "授课教师时间冲突");
                    addConflict(target, "授课教师时间冲突");
                }
                if (StringUtils.hasText(current.getPlaceName())
                        && Objects.equals(current.getPlaceName(), target.getPlaceName())) {
                    addConflict(current, "上课地点时间冲突");
                    addConflict(target, "上课地点时间冲突");
                }
            }
            for (AsCourseSessionEntity existing : emptyList(existingSessions)) {
                if (Objects.equals(current.getCourseClassId(), existing.getCourseClassId())) {
                    continue;
                }
                if (!AssembleUtil.sameDate(current.getSessionDate(), existing.getSessionDate())
                        || !timeOverlap(current.getStartTime(), current.getEndTime(),
                        AssembleUtil.formatTime(existing.getStartTime()), AssembleUtil.formatTime(existing.getEndTime()))) {
                    continue;
                }
                if (current.getTeacherId() != null
                        && Objects.equals(current.getTeacherId(), existing.getTeacherId())) {
                    addConflict(current, "授课教师与已生成课次冲突");
                }
                if (StringUtils.hasText(current.getPlaceName())
                        && Objects.equals(current.getPlaceName(), existing.getPlaceName())) {
                    addConflict(current, "上课地点与已生成课次冲突");
                }
            }
        }
    }

    private void addConflict(AsCourseSessionPreviewVO item, String message) {
        item.setConflict(true);
        if (!item.getConflictMessages().contains(message)) {
            item.getConflictMessages().add(message);
        }
    }

    // ========================= 通用工具方法 =========================

    /**
     * 过滤空值并去重，返回干净的 ID 列表。
     */
    public List<Long> cleanIds(java.util.Collection<Long> ids) {
        if (ids == null) {
            return Collections.emptyList();
        }
        return ids.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }

    /**
     * 解析 JSON 数组字符串为 Long 列表，兼容非标准格式。
     */
    public List<Long> parseLongList(String json) {
        if (!StringUtils.hasText(json)) {
            return Collections.emptyList();
        }
        try {
            return JSON.parseArray(json, Long.class).stream()
                    .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        } catch (Exception ignore) {
            String clean = json.replace("[", "").replace("]", "").replace("\"", "");
            if (!StringUtils.hasText(clean)) {
                return Collections.emptyList();
            }
            List<Long> result = new ArrayList<>();
            for (String item : clean.split(",")) {
                try {
                    if (StringUtils.hasText(item)) {
                        result.add(Long.valueOf(item.trim()));
                    }
                } catch (NumberFormatException ex) {
                    log.debug("ignore invalid id item {}", item, ex);
                }
            }
            return result.stream().distinct().collect(Collectors.toList());
        }
    }

    /**
     * 解析 JSON 数组字符串为 Integer 列表。
     */
    public List<Integer> parseIntegerList(String json) {
        if (!StringUtils.hasText(json)) {
            return Collections.emptyList();
        }
        try {
            return JSON.parseArray(json, Integer.class).stream()
                    .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        } catch (Exception ignore) {
            return Collections.emptyList();
        }
    }

    /**
     * null 安全列表包装。
     */
    public <E> List<E> emptyList(List<E> list) {
        return list == null ? Collections.emptyList() : list;
    }

    /**
     * 分页参数安全处理：页码最小为 1。
     */
    public int safePageIndex(AsServicePageQueryAO query) {
        return query.getPageIndex() <= 0 ? 1 : query.getPageIndex();
    }

    /**
     * 分页参数安全处理：每页条数最小为 20。
     */
    public int safePageSize(AsServicePageQueryAO query) {
        return query.getPageSize() <= 0 ? 20 : query.getPageSize();
    }

    /**
     * 从 MyBatis-Plus Page 构建分页结果。
     */
    public <E> PageResultDTO<E> toPageResult(Page<?> page, List<E> list) {
        PageResultDTO<E> result = new PageResultDTO<>();
        result.setTotal(page == null ? 0L : page.getTotal());
        result.setPages(page == null ? 0L : page.getPages());
        result.setPageIndex(page == null ? 1L : page.getCurrent());
        result.setPageSize(page == null ? 20L : page.getSize());
        result.setList(list == null ? Collections.emptyList() : list);
        return result;
    }

    /**
     * 手动内存分页构建分页结果（用于 Tab 筛选等无法数据库分页的场景）。
     */
    public <E> PageResultDTO<E> toManualPageResult(int pageIndex, int pageSize, List<E> rows) {
        List<E> safeRows = rows == null ? Collections.emptyList() : rows;
        int safePageIndex = pageIndex <= 0 ? 1 : pageIndex;
        int safePageSize = pageSize <= 0 ? 20 : pageSize;
        int fromIndex = Math.min((safePageIndex - 1) * safePageSize, safeRows.size());
        int toIndex = Math.min(fromIndex + safePageSize, safeRows.size());
        PageResultDTO<E> result = new PageResultDTO<>();
        result.setTotal((long) safeRows.size());
        result.setPageIndex((long) safePageIndex);
        result.setPageSize((long) safePageSize);
        long pages = safeRows.isEmpty() ? 0L : (safeRows.size() + safePageSize - 1) / safePageSize;
        result.setPages(pages);
        result.setList(safeRows.subList(fromIndex, toIndex));
        return result;
    }

    /**
     * MyBatis 统计结果空值兜底为 0。
     */
    public int nullToZero(Integer count) {
        return count == null ? 0 : count;
    }

    // ========================= 业务逻辑（无数据库交互） =========================

    /**
     * 重建课次时跳过已经结束的课次，避免编辑已发布课程时把历史课次再次生成。
     */
    public boolean isEndedSessionPreview(AsCourseSessionPreviewVO preview, Date referenceTime) {
        return preview != null
                && preview.getSessionEndDate() != null
                && referenceTime != null
                && !preview.getSessionEndDate().after(referenceTime);
    }

    /**
     * 判断学生性别是否满足课程班要求。
     * genderRequirement：0不限，1男，2女；student.gender：0男，1女。
     */
    public boolean matchesCourseGenderRequirement(AsStudentEntity student, AsCourseClassEntity course) {
        if (student == null || course == null || course.getGenderRequirement() == null
                || course.getGenderRequirement() == 0) {
            return true;
        }
        Integer requiredGender = course.getGenderRequirement() == 1 ? 0 : 1;
        return Objects.equals(student.getGender(), requiredGender);
    }

    /**
     * 判断课程班是否还能接收指定数量的学生。
     */
    public boolean hasCourseCapacity(AsCourseClassEntity course, int nextEnrolledCount) {
        if (course == null || course.getCapacity() == null || course.getCapacity() <= 0) {
            return true;
        }
        return nextEnrolledCount < course.getCapacity();
    }

    /**
     * 行政班安排强制作用于选中的行政班；选了班级时不再保存年级范围，避免扩展到整个年级。
     */
    public String normalizeApplicableGradeIds(AsServiceCourseItemAO item) {
        if (Objects.equals(item.getEnrollType(), CoreConstant.ENROLL_TYPE_CLASS_ASSIGN)
                && !courseClassScopeSupport.parseLongList(item.getApplicableClassIds()).isEmpty()) {
            return null;
        }
        return item.getApplicableGradeIds();
    }

    /**
     * 学生名单展示仅统计已审核通过的有效学生。
     */
    public boolean isApprovedStudent(AsStudentEntity student) {
        return student != null && Objects.equals(student.getRegisterStatus(), 1);
    }



    /**
     * 判断实体是否有效（is_valid = 1）。
     */
    public boolean isValid(AsServiceEntity entity) {
        return entity != null && Objects.equals(entity.getIsValid(), 1);
    }

    /**
     * 判断实体是否有效（is_valid = 1）。
     */
    public boolean isValid(AsCourseClassEntity entity) {
        return entity != null && Objects.equals(entity.getIsValid(), 1);
    }

    /**
     * 判断实体是否有效（is_valid = 1）。
     */
    public boolean isValid(com.sxkj.edu.module.afterschool.domain.entity.EduclTermEntity entity) {
        return entity != null && Objects.equals(entity.getIsValid(), 1);
    }

    /**
     * 判断实体是否有效（is_valid = 1）。
     */
    public boolean isValid(com.sxkj.edu.module.afterschool.domain.entity.AsScheduleTemplateEntity entity) {
        return entity != null && Objects.equals(entity.getIsValid(), 1);
    }

    /**
     * 判断实体是否有效（is_valid = 1）。
     */
    public boolean isValid(com.sxkj.edu.module.afterschool.domain.entity.AsCourseCategoryEntity entity) {
        return entity != null && Objects.equals(entity.getIsValid(), 1);
    }

    /**
     * 判断实体是否有效（is_valid = 1）。
     */
    public boolean isValid(com.sxkj.edu.module.afterschool.domain.entity.AsTeacherEntity entity) {
        return entity != null && Objects.equals(entity.getIsValid(), 1);
    }

    // ========================= 星期解析 =========================

    /** 中文星期单字 -> 数字；"日/天" 统一为 7。 */
    private static final Map<String, Integer> CN_WEEK_MAP = buildCnWeekMap();

    private static Map<String, Integer> buildCnWeekMap() {
        Map<String, Integer> m = new HashMap<>(16);
        m.put("一", 1);
        m.put("二", 2);
        m.put("三", 3);
        m.put("四", 4);
        m.put("五", 5);
        m.put("六", 6);
        m.put("日", 7);
        m.put("天", 7);
        return Collections.unmodifiableMap(m);
    }



}
