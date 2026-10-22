package com.sxkj.edu.module.afterschool.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.module.afterschool.domain.entity.DateHolidayEntity;
import com.sxkj.edu.module.afterschool.mapper.DateHolidayMapper;
import com.sxkj.edu.module.afterschool.service.DateHolidayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 节假日日期 Service 实现类
 *
 * @author system
 * @date 2026-06-04
 */
@Service
@Slf4j
public class DateHolidayServiceImpl extends ServiceImpl<DateHolidayMapper, DateHolidayEntity> implements DateHolidayService {

    private static final String HOLIDAY_API_URL = "https://oneapi.coderbox.cn/openapi/public/holiday?date=";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int syncHolidayData(int year) {
        log.info("【节假日同步】开始同步 {} 年节假日数据", year);

        // 1. 调用远程接口获取节假日数据
        List<DateHolidayEntity> holidays = fetchHolidayFromApi(year);
        if (holidays.isEmpty()) {
            log.warn("【节假日同步】{} 年未获取到节假日数据", year);
            return 0;
        }

        // 2. 删除该年份的旧数据
        LambdaQueryWrapper<DateHolidayEntity> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(DateHolidayEntity::getYear, year);
        this.remove(deleteWrapper);

        // 3. 批量保存新数据
        for (DateHolidayEntity holiday : holidays) {
            holiday.setIsValid(1);
            holiday.setCreateTime(new Date());
            holiday.setUpdateTime(new Date());
            this.save(holiday);
        }

        log.info("【节假日同步】{} 年节假日数据同步完成，共 {} 条", year, holidays.size());
        return holidays.size();
    }

    @Override
    public List<DateHolidayEntity> getHolidaysByYear(int year) {
        LambdaQueryWrapper<DateHolidayEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DateHolidayEntity::getYear, year)
                .eq(DateHolidayEntity::getIsValid, 1)
                .orderByAsc(DateHolidayEntity::getStartDate);
        return this.list(wrapper);
    }

    /**
     * 从远程接口获取节假日数据
     * 接口返回每条记录为单日数据，包含字段：
     * - date: 日期（yyyy-MM-dd）
     * - festival: 节假日名称
     * - badDay: 是否补班（1=补班，0=非补班）
     * - status: 状态码（2=放假）
     * - description: 描述（含"补班"则为补班日）
     */
    private List<DateHolidayEntity> fetchHolidayFromApi(int year) {
        List<DateHolidayEntity> holidays = new ArrayList<>();

        try {
            // 调用接口
            String res = HttpUtil.get(HOLIDAY_API_URL + year);
            if (StrUtil.isBlank(res)) {
                log.error("【节假日同步】接口返回为空");
                return holidays;
            }

            // 解析响应
            JSONObject obj = JSONUtil.parseObj(res);
            if (obj == null) {
                log.error("【节假日同步】接口返回异常: {}", res);
                return holidays;
            }

            JSONArray data = obj.getJSONArray("data");
            if (data == null || data.isEmpty()) {
                log.warn("【节假日同步】{} 年无节假日数据", year);
                return holidays;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            // 1. 过滤出放假数据（排除补班）
            List<JSONObject> holidayItems = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                JSONObject item = data.getJSONObject(i);
                if (item == null) continue;

                String festival = item.getStr("festival");
                String dateStr = item.getStr("date");
                Integer badDay = item.getInt("badDay");
                String description = item.getStr("description");

                if (StrUtil.isBlank(festival) || StrUtil.isBlank(dateStr)) {
                    continue;
                }

                // 排除补班日：badDay=1 或 description 包含"补班"
                if (badDay != null && badDay == 1) {
                    continue;
                }
                if (StrUtil.isNotBlank(description) && description.contains("补班")) {
                    continue;
                }

                holidayItems.add(item);
            }

            // 2. 按节假日名称分组，取每个节假日的最早和最晚日期
            Map<String, List<JSONObject>> grouped = holidayItems.stream()
                    .collect(Collectors.groupingBy(item -> item.getStr("festival")));

            for (Map.Entry<String, List<JSONObject>> entry : grouped.entrySet()) {
                String festivalName = entry.getKey();
                List<JSONObject> items = entry.getValue();

                // 按日期排序，取最早和最晚日期
                items.sort(Comparator.comparing(item -> item.getStr("date")));
                String startDateStr = items.get(0).getStr("date");
                String endDateStr = items.get(items.size() - 1).getStr("date");

                try {
                    DateHolidayEntity entity = new DateHolidayEntity();
                    entity.setYear(year);
                    entity.setHolidayName(festivalName);
                    entity.setStartDate(sdf.parse(startDateStr));
                    entity.setEndDate(sdf.parse(endDateStr));
                    holidays.add(entity);
                } catch (Exception e) {
                    log.warn("【节假日同步】解析节假日数据失败: festival={}, startDate={}, endDate={}", festivalName, startDateStr, endDateStr, e);
                }
            }

            // 按开始日期排序
            holidays.sort(Comparator.comparing(DateHolidayEntity::getStartDate));

        } catch (Exception e) {
            log.error("【节假日同步】获取 {} 年节假日数据异常", year, e);
        }

        return holidays;
    }
}
