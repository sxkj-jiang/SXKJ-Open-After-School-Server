package com.sxkj.edu.common.utils;

import com.sxkj.edu.common.config.GrobProperties;
import org.apache.commons.codec.digest.DigestUtils;

public class SmartDigestUtil extends DigestUtils {

    /**
     * md5加盐加密
     *
     * @param salt
     * @param password
     * @return
     */
    public static String encryptPassword(String salt, String password) {
        return SmartDigestUtil.md5Hex(String.format(salt, password));
    }

    public static void main(String[] args) {
        try {
//            System.out.println(new String(SmartDigestUtil.md5("c655798e4648c540812a1b8f48759af7")));
            String addPwd = SmartDigestUtil.encryptPassword(GrobProperties.getInstance().AES_KEY, "ekcSXaIx6Pk0tV3hkBfp6A==");
//            String pwd = SmartDigestUtil.encryptPassword(GrobProperties.getInstance().AES_KEY,  new String(decoder.decodeBuffer("1jdzWuniG6UMtoa3T6uNLA==")));
            System.out.println(addPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
