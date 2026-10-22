package com.sxkj.edu.common.utils;

import com.sxkj.edu.common.config.GrobProperties;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * 对称加密AES
 */
@Slf4j
public class AesEncryption {


    /**
     * 加密
     * @param data
     * @return
     */
    public static String encrypt(String data) {
        IvParameterSpec iv = null;
        try {
            iv = new IvParameterSpec(GrobProperties.getInstance().AES_KEY.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(GrobProperties.getInstance().AES_KEY.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(data.getBytes());

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            log.info("加密数据出现异常：{}",e.getMessage());
            return null;
        }

    }

    /**
     * 解密
     * @param encrypted
     * @return
     */
    public static String decrypt(String encrypted) {
        IvParameterSpec iv = null;
        try {
            iv = new IvParameterSpec(GrobProperties.getInstance().AES_KEY.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(GrobProperties.getInstance().AES_KEY.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            return new String(original);
        } catch (Exception e) {
            log.info("解密数据出现异常：{}",e.getMessage());
            return null;
        }

    }
}
