package com.sxkj.edu.module.support.websocket.domain;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class MessageDTO {

    /**
     * 消息类型 {@link MessageTypeEnum}
     */
    private Integer messageType;

    /**
     * 消息体
     */
    private String message;

    /**
     * 发送者
     */
    private Long fromUserId;

    /**
     * 接收者，系统通知可为null
     */
    private Long toUserId;

}
