package com.sxkj.edu.common.utils;

import java.security.SecureRandom;
import java.util.Date;
import java.util.UUID;

public class UUIDUtils {

  public static final String ALGORITHM_UUID = "01";

  public static final String generate(String algorithm) {
    if (ALGORITHM_UUID.equals(algorithm)) {
      return random();
    }
    throw new UnsupportedOperationException();
  }

  /**
   *@Description : 生成年份+8位随机数字
   *@Author : JIANGJJ
   *@Date : 2026-1-27 10:53
   *@Company : SXKJ
   *@Version : 1.0
   **/
  public static Long randomNumber12(){
    SecureRandom secureRandom = new SecureRandom();
    String year = DateUtils.format(new Date(),DateUtils.YEAR_01);
    // 生成一个8位的随机数
    int randomNumber = secureRandom.nextInt(99999999); // 注意这里用的是12位数上限（不包括12位，因为nextInt不包括上限）
    // 确保是8位数字，通过添加前导0
    String randomID = String.format("%08d", randomNumber);
    String allNum = year + randomID;
    Long number = new Long(allNum);
    return number;
  }

  public static String random() {
    return UUID.randomUUID().toString().replace("-", "");
  }

  public static void main(String[] args) {
    System.out.println(random());
    System.out.println(random());
    System.out.println(random());
    System.out.println(generate(UUIDUtils.ALGORITHM_UUID));
  }


}
