package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 教务校历特殊日期/事件实体
 *
 * @author system
 * @date 2026-05-17
 */
@Data
@TableName("t_educl_calendar_event")
public class EduclCalendarEventEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属学校，对应 t_school_manage.id
     */
    private Long schoolId;

    /**
     * 所属学期，对应 t_educl_term.id
     */
    private Long termId;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 事件开始日期
     */
    private Date eventDateStart;

    /**
     * 事件结束日期
     */
    private Date eventDateEnd;

    /**
     * 1特殊日期 2考试 3活动 4培训 5其他
     */
    private Integer eventType;

    /**
     * 备注
     */
    private String remark;
}
