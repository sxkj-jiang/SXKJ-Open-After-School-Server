package com.sxkj.edu.module.business.entity.wp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 用户文件标签类
 */
@Data
@TableName("t_wp_user_file_label")
public class WpUserFileLabelPo extends BaseEntity {

    /**
     * 文件id
     */
    private Long fileId;

    /**
     * 标签id
     */
    private Long labelId;

    public WpUserFileLabelPo(Long fileId, Long labelId, Long userId) {
        this.fileId = fileId;
        this.labelId = labelId;
        this.setCreateUser(userId);
        this.setCreateTime(new Date());
        this.setIsValid(1);
    }

}
