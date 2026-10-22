package com.sxkj.edu.module.business.entity.wp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_wp_file_label")
public class WpFileLabelPo extends BaseEntity {


    private Long userId;

    // 标签颜色，由前端传入
    private String colorValue;

    // 标签名称
    private String labelName;

    // 标签类型，用户自定义输入
    private String labelType;

    public WpFileLabelPo() {};

    public WpFileLabelPo(Long userId, String colorValue, String labelName, String labelType) {
        this.userId = userId;
        this.colorValue = colorValue;
        this.labelName = labelName;
        this.labelType = labelType;
        this.setCreateTime(new Date());
        this.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
    }
}
