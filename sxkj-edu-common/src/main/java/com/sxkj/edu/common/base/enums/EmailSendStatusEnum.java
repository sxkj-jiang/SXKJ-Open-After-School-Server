package com.sxkj.edu.common.base.enums;



public enum EmailSendStatusEnum {

    NOT_SEND(0,"未发送"),

    SEND(1,"已发送");

    private Integer type;
    private String desc;

    EmailSendStatusEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }


}
