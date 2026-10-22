package com.sxkj.edu.module.business.entity.wp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

@Data
@TableName("t_wp_file_permission")
public class WpFilePermissionPo extends BaseEntity {

    public Long filePermissionId;
    public String commonFileId;
    public Long userId;
    public Integer filePermissionCode;

}
