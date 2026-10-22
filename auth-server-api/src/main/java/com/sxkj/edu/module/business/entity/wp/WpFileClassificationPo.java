package com.sxkj.edu.module.business.entity.wp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

@Data
@TableName("t_wp_file_classification")
public class WpFileClassificationPo extends BaseEntity {

    private Integer fileTypeId;
    private String fileExtendName;
}
