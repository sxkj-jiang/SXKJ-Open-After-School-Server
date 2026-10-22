package com.sxkj.edu.config.auth.core.ds;

/** 核心文件 A：RSA 公钥分片 1/3，分散存放以抗「替换公钥绕过验签」 */
public final class DataSourceCore {
    public static String pubShard() {
        return "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApoAztgoVNmTTxdbO3beFLjRVvoGvM21UGVeztBbMJ0HnFs9NWmiC8hbZlJ+x8CMe0KIaW9pwQ1c2Z5zUVzFkOlZ";
    }
    private DataSourceCore() {}
}
