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
        validatedBy = EnglishChineseValidator.class
)
public @interface EnglishChineseAnno {

    String message() default "格式校验不正确，应为4至22位之间的英文或下划线横线组成";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
