package com.sxkj.edu.common.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* @desc 防止暴力破解自定义注解
* @company SXKJ
* @author jiangjiangjun
* @param
* @return
* @date 2020/10/13 15:11
* @version
*/
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface AccessLimit {
    int seconds();
    int maxCount();
    String desc();
}
