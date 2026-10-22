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
        validatedBy = LoginNameValidator.class
)
public @interface LoginNameAnno {

    String message() default "格式校验不正确，登录名称必须为大于或等于六位，并且为字母、数字";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
