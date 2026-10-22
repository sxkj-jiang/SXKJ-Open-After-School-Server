package com.sxkj.edu.module.support.websocket.domain;

import lombok.Data;


@Data
public class MessageCommonDTO {
    /**
     * 消息类型 {@link MessageTypeEnum}
     */
    private Integer messageType;

    /**
     * 具体消息内容
     */
    private String jsonStr;


}
