package com.sxkj.edu.module.support.websocket.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class OnMessageDTO {
    /**
     * 消息类型 ：image，file，text，
     */
    private String type;
    /**
     * 文本内容
     */
    private String text;
    /**
     * 文件名
     */
    private String name;
    /**
     * 链接url
     */
    private String url;

    private String roomId;

    private String from;
    private String to;

    private String message;

    private byte[] bytes;

}
