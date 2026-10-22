package com.sxkj.edu.module.afterschool.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 业务附件关联实体
 *
 * @author system
 * @date 2026-05-17
 */
@Data
@TableName("t_as_business_attachment")
public class AsBusinessAttachmentEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属学校
     */
    private Long schoolId;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 业务ID
     */
    private Long bizId;

    /**
     * 文件ID，复用 t_base_file
     */
    private Long fileId;

    /**
     * 文件名称
     */
    private String fileName;

    @TableField(exist = false)
    private Integer businessType;

    @TableField(exist = false)
    private Long businessId;

    @TableField(exist = false)
    private Long uploaderUserId;

    @TableField(exist = false)
    private Long updateUser;

    @TableField(exist = false)
    private java.util.Date updateTime;
}
