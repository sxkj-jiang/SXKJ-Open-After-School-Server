package com.sxkj.edu.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 身份证注解校验
 * @author jiangjj
 */
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
        validatedBy = PersonCardValidator.class
)
public @interface PersonCard {

    String message() default "身份证号格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
