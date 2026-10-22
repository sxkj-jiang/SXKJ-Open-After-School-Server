package com.sxkj.edu.module.system.login.domain;

import lombok.Data;


@Data
public class KaptchaVO {

    /**
     * 验证码UUID
     */
    private String uuid;

    /**
     * base64 验证码
     */
    private String code;

}
