package com.sxkj.edu.module.afterschool.domain.dto;

import com.sxkj.edu.module.afterschool.domain.entity.AsSchedulePeriodEntity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 行政班安排导入-预加载映射集合。所有 Map 的 key 均为名称，value 为对应主键ID，避免循环查库。
 */
public class AdminClassImportRefs {
    public final Map<String, Long> termNameIdMap = new HashMap<>(16);
    /** 学期ID -> 学期类型(1第一学期 2第二学期 3其他)，用于生成 service_code */
    public final Map<Long, Integer> termIdToTypeMap = new HashMap<>(16);
    public final Map<String, Long> gradeNameIdMap = new HashMap<>(16);
    /** key = gradeId + "_" + className */
    public final Map<String, Long> adminClassKeyIdMap = new HashMap<>(32);
    public final Map<String, Long> teacherNameIdMap = new HashMap<>(32);
    public final Map<String, Long> categoryNameIdMap = new HashMap<>(16);
    /** key=标准化教室名或完整展示名，value=唯一教室ID */
    public final Map<String, Long> roomNameIdMap = new HashMap<>(32);
    /** 标准化后命中多个教室的歧义名称，不允许导入自动关联 */
    public final Set<String> ambiguousRoomNames = new HashSet<>(8);
    /** key=节次序号(sectionNo), value=本校启用作息方案下对应的普通节次(含id/起止时间) */
    public final Map<Integer, AsSchedulePeriodEntity> sectionNoPeriodMap = new HashMap<>(16);
    /** 学校编码，用于生成 service_code */
    public String schoolCode;
}
