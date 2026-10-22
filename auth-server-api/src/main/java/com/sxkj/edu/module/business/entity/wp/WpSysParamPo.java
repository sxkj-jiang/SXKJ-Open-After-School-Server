package com.sxkj.edu.module.business.entity.wp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseEntity;
import lombok.Data;

@Data
@TableName("t_wp_sys_param")
public class WpSysParamPo extends BaseEntity {

    private String groupName;
    private String paramDesc;
    private String paramKey;
    private String paramValue;


}
