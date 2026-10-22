package com.sxkj.edu.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author SIBAT-JJJ
 * @version 1.0
 * @Description 核心数据脱密工具类，如人的基本信息：项目、身份证、手机、地址
 * @Classname TuominUtil
 * @company SXKJ
 * @Date 2021/6/16 22:37
 */
public final class TuominUtil {

    /**
     * 身份证脱敏
     * @param idCard
     * @return
     */
    public static String tuomingIdCard(String idCard){
        String idCardRes = "";
        if(StringUtils.isEmpty(idCard)){
            return idCardRes;
        }
        StringBuffer sf = new StringBuffer(512);
        sf.append(idCard.substring(0,4));
        sf.append("********");
        sf.append(idCard.substring(idCard.length()-4,idCard.length()));
        idCardRes = sf.toString();
        return idCardRes;
    }

    /**
     * 手机号脱敏
     * @param phone
     * @return
     */
    public static String tuomingMobile(String phone){
        String res = "";
        if(StringUtils.isEmpty(phone) || phone.length() < 8){
            return phone;
        }
        StringBuffer sf = new StringBuffer(128);
        sf.append(phone.substring(0,3));
        sf.append("****");
        sf.append(phone.substring(phone.length()-4,phone.length()));
        res = sf.toString();
        return res;
    }

    /**
     * 姓名脱敏
     * @param name
     * @return
     */
    public static String tuomingName(String name){
        String res = "";
        if(StringUtils.isEmpty(name)){
            return res;
        }
        StringBuffer sf = new StringBuffer(128);
        sf.append("**");
        sf.append(name.substring(name.length()-1,name.length()));
        res = sf.toString();
        return res;
    }

    public static void main(String[] args) {
        System.out.println(tuomingName("真实"));
        System.out.println(tuomingMobile("1385001"));
    }
}
