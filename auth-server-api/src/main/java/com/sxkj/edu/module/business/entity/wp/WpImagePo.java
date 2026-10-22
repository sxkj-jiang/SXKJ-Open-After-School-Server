package com.sxkj.edu.module.business.entity.wp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_wp_file_image")
public class WpImagePo extends BaseEntity {

    private Long fileId;
    private Integer imageWidth;
    private Integer imageHeight;

    public WpImagePo(){}

    public WpImagePo(Long fileId, Integer imageWidth, Integer imageHeight) {
        this.fileId = fileId;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.setCreateTime(new Date());
        this.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
    }
}
