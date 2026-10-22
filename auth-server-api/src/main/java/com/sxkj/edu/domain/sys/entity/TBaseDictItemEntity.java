package com.sxkj.edu.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.domain.common.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 基础管理-字典
 * @author
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("t_base_dict_item")
public class TBaseDictItemEntity extends BaseModel {

    private Long parentId;

    private String typeName;

    private String typeCode;

    private String typeValue;

    private Integer sort;

    // 状态(1:正常;0:禁用)
    private Integer status;

    private String remark;
}
