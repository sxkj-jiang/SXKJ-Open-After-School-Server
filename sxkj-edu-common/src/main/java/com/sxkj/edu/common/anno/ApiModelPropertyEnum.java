package com.sxkj.edu.common.anno;


import com.sxkj.edu.common.base.domain.BaseEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *@Description : 枚举类字段属性的注解
 *@Author : JIANGJJ
 *@Date : 2022-9-6 15:48
 *@Company : SXKJ
 *@Version : 1.0
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiModelPropertyEnum {

    /**
     * 枚举类对象
     *
     * @return
     */
    Class<? extends BaseEnum> value();

    String example() default "";

    /**
     * 是否隐藏
     *
     * @return
     */
    boolean hidden() default false;

    /**
     * 是否必须
     *
     * @return
     */
    boolean required() default true;

    String dataType() default "";

    String enumDesc() default "";

}
