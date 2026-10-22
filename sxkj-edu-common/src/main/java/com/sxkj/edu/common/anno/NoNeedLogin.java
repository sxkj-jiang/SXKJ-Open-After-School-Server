package com.sxkj.edu.common.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 *@Description : 不需要登陆
 *@Author : JIANGJJ
 *@Date : 2022-9-6 15:48
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NoNeedLogin {
}
