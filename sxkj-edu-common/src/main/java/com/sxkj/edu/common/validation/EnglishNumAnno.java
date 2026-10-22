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
        validatedBy = EnglishNumValidator.class
)
public @interface EnglishNumAnno {

    String message() default "格式校验不正确，应为4至16位之间的英文加横线-组成";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
