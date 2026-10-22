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
        validatedBy = UserNameValidator.class
)
public @interface UserName {

    String message() default "名称请传入2至12位的中文";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
