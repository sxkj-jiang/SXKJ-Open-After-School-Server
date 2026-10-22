package com.sxkj.edu.config.auth.core.security;

import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

/**
 * license 验签：用内嵌公钥校验签名。签名不匹配（文件被篡改/非本服务签发）即返回 false。
 */
public final class LicenseVerifier {

    private LicenseVerifier() {}

    public static boolean verify(License lic, PublicKey publicKey) {
        try {
            if (lic == null || lic.getSignature() == null) return false;
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);
            sig.update(lic.toSignBytes());
            return sig.verify(Base64.getDecoder().decode(lic.getSignature()));
        } catch (Exception e) {
            return false;
        }
    }
}
