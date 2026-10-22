package com.sxkj.edu.module.business.entity.Integration;


import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseAssignIdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_wechat_msg_template")
public class WechatMsgTemplateEntity extends BaseAssignIdEntity {

    private String templateId;

    private String templateName;

    private String templateCode;

    private String templateType;

    private String content;

    private String url;

    private String miniProgramAppid;

    private String miniProgramPagepath;

    private String sceneDesc;

    private Integer status;
}
