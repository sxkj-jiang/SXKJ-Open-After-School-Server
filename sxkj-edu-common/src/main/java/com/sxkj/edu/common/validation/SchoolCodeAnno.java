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
        validatedBy = SchoolCodeValidator.class
)
public @interface SchoolCodeAnno {

    String message() default "格式校验不正确，应为5至10位的数字型";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
