package com.sxkj.edu.module.business.entity.wp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_wp_share")
public class WpSharePo extends BaseEntity {

    private Long userId;
    private Date shareTime;
    private Date endTime;
    private String extractionCode;
    private String shareBatchNum;

    /**
     * 分享类型：0公共，1私密，2好友（暂不支持）
     */
    private Integer shareType;

    // 分享状态(0正常,1已失效,2已撤销)
    private Integer shareStatus;

    public WpSharePo() {}

    public WpSharePo(Long userId, Date endTime, String extractionCode, String batchNum, Integer shareType) {
        this.userId = userId;
        this.shareTime = new Date();
        this.endTime = endTime;
        this.extractionCode = extractionCode;
        this.shareBatchNum = batchNum;
        this.shareType = shareType;
        this.shareStatus = 0;
        this.setCreateTime(new Date());
        this.setCreateUser(userId);
    }

}
