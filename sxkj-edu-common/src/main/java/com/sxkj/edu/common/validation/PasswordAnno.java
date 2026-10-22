package com.sxkj.edu.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({
        ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER,
        ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = PasswordValidator.class
)
public @interface PasswordAnno {

    String message() default "密码必须同时包含数字、小写字母、大写字母和特殊字符， 最少为8位，特殊字符包含：!@#$%^&*()_+=";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
