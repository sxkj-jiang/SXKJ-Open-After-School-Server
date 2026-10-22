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
        validatedBy = ChineseTitleValidator.class
)
public @interface ChineseTitleAnno {

    String message() default "格式校验不正确，名称必须包含中文，并且为最少2位的中文和数字";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
