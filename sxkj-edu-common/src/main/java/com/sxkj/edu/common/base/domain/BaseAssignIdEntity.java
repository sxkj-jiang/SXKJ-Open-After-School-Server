package com.sxkj.edu.common.base.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 *@Description : 基础对象类
 *@Author : JIANGJJ
 *@Date : 2022-9-6 15:51
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Data
public class BaseAssignIdEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.UPDATE)
    private Long updateUser;

    /**
     * 是否有效，1-有效，其他无效
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer isValid;

}
