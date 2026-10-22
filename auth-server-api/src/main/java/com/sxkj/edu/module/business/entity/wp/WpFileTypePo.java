package com.sxkj.edu.module.business.entity.wp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

@Data
@TableName("t_file_type")
public class WpFileTypePo extends BaseEntity {

    private Integer fileTypeId;
    private String fileTypeName;
    private Integer orderNum;
}
