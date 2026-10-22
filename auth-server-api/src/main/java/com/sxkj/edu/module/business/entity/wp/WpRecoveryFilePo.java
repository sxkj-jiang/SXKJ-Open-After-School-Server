package com.sxkj.edu.module.business.entity.wp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

@Deprecated
@Data
@TableName("t_wp_recoveryfile")
public class WpRecoveryFilePo extends BaseEntity {

    private Long userFileId;
    private Date deleteTime;
    private String deleteBatchNum;
}
