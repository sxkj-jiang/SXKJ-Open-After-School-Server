package com.sxkj.edu.module.business.entity.Integration;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseAssignIdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description :
 * @Author : jjj$
 * @Date : 2026$ $
 * @Company : SXKJ
 * @Version : 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_wechat_msg_config")
public class WechatMsgConfigEntity extends BaseAssignIdEntity {

    private String appId;

    private String appSecret;

    private String token;

    private String encodingAesKey;

    private Integer msgType;

    private Integer dailySendLimit;

    private Integer hourlySendLimit;

    private Integer status;

    private String remark;
}