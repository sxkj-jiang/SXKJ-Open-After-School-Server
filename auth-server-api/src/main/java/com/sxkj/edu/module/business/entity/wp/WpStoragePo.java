package com.sxkj.edu.module.business.entity.wp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import lombok.Data;

import java.util.Date;

/**
 * 存储信息类
 */
@Data
@TableName("t_wp_storage")
public class WpStoragePo extends BaseEntity {


    private Long userId;

    private Long schoolId;

    private Long storageSize;

    private Long totalStorageSize;


    public WpStoragePo() {
        this.setCreateTime(new Date());
        this.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
        this.setIsValid(1);
    }

    public WpStoragePo(Long userId) {
        this.userId = userId;
    }

}
