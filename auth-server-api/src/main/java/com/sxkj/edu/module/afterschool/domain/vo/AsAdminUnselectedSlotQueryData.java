package com.sxkj.edu.module.afterschool.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * 管理端未选课分页计算使用的可分配课次时段。
 *
 * <p>该对象只承载数据库候选学生筛选所需的最小字段，避免查询全部学生课次后再分页。</p>
 */
@Data
public class AsAdminUnselectedSlotQueryData {

    /** 课后服务编号。 */
    private Long serviceId;

    /** 实际课次日期，历史周期课次可能为空。 */
    private Date sessionDate;

    /** 星期，取值范围1-7。 */
    private Integer weekDay;

    /** 开始时间对应的分钟数。 */
    private Integer startMinute;

    /** 结束时间对应的分钟数。 */
    private Integer endMinute;

    /**
     * 适用年级编号，格式为“,1,2,”；为空表示不限年级。
     * 使用带边界的字符串匹配，避免年级编号部分匹配。
     */
    private String gradeScope;
}
