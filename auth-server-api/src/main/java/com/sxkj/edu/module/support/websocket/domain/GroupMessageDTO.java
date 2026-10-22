package com.sxkj.edu.module.support.websocket.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class GroupMessageDTO {
    private String type;
    private Integer onlineNum;


}
