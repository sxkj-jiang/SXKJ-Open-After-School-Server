package com.sxkj.edu.common.utils.excel;

import java.lang.annotation.*;

/**
 * @Author Administrator
 * @Date 2024/12/3 18:09
 */
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelImageProperty {
    String[] value() default {""};

    /**
     * 图片在第几列 1开始
     * @return
     */
    int index() default -1;
}
