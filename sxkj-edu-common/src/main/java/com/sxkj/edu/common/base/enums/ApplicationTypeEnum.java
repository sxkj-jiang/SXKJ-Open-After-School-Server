package com.sxkj.edu.common.base.enums;

import lombok.Getter;

/**
 * mq 发送、接收应用代码
 * @author jiangjj
 * @date 2020/6/22
 */
@Getter
public enum ApplicationTypeEnum {

    /**
     * 应用类型
     */
    course_mq("afterSchool","课后服务项目1.0版本");

    private final String key;
    private final String value;

    ApplicationTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
