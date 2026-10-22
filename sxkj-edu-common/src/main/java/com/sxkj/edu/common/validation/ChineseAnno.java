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
        validatedBy = ChineseValidator.class
)
public @interface ChineseAnno {

    String message() default "格式校验不正确，名称请传入2至16位的中文";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
