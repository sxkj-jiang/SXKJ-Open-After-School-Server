package com.sxkj.edu.config.auth.core.security;


import com.sxkj.edu.config.auth.core.auth.AuthCore;
import com.sxkj.edu.config.auth.core.cache.CacheCore;
import com.sxkj.edu.config.auth.core.ds.DataSourceCore;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 公钥组装器：把分散在 3 个核心文件里的 RSA 公钥分片拼回完整公钥。
 * 单独拿到任何一个核心文件都拿不到完整公钥，提高「替换公钥绕过验签」的门槛。
 */
public final class PublicKeyAssembler {

    private PublicKeyAssembler() {}

    public static PublicKey assemble() throws Exception {
        String b64 = DataSourceCore.pubShard()
                + AuthCore.pubShard()
                + CacheCore.pubShard();
        byte[] der = Base64.getDecoder().decode(b64);
        return KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(der));
    }
}
