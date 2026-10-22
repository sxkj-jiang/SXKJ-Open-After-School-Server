package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.domain.common.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 节假日日期实体
 *
 * @author system
 * @date 2026-06-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_date_holiday")
public class DateHolidayEntity extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 年份，如 2026
     */
    private Integer year;

    /**
     * 节假日名称，如 春节、国庆节
     */
    private String holidayName;

    /**
     * 放假开始日期
     */
    private Date startDate;

    /**
     * 放假结束日期
     */
    private Date endDate;
}
