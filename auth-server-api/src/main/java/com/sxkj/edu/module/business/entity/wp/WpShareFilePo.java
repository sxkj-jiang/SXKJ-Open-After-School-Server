package com.sxkj.edu.module.business.entity.wp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import lombok.Data;

import java.util.Date;


@Data
@TableName("t_wp_share_file")
public class WpShareFilePo extends BaseEntity {

    private Long fileId;

    private String shareBatchNum;

    private Long userId;

    private String shareFilePath;

    public WpShareFilePo() {
        this.setCreateTime(new Date());
        this.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
        this.setIsValid(1);
    }

    public WpShareFilePo(Long fileId, String shareBatchNum, Long userId, String shareFilePath) {
        this.fileId = fileId;
        this.shareBatchNum = shareBatchNum;
        this.userId = userId;
        this.shareFilePath = shareFilePath;
        this.setCreateTime(new Date());
        this.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
        this.setIsValid(1);
    }
}
