package com.sxkj.edu.common.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface RolePermission {
    long [] roles() default {};
}
