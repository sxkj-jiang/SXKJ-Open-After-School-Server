package com.sxkj.edu.common.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 *@Description : 用户操作日志
 *@Author : JIANGJJ
 *@Date : 2022-9-6 15:49
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface OperateLog {

    /* 类型，LOGIN-登录⽇志、OPERATION操作⽇志 */
    String type() default "";

    /* 日志内容 */
    String value() default "";

    /* 业务类型，业务⽅⾃⼰定义 */
    String businessType() default "";

    /* 登录用户类型 1 员工 2企业 3 会员(同up_sys_user)*/
    int loginType() default 1;
}
