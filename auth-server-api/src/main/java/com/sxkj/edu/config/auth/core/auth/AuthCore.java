package com.sxkj.edu.config.auth.core.auth;

/** 核心文件 B：RSA 公钥分片 2/3，分散存放以抗「替换公钥绕过验签」 */
public final class AuthCore {
    public static String pubShard() {
        return "DmoBsW9CT0qe5f2Xb5KoTNX5TrcATMJTwOToSJlUdXJsPB3nvSWwT44nS4ppK3QJJFvQvtc2kK1vYnBd+8e7YoEhbNPmibLXgV1b49iAFMvPdDRhQfBX2xDidG+8wfD0xYg";
    }
    private AuthCore() {}
}
