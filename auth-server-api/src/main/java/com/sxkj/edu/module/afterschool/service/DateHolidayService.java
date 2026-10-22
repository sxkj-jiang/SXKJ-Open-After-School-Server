package com.sxkj.edu.module.afterschool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.module.afterschool.domain.entity.DateHolidayEntity;

import java.util.List;

/**
 * 节假日日期 Service 接口
 *
 * @author system
 * @date 2026-06-04
 */
public interface DateHolidayService extends IService<DateHolidayEntity> {

    /**
     * 同步指定年份的节假日数据（从远程接口获取并保存到数据库）
     *
     * @param year 年份
     * @return 同步的节假日数量
     */
    int syncHolidayData(int year);

    /**
     * 查询指定年份的节假日列表
     *
     * @param year 年份
     * @return 节假日列表
     */
    List<DateHolidayEntity> getHolidaysByYear(int year);
}
