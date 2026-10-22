package com.sxkj.edu.common.validation;

import cn.hutool.core.util.StrUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 姓名校验
 * @author
 *
 */

public class PasswordValidator implements ConstraintValidator<PasswordAnno, String> {

    @Override
    public void initialize(PasswordAnno annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果姓名为空，默认不校验，即校验通过
        if (StrUtil.isEmpty(value)) {
            return false;
        }
        // 校验姓名
        return ValidationUtils.passwordValid(value);
    }

}
