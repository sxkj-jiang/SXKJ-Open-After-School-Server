package com.sxkj.edu.common.validation;

import cn.hutool.core.util.StrUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 姓名校验
 * @author
 *
 */

public class LoginNameValidator implements ConstraintValidator<LoginNameAnno, String> {

    @Override
    public void initialize(LoginNameAnno annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StrUtil.isEmpty(value)) {
            return false;
        }
        // 校验字母加横线
        return ValidationUtils.checkLoginNameValid(value);
    }

}
