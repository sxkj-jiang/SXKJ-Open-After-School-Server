package com.sxkj.edu.module.business.entity.Integration;


import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseAssignIdEntity;
import com.sxkj.edu.common.base.domain.BaseEntity;
import com.sxkj.edu.domain.common.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_wechat_msg_send_log")
public class WechatMsgSendLogEntity extends BaseAssignIdEntity {

    private Long configId;

    private Long templateId;

    private String templateCode;

    private String templateName;

    private String openId;

    private String userName;

    private String userPhone;

    private String msgData;

    private Integer sendStatus;

    private Date sendTime;

    private String errorMsg;

    private Integer retryCount;
}