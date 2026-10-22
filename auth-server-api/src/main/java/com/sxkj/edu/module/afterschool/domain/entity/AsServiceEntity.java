package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 课后服务活动实体
 *
 * @author system
 * @date 2026-05-17
 */
@Data
@TableName("t_as_service")
public class AsServiceEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属学校
     */
    private Long schoolId;

    /**
     * 服务编码
     */
    private String serviceCode;

    /**
     * 课程活动名称
     */
    private String serviceName;

    /**
     * 学期ID，对应 t_educl_term.id
     */
    private Long termId;

    /**
     * 报名开始时间
     */
    private Date enrollStartTime;

    /**
     * 报名结束时间
     */
    private Date enrollEndTime;

    /**
     * 开课日期
     */
    private Date courseStartDate;

    /**
     * 结课日期
     */
    private Date courseEndDate;

    /**
     * 课后时间管理方案ID
     */
    private Long afterSchoolTimeTemplateId;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 创建人ID
     */
    private Long creatorId;

    /**
     * 服务状态：0未发布 1报名中 2进行中 3已结束
     */
    private Integer serviceStatus;

    /**
     * 服务适用年级ID集合JSON（保留字段，业务暂不使用）
     */
    @TableField("grade_scope_json")
    private String gradeScopeJson;

    /**
     * 服务适用行政班ID集合JSON（保留字段，业务暂不使用）
     */
    @TableField("class_scope_json")
    private String classScopeJson;

    /**
     * 选课初始化状态：0未初始化 1初始化中 2已初始化
     */
    private Integer selectionInitStatus;

    /**
     * 选课初始化完成时间
     */
    private Date selectionInitTime;

    /**
     * 复制来源课后服务编号，手工新增时为空
     */
    private Long sourceServiceId;
}
