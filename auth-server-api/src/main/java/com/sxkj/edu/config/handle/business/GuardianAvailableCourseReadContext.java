package com.sxkj.edu.config.handle.business;

import com.google.common.collect.Lists;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseClassEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsCourseSessionEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsEnrollmentEntity;
import com.sxkj.edu.module.afterschool.domain.entity.AsServiceEntity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 家长公开课程列表的单次请求读取上下文，不跨请求、学生或写入流程共享。
 */
public class GuardianAvailableCourseReadContext {

    public static final int BATCH_SIZE = 500;

    @Getter
    private final Map<Long, AsServiceEntity> services = new HashMap<>();
    @Getter
    private final Map<Long, AsEnrollmentEntity> enrollments = new HashMap<>();
    @Getter
    private final List<Long> courseClassIds = new ArrayList<>();
    private final Map<Long, String> scopeErrors = new HashMap<>();
    private final Map<Long, String> termNames = new HashMap<>();
    private final Map<String, String> placeNames = new HashMap<>();
    private final Map<Long, List<AsEnrollmentEntity>> activeEnrollments = new HashMap<>();
    private final Map<Long, AsCourseSessionEntity> existingSessions = new HashMap<>();
    private final Map<Long, AsCourseClassEntity> existingCourses = new HashMap<>();
    private Map<Long, List<AsCourseSessionEntity>> targetSessions;

    /** 完整候选筛选后只保留当前页的服务引用，不裁剪学生用于跨服务冲突判断的报名数据。 */
    public void retainPageServices(Collection<Long> serviceIds) {
        services.keySet().retainAll(serviceIds);
        scopeErrors.keySet().retainAll(serviceIds);
    }

    /** 同一学生对同一服务只校验一次适用范围，校验通过的空结果也复用。 */
    public String scopeError(Long serviceId, Supplier<String> loader) {
        return cached(scopeErrors, serviceId, loader);
    }

    /** 复用原学期名称查询结果，不参与业务学期选择。 */
    public String termName(Long termId, Supplier<String> loader) {
        return cached(termNames, termId, loader);
    }

    /** 按原始场地字符串复用解析结果，保留原有多场地格式化规则。 */
    public String placeName(String rawPlaceName, Supplier<String> loader) {
        return cached(placeNames, rawPlaceName, loader);
    }

    /** 当前请求固定为一个学生，按学校复用原有效课次报名查询结果。 */
    public List<AsEnrollmentEntity> activeEnrollments(Long schoolId,
                                                     Supplier<List<AsEnrollmentEntity>> loader) {
        return cached(activeEnrollments, schoolId, loader);
    }

    /**
     * 首次进入冲突检查时批量加载当前页目标课次；当前页全部课程提前返回时不加载。
     */
    public List<AsCourseSessionEntity> targetSessions(Long courseId,
            Supplier<Map<Long, List<AsCourseSessionEntity>>> loader) {
        if (targetSessions == null) {
            targetSessions = loader.get();
        }
        return targetSessions.getOrDefault(courseId, Collections.emptyList());
    }

    /** 分批补齐冲突检查需要的课次，已读取或已确认不存在的记录不重复查询。 */
    public Map<Long, AsCourseSessionEntity> existingSessions(Collection<Long> ids,
            Function<Collection<Long>, List<AsCourseSessionEntity>> loader) {
        loadMissing(existingSessions, ids, loader, AsCourseSessionEntity::getId);
        return existingSessions;
    }

    /** 分批补齐冲突课程信息，不改变冲突筛选及提示顺序。 */
    public Map<Long, AsCourseClassEntity> existingCourses(Collection<Long> ids,
            Function<Collection<Long>, List<AsCourseClassEntity>> loader) {
        loadMissing(existingCourses, ids, loader, AsCourseClassEntity::getId);
        return existingCourses;
    }

    private <K, V> V cached(Map<K, V> cache, K key, Supplier<V> loader) {
        // 空结果也需要记住，避免同一服务校验通过或关联数据缺失时反复查询。
        if (!cache.containsKey(key)) {
            cache.put(key, loader.get());
        }
        return cache.get(key);
    }

    private <T> void loadMissing(Map<Long, T> cache, Collection<Long> ids,
                                Function<Collection<Long>, List<T>> loader, Function<T, Long> idGetter) {
        List<Long> missingIds = ids.stream().filter(Objects::nonNull).distinct()
                .filter(id -> !cache.containsKey(id)).collect(Collectors.toList());
        for (List<Long> batch : Lists.partition(missingIds, BATCH_SIZE)) {
            List<T> rows = loader.apply(batch);
            batch.forEach(id -> cache.put(id, null));
            if (rows != null) {
                rows.forEach(row -> cache.put(idGetter.apply(row), row));
            }
        }
    }
}
