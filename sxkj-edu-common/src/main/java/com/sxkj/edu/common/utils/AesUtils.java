package com.sxkj.edu.common.utils;

import com.sxkj.edu.common.exception.BaseBusinessException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class AesUtils {

    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//
    private static final Logger LOG = LoggerFactory.getLogger(AesUtils.class);

    /**
     * 对称加密
     * @param content
     * @param password
     * @return
     */
    public static String encrypt (String content, String password) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器

            byte[] byteContent = content.getBytes("utf-8");

            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));// 初始化为加密模式的密码器

            byte[] result = cipher.doFinal(byteContent);// 加密

            return Base64.encodeBase64String(result);//通过Base64转码返回
        } catch (Exception ex) {
            ex.printStackTrace();
            LOG.info("加密数据出现异常："+ex.getMessage());
        }

        return null;
    }

    /**
     * 对称解密
     * @param content
     * @param password
     * @return
     */
    public static String decrypt(String content, String password) {

        try {
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));

            //执行操作
            byte[] result = cipher.doFinal(Base64.decodeBase64(content));

            return new String(result, "utf-8");
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }

        return null;
    }

    private static SecretKeySpec getSecretKey(final String key) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        if (null == key || key.length() == 0) {
            throw new NullPointerException("key not is null");
        }
        SecretKeySpec key2 = null;
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        random.setSeed(key.getBytes());
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            key2 = new SecretKeySpec(enCodeFormat, "AES");
        } catch (NoSuchAlgorithmException ex) {

        }
        return key2;
    }

    /**
     * 解密数据
     * @param request
     * @return
     */
    public static String decodeBase64(String request){
        String content = null;
        try {
            //执行操作
            byte[] result = Base64.decodeBase64(request);
            content = new String(result, "UTF-8");
        } catch (UnsupportedEncodingException e) {

        }
        return content;
    }

    /**
     * 解密数据-byte[]
     * @param request
     * @return
     */
    public static String decodeBase64(byte[] request){
        String content = null;
        try {
            //执行操作
            byte[] result = Base64.decodeBase64(request);
            content = new String(result, "UTF-8");
        } catch (UnsupportedEncodingException e) {

        }
        return content;
    }

    /**
     * 加密数据
     * @param request
     * @return
     */
    public static String encodeBase64String(String request) throws BaseBusinessException {
        String content = null;
        try {
            byte[] byteContent = request.getBytes("utf-8");
            content = Base64.encodeBase64String(byteContent);
        } catch (Exception e) {
            throw new BaseBusinessException("call AesUtil encodeBase64String in error",e);
        }
        return content;
    }

    /**
     * 加密数据-返回二进制编码
     * @param request
     * @return
     */
    public static byte[] encodeBase64ForByte(String request) throws BaseBusinessException{
        byte[] content = null;
        try {
            byte[] byteContent = request.getBytes("utf-8");
            content = Base64.encodeBase64(byteContent);
        } catch (UnsupportedEncodingException e) {
            throw new BaseBusinessException("call AesUtil encodeBase64String in error",e.getCause());
        }
        return content;
    }

    /**
     * @param text
     * @return 密文
     */
    // 带秘钥加密
    public static String md5Encode(String text) throws Exception {
        // 加密后的字符串
        String md5str = DigestUtils.md5Hex(text );
        System.out.println("MD5加密后的字符串为:" + md5str);
        return md5str;
    }


    public static String convertMD5(String inStr) {
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;

    }


        // 不带秘钥加密
    public static String md5decode(String text) throws Exception {
        // 加密后的字符串
        String decode = DigestUtils.md5Hex(text);
        return decode;
    }

    /**
     * MD5验证方法
     *
     * @param text
     * @param md5
     */
    // 根据传入的密钥进行验证
    public static boolean verify(String text, String md5) throws Exception {
        String md5str = md5Encode(text);
        if (md5str.equalsIgnoreCase(md5)) {
            System.out.println("MD5验证通过");
            return true;
        }
        return false;
    }

    public static void main(String[] args) {

        try {
            String str = md5Encode("Szair@123");
            System.out.println(str);
            System.out.println(convertMD5(str));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
